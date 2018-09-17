package com.example.feng.xgs.utils.qiyunutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 类注释: 上传图片到七牛云
 * 作者: QSFan
 * 邮箱: qsfan_vip.163.com
 * 时间: 2017/3/6 11:07
 * 版本:
 * 备注：
 */
public class QiNiuUtils {
    private static final String AccessKey = "6WZAXfpVzbxg0of-kIpQLi-EeG2JhgtBHNq41My0"; //ak
    private static final String SecretKey = "KIDDOxju46_y_ZbxjqtmYrMwP-CSBbQo5IULSvrs";


    public static String getUploadToken() {
        Auth auth = Auth.create(AccessKey, SecretKey);
        return auth.uploadToken("xibaoapp");
    }

    public interface getUrlLinster {
        void getUrl(String url);
    }

    private getUrlLinster getUrlLinster;

    public void setGetUrlLinster(getUrlLinster getUrlLinster) {
        this.getUrlLinster = getUrlLinster;
    }


    /**
     * 上传操作
     *
     * @param context
     * @param filePath
     */
    public void upImage(final Context context, String filePath) {
//        Configuration config = new Configuration.Builder().zone(Zone.zone2).build();
        File file = new File(filePath);
        String file1 = saveBitmapToFile(file, filePath);
//        System.out.println(">>filePath>>" + filePath + ">>>fil111>>" + file1);
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String uid = SharedPreferenceUtils.getUserId();
        String key = "xibao/IMG-" + sdf.format(new Date()) + uid + ".jpg";

        uploadManager.put(file1, key, getUploadToken(), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                // info.error中包含了错误信息，可打印调试
                // 上传成功后将key值上传到自己的服务器
                if (info.isOK()) {
                    Log.i("qiniu", "Upload Success" + info.toString());
                    try {
                        String fileKey = res.getString("key");
                        String domain = "http://image.xibao.shanchuang360.com/" + fileKey;
                        //TODO 返回url
                        if (getUrlLinster != null) {
                            getUrlLinster.getUrl(domain);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("qiniu", "Upload Fail" + info.toString());
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                }
            }
        }, new UploadOptions(null, null, false,
                new UpProgressHandler() {

                    private ProgressDialog progressDialog = new ProgressDialog(context);

                    // downloadDialog.setIcon(R.drawable.ic_launcher);
                    public void progress(String key, double percent) {//上传进度
                        Log.i("qiniu", key + ": " + percent);
                        double a = percent * 100;
                        int b = (int) a;
                        progressDialog.setTitle("正在上传请稍等");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setMax(100);
                        progressDialog.setProgress(b);
                        progressDialog.show();
                        if (percent == 1) {
                            progressDialog.dismiss();
                        }
                    }
                }, null));
    }


    /**
     * 压缩图片
     *
     * @param file    要压缩的图片地址
     * @param newpath 压缩后图片地址
     * @return
     */
    public static String saveBitmapToFile(File file, String newpath) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
//            file.createNewFile();
//
//
//            FileOutputStream outputStream = new FileOutputStream(file);
//
//            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);


            File aa = new File(newpath);

            FileOutputStream outputStream = new FileOutputStream(aa);

            //choose another format if PNG doesn't suit you

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);


            String filepath = aa.getAbsolutePath();
            Log.e("qsf", aa.getAbsolutePath());

            return filepath;
        } catch (Exception e) {
            return null;
        }
    }

    public void upImages() {
        byte[] data=new byte[]{ 0, 1, 2, 3, 3, 4, 5, 6,0, 1, 2, 3, 4, 5, 6,0, 1, 2, 3, 4, 5, 6,0, 1, 2, 3, 4, 5, 6,0, 1, 2, 3, 4, 5, 6,0, 1, 2, 3,};

        for(int i=0;i<data.length;i++){
            String expectKey = UUID.randomUUID().toString();
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(data, expectKey, getUploadToken(), new UpCompletionHandler() {
                public void complete(String k, ResponseInfo rinfo, JSONObject response) {
                    String s = k + ", "+ rinfo + ", " + response;
                    Log.i("qiniutest", s);
//                    String key = getKey(k, response);
//                    String o = hint.getText() + "\r\n\r\n";
//                    hint.setText(o + s + "http://xm540.com1.z0.glb.clouddn.com/" + key);
                }
            }, new UploadOptions(null, "test-type", true, null, null));
        }
    }
}
