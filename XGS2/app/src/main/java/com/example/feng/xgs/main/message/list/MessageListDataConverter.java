package com.example.feng.xgs.main.message.list;

import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;

import java.util.ArrayList;

/**
 * Created by feng on 2018/6/14 0014.
 */

public class MessageListDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {
        return ENTITIES;
    }
}
