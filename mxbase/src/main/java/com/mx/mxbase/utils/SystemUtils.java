package com.mx.mxbase.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by xj on 2017/12/19.
 */

public class SystemUtils {

    /**
     * 熄灭屏幕
     */
    public static void putOutScreen(Context context){
        final String DEVICE_GOTO_SLEEP_ACTION = "device_goto_sleep_action";
        context.sendBroadcast(new Intent(DEVICE_GOTO_SLEEP_ACTION));
    }
}
