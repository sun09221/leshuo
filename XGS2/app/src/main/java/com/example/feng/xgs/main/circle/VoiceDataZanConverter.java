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
public class VoiceDataZanConverter extends BaseDataConverter {
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
            String releasetime = object.getString("releasetime");
            String sex = object.getString("sex");
            String nickname = object.getString("nickname");
            String title = object.getString("title");
            String musicpath = object.getString("musicpath");
            String label = object.getString("label");
            String labelname = object.getString("labelname");
            String labelimg = object.getString("labelimg");
            String header = object.getString("header");
            String praiseNum = object.getString("praiseNum");
            String id = object.getString("id");
            String createtime = object.getString("createtime");
            String zan = object.getString("zan");
            String lengthtime = object.getString("lengthtime");
            String comNum = object.getString("comNum");
            String time = object.getString("time");
            String peopleid = object.getString("peopleid");
            String starsign = object.getString("starsign");
            String state = object.getString("state");
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.PEOPLE_ID, peopleid)
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                    .setField("releasetime", releasetime)
                    .setField("sex", sex)
                    .setField("nickname", nickname)
                    .setField("musicpath", musicpath)
                    .setField("label", label)
                    .setField("labelname", labelname)
                    .setField("labelimg", labelimg)
                    .setField("header", header)
                    .setField("praiseNum", praiseNum)
                    .setField("id", id)
                    .setField("title", title)
                    .setField("time", time)
                    .setField("createtime", createtime)
                    .setField("zan", zan)
                    .setField("lengthtime", lengthtime)
                    .setField("comNum", comNum)
                    .setField("starsign", starsign)
                    .setField("state", state)
                    .build();
            ENTITIES.add(entity);


        }
        return ENTITIES;
    }
}
