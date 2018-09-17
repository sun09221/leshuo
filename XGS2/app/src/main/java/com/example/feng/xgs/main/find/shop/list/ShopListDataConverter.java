package com.example.feng.xgs.main.find.shop.list;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/15 0015.
 */

public class ShopListDataConverter extends BaseDataConverter {

    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();

        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("productname");
            String idOnly = object.getString("productid");
            String count = object.getString("salesvolume");
            String price = object.getString("price");
            String sowingmap = object.getString("sowingmap");
            String imgUrl = object.getString("imagespath");

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.COUNT, "销量: " + count)
                    .setField(EntityKeys.PRICE, "￥" + price)
                    .setField("sowingmap", sowingmap)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .build();
            ENTITIES.add(entity);

        }
        return ENTITIES;
    }
}
