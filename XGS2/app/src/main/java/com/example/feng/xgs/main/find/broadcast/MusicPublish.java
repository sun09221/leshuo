package com.example.feng.xgs.main.find.broadcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.chatui.util.Utils;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.circle.MusicLabelDataConverter;
import com.example.feng.xgs.utils.UploadImgUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.file.UriUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MusicPublish extends Activity implements View.OnTouchListener {

    /**
     * 开始录音按钮
     */
    private ImageView voice;
    /**
     * 用于定位。使录音时展示的popupwindow，展示在该控件 的下面
     */
    private TextView voice_popup;
    private String  mVideoPath;
    private String  mVideoLength;
    private String mMusicPath;
    private String mContent;
    private ImageView iv_voiceplay_back;
    private EditText iv_voicepublish_title;
    private VoisePlayingIcon voisePlayingIcon;
    private MusicLabelDataConverter labelDataConverter;
    private RadioGroup radiogroup;
    private static final int HOUR_SECOND = 60 * 60;
    private static final int MINUTE_SECOND = 60;
    private boolean isUploading = false;//是否正在上传图片
    List<String>listid=new ArrayList<String>();
    private String labelid;
    private String times;
    private TextView show_voice_list;
    private Button show_voices_listview;
    private List<String> voiceList;
    private TextView stop_show_voice;
    private ImageView voice_anim;
    private MediaPlayer mediaPlayer;

    private Boolean flag = true;
    private float int_x = 0;
    private float int_y = 0;

    /**
     * 用于限制最大录音时常。单位是秒。意义是：最大录180秒的音频，到了180秒的是，自动停止
     */
    private int maxRecordTime = 180;

    /**
     * 用于显示频繁操作时间间隔。单位是毫秒。意义是：500毫秒内再次操作，就算是频频操作，做相应处理
     */
    private int oftenOperationTime = 500;




    /**
     * 录音popup
     */
    private PopupWindow voice_popupWindow;
    /**
     * 录音时声音变化
     */
    private ImageView voice_shengyin;
    /**
     * 录音计时器
     */
    private MyChronometer mychronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_music_publish);
        File files = new File(Utils.IMAGE_SDCARD_MADER);
        if (!files.exists()) {
            files.mkdirs();
        }
        voiceList = new ArrayList<String>();
        voiceList = Utils.getVideoFiles(Utils.IMAGE_SDCARD_MADER);
        if(voiceList.size()>0){
            deleteDir(Utils.IMAGE_SDCARD_MADERS);
        }
        loadDataLabel();
        voice = (ImageView) findViewById(R.id.voice);
        voice_popup = (TextView) findViewById(R.id.voice_popup);
        iv_voiceplay_back = findViewById(R.id.iv_voiceplay_back);
        show_voice_list = (TextView) findViewById(R.id.show_voice_list);
        iv_voicepublish_title = findViewById(R.id.iv_voicepublish_title);
        voisePlayingIcon = findViewById(R.id.voise_playint_icon);
        iv_voiceplay_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        show_voice_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, CodeKeys.MUSIC_OPEN);
                if (mVideoPath==null){
                    mContent = iv_voicepublish_title.getText().toString();
                    if (TextUtils.isEmpty(mContent)) {
                        ToastUtils.showText(MusicPublish.this, "请输入标题");
                        return;
                    }
                    if (labelid==null) {
                        ToastUtils.showText(MusicPublish.this, "请选择标签");
                        return;
                    }
                    voiceList = Utils.getVideoFiles(Utils.IMAGE_SDCARD_MADER);
                    int size=voiceList.size();
                    if (size>0){
                        mMusicPath = voiceList.get(size-1);
                        if (TextUtils.isEmpty(mMusicPath)) {
                            ToastUtils.showText(MusicPublish.this, "请添加音频");
                            return;
                        }
                    }else{
                        ToastUtils.showText(MusicPublish.this, "请添加音频");
                        return;
                    }

//                mContent = iv_voicepublish_title.getText().toString();
//                if (TextUtils.isEmpty(mContent)) {
//                    ToastUtils.showText(MusicPublish.this, "请选择标签");
//                    return;
//                }
                    uploadVideo(mContent,mMusicPath,times);

                }else{
                    mContent = iv_voicepublish_title.getText().toString();
                    if (TextUtils.isEmpty(mContent)) {
                        ToastUtils.showText(MusicPublish.this, "请输入标题");
                        return;
                    }
                    if (labelid==null) {
                        ToastUtils.showText(MusicPublish.this, "请选择标签");
                        return;
                    }
                    String musictime=getTimeStrBySecond(Integer.parseInt(mVideoLength)/1000);
                    voiceList = Utils.getVideoFiles(Utils.IMAGE_SDCARD_MADER);
                    mMusicPath = mVideoPath;
//                mContent = iv_voicepublish_title.getText().toString();
//                if (TextUtils.isEmpty(mContent)) {
//                    ToastUtils.showText(MusicPublish.this, "请选择标签");
//                    return;
//                }
                    uploadVideo(mContent,mMusicPath,musictime);

                }

            }
        });


        show_voices_listview = (Button) findViewById(R.id.voice_button);
        show_voices_listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVideoPath!=null&&!"".equals(mVideoPath)){
                    if (mediaPlayer != null) {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            voisePlayingIcon.start();
                            show_voices_listview.setBackgroundResource(R.mipmap.play);
                        } else {
                            mediaPlayer.pause();
                            voisePlayingIcon.stop();
                            show_voices_listview.setBackgroundResource(R.mipmap.stop);
                        }
                    }
//                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                            @Override
//                            public void onPrepared(MediaPlayer mp) {
//                                if (mp != null) {
//                                    if (mp.isPlaying()){
//                                        mp.pause();
//                                        show_voices_listview.setBackgroundResource(R.mipmap.stop);
//                                    }else{
//                                        mp.start();
//                                        show_voices_listview.setBackgroundResource(R.mipmap.play);
//                                    }
//                                }
//                            }
//                        });

                    /**
                     *  播放完成监听
                     */
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            show_voices_listview.setBackgroundResource(R.mipmap.stop);
                            voisePlayingIcon.stop();
                            if (mp.isPlaying()) {
                                mp.release();// 释放资源
                            }

                        }
                    });
                }else{
                    voiceList = Utils.getVideoFiles(Utils.IMAGE_SDCARD_MADER);
                    if (voiceList.size()>0) {
                        if (mediaPlayer != null) {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                                voisePlayingIcon.start();
                                show_voices_listview.setBackgroundResource(R.mipmap.play);
                            } else {
                                mediaPlayer.pause();
                                voisePlayingIcon.stop();
                                show_voices_listview.setBackgroundResource(R.mipmap.stop);
                            }
                        }
//                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                            @Override
//                            public void onPrepared(MediaPlayer mp) {
//                                if (mp != null) {
//                                    if (mp.isPlaying()){
//                                        mp.pause();
//                                        show_voices_listview.setBackgroundResource(R.mipmap.stop);
//                                    }else{
//                                        mp.start();
//                                        show_voices_listview.setBackgroundResource(R.mipmap.play);
//                                    }
//                                }
//                            }
//                        });

                        /**
                         *  播放完成监听
                         */
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                show_voices_listview.setBackgroundResource(R.mipmap.stop);
                                voisePlayingIcon.stop();
                                if (mp.isPlaying()) {
                                    mp.release();// 释放资源
                                }

                            }
                        });
                    }else{
                        ToastUtils.showText(MusicPublish.this, "请录入音频");
                    }
                }

            }
        });
        stop_show_voice = (TextView) findViewById(R.id.stop_show_voice);

        /**
         * 停止播放的监听器
         */
        stop_show_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(i, CodeKeys.MUSIC_OPEN);

            }
        });


        voice.setOnTouchListener(this);

    }

//    /**
//     * 声音文件列表的item点击事件,播放对应声音文件
//     *
//     * @param parent
//     * @param view
//     * @param position
//     * @param id
//     */
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        //TODO 以下4行，是用来做测试，点击item，手机SD卡上对应路径下的声音文件就会被删除。如果录制声音失败，或者不满足条件，可以把以下4行写成一个工具方法调用，删除不满意的文件。这里不做详细演示
//        //File f_delete=new File(voiceList.get(position));
//        //f_delete.delete();
//        //voiceList.remove(voiceList.get(position));
//        //myAdapter.notifyDataSetChanged();
//        //TODO 以上4行，是用来做测试，点击item，手机SD卡上对应路径下的声音文件就会被删除。
//
//        try {
//            mediaPlayer = new MediaPlayer();
//
//            /**
//             * 播放过程中展示的动画
//             */
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    if (mp != null) {
//                        mp.start();
//
//                    }
//                }
//            });
//
//            /**
//             *  播放完成监听
//             */
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    if (mp.isPlaying()) {
//                        mp.release();// 释放资源
//                    }
//
//                }
//            });
//            mediaPlayer.setDataSource(voiceList.get(position));
//            // 缓冲
//            mediaPlayer.prepare();
//
//        } catch (Exception e) {
//            Utils.showToast(MusicPublish.this, "语音异常，加载失败");
//        }
//    }

    private void loadData(String titel,String musicpath,String musictime) {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.MUSIC_TITLE, titel);
        map.put(ParameterKeys.MUSIC_LABEL,labelid);
        map.put(ParameterKeys.MUSIC_LENGTHTIME, musictime);
        map.put(ParameterKeys.MUSIC_PATH, musicpath);
        mUrl = UrlKeys.SAVEMUSIC;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                      ToastUtils.showText(MusicPublish.this,"提交成功");
                      Intent intent =new Intent(MusicPublish.this,NewVoice.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        ToastUtils.showText(MusicPublish.this,"发送失败，请再次提交");


                    }
                })
                .build().post();
    }
    /**
     * 上传音频
     */
    private void uploadVideo(final String count, String videoPath , final String times) {
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
                loadData(count,path,times);
            }
        });
    }

    private void loadDataLabel() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        mUrl = UrlKeys.MUSICLABEL;
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        labelDataConverter = new MusicLabelDataConverter();
                        labelDataConverter.clearData();
                        labelDataConverter.setJsonData(response).convert();
                        initViewTwo();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        ToastUtils.showText(MusicPublish.this,"发送失败，请再次提交");

                    }
                })
                .build().post();
    }


    /**
     * 开始录制按钮的onTouch事件
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.voice) {

            //检查权限
            if (!Utils.checkVoice(this)) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Utils.showToast(this, "录音权限未打开，请打开录音权限!");
                }
                return true;
            }

            //避免短时间里频繁操作
            if (!getTimeTF(SystemClock.elapsedRealtime()) && event.getAction() == MotionEvent.ACTION_DOWN) {
                Utils.showToast(this, "操作过于频繁");
                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setTime(SystemClock.elapsedRealtime());
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if ( mVideoPath==null){
                        int_x = event.getRawX();
                        int_y = event.getRawY();
                        VoicePopupWindow();
                        mychronometer.setBase(SystemClock.elapsedRealtime());
                        mychronometer.start();
                        MediaRecorderUtils.getInstence(voice_shengyin).MediaRecorderStart();
                        flag = true;
                        mychronometer.setOnMyChronometerTickListener(new MyChronometer.OnMyChronometerTickListener() {
                            @Override
                            public void onMyChronometerTick(int time) {
                                if (time == maxRecordTime || time > maxRecordTime) {
                                    mychronometer.setText("180");
                                    setVoiceToUp();
                                }
                            }
                        });
                    }else{
                        ToastUtils.showText(MusicPublish.this, "请删除原有录音");
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (flag) {
                        if (Math.abs(int_y) - Math.abs(event.getRawY()) > 100.0 && flag) {
                            voice_popupWindow.dismiss();
                            mychronometer.stop();
                            MediaRecorderUtils.getInstence(voice_shengyin).MediaRecorderStop();
                            MediaRecorderUtils.getInstence(voice_shengyin).MediaRecorderDelete();
                            flag = false;

                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (flag) {
                        voice_popupWindow.dismiss();
                        mychronometer.stop();
                        MediaRecorderUtils.getInstence(voice_shengyin).MediaRecorderStop();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (flag) {
                        setVoiceToUp();
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    private long base_time = 0;

    private void setTime(long time) {
        base_time = time;
    }

    private boolean getTimeTF(long time) {
        int data = (int) (time - base_time) / oftenOperationTime;
        if (data > 1) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 声音popupwindow
     */
    public void VoicePopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.voice_popupwindow, null);
        voice_popupWindow = new PopupWindow(this);
        voice_popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        voice_popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        voice_shengyin = (ImageView) view.findViewById(R.id.voice_shengyin);
        mychronometer = (MyChronometer) view.findViewById(R.id.mychronometer);
        voice_popupWindow.setContentView(view);
        voice_popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        voice_popupWindow.setBackgroundDrawable(dw);
        voice_popupWindow.showAsDropDown(voice_popup);
    }


    private void setVoiceToUp() {
        flag = false;
        voice_popupWindow.dismiss();
        mychronometer.stop();
        MediaRecorderUtils.getInstence(voice_shengyin).MediaRecorderStop();
        int time = Integer.parseInt(mychronometer.getText().toString());
        times=getTimeStrBySecond(time);

        if (time != 0) {
            File file = new File(MediaRecorderUtils.getInstence(voice_shengyin).getPath());
            if (file.length() > 0) {
                voiceList = Utils.getVideoFiles(Utils.IMAGE_SDCARD_MADER);
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(voiceList.get(voiceList.size()-1));
                    // 缓冲
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                Utils.showToast(this, "录音失败，请检查权限");
            }
        } else {
            Utils.showToast(this, "录音时间太短");
        }
    }
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
        //    file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }
    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
          //  else if (file.isDirectory())
              //  deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
      //  dir.delete();// 删除目录本身
    }
    /**
     * 跳转回传
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode == CodeKeys.MUSIC_OPEN && data!=null) {
                Uri uri = data.getData();
                mVideoPath = UriUtils.getPhotoPath(this, uri);
                mVideoLength = UriUtils.getMusicPath(this, uri);
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(mVideoPath);
                    // 缓冲
                    mediaPlayer.prepare();
                    int a=mediaPlayer.getDuration();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
           ToastUtils.showText(MusicPublish.this,"不能从该文件夹选择音频");
        }

    }
    public void initViewTwo(){
        radiogroup=(RadioGroup) findViewById(R.id.rgStorageWay);


        addview(radiogroup);
    }

    public List<String> getListSize(){
        List<String>list=new ArrayList<String>();

        for(int x =0 ; x <labelDataConverter.ENTITIES.size(); x = x+1) {
            list.add(labelDataConverter.ENTITIES.get(x).getField("labelname"));
            listid.add(labelDataConverter.ENTITIES.get(x).getField("id"));
        }

        return list;
    }

    //动态添加视图
    public void addview(RadioGroup radiogroup){

        int index=0;
        for(String ss:getListSize()){

            RadioButton button=new RadioButton(this);
            setRaidBtnAttribute(button,ss,index);

            radiogroup.addView(button);

            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) button
                    .getLayoutParams();
            layoutParams.setMargins(35, 35,  0, 0);//4个参数按顺序分别是左上右下
            button.setLayoutParams(layoutParams);
            index++;
        }


    }


    @SuppressLint("ResourceType")
    private void setRaidBtnAttribute(final RadioButton codeBtn, String btnContent, int id ){
        if( null == codeBtn ){
            return;
        }
        codeBtn.setBackgroundResource(R.drawable.radio_group_selector);
        codeBtn.setTextColor(this.getResources().getColorStateList(R.drawable.color_radiobutton));
        codeBtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
        //codeBtn.setTextSize( ( textSize > 16 )?textSize:24 );
        codeBtn.setId( id );
        codeBtn.setText( btnContent );
        codeBtn.setPadding(5, 0, 5, 0);

        codeBtn.setGravity( Gravity.CENTER );
        codeBtn.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(MusicPublish.this, codeBtn.getText().toString(), Toast.LENGTH_SHORT).show();
                int id = radiogroup.indexOfChild(codeBtn);
                labelid=listid.get(id);


            }
        });
        //DensityUtilHelps.Dp2Px(this,40)
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        codeBtn.setLayoutParams( rlp );
    }
    public static String getTimeStrBySecond(int second) {
        if (second <= 0) {

            return "00:00";
        }

        StringBuilder sb = new StringBuilder();

        int minutes = second / MINUTE_SECOND;
        if (minutes > 0) {

            second -= minutes * MINUTE_SECOND;
        }

        return ( (minutes > 10 ? (minutes + "") : ("0" + minutes)) + ":"
                + (second > 10 ? (second + "") : ("0" + second)));
    }
}
