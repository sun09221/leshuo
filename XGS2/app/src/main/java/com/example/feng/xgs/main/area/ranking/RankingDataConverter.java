package com.example.feng.xgs.main.area.ranking;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/31 0031.
 */

public class RankingDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

//        "datalist":[{"order":"排名","name":"名称","ballotnumber":"票数","popularity":"人气值"}]}
        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("name");
            String position = object.getString("order");
            String ballotnumber = object.getString("ballotnumber");//票数
            String popularityCount = object.getString("popularity");
            String number = object.getString("orderno");
//            String number = "12345";

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.POSITION, position)
                    .setField(EntityKeys.COUNT, popularityCount)
                    .setField(EntityKeys.NUMBER, number)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
