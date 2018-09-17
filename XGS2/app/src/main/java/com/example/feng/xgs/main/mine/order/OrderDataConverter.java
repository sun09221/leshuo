package com.example.feng.xgs.main.mine.order;

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

public class OrderDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");

        if(array != null){
            int size = array.size();
            for (int i = 0; i < size; i++) {
                JSONObject object = JSON.parseObject(array.getString(i));
                String time = object.getString("createtime");
                String price = object.getString("price");
                String orderNo = object.getString("orderNo");
                String type = object.getString("state");
                String orderId = object.getString("orderid");

                BaseEntity entity = BaseEntity.builder()
                        .setField(EntityKeys.TIME, time)
                        .setField(EntityKeys.TYPE, type)
                        .setField(EntityKeys.ORDER_ID, orderId)
                        .setField(EntityKeys.PRICE, "￥" + price)
                        .setField(EntityKeys.NUMBER, orderNo)
                        .build();

                ENTITIES.add(entity);
            }
        }


        return ENTITIES;
    }
}
