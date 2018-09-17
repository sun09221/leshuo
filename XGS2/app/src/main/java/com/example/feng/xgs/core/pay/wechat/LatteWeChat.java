package com.example.feng.xgs.core.pay.wechat;

import android.app.Activity;

import com.example.feng.xgs.config.ConfigKeys;
import com.example.feng.xgs.config.IPerfectConfig;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by 傅令杰 on 2017/4/25
 */

public class LatteWeChat {

    public static final String APP_ID = IPerfectConfig.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
    public static final String APP_SECRET = IPerfectConfig.getConfiguration(ConfigKeys.WE_CHAT_APP_SECRET);
    private IWXAPI WXAPI;
    private IWeChatSignInCallback mSignInCallback = null;


//    public static LatteWeChat create(Activity activity){
//        return new LatteWeChat(activity);
//    }

    private static final class Holder {
        private static final LatteWeChat INSTANCE = new LatteWeChat();
    }

    public static LatteWeChat getInstance() {
        return Holder.INSTANCE;
    }

    private LatteWeChat() {
//        WXAPI = WXAPIFactory.createWXAPI(activity, APP_ID, true);
//        WXAPI.registerApp(APP_ID);
    }

    public LatteWeChat init(Activity activity){
        WXAPI = WXAPIFactory.createWXAPI(activity, APP_ID, true);
        WXAPI.registerApp(APP_ID);
        return this;
    }

    public final IWXAPI getWXAPI() {
        return WXAPI;
    }

    public LatteWeChat onSignSuccess(IWeChatSignInCallback callback) {
        this.mSignInCallback = callback;
        return this;
    }

    public IWeChatSignInCallback getSignInCallback() {
        return mSignInCallback;
    }

    public final void signIn() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "random_state";
        WXAPI.sendReq(req);
    }

}
