package com.example.feng.xgs.config.key;

/**
 * Created by feng on 2018/3/16 0016.
 */

public interface ContentKeys {


    String J_PUSH_APP_KEY = "48a27b3467f0f277c1ff9b0d";

    //区分普通用户和VIP用户
    String USER_TYPE_NORMAL = "0";
    String USER_TYPE_VIP = "1";
    String USER_TYPE_VIP_SUPER = "2";

    //1表示用户是模特
    String USER_MODEL = "1";

    //支付方式
    String ALI_PAY = "2";
    String WE_CHAT = "1";

    //绑定账户，微信、支付宝
    String ACCOUNT_ALI = "2";
    String ACCOUNT_WE_CHAT = "1";

    //组件间传值Key
    String ACTIVITY_TYPE = "activityType";
    String ACTIVITY_MUSIC_HEAD = "head";
    String ACTIVITY_MUSIC_TITLE = "title";
    String ACTIVITY_MUSIC_CREATTIME = "creattime";
    String ACTIVITY_MUSIC_SEX = "sex";
    String ACTIVITY_MUSIC_CONSTELLATION = "constellation";
    String ACTIVITY_MUSIC_MUSICPATH = "musicpath";
    String ACTIVITY_MUSIC_LENGTH = "musiclength";
    String ACTIVITY_ID_ONLY = "activityIdOnly";
    String ACTIVITY_LAMEIMG = "labelimg";
    String ACTIVITY_ID= "id";
    String ACTIVITY_PEOPLEID= "peopleid";
    String ACTIVITY_STATE= "state";
    String ACTIVITY_ONLY = "activityId";
    String ACTIVITY_PEOPLE_ID = "activityPeopleId";
    String ACTIVITY_AREA_ID = "activityAreaId";
    String ACTIVITY_TITLE = "activityTitle";
    String ACTIVITY_INFO = "activityInfo";
    String ACTIVITY_ANSWER = "activityAnswer";
    String ACTIVITY_PRICE = "activityPrice";
    String ACTIVITY_ORDER_NO = "activityOrderNo";
    String ACTIVITY_LINK = "activityLink";
    String ACTIVITY_MOBILE = "activityMobile";
    String ACTIVITY_TIME = "activityTime";
    String ACTIVITY_IMG_URL = "activityImgUrl";

    //分割符
    String DELIMIT = ",";

    String DATING_POSITION = "datingPosition";
    String DATING_TYPE = "datingType";


    //是否是默认地址 1是0否
    String ADDRESS_DEFAULT = "1";
    String ADDRESS_DEFAULT_NO = "0";


    String PAGE_SIZE = "0";
    String PAGE_SIZEAALL = "10";

    //报名活动、非报名活动
    String FIND_ACTIVITY_ENROLL = "1";
    String FIND_ACTIVITY_NOT_ENROLL = "0";
    //系统活动、会员活动
    String FIND_ACTIVITY_SERVICE = "1";
    String FIND_ACTIVITY_VIP = "2";
    //我的活动通过、未通过
    String FIND_ACTIVITY_ADOPT = "1";
    String FIND_ACTIVITY_ADOPT_NO = "0";

    String MINE_ACTIVITY = "mineActivity";
    String FIND_ACTIVITY = "findActivity";

    // 交友资料添加标签时: 是否单选, 是否展示 "自定义标签" 布局
    String IS_DATING_LABEL_SELECT_ONLY = "isDatingLabelSelectOnly";
    String IS_DATING_LABEL_CUSTOM_SHOW = "isDatingLabelCustomShow";

    //选中和非选中
    String SELECT = "select";
    String NORMAL = "normal";


    String LIKE_SELECT = "1";
    String LIKE_NORMAL = "0";

    String ATTENTION_SELECT = "1";
    String ATTENTION_NORMAL = "0";

    //发布本色动态： type:动态类型：1：图文 2：视频
    String NATURAL_PUBLISH_IMAGE = "1";
    String NATURAL_PUBLISH_VIDEO = "2";

    //订单类型
    String ORDER_SHOPPING = "1";
    String ORDER_VIP = "2";
    String ORDER_PAY = "3";
    String ORDER_GIFT = "4";

    //我的产品审核中、审核成功
    String MINE_PRODUCT_CHECKING = "0";
    String MINE_PRODUCT_CHECK_FINISH = "1";

    //轮播图位置 1：商场轮播图 2：发现轮播图 3：赛区轮播图
    String BANNER_TYPE_SHOP = "1";
    String BANNER_TYPE_FIND = "2";
    String BANNER_TYPE_AREA = "3";

    //收益 购物收益，VIP收益
    String MINE_EARNINGS_SHOP = "1";
    String MINE_EARNINGS_VIP = "2";

    String SEARCH_JSON = "searchJson";

    String SEX_MAN = "男";
    String SEX_WOMEN = "女";
    String SEX_ALL = "全部";

    String SEX_MAN_TYPE = "1";
    String SEX_WOMEN_TYPE = "0";

}
