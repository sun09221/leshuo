package com.example.feng.xgs.main.find.shop.detail.parameter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/5/15 0015.
 */

public class ShopParameterDataConverter extends BaseDataConverter{

    private Context mContext;
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONObject object = JSON.parseObject(getJsonData());
//        String site = object.getString("site");//厂址
//        String site = object.getString("licence");//许可证
//        String site = object.getString("series");//系列
//        String site = object.getString("packingtype");//包装类型
//        String site = object.getString("flavor");//口味
//        String site = object.getString("netvolume");//净含量
//        String site = object.getString("id");//商品id
//        String site = object.getString("factoryname");//厂名
//        String site = object.getString("createtime");//创建时间
//        String site = object.getString("province");//省份
//        String site = object.getString("packingmethod");//包装方式
//        String site = object.getString("brand");//品牌
//        String site = object.getString("placeoforigin");//产地

        List<String> list = new ArrayList<>();
        list.add(object.getString("netvolume"));//净含量
        list.add(object.getString("packingmethod"));//包装方式
        list.add(object.getString("packingtype"));//包装类型
        list.add(object.getString("brand"));//品牌
        list.add(object.getString("series"));//系列
        list.add(object.getString("flavor"));//口味
        list.add(object.getString("placeoforigin"));//产地
        list.add(object.getString("province"));//省份
        list.add(object.getString("licence"));//许可证
        list.add(object.getString("factoryname"));//厂名
        list.add(object.getString("site"));//厂址


        String[] array = mContext.getResources().getStringArray(R.array.find_shop_detail_parameter);
        for (int i = 0; i < array.length; i++) {
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, array[i])
                    .setField(EntityKeys.INFO, list.get(i))
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }


    public ShopParameterDataConverter setContext(Context context){
        this.mContext = context;
        return this;
    }


}
