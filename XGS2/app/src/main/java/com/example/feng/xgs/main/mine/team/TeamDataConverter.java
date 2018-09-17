package com.example.feng.xgs.main.mine.team;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/9 0009.
 */

public class TeamDataConverter extends BaseDataConverter{
    @Override
    public ArrayList<BaseEntity> convert() {

//        "datalist": [{
//            "id": "",
//                    "createtime": "",
//                    "num": "0",
//                    "memberid": "7",
//                    "nickname": "",
//                    "code": "",
//                    "type": "0",
//                    "peopleid": "",
//                    "header": ""
//        }]
        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        if(array != null){

            for (int i = 0; i < array.size(); i++) {
                JSONObject object = JSON.parseObject(array.getString(i));
                String name = object.getString("nickname");
                String sex = object.getString("sex");
                String address = object.getString("address");
                String imgUrl = object.getString("header");
                String code = object.getString("code");
                String type = object.getString("type");
                String memberid = object.getString("memberid");
                String peopleid = object.getString("peopleid");
                String count = object.getString("num");

                BaseEntity entity = BaseEntity.builder()
                        .setField(EntityKeys.NAME, name)
                        .setField(EntityKeys.SEX, sex)
                        .setField(EntityKeys.ADDRESS, address)
                        .setField(EntityKeys.COUNT, "团队人数:" + count + "人")
                        .setField(EntityKeys.IMG_URL, imgUrl)
                        .build();
                ENTITIES.add(entity);

            }
        }
        return ENTITIES;
    }
}
