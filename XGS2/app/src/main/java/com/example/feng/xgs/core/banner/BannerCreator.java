package com.example.feng.xgs.core.banner;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.feng.xgs.R;

import java.util.List;

/**
 * Created by feng on 2017/12/20.
 */

public class BannerCreator {

    public static void setDefault(ConvenientBanner<String> convenientBanner,
                                  List<String> banners,
                                  OnItemClickListener clickListener){
        convenientBanner.setPages(new BannerHolder(),banners)
                .setPageIndicator(new int[]{R.drawable.banner_dot_normal, R.drawable.banner_dot})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(clickListener)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);

    }
}
