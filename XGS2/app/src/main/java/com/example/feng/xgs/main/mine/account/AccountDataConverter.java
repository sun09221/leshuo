package com.example.feng.xgs.main.mine.account;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/9 0009.
 */

public class AccountDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

//        {"message":"获取信息成功！","recordCount":1,"result":"1",
//                "pageCount":1,"datalist":[{"id":"账户ID",
//                "type":"账户类型：1：微信 2：支付宝",name:账户名称 number：账户}]}
        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("name");
            String number = object.getString("number");
            String type = object.getString("type");
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.TYPE, type)
                    .setField(EntityKeys.NUMBER, number)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
