package com.example.feng.xgs.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by feng on 2018/1/25.
 */

public class ImUIActivity extends ToolbarActivity {

    @BindView(R.id.rv_robot)RecyclerView mRecyclerView;
//    @BindView(R.id.refresh_robot)SwipeRefreshLayout mRefresh;
    @BindView(R.id.et_robot)EditText mEtMessage;
    private List<BaseEntity> mData = new ArrayList<>();
    private ImUIAdapter mAdapter;
    private LinearLayoutManager mManager;
    private Conversation mConversation;
    private List<Message> allMessage;
    private String mUsername;
    private String mTargetAppKey;
    private String mImgUrlLeft;
    private ImUIDataConverter mDataConverter;
    private int mType;

    @BindView(R.id.ly_im_ui_bottom_menu)LinearLayout mLayoutMenu;

    @OnClick(R.id.iv_robot_bottom_add)void onClickMenu(){
        mLayoutMenu.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_robot_send)void onClickSend(){
        sendMessage();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_im_ui;
    }


    @Override
    public void initBeforeLayout() {
        super.initBeforeLayout();
//        getWindow().setSoftInputMode
//                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
//                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
//        setTitle(getString(R.string.robot));


        Intent intent = getIntent();
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        mUsername = intent.getStringExtra(ContentKeys.ACTIVITY_MOBILE);
        LogUtils.d("获取传递的userName: " + mUsername);
        mTargetAppKey = ContentKeys.J_PUSH_APP_KEY;
        mImgUrlLeft = intent.getStringExtra(ContentKeys.ACTIVITY_IMG_URL);
        mType = intent.getIntExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.INTENT_DEFAULT);
        setTitle(title);

        initRecyclerView();
        if(mType == CodeKeys.J_PUSH_IM_CREATE_MESSAGE){

        }else{
            loadData();
        }



    }

    private void initRecyclerView(){
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new ImUIAdapter(mData, mImgUrlLeft);
        mRecyclerView.setAdapter(mAdapter);
        mDataConverter = new ImUIDataConverter().setContext(this);
//        mRecyclerView.setHasFixedSize(true);

//        mAdapter.setOnScrollLitener(new IRobotScrollListener() {
//            @Override
//            public void onScrollTo() {
//                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
//            }
//        });

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


    private void loadData() {
        initConversation();
        LogUtils.d("初始化会话管理对象");
//        this.mMsgList = mConversation.getMessagesFromNewest(0, mOffset);
        if(mConversation != null){
            allMessage = mConversation.getAllMessage();
            LogUtils.d("消息数量: " + allMessage.size());
            if (allMessage.size() > 0) {
                for (Message message : allMessage) {
//                    JMUIMessage jmuiMessage = new JMUIMessage(message);
//                    mData.add(jmuiMessage);
                    mData.add(mDataConverter.convert(message));
                }

                mAdapter.setNewData(mData);
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            }

        }
    }


    private void sendMessage() {
        String msg = mEtMessage.getText().toString();
        if(TextUtils.isEmpty(msg)){
            ToastUtils.showText(this, "发送内容不能为空");
            return;
        }
        LogUtils.d("初始化会话管理对象");
        initConversation();

        if(mConversation != null){
            //创建文本消息
            TextContent content = new TextContent(msg);
            //获取创建的消息
            final Message message = mConversation.createSendMessage(content);

            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int status, String desc) {
                    mEtMessage.setText("");
                    if (status == 0) {
                        mAdapter.addData(mDataConverter.convert(message));
                        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                        LogUtils.d("send message succeed!");
                    } else {
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


    }


}
