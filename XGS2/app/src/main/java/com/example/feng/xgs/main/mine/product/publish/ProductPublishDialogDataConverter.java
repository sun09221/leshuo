package com.example.feng.xgs.main.mine.product.publish;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/6/19 0019.
 */

public class ProductPublishDialogDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {
        JSONObject objectData = JSON.parseObject(getJsonData());

        JSONArray array = objectData.getJSONArray("datalist");
        if(array == null){
            return ENTITIES;
        }

        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.INFO, object.get("name"))
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
