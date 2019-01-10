package com.mx.cqbookstore.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/12/20.
 */

public class MyHandler extends Handler {
    public final static int JSONFEIL=0x003;
    public final static int SUCEESS=0x001;
    public final static int REQFEIL=0x002;
    public final static int NONET=0x004;
    Context ctx;

    public MyHandler(Context ctx) {
        this.ctx = ctx;
    }

    public MyHandler(Callback callback, Context ctx) {
        super(callback);
        this.ctx = ctx;
    }

    public MyHandler(Looper looper, Context ctx) {
        super(looper);
        this.ctx = ctx;
    }

    public MyHandler(Looper looper, Callback callback, Context ctx) {
        super(looper, callback);
        this.ctx = ctx;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what==NONET)
            Toast.makeText(ctx,"无可用网络",Toast.LENGTH_SHORT).show();
        if (msg.what==JSONFEIL)
            Toast.makeText(ctx,"Json解析失败",Toast.LENGTH_SHORT).show();
        if (msg.what==REQFEIL) {
            Toast.makeText(ctx, (String)(msg.obj), Toast.LENGTH_SHORT).show();
        }
        super.handleMessage(msg);
    }
}
