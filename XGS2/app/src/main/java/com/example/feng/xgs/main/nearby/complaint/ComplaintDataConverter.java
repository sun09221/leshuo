package com.example.feng.xgs.main.nearby.complaint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/4/28 0028.
 */

public class ComplaintDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {
        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();


        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("name");
            String time = object.getString("createtime");
            String parentId = object.getString("parentid");
            String peopleId = object.getString("peopleid");
            String intype = object.getString("intype");
            String type = object.getString("type");
            String id= object.getString("id");


            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.TYPE, ContentKeys.NORMAL)
                    .setField(EntityKeys.ID_ONLY, id)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
