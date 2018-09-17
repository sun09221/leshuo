package com.example.feng.xgs.main.move;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/4/27 0027.
 */

public class NaturalChildDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONObject objectData = JSON.parseObject(getJsonData());

        JSONArray array = objectData.getJSONArray("datalist");
        if(array == null){
            return ENTITIES;
        }

        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSONObject.parseObject(array.getString(i));
            String content = object.getString("content");
            String circle = "圈子: " + object.getString("crname");
            String sex = object.getString("sex");
            String name = object.getString("nickname");
            String imageUrls = object.getString("imagepath");
            String state = object.getString("state");//"state":"状态：0：为未关注 1：已关
            String peopleId = object.getString("peopleid");
            String imageUrl = object.getString("header");
            String dynamicId = object.getString("dynamicid");
            String likeType = object.getString("zan");
            String type = object.getString("type");
            String videoImg = object.getString("videoImg");

            int itemType = ItemTypeKeys.NATURAL_ATTENTION;
            if(ContentKeys.NATURAL_PUBLISH_VIDEO.equals(type)){
                itemType = ItemTypeKeys.NATURAL_VIDEO;
            }

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.PEOPLE_ID, peopleId)
                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.INFO, content)
                    .setField(EntityKeys.SEX, sex)
                    .setField(EntityKeys.IMG_URL, imageUrl)
                    .setField(EntityKeys.IMG_URL_S, imageUrls)
                    .setField(EntityKeys.CIRCLE, circle)
                    .setField(EntityKeys.TYPE_ATTENTION, state)
                    .setField(EntityKeys.TYPE_LIKE, likeType)
                    .setField(EntityKeys.TYPE, type)
                    .setField(ParameterKeys.videoImg, videoImg)
                    .setItemType(itemType)
                    .build();
            ENTITIES.add(entity);

        }

//        for (int i = 0; i < 2; i++) {
//            BaseEntity entity = BaseEntity.builder()
//                    .setField(EntityKeys.PEOPLE_ID, "")
//                    .setField(EntityKeys.DYNAMIC_ID, "")
//                    .setField(EntityKeys.NAME, "")
//                    .setField(EntityKeys.INFO, "")
//                    .setField(EntityKeys.SEX, "")
//                    .setField(EntityKeys.IMG_URL, "")
//                    .setField(EntityKeys.IMG_URL_S, UrlKeys.VIDEO_URL_TEST)
//                    .setField(EntityKeys.CIRCLE, "")
//                    .setField(EntityKeys.TYPE_ATTENTION, "")
//                    .setField(EntityKeys.TYPE_LIKE, "")
//                    .setItemType(ItemTypeKeys.NATURAL_VIDEO)
//                    .build();
//            ENTITIES.add(entity);
//        }
        return ENTITIES;
    }
}
