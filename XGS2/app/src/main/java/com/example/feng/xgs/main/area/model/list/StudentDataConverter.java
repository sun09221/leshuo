package com.example.feng.xgs.main.area.model.list;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/31 0031.
 */

public class StudentDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {


        JSONObject objectData = JSON.parseObject(getJsonData());
        String countAll = "学员个数\n" + objectData.getString("peoplenum");//总人数
        String countPopularity = "总人气值\n" + objectData.getString("total");//总人气
        String countVisit = "访问量\n" + objectData.getString("amount");//总访问量

        BaseEntity entityHead = BaseEntity.builder()
                .setField(EntityKeys.COUNT, countAll)
                .setField(EntityKeys.COUNT_POPULARITY, countPopularity)
                .setField(EntityKeys.COUNT_VISIT, countVisit)
                .setItemType(ItemTypeKeys.LIST_HEAD)
                .setField(EntityKeys.SPAN_SIZE, 2)
                .build();
        ENTITIES.add(entityHead);


        JSONArray array = objectData.getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("name");
            String popularity = object.getString("popularity");
            String imgUrl = object.getString("imgpath");
            String idOnly = object.getString("id");
            String likesnum = object.getString("likesnum");
            String idcard = object.getString("idcard");
            String time = object.getString("createtime");
            String ballotnumber = object.getString("ballotnumber");
            String peopleid = object.getString("peopleid");
            String phone = object.getString("phone");
            String raceareaid = object.getString("raceareaid");
            String state = object.getString("state");

            String info = "人气值" + popularity + "\n赛区: 郑州赛区";
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .setField(EntityKeys.INFO, info)
                    .setField(EntityKeys.PEOPLE_ID, peopleid)
                    .setItemType(ItemTypeKeys.LIST_ITEM)
                    .setField(EntityKeys.SPAN_SIZE, 1)
                    .build();
            ENTITIES.add(entity);
        }

//        String[] imageArray = new String[]{UrlKeys.BEAUTY_IMG_URL_TEST,
//                UrlKeys.BEAUTY_IMG_URL_TEST_TWO, UrlKeys.BEAUTY_IMG_URL_TEST_THREE};
//        Random random = new Random();
//        String info = "人气值: 12345\n赛区: 郑州赛区";
//        for (int i = 0; i < 10; i++) {
//            String imgUrl = imageArray[random.nextInt(3)];
//            BaseEntity entity = BaseEntity.builder()
//                    .setField(EntityKeys.IMG_URL, imgUrl)
//                    .setField(EntityKeys.NAME, "张伟")
//                    .setField(EntityKeys.INFO, info)
//                    .setItemType(ItemTypeKeys.LIST_ITEM)
//                    .setField(EntityKeys.SPAN_SIZE, 1)
//                    .build();
//            ENTITIES.add(entity);
//        }
        return ENTITIES;
    }

}
