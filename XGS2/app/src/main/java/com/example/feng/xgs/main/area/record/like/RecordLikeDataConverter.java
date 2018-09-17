package com.example.feng.xgs.main.area.record.like;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/6/6 0006.
 */

public class RecordLikeDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

//        ,"datalist":[{"nickname":"昵称",
//                "modelid":"模特ID,"peopleid":"个人ID（谁点的赞）","header":"头像"}]}
        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("nickname");
            String modelid = object.getString("modelid");
            String peopleid = object.getString("peopleid");
            String imgUrl = object.getString("header");
            String modelname = object.getString("modelname");

            String info = "给"+modelname+ "点了一个赞。";
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.INFO, info)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.ID_ONLY, modelid)
                    .build();
            ENTITIES.add(entity);

        }
        return ENTITIES;
    }
}
