package com.example.feng.xgs.im;

import android.content.Context;

import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.utils.manager.LogUtils;

import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.enums.MessageStatus;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by feng on 2018/1/25.
 */

public class ImUIDataConverter {

    private Context mContext;
    public BaseEntity convert(Message message) {

        ContentType messageType = message.getContentType();//消息类型
        MessageStatus messageStatus = message.getStatus();//消息状态
        long messageTime = message.getCreateTime();//消息创建时间
        int itemType;

        TimeFormat timeFormat = new TimeFormat(mContext, messageTime);

        LogUtils.d("时间显示: " + timeFormat.getTime() + ", detailTime: " + timeFormat.getDetailTime());

        if(messageType == ContentType.text){//文字消息
            if(message.getDirect() == MessageDirect.send){//发送方
                itemType = ItemTypeKeys.ROBOT_RIGHT;
            }else {
                itemType = ItemTypeKeys.ROBOT_LEFT;
            }

            String messageContent = ((TextContent) message.getContent()).getText();
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.ID_ONLY, String.valueOf(message.getId()))
                    .setField(EntityKeys.INFO, messageContent)
                    .setField(EntityKeys.TYPE, messageStatus)
                    .setItemType(itemType)
                    .build();
            return entity;
        }


        return null;
    }

    public ImUIDataConverter setContext(Context context){
        this.mContext = context;
        return this;
    }
}
