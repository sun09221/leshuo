package com.example.feng.xgs.main.find.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by feng on 2018/5/7 0007.
 */

public class StoreDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {
        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        if(array == null){
            return ENTITIES;
        }

        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("shopname");
            String distance = object.getString("distance");
            String info = object.getString("introduction");
            String address = object.getString("address");
            String mobile = object.getString("phone");
            String imgUrl = object.getString("logopath");
            String idOnly = object.getString("id");
            String createTime = object.getString("createtime");
            String modifyTime = object.getString("modifytime");
            String longitude = object.getString("longitude");
            String latitude = object.getString("latitude");

            String distanceResult = "";
            float distanceM = Float.parseFloat(distance);//转换为float类型，单位m
            DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.

            if(distanceM < 1000){
                distanceResult = decimalFormat.format(distanceM);
            }else {

                float distanceKm = distanceM/1000;
                distanceResult = decimalFormat.format(distanceKm) + "K";//format 返回的是字符串
            }


            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME ,name)
                    .setField(EntityKeys.DISTANCE ,"距您" + distanceResult + "m")
                    .setField(EntityKeys.INFO, info)
                    .setField(EntityKeys.ADDRESS_ALL , address)
                    .setField(EntityKeys.MOBILE , mobile)
                    .setField(EntityKeys.IMG_URL , imgUrl)
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
