package com.example.feng.xgs.main.message.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.chatui.ui.activity.ChatUIActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.im.TimeFormat;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by feng on 2018/6/14 0014.
 */

public class MessageListActivity extends EmptyActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;
    List<BaseEntity> mData = new ArrayList<>();
    private MessageListAdapter mAdapter;
    private int mType;
    private int unReadCount;
    private List<Conversation> mConversationList;
    private String time;

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void emptyLoadData() {
        loadData();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_list;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        mType = intent.getIntExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.INTENT_DEFAULT);
        setTitle(title);

        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        addDivider();
        mAdapter = new MessageListAdapter(R.layout.item_j_push_message, mData);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }

    private void loadData(){
        mData.clear();

        mConversationList = JMessageClient.getConversationList();
        if (mConversationList != null) {

            for (int i = 0; i < mConversationList.size(); i++) {
                Conversation conversation = mConversationList.get(i);

                UserInfo userInfo = (UserInfo) conversation.getTargetInfo();
                String mobile = userInfo.getUserName();

                Message message = conversation.getLatestMessage();//获取最近一条会话
                Message messageFirst = conversation.getAllMessage().get(0);
//                String mobile = "";
                if(message != null ){
                    LogUtils.d("最近一条消息ID: " + message.getId() + ", 第一条消息ID" + messageFirst.getId());
                    LogUtils.d("最近一条消息用户名: " + message.getFromUser().getUserName() + ", 第一条消息用户名" + messageFirst.getFromUser().getUserName());
                }

                //第一条消息不为null
                if(messageFirst != null){
                    String title = conversation.getTitle();
                    LogUtils.d("昵称: " + title );

                    String imgUrl = "";
                    File file = conversation.getAvatarFile();

                    if(file != null){
                        LogUtils.d("头像： " + file.getPath());
//                        /data/user/0/com.example.feng.xdd/files/images/small-avatar/6D51C65B385B5E13469AD882B88712FF
                        imgUrl = file.getPath();
                    }


                    TimeFormat timeFormat = new TimeFormat(this, message.getCreateTime());
                    String time = timeFormat.getTime();


                    ContentType messageType = message.getContentType();//消息类型
                    String info;
                    if(messageType == ContentType.text) {//文字消息
                        info = ((TextContent) message.getContent()).getText();
                        LogUtils.d("最近消息内容: " + info);
                    }else {
                        info = "图片";
                        LogUtils.d("最近消息内容: " + info);
                    }


                     unReadCount = conversation.getUnReadMsgCnt();

                    String firstUserName = messageFirst.getFromUser().getUserName();
                    String myUserName = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.J_PUSH_USER_NAME);

                    LogUtils.d("我的用户名：" + myUserName + ", 第一条消息用户名: " + firstUserName);
                    //我私信的
                    BaseEntity entity = BaseEntity.builder()
                            .setField(EntityKeys.TIME, time)
                            .setField(EntityKeys.IMG_URL, imgUrl)
                            .setField(EntityKeys.COUNT, unReadCount)
                            .setField(EntityKeys.NAME, title)
                            .setField(EntityKeys.INFO, info)
                            .setField(EntityKeys.MOBILE, mobile)
                            .build();

                    mData.add(entity);
//                    if(mType == CodeKeys.MESSAGE_MY_PRIVATE_LATTER){
//
//                        if(firstUserName.equals(myUserName)){
//                            BaseEntity entity = BaseEntity.builder()
//                                    .setField(EntityKeys.TIME, time)
//                                    .setField(EntityKeys.IMG_URL, imgUrl)
//                                    .setField(EntityKeys.COUNT, unReadCount)
//                                    .setField(EntityKeys.NAME, title)
//                                    .setField(EntityKeys.INFO, info)
//                                    .setField(EntityKeys.MOBILE, mobile)
//                                    .build();
//
//                            mData.add(entity);
//                        }
//
//                        //私信我的
//                    }else if(mType == CodeKeys.MESSAGE_PRIVATE_LATTER_ME){
//                        if(!firstUserName.equals(myUserName)){
//
//                        }
//                    }
                }

            }

            mRefresh.setRefreshing(false);
            mAdapter.setEmptyView(emptyView);
            mAdapter.setNewData(mData);
        }
    }
    private void loadData2(){
        mData.clear();

        mConversationList = JMessageClient.getConversationList();
        if (mConversationList != null) {

            for (int i = 0; i < mConversationList.size(); i++) {
                Conversation conversation = mConversationList.get(i);

                UserInfo userInfo = (UserInfo) conversation.getTargetInfo();
                String mobile = userInfo.getUserName();

                Message message = conversation.getLatestMessage();//获取最近一条会话
                Message messageFirst = conversation.getAllMessage().get(0);
//                String mobile = "";
                if(message != null ){
                    LogUtils.d("最近一条消息ID: " + message.getId() + ", 第一条消息ID" + messageFirst.getId());
                    LogUtils.d("最近一条消息用户名: " + message.getFromUser().getUserName() + ", 第一条消息用户名" + messageFirst.getFromUser().getUserName());
                }

                //第一条消息不为null
                if(messageFirst != null){
                    String title = conversation.getTitle();
                    LogUtils.d("昵称: " + title );

                    String imgUrl = "";
                    File file = conversation.getAvatarFile();

                    if(file != null){
                        LogUtils.d("头像： " + file.getPath());
//                        /data/user/0/com.example.feng.xdd/files/images/small-avatar/6D51C65B385B5E13469AD882B88712FF
                        imgUrl = file.getPath();
                    }


                    TimeFormat timeFormat = new TimeFormat(this, message.getCreateTime());
                     time = timeFormat.getTime();


                    ContentType messageType = message.getContentType();//消息类型
                    String info;
                    if(messageType == ContentType.text) {//文字消息
                        info = ((TextContent) message.getContent()).getText();
                        LogUtils.d("最近消息内容: " + info);
                    }else {
                        info = "图片";
                        LogUtils.d("最近消息内容: " + info);
                    }


                    unReadCount = conversation.getUnReadMsgCnt();

                    String firstUserName = messageFirst.getFromUser().getUserName();
                    String myUserName = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.J_PUSH_USER_NAME);

                    LogUtils.d("我的用户名：" + myUserName + ", 第一条消息用户名: " + firstUserName);
                    //我私信的
                    BaseEntity entity = BaseEntity.builder()
                            .setField(EntityKeys.TIME, time)
                            .setField(EntityKeys.IMG_URL, imgUrl)
                            .setField(EntityKeys.COUNT, 0)
                            .setField(EntityKeys.NAME, title)
                            .setField(EntityKeys.INFO, info)
                            .setField(EntityKeys.MOBILE, mobile)
                            .build();

                    mData.add(entity);
//                    if(mType == CodeKeys.MESSAGE_MY_PRIVATE_LATTER){
//
//                        if(firstUserName.equals(myUserName)){
//                            BaseEntity entity = BaseEntity.builder()
//                                    .setField(EntityKeys.TIME, time)
//                                    .setField(EntityKeys.IMG_URL, imgUrl)
//                                    .setField(EntityKeys.COUNT, unReadCount)
//                                    .setField(EntityKeys.NAME, title)
//                                    .setField(EntityKeys.INFO, info)
//                                    .setField(EntityKeys.MOBILE, mobile)
//                                    .build();
//
//                            mData.add(entity);
//                        }
//
//                        //私信我的
//                    }else if(mType == CodeKeys.MESSAGE_PRIVATE_LATTER_ME){
//                        if(!firstUserName.equals(myUserName)){
//
//                        }
//                    }
                }

            }

            mRefresh.setRefreshing(false);
            mAdapter.setEmptyView(emptyView);
            mAdapter.setNewData(mData);
        }
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Conversation conversation = mConversationList.get(position);
        if(conversation != null){
            conversation.resetUnreadCount();
        }
        loadData2();
        BaseEntity entity = mAdapter.getData().get(position);
        Intent intent = new Intent(this, ChatUIActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, entity.getField(EntityKeys.NAME));
        intent.putExtra(ContentKeys.ACTIVITY_IMG_URL, entity.getField(EntityKeys.IMG_URL));
        intent.putExtra(ContentKeys.ACTIVITY_MOBILE, entity.getField(EntityKeys.MOBILE));
        intent.putExtra(ContentKeys.ACTIVITY_TIME, entity.getField(EntityKeys.TIME));

        startActivity(intent);
    }
}
