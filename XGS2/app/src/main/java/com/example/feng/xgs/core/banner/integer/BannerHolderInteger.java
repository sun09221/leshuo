package com.example.feng.xgs.core.banner.integer;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

/**
 * Created by feng on 2018/1/3.
 */

public class BannerHolderInteger implements CBViewHolderCreator<ImageHolderInteger>{
    @Override
    public ImageHolderInteger createHolder() {
        return new ImageHolderInteger();
    }
}
