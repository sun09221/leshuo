package com.example.feng.xgs.main.mine.dating;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.BaseEntityBuilder;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/5/10 0010.
 * 我的交友资料
 */

public class DatingDataConverter extends BaseDataConverter {

    @Override
    public ArrayList<BaseEntity> convert() {

        JSONObject object = JSON.parseObject(getJsonData());

        String mBirthday = object.getString("birthday");//出生日期
        String sex = object.getString("sex");//性别
        String mMobile = object.getString("phone");//电话
        String mNickName = object.getString("nickname");//昵称
        String mImgUrl = object.getString("header");//头像
        String idOnly = object.getString("id");//个人id
        String message = object.getString("message");
        String mEmail = object.getString("email");//邮箱
        String userId = object.getString("userId");
        String imgUrls = object.getString("imagepath");
        String starsign = object.getString("starsign");//星座
        String signIn = object.getString("autograph");

        String label = object.getString("lable");//我的个性标签
        // ---------- 我的信息数据
        String industry = object.getString("industry");//行业信息
        String jobArea = object.getString("jobarea");//工作领域
        String company = object.getString("companyname");//公司
        String hometown = object.getString("hometown");//家乡
        String address = object.getString("constantaddress");//常去地址
        List<String> infoList = new ArrayList<>();
        infoList.add(industry);
        infoList.add(jobArea);
        infoList.add(company);
        infoList.add(hometown);
        infoList.add(address);

        // ------------- 我的兴趣
        String sports = object.getString("sports");//运动
        String music = object.getString("music");//音乐
        String food = object.getString("food");//食物
        String movie = object.getString("film");//电影
        String books = object.getString("books");//阅读
        String travel = object.getString("travel");//旅行

        //----------我问题
        String question = object.getString("question");//旅行
        String answer = object.getString("answer");//回答

        List<String> likeList = new ArrayList<>();
        likeList.add(sports);
        likeList.add(music);
        likeList.add(food);
        likeList.add(movie);
        likeList.add(books);
        likeList.add(travel);

        loadPhotoLayoutData(imgUrls);

        LogUtils.d("signIn: " + signIn);
        loadSignIn(signIn);

        loadInfo(infoList);

        loadLabel(label);

        loadLike(likeList);

        loadConstellation(starsign);

        loadRequest(question, answer);

        return ENTITIES;
    }

    /**
     * 1.
     * 加载相册数据
     * */
    private void loadPhotoLayoutData(String imgUrls){
        BaseEntity entity = BaseEntity.builder()
                .setField(EntityKeys.IMG_URL_S, imgUrls)
                .setItemType(ItemTypeKeys.MINE_DATING_PHOTO_LAYOUT)
                .build();
        ENTITIES.add(entity);
        addDividerData();
    }

    /**
     * 2.
     * 加载个性签名数据
     * */
    private void loadSignIn(String signIn){
        addTitleData("个性签名");
        addAppendData(signIn, "添加你的个性签名", R.mipmap.icon_dating_label, R.mipmap.icon_dating_append_red);
        addDividerData();
    }


    /**
     * 3.
     * 加载我的信息数据
     * */
    private void loadInfo(List<String> infoList){
        addTitleData("我的信息");
        String[] infoArray = new String[]{"添加行业信息", "添加工作领域信息",
                "添加公司信息", "添加你的家乡信息", "添加你常去的地点"};
        int[] infoImgArray = new int[]{R.mipmap.icon_dating_industry,
                R.mipmap.icon_dating_industry, R.mipmap.icon_dating_company,
                R.mipmap.icon_dating_hometown, R.mipmap.icon_dating_address};
        for (int i = 0; i < 5; i++) {
            addAppendData(infoList.get(i), infoArray[i], infoImgArray[i], R.mipmap.icon_dating_append_red);
        }
        addDividerData();
    }

    /**
     * 4.
     * 加载我的标签数据
     * */
    private void loadLabel(String myLabel){
        addTitleData("我的标签");
        addAppendAndLabelData(myLabel, "添加我的个性标签", R.mipmap.icon_dating_label,
                R.mipmap.icon_dating_append_red,
                R.color.label_label_dark, R.drawable.label_label_solid_radius_shape);
        addDividerData();
    }

    /**
     * 5.
     * 加载我的兴趣数据
     * */
    private void loadLike(List<String> likeList){
        addTitleData("我的兴趣");
        String[] likeArray = new String[]{"我喜欢的运动", "我喜欢的音乐",
                "我喜欢的食物", "我喜欢的电影", "我喜欢的书和漫画", "我的旅行足迹"};
        int[] likeImageArray = new int[]{R.mipmap.icon_dating_campaign,
                R.mipmap.icon_dating_music, R.mipmap.icon_dating_food,
                R.mipmap.icon_dating_movie, R.mipmap.icon_dating_writer,
                R.mipmap.icon_dating_city,};

        int[] likeAppendArray = new int[]{R.mipmap.icon_dating_campaign_append,
                R.mipmap.icon_dating_music_append, R.mipmap.icon_dating_food_append,
                R.mipmap.icon_dating_movie_append, R.mipmap.icon_dating_writer_append,
                R.mipmap.icon_dating_city_append};
        int[] colorArray = new int[]{R.color.label_campaign_dark,
                R.color.label_music_dark, R.color.label_food_dark,
                R.color.label_movie_dark, R.color.label_writer_dark,
                R.color.label_city_dark};

        int[] backgroundArray = new int[]{R.drawable.label_camaign_solid_radius_shape,
                R.drawable.label_music_solid_radius_shape, R.drawable.label_food_solid_radius_shape,
                R.drawable.label_movie_solid_radius_shape, R.drawable.label_writer_solid_radius_shape,
                R.drawable.label_city_solid_radius_shape};

        for (int i = 0; i < likeArray.length; i++) {
            addAppendAndLabelData(likeList.get(i), likeArray[i], likeImageArray[i], likeAppendArray[i], colorArray[i], backgroundArray[i]);
        }
        addDividerData();
    }


    /**
     * 6.
     * 加载星座
     * */
    private void loadConstellation(String info){
        addTitleData("我的星座");
        addAppendAndLabelData(info, "点击选择星座", R.mipmap.icon_dating_constellation,
                R.mipmap.icon_dating_append_red,
                R.color.label_constellation_dark, R.drawable.label_constellation_solid_radius_shape);
        addDividerData();
    }


    /**
     * 7.
     * 加载我的问题
     * */
    private void loadRequest(String info, String detail){
        addTitleData("我的问题");

        BaseEntityBuilder builder = BaseEntity.builder();
        String name = "添加问题";

        if(TextUtils.isEmpty(info)){
            builder.setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.IMG_RESOURCE, R.mipmap.icon_dating_append_red)
                    .setItemType(ItemTypeKeys.MINE_DATING_QUESTION_APPEND);
        }else {
            builder.setField(EntityKeys.NAME, info)
                    .setField(EntityKeys.INFO, detail)
                    .setItemType(ItemTypeKeys.MINE_DATING_QUESTION);
        }

        ENTITIES.add(builder.build());
    }



    /**
     * 添加标签布局数据
     *
     * */
    private void addAppendAndLabelData(String info, String name, int drawableDetail, int drawableAdd, int color, int background){
        BaseEntityBuilder builder = BaseEntity.builder();
        if(TextUtils.isEmpty(info)){
            builder.setField(EntityKeys.NAME, name)
                    .setItemType(ItemTypeKeys.MINE_DATING_APPEND);
        }else {
            builder.setField(EntityKeys.INFO, info)
                    .setItemType(ItemTypeKeys.MINE_DATING_LABEL);
        }
        BaseEntity entity = builder
                .setField(EntityKeys.IMG_URL, drawableDetail)
                .setField(EntityKeys.IMG_RESOURCE, drawableAdd)
                .setField(EntityKeys.DATING_COLOR_RESOURCE, color)
                .setField(EntityKeys.DATING_BACKGROUND_RESOURCE, background)
                .build();
        ENTITIES.add(entity);
    }


    /**
     * 添加 文字描述布局 的数据
     * 如果info为null 添加默认布局
     * */
    private void addAppendData(String info, String name, int drawableDetail, int drawableAdd){
        BaseEntityBuilder builder = BaseEntity.builder();
        if(TextUtils.isEmpty(info)){
            builder.setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.IMG_RESOURCE, drawableAdd)
                    .setItemType(ItemTypeKeys.MINE_DATING_APPEND);
        }else {
            builder.setField(EntityKeys.INFO, info)
                    .setItemType(ItemTypeKeys.MINE_DATING_IMG_WITH_TEXT);
        }

        BaseEntity entity = builder
                .setField(EntityKeys.IMG_URL, drawableDetail)
                .build();
        ENTITIES.add(entity);
    }


    //添加 ItemType = MINE_DATING_TITLE的数据
    private void addTitleData(String title){
        BaseEntity entity = BaseEntity.builder()
                .setField(EntityKeys.NAME, title)
                .setItemType(ItemTypeKeys.MINE_DATING_TITLE)
                .build();
        ENTITIES.add(entity);
    }

    //添加一条分割线
    private void addDividerData(){
        BaseEntity entity = BaseEntity.builder()
                .setItemType(ItemTypeKeys.MINE_DATING_DIVIDER)
                .build();
        ENTITIES.add(entity);
    }
}
