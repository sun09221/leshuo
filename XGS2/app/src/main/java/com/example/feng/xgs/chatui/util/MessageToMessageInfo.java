package com.example.feng.xgs.chatui.util;

import android.content.Context;

import com.example.feng.xgs.chatui.enity.MessageInfo;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.LocationContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.enums.MessageStatus;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by feng on 2018/6/15 0015.
 */

public class MessageToMessageInfo {

    private Context mContext;
    private String mImgUrlLeft;
    private String  messagetime;

    public MessageInfo change(Message message) {
        ContentType messageType = message.getContentType();//消息类型
        MessageStatus messageStatus = message.getStatus();//消息状态
        long messageTime = message.getCreateTime();//消息创建时间
        String leftHead = mImgUrlLeft;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        Timestamp now = new Timestamp(messageTime);//获取系统当前时间
        String str = df.format(now);
        messagetime = str;

        String rightHead = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_HEAD);
//        message.get

        MessageInfo messageInfo = new MessageInfo();
        int itemType;
        if (messageType == ContentType.text) {//文字消息
            if (message.getDirect() == MessageDirect.send) {//发送方
                itemType = Constants.CHAT_ITEM_TYPE_RIGHT;
                messageInfo.setHeader(rightHead);
                messageInfo.setTime(messagetime);
            } else {
                itemType = Constants.CHAT_ITEM_TYPE_LEFT;
                messageInfo.setHeader(leftHead);
                messageInfo.setTime(messagetime);
            }


            String messageContent = ((TextContent) message.getContent()).getText();
            messageInfo.setContent(messageContent);
            messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
            messageInfo.setType(itemType);
//            messageInfo.setTime("");

        } else if (messageType == ContentType.image) {
            if (message.getDirect() == MessageDirect.send) {//发送方
                itemType = Constants.CHAT_ITEM_TYPE_RIGHT;
                messageInfo.setHeader(rightHead);
                messageInfo.setTime(messagetime);
            } else {
                itemType = Constants.CHAT_ITEM_TYPE_LEFT;
                messageInfo.setHeader(leftHead);
                messageInfo.setTime(messagetime);
            }

            ImageContent imageContent = (ImageContent) message.getContent();

            String imgUrl = imageContent.getImg_link();
            String imgUrl2 = imageContent.getLocalThumbnailPath();

            LogUtils.d("im: imagePath: " + imgUrl + "LocalThum" + imgUrl2);

            //从服务器上拿缩略图
            imageContent.downloadThumbnailImage(message, new DownloadCompletionCallback() {
                @Override
                public void onComplete(int status, String desc, File file) {
                    if (status == 0) {
                        LogUtils.d("imui 服务器图片:" + file.getPath());
                    }
                }
            });

            messageInfo.setImageUrl(imgUrl2);
            messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
            messageInfo.setType(itemType);
//            messageInfo.setTime("");

        } else if (messageType == ContentType.voice) {
            if (message.getDirect() == MessageDirect.send) {//发送方
                itemType = Constants.CHAT_ITEM_TYPE_RIGHT;
                messageInfo.setHeader(rightHead);
                messageInfo.setTime(messagetime);
            } else {
                itemType = Constants.CHAT_ITEM_TYPE_LEFT;
                messageInfo.setHeader(leftHead);
                messageInfo.setTime(messagetime);
            }
            VoiceContent voiceContent = (VoiceContent) message.getContent();
            final String format = voiceContent.getFormat();
            messageInfo.setFilepath(voiceContent.getLocalPath());
            messageInfo.setVoiceTime(voiceContent.getDuration());
            messageInfo.setType(itemType);

        }
        else if (messageType == ContentType.location) {
            if (message.getDirect() == MessageDirect.send) {//发送方
                itemType = Constants.CHAT_ITEM_TYPE_RIGHT;
                messageInfo.setHeader(rightHead);
                messageInfo.setTime(messagetime);
            } else {
                itemType = Constants.CHAT_ITEM_TYPE_LEFT;
                messageInfo.setHeader(leftHead);
                messageInfo.setTime(messagetime);
            }
            LocationContent Content = (LocationContent) message.getContent();
            final String format = Content.getAddress();
            messageInfo.setVar1(Content.getLatitude());
            messageInfo.setVar3(Content.getLongitude());
 //           messageInfo.setVar5(Integer.parseInt(String.valueOf(Content.getScale())));
            messageInfo.setVar6(Content.getAddress());
            messageInfo.setType(itemType);

        }
        return messageInfo;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setImgUrlLeft(String imgUrl) {
        this.mImgUrlLeft = imgUrl;
    }
}
