package com.example.feng.xgs.config;

import android.os.Handler;

import java.util.HashMap;

/**
 * Created by feng on 2017/12/18.
 * 添加全局配置
 */

public class Configurator {
    private static final HashMap<Object, Object> CONFIGS = new HashMap<>();
    private static final Handler HANDLER = new Handler();

    private Configurator(){
        CONFIGS.put(ConfigKeys.CONFIG_READY,false);
    }

    public void configure(){
        CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
        CONFIGS.put(ConfigKeys.CONFIG_READY, true);
    }

    public HashMap<Object, Object> getConfigs(){
        return CONFIGS;
    }

    public Configurator withApiHost(String apiHost){
        CONFIGS.put(ConfigKeys.API_HOST,apiHost);
        return this;
    }


    public Configurator withJavaScript(String name){
        CONFIGS.put(ConfigKeys.JAVASCRIPT_INTERFACE,name);
        return this;
    }

    public Configurator withMapKey(String name){
        CONFIGS.put(ConfigKeys.MAP_KEY,name);
        return this;
    }
    public Configurator withMapKey2(String name){
        CONFIGS.put(ConfigKeys.MAP_KEY,name);
        return this;
    }

    public Configurator withWeChatAppId(String name){
        CONFIGS.put(ConfigKeys.WE_CHAT_APP_ID,name);
        return this;
    }

    public Configurator withWeChatAppSecret(String name){
        CONFIGS.put(ConfigKeys.WE_CHAT_APP_SECRET,name);
        return this;
    }

//    public final Configurator withWebEvent(@NonNull String name, @NonNull Event event){
//        EventManager manager = EventManager.getInstance();
//        manager.addEvent(name, event);
//        return this;
//    }


    public <T> T getConfiguration(Object key){
        checkConfiguration();
        Object value = CONFIGS.get(key);
        if(value == null){
            throw new NullPointerException(key.toString() +"is null");
        }
        return (T) value;
    }

    private void checkConfiguration(){
        final boolean isReady = (boolean) CONFIGS.get(ConfigKeys.CONFIG_READY);
        //如果配置没有完成，却急着去操作，抛出运行时异常
        if(!isReady){
            throw new RuntimeException("Configuration is not ready");
        }
    }


    private static final class Holder{
        private static final Configurator INSTANCE = new Configurator();
    }

    public static Configurator getInstance(){
        return Holder.INSTANCE;
    }
}
