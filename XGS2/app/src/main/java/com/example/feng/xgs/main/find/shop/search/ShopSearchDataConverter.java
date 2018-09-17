package com.example.feng.xgs.main.find.shop.search;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/6/11 0011.
 */

public class ShopSearchDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {
        addHeadData();
        addItemData();

        return ENTITIES;
    }

    private void addHeadData(){
        List<String> list = new ArrayList<>();
        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        if(array != null){
            int size = array.size();
            for (int i = 0; i < size; i++) {
                JSONObject object = JSON.parseObject(array.getString(i));
                String name = object.getString("word");
                String count = object.getString("num");//搜索量
                list.add(name);
            }
        }


        BaseEntity entity = BaseEntity.builder()
                .setField(EntityKeys.ENTITY, list)
                .setItemType(ItemTypeKeys.FIND_SHOP_SEARCH_HOT)
                .build();
        ENTITIES.add(entity);
    }

    private void addItemData(){

        String jsonOld = SharedPreferenceUtils.getCustomAppProfile(ContentKeys.SEARCH_JSON);
        if(!TextUtils.isEmpty(jsonOld)){
            String[] searchArrayOld = jsonOld.split(ContentKeys.DELIMIT);
            for (int i = 0; i < searchArrayOld.length; i++) {
                BaseEntity entity = BaseEntity.builder()
                        .setField(EntityKeys.NAME, searchArrayOld[i])
                        .setItemType(ItemTypeKeys.FIND_SHOP_SEARCH_HISTORY)
                        .build();
                ENTITIES.add(entity);
            }
        }

    }

//    public void get
}
