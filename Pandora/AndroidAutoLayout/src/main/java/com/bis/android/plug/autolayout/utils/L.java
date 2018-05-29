package com.bis.android.plug.autolayout.utils;

import android.util.Log;

/**
 * Created  on 15/11/18.
 */
public class L
{
    public static boolean debug = false;
    private static final String TAG = "AUTO_LAYOUT";

    public static void e(String msg)
    {
        if (debug)
        {
            Log.e(TAG, msg);
        }
    }


}
