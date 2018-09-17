package com.example.feng.xgs.main.find.shop.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/21 0021.
 */

public class SureOrderDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseArray(getJsonData());
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("name");
            String imgUrl = object.getString("imgUrl");
            String price = object.getString("price");
            String count = object.getString("count");
            String onlyId = object.getString("onlyId");
            String productId = object.getString("productId");
            String type = object.getString("type");

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.PRICE, "￥" + price)
                    .setField(EntityKeys.COUNT,  count)
                    .setField(EntityKeys.ID_ONLY, onlyId)
                    .setField(EntityKeys.PRODUCT_ID, productId)
                    .build();
            ENTITIES.add(entity);
        }
//        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
//        int size = array.size();
//        for (int i = 0; i < size; i++) {
//            JSONObject object = JSON.parseObject(array.getString(i));
//            String name = object.getString("productname");
//            String mobile = object.getString("phone");
//            String price = object.getString("price");
//            String receiverName = object.getString("receivername");
//            String address = object.getString("address");
//            String productid = object.getString("productid");
//            String state = object.getString("state");
//            String netvolume = object.getString("netvolume");//净含量
//            String imgUrl = object.getString("imagespath");
//            String orderid = object.getString("orderid");
//            String count = object.getString("num");
//
//
//            String qualityResult = "";
//            if(!TextUtils.isEmpty(netvolume)){
//                float qualityG = Float.parseFloat(netvolume);//转换为float类型，单位m
//                DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//
//                if(qualityG < 1000){
//                    qualityResult = decimalFormat.format(qualityG);
//                }else {
//
//                    float  qualityKG = qualityG/1000;
//                    qualityResult = decimalFormat.format(qualityKG) + "K";//format 返回的是字符串
//                }
//            }else {
//                qualityResult = netvolume;
//            }
//
//
//
//        }

        return ENTITIES;
    }
}
