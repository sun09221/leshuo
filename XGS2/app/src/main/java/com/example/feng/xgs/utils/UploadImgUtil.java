package com.example.feng.xgs.utils;

import android.content.Context;
import android.util.Log;

import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.utils.file.FileUtil;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.qiyunutils.Auth;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

/**
 * Created by feng on 2018/1/3.
 */

public class UploadImgUtil {

    private String AccessKey = "6WZAXfpVzbxg0of-kIpQLi-EeG2JhgtBHNq41My0"; //ak
    private String SecretKey = "KIDDOxju46_y_ZbxjqtmYrMwP-CSBbQo5IULSvrs";


    public UploadImgUtil(){}

    public static UploadImgUtil create(){
        return new UploadImgUtil();
    }

    private String getUploadToken() {
        Auth auth = Auth.create(AccessKey, SecretKey);
        return auth.uploadToken("bensejiaoyou");
    }

    public UploadImgUtil loader(Context context){
        NaturalLoader.showLoading(context);
        return this;
    }



    public void uploadImg(String path, IUploadImgListener listener){
        Log.d("TYPE", "uploadImg: "+path);
        this.mListener = listener;

        UploadManager uploadManager=new UploadManager();
        long time = System.currentTimeMillis();
        LogUtils.d("uploadImg: " + time);
        String img_key = "img_" + time;
        String token = getUploadToken();

        uploadManager.put(path, img_key, token, new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo info, org.json.JSONObject response) {
                NaturalLoader.stopLoading();
                if (info.isOK()) {
//        返回的网址，域名+key
//                    final String headPath = UrlKeys.BASE_URL + key;
//                    final String headPath = "http://p0ze2yees.bkt.clouddn.com/img_20171220154032";
//                    Log.d("mineFragment", "complete: 返回的图片路径"+headPath);
                    String path = UrlKeys.BASE_QINIU_URL + key;
                    LogUtils.d("uploadImg: "+path);
                    if(mListener != null){
                        mListener.onSuccess(path);
                    }
                }else{
                    //错误信息显示
                    if(mListenerErro != null){
                        mListenerErro.onError();
                    }

                    NaturalLoader.stopLoading();
                    LogUtils.d("uploadImg: "+info.error.toString());
                }
            }
        },null);
    }



    public void uploadVideo(String path, IUploadImgListener listener){
        Log.d("TYPE", "uploadImg: "+path);
        this.mListener = listener;

        UploadManager uploadManager=new UploadManager();
        long time = System.currentTimeMillis();
        String extension = FileUtil.getExtension(path);
        String img_key = "video_" + time;
        String token = getUploadToken();

//        UploadOptions options = new UploadOptions();

        uploadManager.put(path, img_key, token, new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo info, org.json.JSONObject response) {
                NaturalLoader.stopLoading();
                if (info.isOK()) {
                    String path = UrlKeys.BASE_QINIU_URL + key;
                    LogUtils.d("uploadImg: "+path);
                    if(mListener != null){
                        mListener.onSuccess(path);
                    }
                }else{
                    //错误信息显示
                    if(mListenerErro != null){
                        mListenerErro.onError();
                    }
                    LogUtils.d("uploadImg: "+info.error.toString());
                }
            }
        },null);
    }



    public UploadImgUtil setErrorListener(IUploadErrorListener listener){
        this.mListenerErro = listener;
        return this;
    }

    private IUploadImgListener mListener;
    private IUploadErrorListener mListenerErro;

    public interface IUploadImgListener{
        void onSuccess(String path);
    }

    public interface IUploadErrorListener{
        void onError();
    }
}
