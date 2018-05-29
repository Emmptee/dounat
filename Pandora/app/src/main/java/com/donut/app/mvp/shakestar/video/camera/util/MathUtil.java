package com.donut.app.mvp.shakestar.video.camera.util;

public class MathUtil
{
    //标准化数值
    public static int clamp(int val, int min, int max)
    {
        if(val < min) {
            return min;
        }
        if(val > max) {
            return max;
        }

        return val;
    }

    public static float clamp(float val, float min, float max)
    {
        if(val < min) {
            return min;
        }
        if(val > max) {
            return max;
        }

        return val;
    }
}
