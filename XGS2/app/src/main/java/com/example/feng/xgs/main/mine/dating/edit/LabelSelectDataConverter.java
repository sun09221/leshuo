package com.example.feng.xgs.main.mine.dating.edit;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;


import java.util.ArrayList;

/**
 * Created by feng on 2018/5/11 0011.
 */

public class LabelSelectDataConverter extends BaseDataConverter {

    private boolean isShowHead = false;
    //从上个页面传递来的， 要编辑的信息
    private String mEditorInfo;

    private int mPosition = -1;

    @Override
    public ArrayList<BaseEntity> convert() {

        addHeadData();

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();


        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("name");
            String time = object.getString("createtime");
            String parentId = object.getString("parentid");
            String peopleId = object.getString("peopleid");
            String intype = object.getString("intype");
            String type = object.getString("type");
            String id= object.getString("id");

            boolean isSelect = false;
            if(!TextUtils.isEmpty(mEditorInfo)){
                String[] oldArray = mEditorInfo.split(",");
                for (int j = 0; j < oldArray.length; j++) {
                    String oldName = oldArray[j];
                    if(name.equals(oldName)){
                        isSelect = true;
                        mPosition = i;
                        break;
                    }
                }
            }

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.TYPE, isSelect ? ContentKeys.SELECT : ContentKeys.NORMAL)
                    .setItemType(ItemTypeKeys.DATING_LABEL_SELECT_ITEM)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }

    private void addHeadData(){
        if(isShowHead){
            BaseEntity entity = BaseEntity.builder()
                    .setItemType(ItemTypeKeys.DATING_LABEL_SELECT_HEAD)
                    .build();
            ENTITIES.add(entity);
        }
    }

    public int getSelectPosition(){
        return mPosition;
    }

    public LabelSelectDataConverter setShowHead(boolean isShowHead){
        this.isShowHead = isShowHead;
        return this;
    }

    public LabelSelectDataConverter setEditorInfo(String info){
        this.mEditorInfo = info;
        return this;
    }
}
