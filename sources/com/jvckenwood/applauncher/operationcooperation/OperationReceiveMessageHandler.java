package com.jvckenwood.applauncher.operationcooperation;

import android.content.Context;
import android.content.Intent;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.ce.globalactor.GlobalActorService;
import com.jvckenwood.cooperationlib.CommandId;
import com.jvckenwood.cooperationlib.CommandUtil;

/* JADX INFO: loaded from: classes.dex */
public class OperationReceiveMessageHandler implements ICooperationLib.IReceiveMessageHandler {
    public static final String TAG = "OperationReceiveMessageHandler";
    public static final int[] commands = {CommandId.CMD_SEND_KEY.value};
    private Context _context = null;

    public static void register(Context context, ICooperationLib sender) {
        OperationReceiveMessageHandler o = new OperationReceiveMessageHandler();
        o.internalRegister(context, sender);
    }

    private void internalRegister(Context context, ICooperationLib sender) {
        this._context = context;
        int[] arr$ = commands;
        for (int commandId : arr$) {
            sender.addReceiveMessageHandler(commandId, this);
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveMessageHandler
    public void onReceiveMessage(ICooperationLib sender, byte[] message) {
        int commandId = CommandUtil.readInt(message, 0, 2);
        if (commandId == CommandId.CMD_SEND_KEY.value) {
            int key = message[2];
            switch (key) {
                case 1:
                    this._context.sendBroadcast(new Intent(GlobalActorService.ACTION_HOMEBUTTON));
                    break;
                case 2:
                    this._context.sendBroadcast(new Intent(GlobalActorService.ACTION_BACKBUTTON));
                    break;
                case 3:
                    this._context.sendBroadcast(new Intent(GlobalActorService.ACTION_TASKBUTTON));
                    break;
            }
        }
    }
}
