package com.example.feng.xgs.core.banner.integer;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Created by feng on 2018/1/3.
 */

public class ImageHolderInteger implements Holder<Integer>{

    private ImageView mImage;

    @Override
    public View createView(Context context) {
        mImage = new ImageView(context);
        return mImage;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        mImage.setBackgroundResource(data);
    }
}
