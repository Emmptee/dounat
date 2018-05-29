package com.donut.app.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.donut.app.R;

import java.text.DecimalFormat;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class BindingUtils {

    @BindingAdapter({"imageUrl"})
    public static void loadImg(ImageView v, String url) {
        Glide.with(v.getContext())
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.default_bg)
                .error(R.drawable.default_bg)
                .into(v);
    }

    @BindingAdapter({"roundImageUrl"})
    public static void loadRoundImg(ImageView v, String url) {
        Glide.with(v.getContext())
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(v.getContext()))
                .into(v);
    }

    public static String formatCountdown(long payCountdown) {
        int minute = (int) (payCountdown / 60);
        int second = (int) (payCountdown % 60);

        String strSecond = String.valueOf(second);
        if (second < 10) {
            strSecond = "0" + strSecond;
        }
        return minute + ":" + strSecond;
    }

    public static String formatNum(long num) {
        if (num > 10000) {
            DecimalFormat df = new DecimalFormat("0.0");
            return df.format(num / 10000F) + "万";
        }
        return String.valueOf(num);
    }
}
