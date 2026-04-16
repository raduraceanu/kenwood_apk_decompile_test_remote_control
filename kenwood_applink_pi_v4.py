#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Kenwood AppLink server for Raspberry Pi — v4
Fixed from APK source analysis of com.jvckenwood.android.launcherconnectionservice

KEY FIXES vs v3:
  1. Escape encoding is WRONG in v3.
       v3 uses:  ESC then (byte ^ 0x20)
       Real app: ESC(0xFD) then fixed lookup table:
                   0xFC (END)  → FD 5C
                   0xFD (ESC)  → FD 5D
                   0xFE (START)→ FD 5E
       MHLSpecs.java / Frame.java are the reference.

  2. Session command format is WRONG in v3.
       v3 uses:  F1 01 subcmd sid  (4-byte header, 1-byte sid)
       Real app: F1 01 HI(subcmd) LO(subcmd) HI(sid) LO(sid)
                 i.e. subcommand AND session ID are both 2-byte big-endian words.
       SessionManager.java::sendSessionCommand() + dispatchReceivedCommand() confirm this.

  3. Handshake trigger is WRONG in v3.
       v3 waits for subcmd=0x07 (CONNECTION_NOTIFY_ESTABLISHED).
       Real app: HU sends subcmd=7 (CONNECTION_NOTIFY_ESTABLISHED) to phone.
                 Phone responds with SESSION_NOTIFY_CONNECT (subcmd=1) per session.
       The pi is the *server* so it must wait for 0x0007 then send 0x0001 per session.

  4. Application command payload format is WRONG in v3.
       Real app (CommandId.java) uses 2-byte big-endian command IDs, not 1-byte.
         CMD_CONNECT               = 0x0001
         CMD_RETURN_CONNECT        = 0x8001
         CMD_NOTIFY_READY_CONNECT  = 0x0002
         CMD_GET_DEVICE_INFO       = 0x0121
         CMD_RETURN_GET_DEVICE_INFO= 0x8121
         CMD_GET_DEVICE_STAT       = 0x0131
         CMD_NOTIFY_DEVICE_STAT    = 0x8131
         CMD_GET_CALIBRATION_DATA  = 0x0152
         CMD_RETURN_CALIBRATION    = 0x8152

  5. CMD_RETURN_CONNECT / CMD_RETURN_GET_DEVICE_INFO payload format:
       CommandUtil.createBasicReturnCommand() → [HI, LO, resultCode, 0, 0, 0, 0, 1]
       v3 was sending arbitrary short payloads.

  6. CMD_RETURN_GET_DEVICE_INFO must include:
       basicCmd(8) + width_px(2) + height_px(2) + xdpi(4f) + ydpi(4f) + sdk_ver(2)
       We supply safe defaults (1280×720, 160dpi, SDK 21).

  7. SDP registration: The Pi must advertise via sdptool BEFORE accepting, so the
       HU can discover the service. Use listenUsingRfcommWithServiceRecord equivalent:
       bind to RFCOMM channel 3 and advertise the UUID via sdptool.
       A helper is provided at the bottom.
"""

import struct
import time
import threading
import bluetooth
import traceback
import subprocess
import sys

# ─────────────────────────────────────────
# Config
# ─────────────────────────────────────────
RFCOMM_CHANNEL  = 3
SERVICE_NAME    = "Launcher Application"
APPLINK_UUID    = "160b3720-7f28-11e3-ba0e-0002a5d5c51b"

# Frame delimiters (MHLSpecs.java)
START_SYNC = 0xFE
ESC_HEAD   = 0xFD
END_SYNC   = 0xFC

# Escape substitution bytes (Frame.java)
ESC_START_SYNC = 0x5E   # FD 5E → 0xFE
ESC_ESC_HEAD   = 0x5D   # FD 5D → 0xFD
ESC_END_SYNC   = 0x5C   # FD 5C → 0xFC

# Session layer sub-commands  (SessionManager.java, 2-byte big-endian)
SESSION_NOTIFY_CONNECT      = 0x0001
SESSION_NOTIFY_DISCONNECT   = 0x0002
SESSION_REQUEST_DISCONNECT  = 0x0003
SESSION_COMMAND             = 0x0004
SESSION_NOOP                = 0x0005
CONNECTION_CLOSE            = 0x0006
CONNECTION_NOTIFY_ESTABLISHED = 0x0007

# Session header bytes (SessionManager.java::sendSessionCommand)
CMD_SESSION_0 = 0xF1   # -15 as signed byte = 0xF1
CMD_SESSION_1 = 0x01

# App-level command IDs — 2-byte big-endian (CommandId.java)
CMD_CONNECT                  = 0x0001
CMD_RETURN_CONNECT           = 0x8001
CMD_NOTIFY_READY_CONNECT     = 0x0002
CMD_GET_DEVICE_INFO          = 0x0121
CMD_RETURN_GET_DEVICE_INFO   = 0x8121
CMD_GET_DEVICE_STAT          = 0x0131
CMD_NOTIFY_DEVICE_STAT       = 0x8131
CMD_GET_CALIBRATION_DATA     = 0x0152
CMD_RETURN_CALIBRATION_DATA  = 0x8152

KEEPALIVE_SEC   = 8     # < 10s send-timeout in SessionManager
READ_TIMEOUT    = 1.0

# Fake display info sent in CMD_RETURN_GET_DEVICE_INFO
SCREEN_W   = 1280
SCREEN_H   = 720
XDPI       = 160.0
YDPI       = 160.0
SDK_VER    = 21         # Android 5.0


# ─────────────────────────────────────────
# Logging
# ─────────────────────────────────────────
def log(msg):
    print(f"[{time.strftime('%H:%M:%S')}] {msg}", flush=True)


def hexs(b: bytes) -> str:
    return " ".join(f"{x:02X}" for x in b)


# ─────────────────────────────────────────
# Frame encoding / decoding  (Frame.java)
# ─────────────────────────────────────────
def esc_encode(data: bytes) -> bytes:
    """Escape special bytes in payload before wrapping with START/END."""
    out = bytearray()
    for b in data:
        if b == END_SYNC:
            out += bytes([ESC_HEAD, ESC_END_SYNC])
        elif b == ESC_HEAD:
            out += bytes([ESC_HEAD, ESC_ESC_HEAD])
        elif b == START_SYNC:
            out += bytes([ESC_HEAD, ESC_START_SYNC])
        else:
            out.append(b)
    return bytes(out)


def esc_decode(raw: bytes) -> bytes:
    """Unescape a raw frame body (between START and END, not including them)."""
    out = bytearray()
    i = 0
    while i < len(raw):
        b = raw[i]
        if b == ESC_HEAD and i + 1 < len(raw):
            nxt = raw[i + 1]
            if   nxt == ESC_END_SYNC:   out.append(END_SYNC)
            elif nxt == ESC_ESC_HEAD:   out.append(ESC_HEAD)
            elif nxt == ESC_START_SYNC: out.append(START_SYNC)
            else:                       out.append(nxt)  # shouldn't happen
            i += 2
        else:
            out.append(b)
            i += 1
    return bytes(out)


def build_frame(payload: bytes) -> bytes:
    return bytes([START_SYNC]) + esc_encode(payload) + bytes([END_SYNC])


def parse_frames(rxbuf: bytearray):
    """Extract all complete frames from rxbuf, consuming them in place."""
    frames = []
    while True:
        try:
            s = rxbuf.index(START_SYNC)
        except ValueError:
            rxbuf.clear()
            break
        try:
            e = rxbuf.index(END_SYNC, s + 1)
        except ValueError:
            if s > 0:
                del rxbuf[:s]   # discard garbage before START
            break
        raw_body = bytes(rxbuf[s + 1 : e])
        del rxbuf[: e + 1]
        frames.append(esc_decode(raw_body))
    return frames


# ─────────────────────────────────────────
# Session-layer helpers  (SessionManager.java)
# ─────────────────────────────────────────
def pack_session(subcmd: int, session_id: int, extra: bytes = b"") -> bytes:
    """Build a raw session payload (before frame-encoding).
    Format: F1 01 HI(subcmd) LO(subcmd) HI(sid) LO(sid) [extra...]
    """
    return bytes([
        CMD_SESSION_0, CMD_SESSION_1,
        (subcmd >> 8) & 0xFF, subcmd & 0xFF,
        (session_id >> 8) & 0xFF, session_id & 0xFF,
    ]) + extra


def pack_app_command(cmd_id: int, result_code: int = 0, extra: bytes = b"") -> bytes:
    """Build an app-level command using createBasicReturnCommand layout.
    Format: HI(cmd) LO(cmd) result 0 0 0 0 1 [extra...]
    """
    return bytes([
        (cmd_id >> 8) & 0xFF, cmd_id & 0xFF,
        result_code, 0, 0, 0, 0, 1,
    ]) + extra


# ─────────────────────────────────────────
# Server
# ─────────────────────────────────────────
class Server:
    def __init__(self):
        self.client      = None
        self.running     = False
        self.session_id  = 1      # assigned by HU; we track the last one seen
        self.last_rx_ts  = 0.0
        self.lock        = threading.Lock()

    # ── low-level send ──────────────────────────────────────────────────────
    def _send_raw(self, payload: bytes):
        frame = build_frame(payload)
        with self.lock:
            if self.client:
                try:
                    self.client.send(frame)
                    log(f"TX  {hexs(payload)}")
                except Exception as e:
                    log(f"TX error: {e}")

    # ── session layer ───────────────────────────────────────────────────────
    def send_session(self, subcmd: int, extra: bytes = b"", sid: int = None):
        sid = sid if sid is not None else self.session_id
        self._send_raw(pack_session(subcmd, sid, extra))

    def send_session_noop(self):
        self.send_session(SESSION_NOOP, b"")

    # ── app-command layer ───────────────────────────────────────────────────
    def send_app_cmd(self, cmd_id: int, result: int = 0, extra: bytes = b"", sid: int = None):
        """Wrap an app command in the SESSION_COMMAND envelope."""
        app_payload = pack_app_command(cmd_id, result, extra)
        self.send_session(SESSION_COMMAND, app_payload, sid=sid)

    # ── dispatch ────────────────────────────────────────────────────────────
    def handle_frame(self, pkt: bytes):
        if len(pkt) < 6:
            log(f"Short frame ignored: {hexs(pkt)}")
            return
        if pkt[0] != CMD_SESSION_0 or pkt[1] != CMD_SESSION_1:
            log(f"Unknown header: {hexs(pkt)}")
            return

        subcmd = (pkt[2] << 8) | pkt[3]
        sid    = (pkt[4] << 8) | pkt[5]
        body   = pkt[6:]
        self.session_id = sid

        log(f"RX  subcmd=0x{subcmd:04X} sid={sid} body={hexs(body)}")

        if subcmd == CONNECTION_NOTIFY_ESTABLISHED:
            # HU is ready — start session for our "launcher" connection string
            conn_str = b"com.example.applauncher\x00"
            self.send_session(SESSION_NOTIFY_CONNECT, conn_str, sid=sid)

        elif subcmd == SESSION_NOOP:
            # Keep-alive ping from HU — echo back
            self.send_session(SESSION_NOOP, b"", sid=sid)

        elif subcmd == SESSION_COMMAND:
            self.handle_app_command(body, sid)

        elif subcmd == CONNECTION_CLOSE or subcmd == SESSION_NOTIFY_DISCONNECT:
            log("HU requested disconnect")
            self.running = False

        else:
            log(f"Unhandled subcmd 0x{subcmd:04X}")

    def handle_app_command(self, body: bytes, sid: int):
        if len(body) < 2:
            return
        cmd_id = (body[0] << 8) | body[1]
        log(f"APP CMD=0x{cmd_id:04X}")

        if cmd_id == CMD_CONNECT:
            # Reply: RETURN_CONNECT (result=0) then NOTIFY_READY_CONNECT
            self.send_app_cmd(CMD_RETURN_CONNECT,    result=0, sid=sid)
            self.send_app_cmd(CMD_NOTIFY_READY_CONNECT, result=0, sid=sid)

        elif cmd_id == CMD_GET_DEVICE_INFO:
            # NegotiationReceiveMessageHandler.sendDeviceInfo()
            # basicCmd(8) + W(2) + H(2) + xdpi(4f) + ydpi(4f) + sdk(2)
            extra = (
                struct.pack(">H", SCREEN_W) +
                struct.pack(">H", SCREEN_H) +
                struct.pack(">f", XDPI)    +
                struct.pack(">f", YDPI)    +
                struct.pack(">H", SDK_VER)
            )
            self.send_app_cmd(CMD_RETURN_GET_DEVICE_INFO, result=0, extra=extra, sid=sid)

        elif cmd_id == CMD_GET_DEVICE_STAT:
            # AppObserver.sendDeviceState(0)
            # basicCmd(8) + changedMask(2) + orientation(1) + hdmi(1) +
            #              sleep(1) + speed(1) + country(4) + language(4) +
            #              wave(1) + space(1)
            extra = (
                struct.pack(">H", 0) +  # changedMask = 0
                bytes([2]) +            # LANDSCAPE = 2 (DeviceStateId)
                bytes([0]) +            # hdmi undetected
                bytes([1]) +            # screen on
                bytes([7]) +            # pointer speed default
                b"en\x00\x00" +        # country
                b"en\x00\x00" +        # language
                bytes([0]) +            # wave
                bytes([0])              # padding
            )
            self.send_app_cmd(CMD_NOTIFY_DEVICE_STAT, result=0, extra=extra, sid=sid)

        elif cmd_id == CMD_GET_CALIBRATION_DATA:
            # CalibrationReceiveMessageHandler — send empty calibration
            # 5 points × 4 bytes each = 20 bytes zeros is a safe default
            cal_data = bytes(20)
            self.send_app_cmd(CMD_RETURN_CALIBRATION_DATA, result=0, extra=cal_data, sid=sid)

        else:
            log(f"Unhandled app cmd 0x{cmd_id:04X} — ignoring")

    # ── keep-alive thread ───────────────────────────────────────────────────
    def keepalive_loop(self):
        while self.running:
            time.sleep(KEEPALIVE_SEC)
            if not self.running:
                break
            try:
                self.send_session_noop()
            except Exception as e:
                log(f"Keepalive error: {e}")
                break

    # ── client handler ──────────────────────────────────────────────────────
    def handle_client(self, client):
        self.client     = client
        self.running    = True
        self.last_rx_ts = time.time()

        try:
            client.settimeout(READ_TIMEOUT)
        except Exception:
            pass

        threading.Thread(target=self.keepalive_loop, daemon=True).start()

        rxbuf = bytearray()
        while self.running:
            try:
                data = client.recv(4096)
                if not data:
                    break
                self.last_rx_ts = time.time()
                rxbuf.extend(data)
                for pkt in parse_frames(rxbuf):
                    log(f"FRAME {hexs(pkt)}")
                    self.handle_frame(pkt)
            except bluetooth.btcommon.BluetoothError:
                if time.time() - self.last_rx_ts > KEEPALIVE_SEC * 4:
                    log("RX timeout — disconnecting")
                    break
                continue
            except Exception as e:
                log(f"RX error: {e}\n{traceback.format_exc()}")
                break

        self.running = False
        self.client  = None

    # ── main server loop ────────────────────────────────────────────────────
    def serve(self):
        register_sdp()

        sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
        sock.bind(("", RFCOMM_CHANNEL))   # "" = any local adapter
        sock.listen(1)

        log(f"Listening on RFCOMM ch={RFCOMM_CHANNEL}  uuid={APPLINK_UUID}")
        log(f"Service name: {SERVICE_NAME}")

        while True:
            client, info = sock.accept()
            log(f"Client connected: {info}")
            try:
                self.handle_client(client)
            except Exception as e:
                log(f"Client error: {e}\n{traceback.format_exc()}")
            finally:
                try:
                    client.close()
                except Exception:
                    pass
                log("Client disconnected — waiting for next connection")


# ─────────────────────────────────────────
# SDP registration helper
# ─────────────────────────────────────────
def register_sdp():
    """
    Advertise the SPP service in BlueZ SDP so the HU can discover it.
    Equivalent to Android's listenUsingRfcommWithServiceRecord().

    Requires: sdptool (part of bluez package) and bluetoothd running.
    Run once before accepting — the record persists until the process exits
    or you call 'sdptool del <handle>'.
    """
    try:
        cmd = [
            "sdptool", "add",
            "--channel", str(RFCOMM_CHANNEL),
            "--uuid",    APPLINK_UUID,
            "--norecord", f"--name={SERVICE_NAME}",
            "SP",
        ]
        # Simpler, more compatible form:
        cmd = [
            "sdptool", "add",
            f"--channel={RFCOMM_CHANNEL}",
            "SP",
        ]
        result = subprocess.run(cmd, capture_output=True, text=True, timeout=5)
        if result.returncode == 0:
            log("SDP record added (sdptool add SP)")
        else:
            log(f"sdptool warning (non-fatal): {result.stderr.strip()}")
    except FileNotFoundError:
        log("sdptool not found — skipping SDP registration (may still work if bluetoothd handles it)")
    except Exception as e:
        log(f"SDP registration error (non-fatal): {e}")

    # Also make sure bluetoothd is in compatibility mode for sdptool to work.
    # If not, you may need: sudo service bluetooth stop && sudo bluetoothd --compat &
    # Or edit /etc/systemd/system/bluetooth.service.d/override.conf:
    #   ExecStart=/usr/lib/bluetooth/bluetoothd --compat


# ─────────────────────────────────────────
# Entry point
# ─────────────────────────────────────────
if __name__ == "__main__":
    log("=== Kenwood AppLink Pi Server v4 ===")
    log("Make sure:")
    log("  1. Pi is paired with the HU")
    log("  2. bluetoothd running with --compat flag (for sdptool)")
    log("  3. rfcomm ch=3 is free: sudo rfcomm release 3")
    log("")
    Server().serve()
