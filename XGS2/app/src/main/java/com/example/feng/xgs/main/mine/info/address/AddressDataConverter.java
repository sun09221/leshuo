package com.example.feng.xgs.main.mine.info.address;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2017/12/22.
 * 收货地址
 */

public class AddressDataConverter extends BaseDataConverter {


    @Override
    public ArrayList<BaseEntity> convert() {
        JSONObject objectData = JSON.parseObject(getJsonData());
        JSONArray array = objectData.getJSONArray("datalist");
        if(array == null) return ENTITIES;
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = JSON.parseObject(array.getString(i));

            String peopleId = object.getString("peopleid");
            String idOnly = object.getString("id");
            String type = object.getString("type");

            String name = object.getString("receivername");
            String mobile = object.getString("receiverphone");

            String postcode = object.getString("postcode");//邮编

            String editorTime = object.getString("modifytime");
            String createTime = object.getString("createtime");

            String address = object.getString("address");
            String province = object.getString("province");
            String city = object.getString("city");
            String county = object.getString("county");
            String street = object.getString("street");

            String addressAll = province + city + county + ", " + street + address;

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.ADDRESS_ALL, addressAll)
                    .setField(EntityKeys.MOBILE, mobile)
                    .setField(EntityKeys.TYPE, type)
                    .build();
            if(type.equals(ContentKeys.ADDRESS_DEFAULT)){
                ENTITIES.add(0, entity);
            }else {
                ENTITIES.add(entity);
            }
        }

        return ENTITIES;
    }

}
