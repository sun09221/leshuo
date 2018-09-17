package com.example.feng.xgs.main.find;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/4/27 0027.
 * 发现data类
 */

public class FindDataConverter extends BaseDataConverter{
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("areaname");
            String idOnly = object.getString("id");
            String time = object.getString("createtime");
            String count = object.getString("amount");//访问量
            String address = object.getString("address");
            String province = object.getString("proviceid");//省份
            String peopleCount = object.getString("peoplenum");//总人数

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .build();

            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
