package com.example.feng.xgs.main.area.record.gift;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/6/6 0006.
 */

public class RecordGiftDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("nickname");
            String total = object.getString("total");//赠送人气值
            String price = object.getString("price");//价格
            String rewardName = object.getString("rewardname");//礼物名称
            String imgUrl = object.getString("imagepath");//礼物图片
            String header = object.getString("header");//头像
            String popularity = object.getString("popularity");//礼物人气值


//            String count = "1";
//            String info = "";
//            if(TextUtils.isEmpty(count) && TextUtils.isEmpty(popularity)){
//                int countInteger = Integer.parseInt(count);
//                int popularityInteger = Integer.parseInt(popularity);
//                int popularityAll = countInteger * popularityInteger;
//            }

            String info = "一个" + rewardName + " = " + popularity + "人气值";
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.INFO, info)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.IMG_URL_HEAD, header)
                    .setField(EntityKeys.PRICE, price)
                    .build();
            ENTITIES.add(entity);

        }
        return ENTITIES;
    }
}
