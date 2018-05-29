package com.donut.app.utils;

import android.content.Context;
import android.support.annotation.UiThread;
import android.widget.Toast;

public class ToastUtil {
    // Toast
    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    @UiThread
    public static void showShort(Context context, CharSequence message) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                // toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                toast.setText(message);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    @UiThread
    public static void showShort(Context context, int message) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                // toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                toast.setText(message);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    @UiThread
    public static void showLong(Context context, CharSequence message) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                // toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                toast.setText(message);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    @UiThread
    public static void showLong(Context context, int message) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                // toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                toast.setText(message);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    @UiThread
    public static void show(Context context, CharSequence message, int duration) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, message, duration);
                // toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                toast.setText(message);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    @UiThread
    public static void show(Context context, int message, int duration) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, message, duration);
                // toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                toast.setText(message);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide the toast, if any.
     */
    @UiThread
    public static void hideToast() {
        try {
            if (null != toast) {
                toast.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
