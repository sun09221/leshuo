package com.example.feng.xgs.main.area.model.detail.gift;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/6/1 0001.
 */

public class GiftListDataConverter extends BaseDataConverter{
    @Override
    public ArrayList<BaseEntity> convert() {

//        {"message":"获取数据成功","recordCount":1,"result":"1",
//                "pageCount":1,"datalist":[{"price":"价格","rewardname":"兰博基尼",
//                "rewardid":"1","imagepath":"","popularity":"0"}]}
        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("rewardname");
            String price = object.getString("price");
            String giftId = object.getString("rewardid");
            String imgUrl = object.getString("imagepath");
            String popularity = object.getString("popularity");

            String info = "一个" + name + " = " + popularity + "人气值";
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.PRICE, price)
                    .setField(EntityKeys.ID_ONLY, giftId)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.INFO, info)
                    .setField(EntityKeys.COUNT, 0)
                    .build();

            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
