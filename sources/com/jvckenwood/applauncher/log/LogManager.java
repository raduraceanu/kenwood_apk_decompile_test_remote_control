package com.jvckenwood.applauncher.log;

import android.util.Log;
import com.jvckenwood.carconnectcontrol.DebugMode;
import com.jvckenwood.tools.AppLog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class LogManager {
    private static final String C_LOG_FILE_NAME = "idp.log";
    private static final long C_LOG_SIZE_LIMIT = 1047552;
    private static final String C_OLD_LOG_FILE_NAME = "idp.log.old";
    private static final SimpleDateFormat _fmtDt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.US);
    private static final HashMap<LogLevel, Character> _mapLevelChar = new HashMap<>();
    private static LogManager _singleton = null;
    private File _dirLog;
    private File _fileLog;
    private File _fileOldLog;
    private boolean _isTagOutput;
    private LogLevel _levelLogCat;
    private LogLevel _levelLogFile;
    private AppLog.IImplement _logImpl;
    private PrintWriter _pwLog;

    public enum LogLevel {
        Verbose,
        Debug,
        Info,
        Warning,
        Error,
        Silent
    }

    public LogManager() {
        _mapLevelChar.put(LogLevel.Verbose, 'V');
        _mapLevelChar.put(LogLevel.Debug, 'D');
        _mapLevelChar.put(LogLevel.Info, 'I');
        _mapLevelChar.put(LogLevel.Warning, 'W');
        _mapLevelChar.put(LogLevel.Error, 'E');
        this._levelLogCat = LogLevel.Silent;
        this._levelLogFile = LogLevel.Silent;
        this._dirLog = null;
        this._fileLog = null;
        this._fileOldLog = null;
        this._pwLog = null;
        this._isTagOutput = false;
        this._logImpl = new AppLog.IImplement() { // from class: com.jvckenwood.applauncher.log.LogManager.1
            @Override // com.jvckenwood.tools.AppLog.IImplement
            public int e(String tag, String msg) {
                if (DebugMode.isLoggingEnabled()) {
                    LogLevel levelMe = LogLevel.Error;
                    LogManager.this.writeLogFile(levelMe, tag, msg, null);
                    LogManager.this.writeLogCat(levelMe, tag, msg, null);
                }
                return 6;
            }

            @Override // com.jvckenwood.tools.AppLog.IImplement
            public int w(String tag, String msg) {
                if (DebugMode.isLoggingEnabled()) {
                    LogLevel levelMe = LogLevel.Warning;
                    LogManager.this.writeLogFile(levelMe, tag, msg, null);
                    LogManager.this.writeLogCat(levelMe, tag, msg, null);
                }
                return 5;
            }

            @Override // com.jvckenwood.tools.AppLog.IImplement
            public int i(String tag, String msg) {
                if (DebugMode.isLoggingEnabled()) {
                    LogLevel levelMe = LogLevel.Info;
                    LogManager.this.writeLogFile(levelMe, tag, msg, null);
                    LogManager.this.writeLogCat(levelMe, tag, msg, null);
                }
                return 4;
            }

            @Override // com.jvckenwood.tools.AppLog.IImplement
            public int d(String tag, String msg) {
                if (DebugMode.isLoggingEnabled()) {
                    LogLevel levelMe = LogLevel.Debug;
                    LogManager.this.writeLogFile(levelMe, tag, msg, null);
                    LogManager.this.writeLogCat(levelMe, tag, msg, null);
                }
                return 3;
            }

            @Override // com.jvckenwood.tools.AppLog.IImplement
            public int v(String tag, String msg) {
                if (DebugMode.isLoggingEnabled()) {
                    LogLevel levelMe = LogLevel.Verbose;
                    LogManager.this.writeLogFile(levelMe, tag, msg, null);
                    LogManager.this.writeLogCat(levelMe, tag, msg, null);
                }
                return 2;
            }

            @Override // com.jvckenwood.tools.AppLog.IImplement
            public int e(String tag, String msg, Throwable err) {
                if (DebugMode.isLoggingEnabled()) {
                    LogLevel levelMe = LogLevel.Error;
                    LogManager.this.writeLogFile(levelMe, tag, msg, err);
                    LogManager.this.writeLogCat(levelMe, tag, msg, err);
                }
                return 6;
            }

            @Override // com.jvckenwood.tools.AppLog.IImplement
            public int w(String tag, String msg, Throwable err) {
                if (DebugMode.isLoggingEnabled()) {
                    LogLevel levelMe = LogLevel.Warning;
                    LogManager.this.writeLogFile(levelMe, tag, msg, err);
                    LogManager.this.writeLogCat(levelMe, tag, msg, err);
                }
                return 5;
            }

            @Override // com.jvckenwood.tools.AppLog.IImplement
            public int i(String tag, String msg, Throwable err) {
                if (DebugMode.isLoggingEnabled()) {
                    LogLevel levelMe = LogLevel.Info;
                    LogManager.this.writeLogFile(levelMe, tag, msg, err);
                    LogManager.this.writeLogCat(levelMe, tag, msg, err);
                }
                return 4;
            }

            @Override // com.jvckenwood.tools.AppLog.IImplement
            public int d(String tag, String msg, Throwable err) {
                if (DebugMode.isLoggingEnabled()) {
                    LogLevel levelMe = LogLevel.Debug;
                    LogManager.this.writeLogFile(levelMe, tag, msg, err);
                    LogManager.this.writeLogCat(levelMe, tag, msg, err);
                }
                return 3;
            }

            @Override // com.jvckenwood.tools.AppLog.IImplement
            public int v(String tag, String msg, Throwable err) {
                if (DebugMode.isLoggingEnabled()) {
                    LogLevel levelMe = LogLevel.Verbose;
                    LogManager.this.writeLogFile(levelMe, tag, msg, err);
                    LogManager.this.writeLogCat(levelMe, tag, msg, err);
                }
                return 2;
            }
        };
    }

    public static synchronized void setup(LogLevel levelLogCat, LogLevel levelLogFile, File logDir) {
        try {
            if (_singleton == null) {
                _singleton = new LogManager();
            }
            _singleton.internalSetup(levelLogCat, levelLogFile, logDir);
        } catch (Exception e) {
            if (_singleton != null) {
                _singleton.writeLogCat(LogLevel.Error, "LogManager", e.getMessage(), e);
            }
        }
    }

    public static synchronized void setTagOutput(boolean sw) {
        try {
            if (_singleton == null) {
                _singleton = new LogManager();
            }
            _singleton._isTagOutput = sw;
        } catch (Exception e) {
            if (_singleton != null) {
                _singleton.writeLogCat(LogLevel.Error, "LogManager", e.getMessage(), e);
            }
        }
    }

    public static synchronized boolean getTagOutput() {
        boolean z;
        try {
            if (_singleton == null) {
                _singleton = new LogManager();
            }
            z = _singleton._isTagOutput;
        } catch (Exception e) {
            if (_singleton != null) {
                _singleton.writeLogCat(LogLevel.Error, "LogManager", e.getMessage(), e);
            }
            z = false;
        }
        return z;
    }

    private void internalSetup(LogLevel levelLogCat, LogLevel levelLogFile, File logDir) throws IOException {
        this._levelLogCat = levelLogCat;
        this._levelLogFile = levelLogFile;
        this._dirLog = null;
        this._fileLog = null;
        this._fileOldLog = null;
        this._pwLog = null;
        AppLog.setImplement(this._logImpl);
        if (DebugMode.isLoggingEnabled() && this._levelLogFile != LogLevel.Silent) {
            this._dirLog = logDir;
            if (!this._dirLog.exists()) {
                this._dirLog.mkdirs();
            }
            if (!this._dirLog.exists()) {
                throw new IOException(String.format("Log directory create error(%s).", logDir));
            }
            this._fileLog = new File(this._dirLog, C_LOG_FILE_NAME);
            this._fileOldLog = new File(this._dirLog, C_OLD_LOG_FILE_NAME);
            switchLogFile();
        }
    }

    private synchronized void switchLogFile() {
        if (C_LOG_SIZE_LIMIT <= this._fileLog.length()) {
            if (this._pwLog != null) {
                this._pwLog.close();
                this._pwLog = null;
            }
            if (this._fileOldLog.exists() && !this._fileOldLog.delete()) {
                writeLogCat(LogLevel.Error, getClass().getName(), "Old log file delete error.", null);
            }
            if (!this._fileLog.renameTo(this._fileOldLog)) {
                writeLogCat(LogLevel.Error, getClass().getName(), "log file move error.", null);
            }
            this._fileLog = new File(this._dirLog, C_LOG_FILE_NAME);
        }
        if (this._pwLog == null) {
            try {
                this._pwLog = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this._fileLog, true), "UTF-8"));
            } catch (FileNotFoundException e) {
                writeLogCat(LogLevel.Error, getClass().getName(), "Log file open error", null);
            } catch (UnsupportedEncodingException e2) {
                writeLogCat(LogLevel.Error, getClass().getName(), "Unsupported encode \"UTF-8\"", null);
            } catch (SecurityException e3) {
                writeLogCat(LogLevel.Error, getClass().getName(), "Log file open error(security error)", null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void writeLogFile(LogLevel logLevel, String tag, String message, Throwable err) {
        if (logLevel.ordinal() >= this._levelLogFile.ordinal() && this._pwLog != null) {
            char chLogLevel = '?';
            if (_mapLevelChar.containsKey(logLevel)) {
                chLogLevel = _mapLevelChar.get(logLevel).charValue();
            }
            switchLogFile();
            if (this._isTagOutput) {
                this._pwLog.format("%s,%c,%s:%s", _fmtDt.format(new Date()), Character.valueOf(chLogLevel), tag, message);
            } else {
                this._pwLog.format("%s,%c,%s", _fmtDt.format(new Date()), Character.valueOf(chLogLevel), message);
            }
            this._pwLog.println();
            if (err != null) {
                err.printStackTrace(this._pwLog);
                this._pwLog.println();
            }
            this._pwLog.flush();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeLogCat(LogLevel logLevel, String tag, String message, Throwable err) {
        if (logLevel.ordinal() >= this._levelLogCat.ordinal()) {
            switch (logLevel) {
                case Verbose:
                    if (err == null) {
                        Log.v(tag, message);
                    } else {
                        Log.v(tag, message, err);
                    }
                    break;
                case Debug:
                    if (err == null) {
                        Log.d(tag, message);
                    } else {
                        Log.d(tag, message, err);
                    }
                    break;
                case Info:
                    if (err == null) {
                        Log.i(tag, message);
                    } else {
                        Log.i(tag, message, err);
                    }
                    break;
                case Warning:
                    if (err == null) {
                        Log.w(tag, message);
                    } else {
                        Log.w(tag, message, err);
                    }
                    break;
                case Error:
                    if (err == null) {
                        Log.e(tag, message);
                    } else {
                        Log.e(tag, message, err);
                    }
                    break;
            }
        }
    }
}
