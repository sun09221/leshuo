package com.example.feng.xgs.base.recycler.entity;

import java.util.LinkedHashMap;
import java.util.WeakHashMap;

/**
 * Created by feng on 2017/12/6.
 */

public class BaseEntityBuilder {
    private static final LinkedHashMap<Object, Object> FIELDS = new LinkedHashMap<>();

    public BaseEntityBuilder() {
        FIELDS.clear();
    }

    public final BaseEntityBuilder setItemType(int itemType){
        FIELDS.put(EntityKeys.ITEM_TYPE,itemType);
        return this;
    }

    public final BaseEntityBuilder setField(Object key, Object value){
        FIELDS.put(key,value);
        return this;
    }

//    public final BaseEntityBuilder setField(Object key, List<BaseEntity> value){
//        FIELDS.put(key,value);
//        return this;
//    }

    public final BaseEntityBuilder setFields(WeakHashMap<?, ?> map){
        FIELDS.putAll(map);
        return this;
    }

    public final BaseEntity build(){
        return new BaseEntity(FIELDS);
    }
}
