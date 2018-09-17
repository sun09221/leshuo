package com.example.feng.xgs.main.circle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;

import java.util.ArrayList;

/**
 * 描述：添加类的描述
 *
 * @author
 * @time 2018/7/23
 */
public class MoodViewConverter extends BaseDataConverter {
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
            String id = object.getString("id");
            String imgpath = object.getString("imgpath");
            BaseEntity entity = BaseEntity.builder()
                    .setField("id", id)
                    .setField("imgpath", imgpath)
                    .build();
            ENTITIES.add(entity);

        }
        return ENTITIES;
    }
}
