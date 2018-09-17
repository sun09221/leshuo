package com.example.feng.xgs.core.banner;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

/**
 * Created by feng on 2017/12/20.
 */

public class BannerHolder implements CBViewHolderCreator<ImageHolder>{
    @Override
    public ImageHolder createHolder() {
        return new ImageHolder();
    }
}
