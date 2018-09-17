package com.example.feng.xgs.main.nearby;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by feng on 2018/4/26 0026.
 */

public class NearbyDataConverter extends BaseDataConverter {

    @Override
    public ArrayList<BaseEntity> convert() {
        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");

        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String birthday = object.getString("birthday");
            String age = object.getString("age");
            String phone = object.getString("phone");
            String sex = object.getString("sex");
            String hometown = object.getString("hometown");//家乡
            String nickname = object.getString("nickname");//昵称
            String address = object.getString("jobarea");//地址
            String imgUrl = object.getString("header");//
            String idOnly = object.getString("id");
            String distance = object.getString("distance");//距离
            String email = object.getString("email");
            String userId = object.getString("userId");
            String starSign = object.getString("starsign");//星座
            String ismodel = object.getString("isModel");//星座
            String industry = object.getString("industry");//职业
            String constantAddress = object.getString("constantaddress");//固定的地址
            String count = object.getString("imageNum");//固定的地址
            String type = object.getString("type");//是否VIP



            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, nickname)
                    .setField(EntityKeys.SEX, sex)
                    .setField(EntityKeys.AGE, age)
                    .setField("ismodel", ismodel)
                    .setField(EntityKeys.INDUSTRY, industry)
                    .setField(EntityKeys.HOMETOWN, hometown)
                    .setField(EntityKeys.DISTANCE, formatDistance(distance) + "m")
                    .setField(EntityKeys.ADDRESS, constantAddress)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .setField(EntityKeys.HIS_UID, userId)
                    .setField(EntityKeys.CONSTELLATION, starSign)
                    .setField(EntityKeys.COUNT, count)
                    .setField(EntityKeys.TYPE, type)
                    .build();

            ENTITIES.add(entity);
        }
        return ENTITIES;
    }

    /**
     * 格式化距离
     * */
    private String formatDistance(String distance){
        if(TextUtils.isEmpty(distance)){
            return "";
        }

        String distanceResult = "";
        float distanceM = Float.parseFloat(distance);//转换为float类型，单位m
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.

        if(distanceM < 1000){
            distanceResult = decimalFormat.format(distanceM);
        }else {

            float distanceKm = distanceM/1000;
            distanceResult = decimalFormat.format(distanceKm) + "K";//format 返回的是字符串
        }
        return distanceResult;
    }
}
