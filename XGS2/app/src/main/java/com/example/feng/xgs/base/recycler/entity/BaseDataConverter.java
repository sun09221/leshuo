package com.example.feng.xgs.base.recycler.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/12/6.
 * 数据处理基础类
 */

public abstract class BaseDataConverter {

    public final ArrayList<BaseEntity> ENTITIES = new ArrayList<>();
    public final List<BaseEntity> TITLES = new ArrayList<>();
    private String mJsonData = null;
    public abstract ArrayList<BaseEntity> convert();

    public BaseEntity title(){
        return null;
    }

    public void clearData(){
        ENTITIES.clear();
    }

    public BaseDataConverter setJsonData(String json){
        this.mJsonData = json;
        return this;
    }

    public String getJsonData(){
        if(mJsonData == null || mJsonData.isEmpty()){
            throw new NullPointerException("DATA IS NULL");
        }
        return mJsonData;
    }
}
