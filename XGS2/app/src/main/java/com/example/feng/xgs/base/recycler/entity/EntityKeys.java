package com.example.feng.xgs.base.recycler.entity;

/**
 * Created by feng on 2017/12/6.
 */

public interface EntityKeys {


    String ITEM_TYPE = "itemType";

    //id
    String UID = "uId";
    String PEOPLE_ID = "pId";
    String HIS_UID = "hisUId";
    String ID_ONLY = "onlyId";
    String DYNAMIC_ID = "dynamicId";
    String PRODUCT_ID = "productId";
    String ORDER_ID = "orderId";
    String CREATETIME = "createtime";


    //图片
    String IMG_URL = "imgUrl";
    String IMG_URL_HEAD = "imgUrlHead";
    String IMG_URL_S = "imgUrlS";
    String IMG_PATH= "imgpath";
    String IMG_RESOURCE = "imgResource";



    /**
     * 常用字段
     * */
    String NAME = "name";
    String COUNT = "count";

    String PRICE = "price";
    String PRICE_ALL = "priceAll";
    String INFO = "info";//详细信息
    String autograph = "autograph";//详细信息
    String DETAIL = "detail";
    String TIME = "time";
    String NUMBER = "number";
    String MOBILE = "mobile";
    String DISTANCE = "distance";//距离
    String SEX = "sex";
    String HINT = "hint";//提示信息


    //交友资料
    String AGE = "age";
    String BIRTHDAY = "birthday";
    String HOMETOWN = "hometown";//家乡
    String CONSTELLATION = "constellation";//星座
    String INDUSTRY = "industry";//职业
    String CIRCLE = "circle";


    String LINK = "link";//链接
    String ENTITY = "entity";//对象
    String POSITION = "position";//排名索引值


    //count
    String COUNT_POPULARITY = "popularityCount";
    String COUNT_VISIT = "VISITCount";
    String COUNT_LIKE = "countLike";
    String COUNT_COMMENT = "countComment";
    String COUNT_ATTENTION = "countAttention";
    String COUNT_GIVE = "countGive";

    /**
     * 地址相关字段
     * */
    String ADDRESS_ALL = "addressAll";
    String ADDRESS = "address";
    String PROVINCE = "province";
    String CITY = "city";
    String AREA = "area";
    String COUNTY = "county";
    String ADDRSS_CONSTANT = "addressConstant";//固定地址

    //交友资料，标签颜色和背景色
    String DATING_COLOR_RESOURCE = "color";
    String DATING_BACKGROUND_RESOURCE = "background";

    String TYPE_ATTENTION = "typeAttention";
    String TYPE_LIKE = "typeLike";
    String TYPE = "type";
    String SPAN_SIZE = "spanSize";
    String ZAN = "zan";


}
