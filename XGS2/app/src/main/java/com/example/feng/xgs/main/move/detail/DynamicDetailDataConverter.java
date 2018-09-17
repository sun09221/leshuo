package com.example.feng.xgs.main.move.detail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/4/28 0028.
 */

public class DynamicDetailDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String createtime = object.getString("createtime");
            String name = object.getString("nickname");
            String sex = object.getString("sex");
            String info = object.getString("content");
            String imUrl = object.getString("header");
            String id = object.getString("peopleid");
            String nameResult = name + "\n" + sex;
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, nameResult)
                    .setField(EntityKeys.INFO, info)
                    .setField(EntityKeys.IMG_URL, imUrl)
                    .setField(EntityKeys.PEOPLE_ID, id)
                    .setField(EntityKeys.CREATETIME, createtime)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
