package com.example.feng.xgs.main.mine.setting.help;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/6/27 0027.
 */

public class HelpDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {
//        {
//            "message": "获取数据成功",
//                "result": "1",
//                "datalist": [{
//            "answer": "答案",
//                    "question": "问题"
//        }]
//        }

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        if(array != null){
            int size = array.size();
            for (int i = 0; i < size; i++) {
                JSONObject object = JSON.parseObject(array.getString(i));
                String question = object.getString("question");
                String answer = object.getString("answer");

                BaseEntity entity = BaseEntity.builder()
                        .setField(EntityKeys.NAME, question)
                        .setField(EntityKeys.INFO, answer)
                        .setField(EntityKeys.POSITION, String.valueOf(i + 1) + ".")
                        .setField(EntityKeys.TYPE, ContentKeys.NORMAL)
                        .build();

                ENTITIES.add(entity);
            }
        }
        return ENTITIES;
    }
}
