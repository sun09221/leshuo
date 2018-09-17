package com.example.feng.xgs.base.recycler.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * Created by feng on 2017/12/6.
 */

public class BaseEntity implements MultiItemEntity{
    private final ReferenceQueue<LinkedHashMap<Object, Object>> ITEM_QUEUE = new ReferenceQueue<>();
    private final LinkedHashMap<Object, Object> MULTIPLE_FIELDS = new LinkedHashMap<>();
    private final SoftReference<LinkedHashMap<Object, Object>> FIELDS_REFERENCE =
            new SoftReference<>(MULTIPLE_FIELDS, ITEM_QUEUE);

    public BaseEntity(LinkedHashMap<Object, Object> fields) {
        FIELDS_REFERENCE.get().putAll(fields);
    }

    public static BaseEntityBuilder builder(){
        return new BaseEntityBuilder();
    }

    @Override
    public int getItemType() {
        return (int) FIELDS_REFERENCE.get().get(EntityKeys.ITEM_TYPE);
    }

    public final <T> T getFieldObject(Object key){
        return (T) FIELDS_REFERENCE.get().get(key);
    }

    public final String getField(String key){
        return (String) FIELDS_REFERENCE.get().get(key);
    }

//    public final int getFieldInteger(String key){
//        return (int) FIELDS_REFERENCE.get().get(key);
//    }
//
//    public final String getField(String key){
//        return (String) FIELDS_REFERENCE.get().get(key);
//    }

//    public final List<BaseEntity> getFields(String key){
//        return (List<BaseEntity>) FIELDS_REFERENCE.get().get(key);
//    }

    public final LinkedHashMap<?, ?> getFields(){
        return FIELDS_REFERENCE.get();
    }

    public final BaseEntity setField(Object key, Object value){
        FIELDS_REFERENCE.get().put(key,value);
        return this;
    }


}
