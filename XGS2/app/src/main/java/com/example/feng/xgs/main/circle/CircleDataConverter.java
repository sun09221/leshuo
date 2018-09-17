package com.example.feng.xgs.main.circle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * 描述：添加类的描述
 *
 * @author 金源
 * @time 2018/7/23
 */
public class CircleDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONObject objectData = JSON.parseObject(getJsonData());

        JSONArray array = objectData.getJSONArray("datalist");
        if (array == null) {
            return ENTITIES;
        }

        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSONObject.parseObject(array.getString(i));
            String name = object.getString("name");
            String createtime = object.getString("createtime");
            String describe = object.getString("describe");
            String peoplenum = object.getString("peoplenum");
            String id = object.getString("id");
            String images = object.getString("images");
            String peopleId = object.getString("peopleId");
            String state = object.getString("state");
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.PEOPLE_ID, peopleId)
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                    .setField("createtime", createtime)
                    .setField("describe", describe)
                    .setField("id", id)
                    .setField("peoplenum", peoplenum)
                    .setField("images", images)
                    .setField("name", name)
                    .setField("state", state)
                    .build();
            ENTITIES.add(entity);

        }
        return ENTITIES;
    }
}
