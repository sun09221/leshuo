package com.example.feng.xgs.core.banner.integer;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.feng.xgs.R;

import java.util.List;

/**
 * Created by feng on 2018/1/3.
 */

public class BannerCreatorInteger {

    public static void setDefault(ConvenientBanner<Integer> convenientBanner,
                                  List<Integer> banners,
                                  OnItemClickListener clickListener){
        convenientBanner.setPages(new BannerHolderInteger(),banners)
                .setPageIndicator(new int[]{R.drawable.banner_dot_normal, R.drawable.banner_dot})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(clickListener)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);

    }
}
