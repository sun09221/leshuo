package com.example.feng.xgs.config;

import android.content.Context;

/**
 * Created by feng on 2017/12/18.
 * 获取全局配置
 */
public class IPerfectConfig {

    public static Configurator init(Context context){
        getConfigurator().getConfigs()
                .put(ConfigKeys.APPLICATION_CONTEXT,context.getApplicationContext());
        return Configurator.getInstance();
    }

    public static <T> T getConfiguration(Object key) {
        return getConfigurator().getConfiguration(key);
    }

    public static Context getApplicationContext(){
        return getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
    }


    public static Configurator getConfigurator(){
        return Configurator.getInstance();
    }
}
