package com.example.feng.xgs.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

/**
 * Created by feng on 2017/12/4.
 * 动态修改文字大小和颜色
 */

public class SpannableUtil {


    /**
     * 改变字体颜色
     * */
    public static SpannableStringBuilder setTextAndColor(Context context, String text, String divider, int color){
        int first = text.indexOf(divider);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if(first < 0){
            return builder;
        }
        CharacterStyle style = new ForegroundColorSpan(ContextCompat.getColor(context, color));
        builder.setSpan(style,first,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }


    /**
     * 改变字体大小
     * */
    public static SpannableStringBuilder setTextAndColor(String text, String divider, int size){
        int first = text.indexOf(divider);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        if(first < 0){
            return builder;
        }

        CharacterStyle styleSize = new AbsoluteSizeSpan(size);
        builder.setSpan(styleSize,first,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }


    /**
     * 改变字体大小和颜色
     * */
    public static SpannableStringBuilder setTextAndColor(Context context, String text, String divider, int color, int size){
        int first = text.indexOf(divider);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if(first < 0){
            return builder;
        }

        CharacterStyle style = new ForegroundColorSpan(ContextCompat.getColor(context, color));
        builder.setSpan(style,first,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        CharacterStyle styleSize = new AbsoluteSizeSpan(DensityUtils.dp2px(context,size));
        builder.setSpan(styleSize,first,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }
}
