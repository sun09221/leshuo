package com.example.feng.xgs.main.move.publish;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.PermissionDialog;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.core.video.LandLayoutVideo;
import com.example.feng.xgs.utils.DateUtils;
import com.example.feng.xgs.utils.UploadImgUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.file.UriUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/28 0028.
 */

public class NaturalPublishVideoActivity extends ToolbarActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.et_natural_publish)
    EditText mEtInfo;

    @BindView(R.id.switch_natural_publish)
    Switch mSwitch;
    @BindView(R.id.tv_toolbar_save)
    TextView mTvPublish;

    @BindView(R.id.iv_natural_publish_video)
    ImageView mIvAdd;
    @BindView(R.id.video_natural_publish_video)
    LandLayoutVideo detailPlayer;

    private OrientationUtils orientationUtils;
    private boolean isPause;
    private boolean isPlay;
    private FileOutputStream  out;
    private ImageView videofaceImage;

    private String mContent;

    private boolean isUploading = false;//是否正在上传图片
    private boolean isVideoUploading = false;//是否正在上传视频
    private String mType;
    private String mVideoPath;
    private String circleid = "";
    private Dialog dialog;
    private Activity mActivity;
    private Uri fileUri;
    private  String mCurrentPhotoPath;
    private int dur;
    private View inflate;
    private TextView choosePhoto;
    private TextView takePhoto;
    private String imagepath;
    private String a;
    //发布
    @OnClick(R.id.tv_toolbar_save)
    void onClickPublish() {
        if (!isUploading) {
            checkParameter();
        } else {
            ToastUtils.showText(this, "正在发布，请稍候...");
        }

    }


    //选择视频
    @OnClick(R.id.iv_natural_publish_video)
    void onClickAdd() {
        requestCameraPermission();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_natural_video_publish;
    }


    @Override
    public void onBindView(Bundle savedInstanceState) {
        mSwitch.setOnCheckedChangeListener(this);
        mTvPublish.setText(getString(R.string.publish));

        Intent intent = getIntent();
        mType = intent.getStringExtra(ContentKeys.ACTIVITY_TYPE);
        circleid = intent.getStringExtra("circleid");
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        setTitle(title);

    }

    /**
     * 1.先检验输入内容和圈子id不为空
     * 2.然后上传图片
     * 3.图片上传完毕，再发布动态
     */
    private void checkParameter() {

        if (!ContentKeys.NATURAL_PUBLISH_IMAGE.equals(mType) && !ContentKeys.NATURAL_PUBLISH_VIDEO.equals(mType)) {
            return;
        }

        mContent = mEtInfo.getText().toString();
        if (TextUtils.isEmpty(mContent)) {
            ToastUtils.showText(this, "请输入这一刻的想法...");
            return;
        }


//        mVideoPath = "http://p8swfk8xu.bkt.clouddn.com/video_1527496268655";

        if (mVideoPath == null) {
            ToastUtils.showText(this, "请上传视频");
            return;
        }


//        MediaMetadataRetriever mmr=new MediaMetadataRetriever();
//        String path=mVideoPath;
////绑定资源
//        mmr.setDataSource(path);
////获取第一帧图像的bitmap对象
//        Bitmap bitmap=mmr.getFrameAtTime();
////加载到ImageView控件上
//        if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
//        {	  // 获取SDCard指定目录下
//            String  sdCardDir = Environment.getExternalStorageDirectory()+ "/CoolImage/";
//            File dirFile  = new File(sdCardDir);  //目录转化成文件夹
//            if (!dirFile .exists()) {				//如果不存在，那就建立这个文件夹
//                dirFile .mkdirs();
//            }
//             a=String.valueOf(System.currentTimeMillis());
//                    //文件夹有啦，就可以保存图片啦
//            File file = new File(sdCardDir, a+".jpg");// 在SDcard的目录下创建图片文,以当前时间为其命名
//
//            try {
//                 out = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//                System.out.println("_________保存到____sd______指定目录文件夹下____________________");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            try {
//                out.flush();
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
        uploadImage(Environment.getExternalStorageDirectory()+"/CoolImage/"+a+".jpg");


    }

    /**
     * 上传图片
     */
    private void uploadVideo(String videoPath) {
        isUploading = true;
        NaturalLoader.showLoading(this);
        UploadImgUtil.create().setErrorListener(new UploadImgUtil.IUploadErrorListener() {
            @Override
            public void onError() {
                NaturalLoader.stopLoading();
                isUploading = false;
            }
        }).uploadVideo(videoPath, new UploadImgUtil.IUploadImgListener() {
            @Override
            public void onSuccess(String path) {
                publishVideo(path);
            }
        });
    }
    /**
     * 上传首页图片
     */
    private void uploadImage(String videoPath) {
        isUploading = true;
        NaturalLoader.showLoading(this);
        UploadImgUtil.create().setErrorListener(new UploadImgUtil.IUploadErrorListener() {
            @Override
            public void onError() {
                NaturalLoader.stopLoading();
                isUploading = false;
            }
        }).uploadVideo(videoPath, new UploadImgUtil.IUploadImgListener() {
            @Override
            public void onSuccess(String path) {
                imagepath=path;
                uploadVideo(mVideoPath);
            }
        });
    }
    //type:动态类型：1：图文 2：视频
    private void publishVideo(String videoPath) {


        String time = DateUtils.getNowDate();
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.CONTENT, mContent);
        map.put(ParameterKeys.IMAGE_PATH, videoPath);
        map.put(ParameterKeys.PUSH_TIME, time);
        map.put("circleid", circleid);
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.TYPE, mType);
        map.put(ParameterKeys.videoImg,imagepath);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.DYNAMIC_PUBLISH_NATURAL)
                .raw(raw)
                .toast(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        isUploading = false;
                        NaturalLoader.stopLoading();
                        ToastUtils.showText(NaturalPublishVideoActivity.this, "发布成功");
                        finish();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        NaturalLoader.stopLoading();
                        isUploading = false;
                    }
                })
                .build().post();
    }


    /**
     * 跳转回传
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CodeKeys.FOR_RESULT_CODE) {

        } else if (resultCode == RESULT_OK && requestCode == CodeKeys.VIDEO_OPEN && data != null) {
            Uri uri = data.getData();
            mVideoPath = UriUtils.getPhotoPath(this, uri);
            LogUtils.d("onActivityResult: path" + mVideoPath);
            mIvAdd.setVisibility(View.GONE);
            detailPlayer.setVisibility(View.VISIBLE);
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mVideoPath);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
            dur=Integer.parseInt(duration);
            Log.d("TYPE", "duration: 视频播放时长: " + dur);
            if (dur<15000){
                initVideo(mVideoPath);
            }
            else{
                mVideoPath=null;
                ToastUtils.showText(this, "普通用户最多上传15秒");
            }
        }else if (resultCode == RESULT_OK && requestCode == CodeKeys.VIDEO_OPEN_VIP && data != null) {
            Uri uri = data.getData();
            mVideoPath = UriUtils.getPhotoPath(this, uri);
            LogUtils.d("onActivityResult: path" + mVideoPath);
            mIvAdd.setVisibility(View.GONE);
            detailPlayer.setVisibility(View.VISIBLE);
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mVideoPath);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
            dur=Integer.parseInt(duration);
            Log.d("TYPE", "duration: 视频播放时长: " + dur);
            if (dur<60000){
                initVideo(mVideoPath);
            }
            else{
                mVideoPath=null;
                ToastUtils.showText(this, "VIP用户最多上传60秒");
            }
        }else if (resultCode == RESULT_OK && requestCode == CodeKeys.VIDEO_CUSTOM && data != null) {
            mVideoPath = mCurrentPhotoPath;
            //  mVideoPath = UriUtils.getPhotoPath(this, fileUri);
            LogUtils.d("onActivityResult: path" + mVideoPath);
            mIvAdd.setVisibility(View.GONE);
            detailPlayer.setVisibility(View.VISIBLE);
            initVideo(mVideoPath);
        }else if (resultCode == RESULT_OK && requestCode == CodeKeys.VIDEO_VIP && data != null) {
            mVideoPath = mCurrentPhotoPath;
            //  mVideoPath = UriUtils.getPhotoPath(this, fileUri);
            LogUtils.d("onActivityResult: path" + mVideoPath);
            mIvAdd.setVisibility(View.GONE);
            detailPlayer.setVisibility(View.VISIBLE);
            initVideo(mVideoPath);
        }

    }


    /**
     * switch的选中事件
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "onCheckedChanged: " + isChecked);
    }


    /**
     * 从相册中选择视频
     */
    private void choiceVideo_normal() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, CodeKeys.VIDEO_OPEN);
    }
    private void choiceVideo_vip() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, CodeKeys.VIDEO_OPEN_VIP);
    }

    //权限请求
    public void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, CodeKeys.CAMERA_PERMISSION);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CodeKeys.CAMERA_PERMISSION:
                boolean isSuccess = true;
                for (int i = 0; i < grantResults.length; i++) {
                    Log.d("permission", "onRequestPermissionsResult: 权限请求成功，打开照相机"
                            + grantResults[i]);
                    if (grantResults[i] != 0) {
                        isSuccess = false;
                        break;
                    }
                }
                if (isSuccess) {
                    String type= SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_TYPE);
                    if (type.equals("1")){
                        choiceVideo_vip();
                    }else{
                        choiceVideo_normal();
                    }
                } else {
                    //打开权限管理
                    PermissionDialog.create(this).beginPermissionDialog();
                }
                break;
            default:
                break;
        }
    }

    public void show(){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        //初始化控件
        choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type= SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_TYPE);
                if (type.equals("1")){
                    Video_vip();
                    dialog.dismiss();
                }else{
                    Video_normal();
                    dialog.dismiss();
                }

            }
        });
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type= SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_TYPE);
                if (type.equals("1")){
                    choiceVideo_vip();
                    dialog.dismiss();
                }else{
                    choiceVideo_normal();
                    dialog.dismiss();
                }

            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        dialogWindow.setAttributes(params);
        dialog.show();//显示对话框
    }
    /**
     * 录制视频
     */
    private void Video_normal() {

        fileUri = FileProvider7.getUriForFile(this, getOutputMediaFile());
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".mp4";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            mCurrentPhotoPath = file.getAbsolutePath();
            fileUri = FileProvider7.getUriForFile(this, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);     //限制的录制时长 以秒为单位
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024);        //限制视频文件大小 以字节为单位
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);      //设置拍摄的质量0~1
//        intent.putExtra(MediaStore.EXTRA_FULL_SCREEN, false);        // 全屏设置
            startActivityForResult(intent, CodeKeys.VIDEO_CUSTOM);
        }
    }
    private void Video_vip() {

        fileUri = FileProvider7.getUriForFile(this, getOutputMediaFile());
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".mp4";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            mCurrentPhotoPath = file.getAbsolutePath();
            fileUri = FileProvider7.getUriForFile(this, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);     //限制的录制时长 以秒为单位
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024);        //限制视频文件大小 以字节为单位
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);      //设置拍摄的质量0~1
//        intent.putExtra(MediaStore.EXTRA_FULL_SCREEN, false);        // 全屏设置
            startActivityForResult(intent, CodeKeys.VIDEO_VIP);
        }
    }
    /**
     * Create a File for saving an video
     */
    private File getOutputMediaFile() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "请检查SDCard！", Toast.LENGTH_SHORT).show();
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }
    /************************************** 以下为视频播放 **********************************/
    private void initVideo(String url) {
//        //增加封面
//        ImageView imageView = new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.mipmap.xxx1);

        resolveNormalVideoUI();

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        init(url);
        videofaceImage=new ImageView(NaturalPublishVideoActivity.this);
        loadImg(Environment.getExternalStorageDirectory()+"/CoolImage/"+a+".jpg",videofaceImage);
        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setIsTouchWiget(true)
                .setThumbImageView(videofaceImage)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setVideoTitle(" ")
                .setVideoAllCallBack(mGSYSampleCallBack)
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    }
                })
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                        Debuger.printfLog(" progress " + progress + " secProgress " + secProgress + " currentPosition " + currentPosition + " duration " + duration);
                    }
                })
                .build(detailPlayer);

        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(NaturalPublishVideoActivity.this, true, true);
            }
        });

    }


    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);
    }


    //========================== 视频播放回调 =================================
    private GSYSampleCallBack mGSYSampleCallBack = new GSYSampleCallBack() {
        @Override
        public void onPrepared(String url, Object... objects) {
            Debuger.printfError("***** onPrepared **** " + objects[0]);
            Debuger.printfError("***** onPrepared **** " + objects[1]);
            super.onPrepared(url, objects);
            //开始播放了才能旋转和全屏
            orientationUtils.setEnable(true);
            isPlay = true;
        }

        @Override
        public void onEnterFullscreen(String url, Object... objects) {
            super.onEnterFullscreen(url, objects);
            Debuger.printfError("***** onEnterFullscreen **** " + objects[0]);//title
            Debuger.printfError("***** onEnterFullscreen **** " + objects[1]);//当前全屏player
        }

        @Override
        public void onAutoComplete(String url, Object... objects) {
            super.onAutoComplete(url, objects);
        }

        @Override
        public void onClickStartError(String url, Object... objects) {
            super.onClickStartError(url, objects);
        }

        @Override
        public void onQuitFullscreen(String url, Object... objects) {
            super.onQuitFullscreen(url, objects);
            Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
            Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
            if (orientationUtils != null) {
                orientationUtils.backToProtVideo();
            }
        }
    };

    //========================== 视频播放回调结束 =================================


    //===================== 以下为视频资源释放 ========================
    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }
    public void init(String videopath){
        MediaMetadataRetriever mmr=new MediaMetadataRetriever();
        String path=videopath;
//绑定资源
        mmr.setDataSource(path);
//获取第一帧图像的bitmap对象
        Bitmap bitmap=mmr.getFrameAtTime();
//加载到ImageView控件上
        if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {	  // 获取SDCard指定目录下
            String  sdCardDir = Environment.getExternalStorageDirectory()+ "/CoolImage/";
            File dirFile  = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile .exists()) {				//如果不存在，那就建立这个文件夹
                dirFile .mkdirs();
            }
            a=String.valueOf(System.currentTimeMillis());
            //文件夹有啦，就可以保存图片啦
            File file = new File(sdCardDir, a+".jpg");// 在SDcard的目录下创建图片文,以当前时间为其命名

            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                System.out.println("_________保存到____sd______指定目录文件夹下____________________");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
