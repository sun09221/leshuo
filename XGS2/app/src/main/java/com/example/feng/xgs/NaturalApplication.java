package com.example.feng.xgs;

import android.support.multidex.MultiDexApplication;

import com.example.feng.xgs.chatui.ui.activity.LocationService;
import com.example.feng.xgs.config.IPerfectConfig;
import com.example.feng.xgs.config.key.UrlKeys;
import com.mob.MobSDK;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by feng on 2018/3/15 0015.
 * application
 */

public class NaturalApplication extends MultiDexApplication{
    public static LocationService locationService;
    @Override
    public void onCreate() {
    //
        super.onCreate();

        IPerfectConfig.init(this)
                .withApiHost(UrlKeys.BASE_URL)
                .withWeChatAppId("wx4ef8eca6f0338949")
                .withWeChatAppSecret("15c062953fa75f14962ccb1e390c6400")
                .withMapKey("6995160db35b1961e9e38de4c730de1a")
                .configure();

        JMessageClient.init(this, false);
        JMessageClient.setDebugMode(true);
        locationService = new LocationService(getApplicationContext());
        MobSDK.init(this);
    }
}
