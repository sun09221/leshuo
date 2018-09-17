package com.example.feng.xgs.main.circle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * 描述：添加类的描述
 *
 * @author
 * @time 2018/7/23
 */
public class VoiceCommentDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONObject objectData = JSON.parseObject(getJsonData());

        JSONArray array = objectData.getJSONArray("datalist");
        if (array == null) {
            return ENTITIES;
        }

        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSONObject.parseObject(array.getString(i));
            String id = object.getString("id");
            String content = object.getString("content");
            String createtime = object.getString("createtime");
            String musicid = object.getString("musicid");
            String nickname = object.getString("nickname");
            String peopleid = object.getString("peopleid");
            String ctime = object.getString("ctime");
            String header = object.getString("header");
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.PEOPLE_ID, peopleid)
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                    .setField("id", id)
                    .setField("content", content)
                    .setField("createtime", createtime)
                    .setField("musicid", musicid)
                    .setField("nickname", nickname)
                    .setField("peopleid", peopleid)
                    .setField("ctime", ctime)
                    .setField("header", header)
                    .build();
            ENTITIES.add(entity);

        }
        return ENTITIES;
    }
}
