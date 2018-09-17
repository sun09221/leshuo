package com.example.feng.xgs.main.circle;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.move.detail.DynamicDetailDataConverter;
import com.example.feng.xgs.main.move.detail.RadioDetailAdapter;
import com.example.feng.xgs.main.move.handler.RadioSendCommentDialog;
import com.example.feng.xgs.ui.dialog.IDialogListener;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RadioPlayActivity extends AppCompatActivity {
  private ImageView iv_toolbar_back;
  private CircleImageView radioplay_anchor_head;
  private TextView radioplay_item_playtime;
  private TextView radioplay_anchor_prompt;
  private TextView radio_time;
  private SeekBar seekbar;
  private TextView tv_start,tv_end;
  private TextView et_dialog_send_content;
  private ImageView radio_voiceplay;
  private String peopleId,labelimg,id,loaclStorageAudioPath;
  private  MediaPlayer mediaPlayer;
  private MyPhoneStateListener myPhoneStateListener;//电话监听
  private Timer timer;
  private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
  private boolean isCellPlay;/*在挂断电话的时候，用于判断是否为是来电时中断*/
  private  boolean isFitstInit=true;//用户第一次打开时
  private int currentPosition;//记录当前播放位置
  private LinearLayoutManager mManager;
  private LinearLayoutManager ManagerComment;
  private RecyclerView recData,recComment;
  VoiceTitleDataConverter mDataConverter;
  private RadioPlayAnchor mCircleAdapter;
  private DynamicDetailDataConverter mCommentConverter;
  private RadioDetailAdapter mCommentAdapter;
  private TextView mTvCommentTitle,tv_dynamic_detail_head_number_title,mTvCommentNormal,playComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_play);
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
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initRecViews();
        loadData();
        initRecCommentViews();
        loadDataComment();
        et_dialog_send_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //留言
                RadioSendCommentDialog.create(RadioPlayActivity.this).beginShow(SharedPreferenceUtils.getPeopleId(), new IDialogListener() {
                    @Override
                    public void onSure() {
                     //   loadData();
                    }
                });
            }
        });
    }
    private void init(){
        et_dialog_send_content = findViewById(R.id.et_dialog_send_content);
        recData = findViewById(R.id.radioplay_anchorall);
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        radioplay_anchor_head = findViewById(R.id.radioplay_anchor_head);
        radioplay_item_playtime = findViewById(R.id.radioplay_item_playtime);
        radioplay_anchor_prompt = findViewById(R.id.radioplay_anchor_prompt);
        radio_time = findViewById(R.id.radio_time);
        seekbar = findViewById(R.id.seekbar);
        tv_start = findViewById(R.id.tv_start);
        tv_end = findViewById(R.id.tv_end);
        radio_voiceplay = findViewById(R.id.radio_voiceplay);
        recComment = findViewById(R.id.rv_dynamic_detail);
        mTvCommentTitle = findViewById(R.id.tv_dynamic_detail_head_comment_title);
        tv_dynamic_detail_head_number_title = findViewById(R.id.tv_dynamic_detail_head_number_title);
        mTvCommentNormal = findViewById(R.id.tv_dynamic_detail_head_no_comment);
        playComment = findViewById(R.id.tv_dynamic_detail_playcommen);
        radio_voiceplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //按钮点击事件
            //    ToastUtils.showText(RadioPlayActivity.this, "请稍后，音乐正在加载");
                isPlayOrPause();
            }
        });
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
                try {
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
            radio_voiceplay.setImageResource(R.mipmap.radioplay_play);
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
            radio_voiceplay.setImageResource(R.mipmap.radioplay_stop);
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
            radio_voiceplay.setImageResource(R.mipmap.radioplay_play);

        }

        //播放完监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //播放位置变为0
                mediaPlayer.seekTo(0);
                radio_voiceplay.setImageResource(R.mipmap.radioplay_stop);
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
            radio_voiceplay.setImageResource(R.mipmap.radioplay_stop);
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
                        radio_voiceplay.setImageResource(R.mipmap.radioplay_stop);
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
    private void initRecViews() {
        mManager = new LinearLayoutManager(this);
        mManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recData.setLayoutManager(mManager);
        //  addDivider(10);
        mDataConverter = new VoiceTitleDataConverter();
        mCircleAdapter = new RadioPlayAnchor(R.layout.radio_anchor_item, mDataConverter.ENTITIES);
        recData.setAdapter(mCircleAdapter);
    }
    private void loadData() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(1));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        mUrl = UrlKeys.ALL_VOICE;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.clearData();
                        mCircleAdapter.setNewData(mDataConverter.setJsonData(response).convert());

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
    private void initRecCommentViews() {
        ManagerComment = new LinearLayoutManager(this);
        recComment.setLayoutManager(ManagerComment);
        //  addDivider(10);
        mCommentConverter = new DynamicDetailDataConverter();
        mCommentAdapter = new RadioDetailAdapter(R.layout.item_comment_detail_item, mCommentConverter.ENTITIES);
        recComment.setAdapter(mCommentAdapter);
    }
    private void loadDataComment() {

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.DYNAMIC_ID, "197");
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.DYNAMIC_DETAIL_COMMENT)
                .loader(this)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mCommentConverter.clearData();
                        mCommentAdapter.setNewData(mCommentConverter.setJsonData(response).convert());
                        setCommentHead();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        setCommentHead();
                    }
                })
                .build().post();
    }
    /**
     * 当没有评论时，展示暂无评论
     */
    private void setCommentHead() {
        if (mCommentAdapter.getData().size() > 0) {
            mTvCommentTitle.setVisibility(View.VISIBLE);
            tv_dynamic_detail_head_number_title.setVisibility(View.VISIBLE);
            tv_dynamic_detail_head_number_title.setText(mCommentAdapter.getData().size()+"条");
            mTvCommentNormal.setVisibility(View.GONE);
        } else {
            mTvCommentTitle.setVisibility(View.GONE);
            tv_dynamic_detail_head_number_title.setVisibility(View.GONE);
            mTvCommentNormal.setVisibility(View.VISIBLE);
        }
    }
}
