package com.example.feng.xgs.main.circle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.ArrayList;

/**
 * 描述：添加类的描述
 *
 * @author 金源
 * @time 2018/7/23
 */
public class TitleDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONObject objectData = JSON.parseObject(getJsonData());

        JSONArray array = objectData.getJSONArray("datalist");
        if (array == null) {
            return ENTITIES;
        }

        int size = array.size();
        BaseEntity entity1 = BaseEntity.builder()
                .setField(EntityKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId())
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                .setField("createtime", "")
                .setField("describe", "全部")
                .setField("id", "111")
                .setField("peoplenum", "1")
                .setField("images", "")
                .setField("name", "全部")
                .setField(EntityKeys.TYPE, "0")
                .build();
        ENTITIES.add(entity1);
        BaseEntity entity2 = BaseEntity.builder()
                .setField(EntityKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId())
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                .setField("createtime", "")
                .setField("describe", "好友动态")
                .setField("id", "222")
                .setField("peoplenum", "1")
                .setField("images", "")
                .setField("name", "好友动态")
                .setField(EntityKeys.TYPE, "0")
                .build();
        ENTITIES.add(entity2);
        BaseEntity entity3 = BaseEntity.builder()
                .setField(EntityKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId())
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                .setField("createtime", "")
                .setField("describe", "我的关注")
                .setField("id", "333")
                .setField("peoplenum", "1")
                .setField("images", "")
                .setField("name", "我的关注")
                .setField(EntityKeys.TYPE, "0")
                .build();
        ENTITIES.add(entity3);
        for (int i = 0; i < size; i++) {
            JSONObject object = JSONObject.parseObject(array.getString(i));

            String type = "0";
            String name = object.getString("name");
            String createtime = object.getString("createtime");
            String describe = object.getString("describe");
            String peoplenum = object.getString("peoplenum");
            String id = object.getString("id");
            String images = object.getString("images");
            String peopleId = object.getString("peopleId");
//            String type = object.getString("type");
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.PEOPLE_ID, peopleId)
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                    .setField("createtime", createtime)
                    .setField("describe", describe)
                    .setField("id", id)
                    .setField("peoplenum", peoplenum)
                    .setField("images", images)
                    .setField("name", name)
                    .setField(EntityKeys.TYPE, type)
                    .build();
            ENTITIES.add(entity);

        }
        return ENTITIES;
    }
}
