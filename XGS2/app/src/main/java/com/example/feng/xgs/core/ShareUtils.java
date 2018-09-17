package com.example.feng.xgs.core;

import android.app.Activity;

import cn.sharesdk.onekeyshare.OnekeyShare;

//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by feng on 2018/6/20 0020.
 * 分享
 */

public class ShareUtils {

    private final OnekeyShare oks;
    private Activity mActivity;

    private ShareUtils(Activity activity){
        this.mActivity = activity;
        oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("乐说");

        oks.setText("乐说");

//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                LogUtils.d("share: onComplete");
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                LogUtils.d("share: onError" + i);
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//                LogUtils.d("share: onCancel" + i );
//            }
//        });

    }

    public static ShareUtils create(Activity activity){
        return new ShareUtils(activity);
    }

    public void showDialog(){
        oks.show(mActivity);
    }

    public ShareUtils setText(String text){
        oks.setText(text);
        return this;
    }
    public ShareUtils setTitleUrl(String url){
        oks.setTitleUrl(url);
        return this;
    }
    public ShareUtils setImgUrl(String imgUrl){
        oks.setImageUrl(imgUrl);
        return this;
    }


    public ShareUtils setImageArray(String[] imageArray){
        oks.setImageArray(imageArray);
        return this;
    }

    public ShareUtils setVideoUrl(String videoUrl){
        oks.setVideoUrl(videoUrl);

        return this;
    }

    public ShareUtils setUrl(String url){
        oks.setUrl(url);
        return this;
    }
    public ShareUtils setImaPath(String imaPath){
        oks.setImagePath(imaPath);
        return this;
    }



}
