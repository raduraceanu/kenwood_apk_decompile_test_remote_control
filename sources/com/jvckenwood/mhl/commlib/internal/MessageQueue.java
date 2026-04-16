package com.jvckenwood.mhl.commlib.internal;

import com.jvckenwood.mhl.commlib.tools.AppLog;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class MessageQueue {
    private static final String TAG = MessageQueue.class.getSimpleName();
    private final LinkedList<Message> _queue = new LinkedList<>();

    public synchronized Message getMessage() {
        Message messageRemoveFirst;
        while (this._queue.size() <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                AppLog.e(TAG, e.getMessage());
                messageRemoveFirst = null;
            }
        }
        messageRemoveFirst = this._queue.removeFirst();
        return messageRemoveFirst;
    }

    public synchronized void putMessage(Message message) {
        this._queue.addLast(message);
        notifyAll();
    }

    public synchronized void clear() {
        this._queue.clear();
    }

    public synchronized int getCount() {
        return this._queue.size();
    }
}
