package com.example.feng.xgs.main.find.broadcast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.GlideApp;
import com.example.feng.xgs.core.ShareUtils;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.circle.CommentListAdapter;
import com.example.feng.xgs.main.circle.NoCommentListAdapter;
import com.example.feng.xgs.main.circle.VoiceCommentDataConverter;
import com.example.feng.xgs.main.move.handler.SendCommentDialogMusic;
import com.example.feng.xgs.main.nearby.detail.NearbyDetailActivity;
import com.example.feng.xgs.ui.dialog.IDialogListener;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlay extends AppCompatActivity {
    private ImageView head;
    private ImageView play_music_bg;
    private TextView titles;
    private TextView time;
    private TextView sexs;
    private TextView startsign;
    private String labelimg;
    private String loaclStorageAudioPath;//audioPath本地保存路径.wav
    private MediaPlayer mediaPlayer;
    private TextView tv_start;
    private TextView tv_end;
    private SeekBar seekbar;
    private ImageButton imageButton;
    private String id;
    private ImageView comment_back;
    private String  peopleId;
    private TextView tv_natural_noitem_name;
    private LinearLayoutManager mManager;
    private CommentListAdapter mCircleAdapter;
    private NoCommentListAdapter noCommentListAdapter;
    private RecyclerView recData;
    private RelativeLayout comment_list;
    private RelativeLayout playmusic;
    private ImageView iv_voiceplay_back;
    VoiceCommentDataConverter mDataConverter;
    private TextView tv_natural_item_like_select;
    private TextView tv_natural_item_leave_message;
    private TextView tv_natural_item_leave_share;
    private MyPhoneStateListener myPhoneStateListener;//电话监听
    private Timer timer;
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private boolean isCellPlay;/*在挂断电话的时候，用于判断是否为是来电时中断*/
    private  boolean isFitstInit=true;//用户第一次打开时
    private int currentPosition;//记录当前播放位置
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_music_play);
        init();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        peopleId = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        labelimg = intent.getStringExtra(ContentKeys.ACTIVITY_LAMEIMG);
        String state= intent.getStringExtra(ContentKeys.ACTIVITY_STATE);
        String header = intent.getStringExtra(ContentKeys.ACTIVITY_MUSIC_HEAD);
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_MUSIC_TITLE);
        String createtime = intent.getStringExtra(ContentKeys.ACTIVITY_MUSIC_CREATTIME);
        String sex = intent.getStringExtra(ContentKeys.ACTIVITY_MUSIC_SEX);
        String starsign = intent.getStringExtra(ContentKeys.ACTIVITY_MUSIC_CONSTELLATION);
        String musiclength = intent.getStringExtra(ContentKeys.ACTIVITY_MUSIC_LENGTH);
        id = intent.getStringExtra(ContentKeys.ACTIVITY_ID);
        loaclStorageAudioPath = intent.getStringExtra(ContentKeys.ACTIVITY_MUSIC_MUSICPATH);
        loadImg(header,head);
        if (labelimg!=null){
            loadImg(labelimg, play_music_bg);
        }
        titles.setText(title);
        time.setText(createtime);
        tv_end.setText(musiclength);
        if (state.equals("1")){
            tv_natural_item_like_select.setText("已关注");
        }
        if (!sex.equals("")){
            sexs.setVisibility(View.VISIBLE);
            sexs.setText(sex);
        }else{
            sexs.setVisibility(View.GONE);
        }
        try {
            if (!starsign.equals("")){
                startsign.setVisibility(View.VISIBLE);
                startsign.setText(starsign);
            }else{
                startsign.setVisibility(View.GONE);
            }
        }catch (Exception e){
            startsign.setVisibility(View.GONE);
        }

        tv_natural_item_leave_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_natural_noitem_name.setVisibility(View.VISIBLE);
                recData.setVisibility(View.GONE);
                loadData();
                initRecViews();
                comment_list.setVisibility(View.VISIBLE);
                SendCommentDialogMusic.create(MusicPlay.this).beginShow(id, new IDialogListener() {
                    @Override
                    public void onSure() {
                        loadData();
                        initRecViews();
                    }
                });
            }
        });
        comment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment_list.setVisibility(View.GONE);
            }
        });
        iv_voiceplay_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MusicPlay.this, NearbyDetailActivity.class);
                intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, peopleId);
                startActivity(intent);
            }
        });
        tv_natural_item_leave_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtils.create(MusicPlay.this)
                        .setText("动听时刻")
                        .setImgUrl(labelimg)
                        .setTitleUrl(UrlKeys.BASE_NAME + UrlKeys.MUSIC_SHARE + "musicid=" + id)
                        .setUrl(UrlKeys.BASE_NAME + UrlKeys.MUSIC_SHARE + "musicid=" + id)
                        .showDialog();
            }
        });
        tv_natural_item_like_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (tv_natural_item_like_select.getText().equals("已关注")) {
               ToastUtils.showText(MusicPlay.this,"已关注过该用户");
             } else{

                 attention(peopleId);
             }
            }
        });

    }
    private void init() {
         playmusic = findViewById(R.id.playmusic);
         head = findViewById(R.id.iv_natural_item_head);
         titles = findViewById(R.id.tv_natural_item_name);
         time = findViewById(R.id.tv_natural_item_date);
         sexs = findViewById(R.id.tv_natural_item_sex);
        play_music_bg = findViewById(R.id.play_music_bg);
        tv_natural_item_leave_share = findViewById(R.id.tv_natural_item_leave_share);
        tv_natural_noitem_name= findViewById(R.id.tv_natural_noitem_name);
         startsign = findViewById(R.id.tv_natural_item_constellation);
        tv_natural_item_leave_message = findViewById(R.id.tv_natural_item_leave_message);
         recData = findViewById(R.id.music_comment);
         comment_list = findViewById(R.id.comment_list);
         comment_back = findViewById(R.id.comment_back);
         tv_natural_item_like_select = findViewById(R.id.tv_natural_item_like_select);
        iv_voiceplay_back = findViewById(R.id.iv_voiceplay_back);
         //开始时间
        tv_start = (TextView) findViewById(R.id.tv_start);
        //结束时间
        tv_end = (TextView) findViewById(R.id.tv_end);

        //进度条
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        //设置监听
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             *
             * @param seekBar  当前被修改进度的SeekBar
             * @param progress 当前的进度值。此值的取值范围为0到max之间。Max为用户通过setMax(int)设置的值，默认为100
             * @param fromUser 如果是用户触发的改变则返回True
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                try {
                    //获取音乐总时间
                    int duration2 = mediaPlayer.getDuration() / 1000;
                    //获取音乐当前播放的位置
                    int position = mediaPlayer.getCurrentPosition();
                    //开始时间
                    tv_start.setText(calculateTime(position / 1000));
                    //结束时间
                    tv_end.setText(calculateTime(duration2));
                }catch (Exception e){

                }
            }

            // 通知用户已经开始一个触摸拖动手势。客户端可能需要使用这个来禁用seekbar的滑动功能。
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarChanging = true;
            }

            //通知用户触摸手势已经结束。户端可能需要使用这个来启用seekbar的滑动功能。
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try{
                    isSeekBarChanging = false;
                    //在当前位置播放
                    mediaPlayer.seekTo(seekBar.getProgress());

                    //停止时播放时间
                    //开始时间
                    tv_start.setText(calculateTime(mediaPlayer.getCurrentPosition() / 1000));
                }catch (Exception e){

                }

            }
        });

        imageButton = (ImageButton) findViewById(R.id.iv_voiceplay_stop);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //按钮点击事件
              //  ToastUtils.showText(MusicPlay.this, "请稍后，音乐正在加载");
                isPlayOrPause();
            }
        });


        myPhoneStateListener=new MyPhoneStateListener();
        //监听来电事件
        TelephonyManager phoneyMana = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneyMana.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    public void loadImg(String imgUrl, ImageView view){
        GlideApp.with(getApplication())
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .centerCrop()
                .dontAnimate()
                .into(view);
    }



    //初始化
    private void initMediaPlayer()
    {
       // File file = new File(loaclStorageAudioPath);
        //判断音频文件是否为空
       // if (file.exists()) {
            if(mediaPlayer==null)
            {
                //为空则创建音乐文件并播放改变按钮样式
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.reset();//处于空闲状态
                    mediaPlayer.setDataSource(loaclStorageAudioPath);//文件路径
                    mediaPlayer.prepare();// 准备
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //获取音乐总时间
                int duration2 = mediaPlayer.getDuration() / 1000;
                //获取音乐当前播放的位置
                int position = mediaPlayer.getCurrentPosition();
                //开始时间
                tv_start.setText(calculateTime(position / 1000));
                //结束时间
                tv_end.setText(calculateTime(duration2));

        //    }
        }
    }

    //按钮点击事件
    public void isPlayOrPause() {
        initMediaPlayer();
      //  File file = new File(loaclStorageAudioPath);
        //判断音频文件是否为空
     //   if (file.exists()) {
            if (mediaPlayer!=null&&isFitstInit==true) {

                isFitstInit=false;

//                //为空则创建音乐文件并播放改变按钮样式
//                mediaPlayer = new MediaPlayer();
//                try {
//                    mediaPlayer.reset();//处于空闲状态
//                    mediaPlayer.setDataSource(file.getAbsolutePath());//文件路径
//                    mediaPlayer.prepare();// 准备
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                mediaPlayer.start();//播放
                imageButton.setImageResource(android.R.drawable.ic_media_pause);
                //获取音乐总时间
                int duration = mediaPlayer.getDuration();
                //将音乐总时间设置为SeekBar的最大值
                seekbar.setMax(duration);

                //-------------------------
                //监听播放时回调函数
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(!isSeekBarChanging){
                            seekbar.setProgress(mediaPlayer.getCurrentPosition());
                        }

                    }
                },0,50);
            }
            //音乐文件正在播放，则暂停并改变按钮样式
            else if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
              //  LoggerUtil.i("Novate","音乐文件正在播放，则暂停并改变按钮样式");
                mediaPlayer.pause();
                imageButton.setImageResource(android.R.drawable.ic_media_play);
            }
            //如果为暂停状态则继续播放，同时改变按钮样式
            else if(mediaPlayer!=null&&(!mediaPlayer.isPlaying())){
             //   LoggerUtil.i("Novate","如果为暂停状态则继续播放，同时改变按钮样式");

                if(currentPosition>0)
                {
                    mediaPlayer.seekTo(currentPosition);
                    currentPosition=0;
                }
                //启动播放
                mediaPlayer.start();
                imageButton.setImageResource(android.R.drawable.ic_media_pause);

            }

            //播放完监听
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放位置变为0
                    mediaPlayer.seekTo(0);
                    imageButton.setImageResource(android.R.drawable.ic_media_play);
                }
            });

//        }
//        else {
//            ToastUtils.showText(MusicPlay.this, "您要播放的音乐不存在");
//        }
    }


    //计算播放时间
    public String calculateTime(int time) {
        int minute;
        int second;

        String strMinute;

        if (time >= 60) {
            minute = time / 60;
            second = time % 60;

            //分钟在0-9
            if(minute>=0&&minute<10)
            {
                //判断秒
                if(second>=0&&second<10)
                {
                    return "0"+minute+":"+"0"+second;
                }else
                {
                    return "0"+minute+":"+second;
                }

            }else
            //分钟在10以上
            {
                //判断秒
                if(second>=0&&second<10)
                {
                    return minute+":"+"0"+second;
                }else
                {
                    return minute+":"+second;
                }
            }

        } else if (time < 60) {
            second = time;
            if(second>=0&&second<10)
            {
                return "00:"+"0"+second;
            }else
            {
                return "00:" + second;
            }

        }
        return null;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.loaclStorageAudioPath = savedInstanceState.getString("filename");
        this.currentPosition = savedInstanceState.getInt("position");
        super.onRestoreInstanceState(savedInstanceState);
    //    LoggerUtil.i("Novate", "回到当前音乐文件onRestoreInstanceState()");
    }

    //当内存空间不足，关闭应用时
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("filename", loaclStorageAudioPath);
        outState.putInt("position", currentPosition);
        super.onSaveInstanceState(outState);
     //   LoggerUtil.i("Novate", "保存音乐onSaveInstanceState()");
    }

    //如果突然电话到来，停止播放音乐
    @Override
    protected void onPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            currentPosition = mediaPlayer.getCurrentPosition();//保存当前播放点
            mediaPlayer.pause();
            timer.purge();//移除所有任务;
            imageButton.setImageResource(android.R.drawable.ic_media_play);
        }
        super.onPause();
     //   ToastUtils.showText(MusicPlay.this, "onPause");
    }

    //如果电话结束，继续播放音乐
    @Override
    protected void onResume() {
        super.onResume();

    }

    /*来电事件处理*/
    private class MyPhoneStateListener extends PhoneStateListener
    {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://来电，应当停止音乐
                    if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
                        currentPosition = mediaPlayer.getCurrentPosition();//记录播放的位置
                        mediaPlayer.pause();//暂停
                        isCellPlay = true;//标记这是属于来电时暂停的标记
                        imageButton.setImageResource(android.R.drawable.ic_media_play);
                        timer.purge();//移除定时器任务;
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE://无电话状态
                    if(isCellPlay){
                        isCellPlay = false;
                    }
                    break;
            }
        }
    }

    /*销毁时释资源*/
    @Override
    protected void onDestroy() {

        if(timer!=null)
        {
            timer.cancel();
            timer.purge();
            timer = null;
        }

        if(mediaPlayer!=null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        //关闭监听
        TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tmgr.listen(myPhoneStateListener, 0);

        super.onDestroy();

      //  ToastUtil.showToast(PlayMusicActivity.this,"onDestroy");
       // ToastUtils.showText(MusicPlay.this, "onDestroy");
    }
    private void loadData() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.MUSIC_ID, id);
        mUrl = UrlKeys.COMMENT_MUSICLIST;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.clearData();
                        mCircleAdapter.setNewData(mDataConverter.setJsonData(response).convert());
                            tv_natural_noitem_name.setVisibility(View.GONE);
                            recData.setVisibility(View.VISIBLE);

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");

                    }
                })
                .build().post();
    }
    private void initRecViews() {
        mManager = new LinearLayoutManager(this);
        recData.setLayoutManager(mManager);
        //  addDivider(10);
        mDataConverter = new VoiceCommentDataConverter();
        mCircleAdapter = new CommentListAdapter(R.layout.item_comment, mDataConverter.ENTITIES);
        recData.setAdapter(mCircleAdapter);
    }
    //关注
    public void attention(String attentionPeopleId){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.ATTENTION_PEOPLE_ID, attentionPeopleId);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.ATTENTION)
                .loader(MusicPlay.this)
                .raw(raw)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(MusicPlay.this, "关注成功");
                        tv_natural_item_like_select.setText("已关注");
                        Intent intent = new Intent();
                        intent.putExtra("state", "1"); //将计算的值回传回去
                        setResult(CodeKeys.FOLLOW, intent);
                    }
                })
                .build().post();

    }
}