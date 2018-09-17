package com.example.feng.xgs.config.key;

/**
 * Created by feng on 2018/3/15 0015.
 * 状态码
 */

public interface CodeKeys {

    /**
     * 跳转回传
     * */
    int FOR_RESULT_CODE = 200;
    int ADD_REQUEST = 201;
    int EDITOR_REQUEST = 202;

    int INTENT_DEFAULT = -1;

    int J_PUSH_IM_CREATE_MESSAGE = 301;
    int J_PUSH_IM_GET_MESSAGE = 302;

    int NEARBY_SEARCH = 1;
    int NEARBY_SHOW = 2;

    /**
     * 相机相关
     * */
    int CAMERA_PERMISSION = 11;
    int CAMERA_OPEN = 12;
    int CAMERA_VIDEO = 19;
    int PHOTO_OPEN = 13;
    int NEARBAY = 31;
    int VIDEO_OPEN = 14;
    int VIDEO_CUSTOM = 15;
    int VIDEO_VIP = 17;
    int LOCATION_PERMISSION = 16;
    int VIDEO_OPEN_VIP = 18;
    int MAP_ADDRESS = 20;
    int MUSIC_OPEN = 20;
    int PERSION_AUTHENTICATION = 30;
    int FOLLOW= 20;




    //icon_natural_select 本色关注，本色最新、本色视频、圈子最新、圈子全部、个人动态
    int DYNAMIC_NATURAL_ATTENTION = 21;
    int DYNAMIC_NATURAL_HOT = 22;
    int DYNAMIC_NATURAL_VIDEO = 23;
    int DYNAMIC_FIND_CIRCLE_NEW = 24;
    int DYNAMIC_FIND_CIRCLE_ALL = 25;
    int DYNAMIC_PERSON = 26;


    //添加收货地址、编辑收货地址
    int ADDRESS_ADD_TAG = 41;
    int ADDRESS_EDITOR_TAG = 42;

    //选择收货地址、管理收货地址
    int ADDRESS_SELECT = 43;
    int ADDRESS_MANAGER = 44;

    //icon_message_select 我的关注，关注我的，评论我的
    int MESSAGE_MY_ATTENTION = 51;
    int MESSAGE_ATTENTION_MY = 52;
    int MESSAGE_COMMENT_MY = 53;



    //支付方式
    int PAY_ALI = 210;
    int PAY_WE_CHAT = 211;





    //选择圈子、展示圈子
    int CIRCLE_SELECT = 83;
    int CIRCLE_SHOW = 84;



    //商城页面: 图文详情、产品参数、评论
    int FIND_SHOP_DETAIL_IMAGE = 93;
    int FIND_SHOP_DETAIL_PARAMETER = 94;
    int FIND_SHOP_DETAIL_COMMENT = 95;

    //我的问题跳转回传，请求码
    int DATING_QUESTION_REQUEST = 96;

    //模特详情，判断是产看自己信息还是他人信息
    int MODEL_PERSON = 101;
    int MODEL_OTHER = 102;

    //查看我的记录，查看模特的记录
    int RECORD_MODEL = 105;
    int RECORD_MINE = 106;

    int MESSAGE_PRIVATE_LATTER_ME = 111;
    int MESSAGE_MY_PRIVATE_LATTER = 112;

    //商品搜索，更多商品， 商品类型列表
    int SHOP_LIST_SEARCH = 121;
    int SHOP_LIST_MORE = 122;
    int SHOP_LIST_TYPE = 123;



}
