package com.example.feng.xgs.main.find.shop.cart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2017/12/21.
 */

public class ShopCartDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("productname");
            String imgUrl = object.getString("imagespath");
            imgUrl=imgUrl.split(",")[0];
            String count = object.getString("number");
            String productId = object.getString("productid");
            String price = object.getString("price");
            String idOnly = object.getString("id");


            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.PRICE, price)
                    .setField(EntityKeys.COUNT, count)
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .setField(EntityKeys.PRODUCT_ID, productId)
                    .setField(EntityKeys.TYPE, ContentKeys.NORMAL)
                    .build();
            ENTITIES.add(entity);

        }

        return ENTITIES;
    }
}
