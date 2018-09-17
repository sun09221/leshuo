package com.example.feng.xgs.chatui.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.xgs.R;
import com.example.feng.xgs.chatui.IImUIMenuTouchListener;
import com.example.feng.xgs.chatui.adapter.ChatAdapter;
import com.example.feng.xgs.chatui.adapter.CommonFragmentPagerAdapter;
import com.example.feng.xgs.chatui.enity.MessageInfo;
import com.example.feng.xgs.chatui.ui.fragment.ChatEmotionFragment;
import com.example.feng.xgs.chatui.ui.fragment.ChatFunctionFragment;
import com.example.feng.xgs.chatui.util.Constants;
import com.example.feng.xgs.chatui.util.GlobalOnItemClickManagerUtils;
import com.example.feng.xgs.chatui.util.MessageToMessageInfo;
import com.example.feng.xgs.chatui.widget.EmotionInputDetector;
import com.example.feng.xgs.chatui.widget.NoScrollViewPager;
import com.example.feng.xgs.chatui.widget.StateButton;
import com.example.feng.xgs.config.callback.CallbackManager;
import com.example.feng.xgs.config.callback.CallbackType;
import com.example.feng.xgs.config.callback.IGlobalCallback;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.LocationContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class ChatUIActivity extends AppCompatActivity {

    @BindView(R.id.chat_list)
    EasyRecyclerView chatList;
    @BindView(R.id.emotion_voice)
    ImageView emotionVoice;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.voice_text)
    TextView voiceText;
    @BindView(R.id.emotion_button)
    ImageView emotionButton;
    @BindView(R.id.emotion_add)
    ImageView emotionAdd;
    @BindView(R.id.emotion_send)
    StateButton emotionSend;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.emotion_layout)
    RelativeLayout emotionLayout;

    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    private String mImgUrlLeft;
    private String mRightHead;

    @OnClick(R.id.iv_toolbar_back)
    void onClickBack() {
        finish();
    }

    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private ChatEmotionFragment chatEmotionFragment;
    private ChatFunctionFragment chatFunctionFragment;
    private CommonFragmentPagerAdapter adapter;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<MessageInfo> messageInfos;
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;
    private Conversation mConversation;
    private MessageToMessageInfo messageToMessageInfo;
    private String mUsername;
    private String mTargetAppKey;
    private String activity_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ui);

        ButterKnife.bind(this);

        JMessageClient.registerEventReceiver(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        mTvTitle.setText(title);
        mUsername = intent.getStringExtra(ContentKeys.ACTIVITY_MOBILE);
        LogUtils.d("获取传递的userName: " + mUsername);
        mTargetAppKey = ContentKeys.J_PUSH_APP_KEY;
        mImgUrlLeft = intent.getStringExtra(ContentKeys.ACTIVITY_IMG_URL);
        mRightHead = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_HEAD);
        //        if(mRightHead.isEmpty()){
//            mRightHead =JMessageClient.getMyInfo().getAvatar();
//        }

        LogUtils.d("左边头像: " + mImgUrlLeft + ", 右边头像: " + mRightHead);
        initWidget();
        initSendMessage();

    }
    private void initSendMessage() {
        CallbackManager
                .getInstance()
                .addCallback(CallbackType.IM_TEXT_MESSAGE, new IGlobalCallback<String>() {
                    @Override
                    public void executeCallback(@Nullable String args) {
                        LogUtils.d("发送文字消息: " + args);
                        sendTextMessage(args);
                    }

                    @Override
                    public void executeCallback1(@Nullable String args, long time) {

                    }

                    @Override
                    public void executeCallback2(double latitude, double longitude, int mapview, String street, String path) {

                    }

                });

        CallbackManager
                .getInstance()
                .addCallback(CallbackType.IM_IMAGE_MESSAGE, new IGlobalCallback<String>() {
                    @Override
                    public void executeCallback(@Nullable String args) {
                        sendImgMessage(args);
                    }

                    @Override
                    public void executeCallback1(@Nullable String args, long time) {

                    }

                    @Override
                    public void executeCallback2(double latitude, double longitude, int mapview, String street, String path) {

                    }


                });
        CallbackManager.getInstance().addCallback(CallbackType.IM_VOICE_MESSAGE, new IGlobalCallback<String>() {
            @Override
            public void executeCallback(@Nullable String args) {


            }

            @Override
            public void executeCallback1(@Nullable String args, long time) {
                sendVoiceMessage(args, time);
            }

            @Override
            public void executeCallback2(double latitude, double longitude, int mapview, String street, String path) {

            }


        });
        CallbackManager.getInstance().addCallback(CallbackType.IM_LOCATION_MESSAGE, new IGlobalCallback<String>() {
            @Override
            public void executeCallback(@Nullable String args) {


            }

            @Override
            public void executeCallback1(@Nullable String args, long time) {
                sendVoiceMessage(args, time);
            }

            @Override
            public void executeCallback2(double latitude, double longitude, int mapview, String street, String path) {
                        sendAddressMessage(latitude,longitude,mapview, street, path);
            }


        });


    }

    private void initWidget() {
        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(editText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();


        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(editText);


        mDetector.setOnMenuTouchListener(new IImUIMenuTouchListener() {
            @Override
            public void onMenuTouch() {
                LogUtils.d("onMenuTouch recyclerView滑动到底部");
                chatList.scrollToPosition(chatAdapter.getCount() - 1);
            }
        });

        chatAdapter = new ChatAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);
        chatAdapter.addItemClickListener(new ChatAdapter.onItemClickListener() {
            @Override
            public void onHeaderClick(int position) {

            }

            @Override
            public void onImageClick(View view, int position) {


            }

            @Override
            public void onVoiceClick(ImageView imageView, int position) {

                playVoice(messageInfos.get(position).getFilepath());


            }

            @Override
            public void onLocationClick(View view, int position) {
                        Intent intent =new Intent(ChatUIActivity.this,MapPickerActivity.class);
                        intent.putExtra("latitude",messageInfos.get(position).getVar1());
                        intent.putExtra("longitude",messageInfos.get(position).getVar3());
                        intent.putExtra("sendLocation", false);
                        startActivity(intent);
            }
        });
//        chatList.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mDetector.hideEmotionLayout(false);
//                mDetector.hideSoftInput();
//                return false;
//            }
//        });

        LoadData();
    }

    private void playVoice(String path) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);

            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "语音文件未找到",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {

        }
    }

    MediaPlayer mMediaPlayer = new MediaPlayer();

    /**
     * 构造聊天数据
     */
    private void LoadData() {
        messageInfos = new ArrayList<>();
        messageToMessageInfo = new MessageToMessageInfo();
        messageToMessageInfo.setImgUrlLeft(mImgUrlLeft);

        mConversation = JMessageClient.getSingleConversation(mUsername, mTargetAppKey);
        LogUtils.d("初始化会话管理对象");
//        this.mMsgList = mConversation.getMessagesFromNewest(0, mOffset);
        if (mConversation != null) {
            List<Message> allMessage = mConversation.getAllMessage();
            LogUtils.d("消息数量: " + allMessage.size());
            if (allMessage.size() > 0) {
                for (Message message : allMessage) {
                    messageInfos.add(messageToMessageInfo.change(message));
                }

                chatAdapter.addAll(messageInfos);
                chatList.scrollToPosition(chatAdapter.getCount() - 1);
//                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            }
        }

    }


    /**
     * 初始化Conversation对象（会话管理对象）
     */
    private void initConversation() {
        LogUtils.d("jPushUserName：" + mUsername);
        //单聊
        mConversation = JMessageClient.getSingleConversation(mUsername, mTargetAppKey);
        if (mConversation == null) {
            mConversation = Conversation.createSingleConversation(mUsername, mTargetAppKey);
        }
    }


    private void sendTextMessage(String msg) {
        if (TextUtils.isEmpty(msg)) {
            ToastUtils.showText(this, "发送内容不能为空");
            return;
        }

        initConversation();

        if (mConversation != null) {
            //创建文本消息

            TextContent content = new TextContent(msg);
            final MessageInfo messageInfo = new MessageInfo();
            messageInfo.setContent(msg);

            sendMessage(content, messageInfo);
        }

    }
    private void sendAddressMessage(double var1, double var3, int var5, String var6 ,String path) {

        initConversation();

        if (mConversation != null) {
            //创建位置消息
            LocationContent content =new LocationContent(var1,var3,var5,var6);
            final MessageInfo messageInfo = new MessageInfo();
            messageInfo.setVar1(Double.valueOf(var1));
            messageInfo.setVar3(Double.valueOf(var3));
            messageInfo.setVar5(var5);
            messageInfo.setVar6(var6);

            sendMessage(content, messageInfo);
        }

    }

    private void sendImgMessage(String path) {
//        String msg = editText.getText().toString();
        if (TextUtils.isEmpty(path)) {
            ToastUtils.showText(this, "发送内容不能为空");
            return;
        }
        initConversation();

        if (mConversation != null) {

            final MessageInfo messageInfo = new MessageInfo();
            messageInfo.setImageUrl(path);


            File file = new File(path);
            //创建文本消息
            ImageContent content = null;
            try {
                content = new ImageContent(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }

            sendMessage(content, messageInfo);
        }
    }

    private void sendVoiceMessage(String path, long time) {
//        String msg = editText.getText().toString();
        if (TextUtils.isEmpty(path)) {
            ToastUtils.showText(this, "发送内容不能为空");
            return;
        }
        initConversation();

        if (mConversation != null) {
            try {

                final MessageInfo messageInfo = new MessageInfo();
                messageInfo.setFilepath(path);
                messageInfo.setVoiceTime(time);
                File file = new File(path);
                //构造message content对象
                VoiceContent voiceContent = new VoiceContent(file, (int) time);
                //设置自定义的extra参数
                //创建message实体，设置消息发送回调。
                sendMessage(voiceContent, messageInfo);
//                int dur = formattime(time);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendMessage(MessageContent content, final MessageInfo messageInfo) {
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo.setHeader(mRightHead);
        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
        messageInfos.add(messageInfo);
        chatAdapter.add(messageInfo);
        //获取创建的消息
        final Message message = mConversation.createSendMessage(content);
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int status, String desc) {
//                    editText.setText("");
                if (status == 0) {
                    messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);

                    chatAdapter.notifyDataSetChanged();
                    chatList.scrollToPosition(chatAdapter.getCount() - 1);
//                        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                    LogUtils.d("send message succeed!");
                } else {
                    messageInfo.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
                    LogUtils.d("send message failed " + desc);
                }
            }
        });

        //设置需要已读回执
        MessageSendingOptions options = new MessageSendingOptions();
        options.setNeedReadReceipt(true);

        //向服务器发送消息
        JMessageClient.sendMessage(message);
    }


    /**
     * 接收消息，在主线程中，可以刷新UI
     */
    public void onEventMainThread(MessageEvent event) {
        LogUtils.d("onEventMainThread: 收到消息");
        Message message = event.getMessage();

        messageToMessageInfo = new MessageToMessageInfo();
        messageToMessageInfo.setImgUrlLeft(mImgUrlLeft);

        MessageInfo messageInfo = messageToMessageInfo.change(message);
        messageInfos.add(messageInfo);
        chatAdapter.add(messageInfo);

        chatList.scrollToPosition(chatAdapter.getCount() - 1);


    }

    /**
     * 接收消息，在子线程中，不可以直接刷新UI
     * 需要处理耗时操作可以使用该方法
     */
    public void onEvent(MessageEvent event) {
        final Message message = event.getMessage();
        String mobile = message.getFromUser().getUserName();
        LogUtils.d("onEvent: 用户名: " + mobile);
    }


//    /**
//     * 当在聊天界面断网再次连接时收离线事件刷新
//     */
//    public void onEvent(OfflineMessageEvent event) {
//        Conversation conv = event.getConversation();
//        UserInfo userInfo = (UserInfo) conv.getTargetInfo();
//        String targetId = userInfo.getUserName();
//        String appKey = userInfo.getAppKey();
//        List<Message> singleOfflineMsgList = event.getOfflineMessageList();
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.d("onTouchEvent执行了");
        mDetector.hideEmotionLayout(false);
        mDetector.hideSoftInput();

//        if (null != this.getCurrentFocus()) {
//            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
//        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销消息接收
        mMediaPlayer.release();
        mMediaPlayer = null;
        JMessageClient.unRegisterEventReceiver(this);
//        EventBus.getDefault().removeStickyEvent(this);
//        EventBus.getDefault().unregister(this);
    }
}
