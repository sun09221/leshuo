package com.example.feng.xgs.main.find.shop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/5/15 0015.
 */

public class ShopDataConverter extends BaseDataConverter {

    private String mRecommend;
    private String mNormal;
    private String mHotTabJson;
    private String mBannerJson;


    @Override
    public ArrayList<BaseEntity> convert() {

        addBannerData();

        addHotTab();

        addTitleData("每日推荐");

        if(mRecommend != null){
            initJson(mRecommend, true);
        }

        addMoreData();

        addTitleData("精品特价");

        if(mNormal != null){
            initJson(mNormal, false);
        }

        return ENTITIES;
    }

    private void initJson(String json, boolean isLoadThree){
        JSONArray array = JSON.parseObject(json).getJSONArray("datalist");
        int size = array.size();
//        int spanSize = 2;
//        if(itemType == ItemTypeKeys.FIND_SHOP_IMAGE_BIG){
//            spanSize = 3;
//        }
        for (int i = 0; i < size; i++) {

            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("productname");
            String idOnly = object.getString("productid");
            String count = object.getString("salesvolume");
            String price = object.getString("price");
            String imgUrl = object.getString("sowingmap");

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.COUNT, "销量: " + count)
                    .setField(EntityKeys.PRICE, "￥" + price)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setItemType(ItemTypeKeys.FIND_SHOP_IMAGE_BIG)
                    .setField(EntityKeys.SPAN_SIZE, 2)
                    .build();
            ENTITIES.add(entity);

            //如果是每日推荐商品 --只加载三条数据
            if(isLoadThree){
                if(i == 2){
                    break;
                }
            }
        }
    }

    private void addBannerData(){

        if(mBannerJson != null){
            JSONArray array = JSON.parseObject(mBannerJson).getJSONArray("imagepath");
            if(array != null){
                List<String> imgList = new ArrayList<>();

                for (int i = 0; i < array.size(); i++) {
                    String imgUrl = array.getString(i);
                    imgList.add(imgUrl);
                }

                BaseEntity entity = BaseEntity.builder()
                        .setField(EntityKeys.IMG_URL_S, imgList)
                        .setItemType(ItemTypeKeys.BANNER)
                        .setField(EntityKeys.SPAN_SIZE, 4)
                        .build();
                ENTITIES.add(entity);
            }
        }

    }

    private void addHotTab(){
        if(mHotTabJson != null){
            JSONArray array = JSON.parseObject(mHotTabJson).getJSONArray("datalist");
            if(array != null){
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = JSON.parseObject(array.getString(i));
                    String name = object.getString("name");
                    String imgUrl = object.getString("imagepath");
                    String idOnly = object.getString("id");
                    String parentId = object.getString("parentid");

                    BaseEntity entity = BaseEntity.builder()
                            .setField(EntityKeys.NAME, name)
                            .setField(EntityKeys.IMG_URL, imgUrl)
                            .setField(EntityKeys.ID_ONLY, idOnly)
                            .setItemType(ItemTypeKeys.FIND_SHOP_HOT_TAB)
                            .setField(EntityKeys.SPAN_SIZE, 1)
                            .build();

                    ENTITIES.add(entity);
                }

            }
        }


    }

    private void addTitleData(String title){
        BaseEntity entity = BaseEntity.builder()
                .setField(EntityKeys.NAME, title)
                .setItemType(ItemTypeKeys.FIND_SHOP_TITLE)
                .setField(EntityKeys.SPAN_SIZE, 4)
                .build();

        ENTITIES.add(entity);
    }


    private void addMoreData(){
        BaseEntity entity = BaseEntity.builder()
                .setItemType(ItemTypeKeys.FIND_SHOP_MORE)
                .setField(EntityKeys.SPAN_SIZE, 4)
                .build();

        ENTITIES.add(entity);
    }

    public ShopDataConverter setRecommendJson(String recommend){
        this.mRecommend = recommend;
        return this;
    }

    public ShopDataConverter setNormalJson(String normal){
        this.mNormal = normal;
        return this;
    }

    public ShopDataConverter setBannerJson(String bannerJson){
        this.mBannerJson = bannerJson;
        return this;
    }

    public ShopDataConverter setHotTabJson(String hotTabJson){
        this.mHotTabJson = hotTabJson;
        return this;
    }




}
