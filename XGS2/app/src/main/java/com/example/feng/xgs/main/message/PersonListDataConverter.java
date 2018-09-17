package com.example.feng.xgs.main.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;


import java.util.ArrayList;

/**
 * Created by feng on 2018/5/4 0004.
 */

public class PersonListDataConverter extends BaseDataConverter{

    @Override
    public ArrayList<BaseEntity> convert() {
        JSONObject objectData = JSON.parseObject(getJsonData());
        JSONArray array = objectData.getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String imgUrl = object.getString("header");
            String name = object.getString("nickname");
            String info = object.getString("autograph");//签名
            String content = object.getString("content");//签名
            String jgusername = object.getString("jgusername");//极光姓名
            String jgpassword = object.getString("jgpassword");//签名
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.INFO, info)
                    .setField("content", content)
                    .setField("jgusername", jgusername)
                    .setField("jgpassword", jgpassword)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
