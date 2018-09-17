package com.example.feng.xgs.main.mine.product;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/30 0030.
 */

public class ProductDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

//        {
//            "message": "获取数据成功",
//                "recordCount": 总数,
//                "result": "1",
//                "pageCount": 总页数,
//                "datalist": [{
//            "price": "价格",
//                    "productid": "商品ID",
//                    "state": "状态：0：未审核 1：审核通过 2：审核未通过",
//                    "imagespath": "图片",
//                    "productname": "商品名称"
//        }]
//        }

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        if(array != null){
            int size = array.size();
            for (int i = 0; i < size; i++) {
                JSONObject object = JSON.parseObject(array.getString(i));
                String productId = object.getString("productid");
                String state = object.getString("state");
                String imgUrl = object.getString("imagespath");
                String name = object.getString("productname");
                String price = object.getString("price");

                BaseEntity entity = BaseEntity.builder()
                        .setField(EntityKeys.ID_ONLY, productId)
                        .setField(EntityKeys.TYPE, state)
                        .setField(EntityKeys.IMG_URL, imgUrl)
                        .setField(EntityKeys.NAME, name)
                        .setField(EntityKeys.PRICE, "￥" + price)
                        .build();
                ENTITIES.add(entity);
            }
        }
        return ENTITIES;
    }
}
