package com.example.feng.xgs.main.nearby.location;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/6/9 0009.
 *
 */

public class NearbyLocationDataConverter extends BaseDataConverter {

    private String mCity;

    @Override
    public ArrayList<BaseEntity> convert() {

        addHeadData();

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");


//        String[] array = new String[]{"北京", "天津", "上海", "深圳", "河北省",
//                "山西省", "辽宁省", "吉林省", "黑龙江省", "江苏省",
//                "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省"};

//        String[] array = new String[]{"北京", "天津", "上海", "深圳", "河北省",
//                "山西省", "内蒙古", "辽宁省", "吉林省", "黑龙江省", "江苏省",
//                "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省",
//                "湖北省", "湖南省", "广东省", "广西省", "海南省", "四川省",
//                "贵州省", "云南省", "西藏", "陕西省", "甘肃省", "青海省",
//                "宁夏", "新疆", "台湾省", "澳门", "香港"};
        int size = array.size();
        for (int i = 0; i < size; i++) {
            String province = array.getString(i);
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.CITY, province)
                    .setItemType(ItemTypeKeys.LIST_ITEM)
                    .setField(EntityKeys.SPAN_SIZE, 1)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }

    private void addHeadData(){
        BaseEntity entity = BaseEntity.builder()
                .setField(EntityKeys.CITY, mCity)
                .setItemType(ItemTypeKeys.LIST_HEAD)
                .setField(EntityKeys.SPAN_SIZE, 3)
                .build();
        ENTITIES.add(entity);
    }

    public NearbyLocationDataConverter setCity(String city){
        this.mCity = city;
        return this;
    }
}
