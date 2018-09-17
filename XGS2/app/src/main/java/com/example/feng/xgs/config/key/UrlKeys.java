package com.example.feng.xgs.config.key;

/**
 * Created by feng on 2018/3/15 0015.
 * 接口地址key
 */

public interface UrlKeys {

    String BEAUTY_IMG_URL_TEST = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527763731522&di=50381b6bbec331ea2341e4e2c6dcaa07&imgtype=0&src=http%3A%2F%2Fwww.taopic.com%2Fuploads%2Fallimg%2F140320%2F235013-14032020515270.jpg";
    String BEAUTY_IMG_URL_TEST_TWO = "http://i1.umei.cc/uploads/tu/201803/9999/0820900274.jpg";
    String BEAUTY_IMG_URL_TEST_THREE = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527763731521&di=35b00a25f9a96151c205e0e162e1948e&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2014%2F348%2F58%2F69HYFVT5E6TE.jpg";
    String IMG_URL_TEST = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524831819928&di=0eb1cb58ca5f4bb00c61971dd60dc35a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fe1fe9925bc315c602050233b87b1cb1348547718.jpg";
    String VIDEO_URL_TEST = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";

    String BASE_QINIU_URL = "http://p8swfk8xu.bkt.clouddn.com/";

   // String BASE_URL = "http://192.168.1.109:8080";
   // String BASE_URL = "http://192.168.1.167:8080";
     String BASE_URL = "http://47.92.86.192";  // 外网
     String BASE_NAME = "http://ls.xgshx.cn";  // 外网
    /**
     * 登录相关
     */
    String SIGN_LOGIN = "/sc_xgs/user/login.action";
    String SIGN_FORGET = "/sc_xgs/user/modifyPassword.action";
    String SIGN_REGISTER = "/sc_xgs/user/register.action";
    String WE_CHAT_LOGIN = "/sc_xgs/user/wxlogin.action";
    String SIGN_CODE = "/sc_xgs/user/login.action";

    /**
     * 分享
     */
    String DYNAMIC_SHARE = "/sc_xgs/share/toDynamicSharePage.action?";
    String PERSON_SHARE = "/sc_xgs/share/toPeopleSharePage.action?";
    String STUDENT_SHARE = "/sc_xgs/share/toStudentSharePage.action?";
    String PRODUCT_SHARE = "/sc_xgs/share/toProductSharePage.action?";
    String MUSIC_SHARE = "/sc_xgs/share/shareMusic.action?";
    /**
     * 首页
     */
    String NEARBY = "/sc_xgs/people/getNearbyPeople.action";
    String NEARBY_SEARCH = "/sc_xgs/people/getPeopleList.action";
    String LIKE = "/sc_xgs/dynamic/saveDynamicPraise.action";
    String NEARBY_DYNAMIC_LIST = "/sc_xgs/dynamic/getDynamicList.action";
    String COMPLAINT = "/sc_xgs/complaint/addComplaint.action";


    /**
     * 本色
     */
    String NATURAL_ATTENTION = "/sc_xgs/attention/selectAttentionPeopleDynamic.action";
    String NATURAL_NEW = "/sc_xgs/dynamic/getAllDynamicList.action";
    String NATURAL_VIDEO = "/sc_xgs/dynamic/getAllDynamicVideoList.action";

    String DYNAMIC_DETAIL = "/sc_xgs/dynamic/getDynamic.action";
    String DYNAMIC_PUBLISH_NATURAL = "/sc_xgs/dynamic/saveDynamic.action";
    String DYNAMIC_DETAIL_COMMENT = "/sc_xgs/comment/getCommentList.action";
    String GETLOVE = "/sc_xgs/people/getLoves.action";
    String REWARD_LIST = "/sc_xgs/reward/getRewardList.action";
    String REWARD = "/sc_xgs/reward/savePeopleReward.action";


    /**
     * 消息
     */
    String ATTENTION = "/sc_xgs/attention/addAttention.action";
    String ATTENTION_MY = "/sc_xgs/attention/getAttentionPeople.action";
    String MY_ATTENTION = "/sc_xgs/attention/getPeopleAttention.action";

    String COMMENT = "/sc_xgs/comment/saveDynamicComment.action";
    String COMMENT_MY = "/sc_xgs/comment/getAllComment.action";


    /**
     * 赛区
     */
    String FIND_LIST = "/sc_xgs/racearea/getRaceAreaList.action";
    String FIND_STUDENT_LIST = "/sc_xgs/model/getModelList.action";
    String FIND_COMPANY_LIST = "/sc_xgs/unit/getCooperativeUnit.action";
    String FIND_AREA_INFO = "/sc_xgs/unit/getEntryInformation.action";
    String FIND_RANKING_LIST = "/sc_xgs/model/getModelRanking.action";
    String FIND_MODEL_DETAIL = "/sc_xgs/model/getModelDetail.action";
    String FIND_GIFT_LIST = "/sc_xgs/reward/getRewardList.action";
    String FIND_MODEL_LIKE = "/sc_xgs/model/saveModelLikes.action";
    String FIND_MODEL_COMMENT = "/sc_xgs/fans/saveFansMessage.action";
    String FIND_PROVINCE = "/sc_xgs/racearea/getAllProvince.action";

    String AREA_RECORD_COMMENT = "/sc_xgs/fans/getModelRewardList.action";
    String AREA_RECORD_COMMENT_MINE = "/sc_xgs/fans/getMyRewardList.action";

    String AREA_RECORD_LIKE = "/sc_xgs/model/getModelLikesList.action";

    String AREA_RECORD_GIFT = "/sc_xgs/model/getModelRewardList.action";//模特礼物记录
    String AREA_RECORD_GIFT_MINE = "/sc_xgs/model/getModelRewardList.action";//我送出的礼物记录
    String AREA_RECORD_LIKE_MINE = "/sc_xgs/model/getMyModelLikesList.action";//我的点赞记录


    /**
     * 发现
     */
    String BANNER = "/sc_xgs/sowingmap/getSowingMap.action";
    //活动
    String FIND_ACTIVITY_PUBLISH = "/sc_xgs/activity/saveActivity.action";
    String FIND_ACTIVITY_LIST = "/sc_xgs/activity/getActivityList.action";
    String MINE_ACTIVITY_LIST = "/sc_xgs/activity/getPeopleActivityList.action";
    String FIND_ACTIVITY_DETAIL = "/sc_xgs/activity/getActivity.action";
    String FIND_ACTIVITY_ENROLL = "/sc_xgs/activity/saveActivityEnter.action";
    //广播
    String FIND_BROADCAST_LIST = "/sc_xgs/broadcast/getBroadcastList.action";
    String FIND_BROADCAST_PUBLISH = "/sc_xgs/broadcast/saveBroadcast.action";
    //店铺
    String FIND_STORE_LIST = "/sc_xgs/shop/getShopList.action";
    //商城
    String SHOP_LIST = "/sc_xgs/product/getProductList.action";
    String SHOP_DETAIL = "/sc_xgs/product/getProduct.action";
    String SHOP_COMMENT_LIST = "/sc_xgs/product/getProductComment.action";
    String SHOP_HOT_TAB = "/sc_xgs/product/getProductTypeList.action";
    String SHOP_COMMENT = "/sc_xgs/product/saveProductComment.action";
    String SHOP_COMMENT_COUNT = "/sc_xgs/product/getProductCommentCount.action";
    String SHOP_SEARCH_HOT = "/sc_xgs/product/getHotSearch.action";
    String SHOP_SEARCH = "/sc_xgs/product/searchProductList.action";
    //购物车
    String SHOP_CART_ADD = "/sc_xgs/shoppe/addshoppingCar.action";
    String SHOP_CART_LIST = "/sc_xgs/shoppe/getShoppingCarProductList.action";
    String SHOP_CART_DELETE = "/sc_xgs/shoppe/delShoppingCarProduct.action";
    /**
     * 个人动态
     */
    String PERSON_DYNAMIC = "sc_xgs/dynamic/getDynamicList.action";

    /**
     * 个人中心
     */
    String USER_INFO = "/sc_xgs/people/getPeople.action";
    String USER_INFO_EDIT = "/sc_xgs/people/updatePeople.action";
    String USER_PHOTO_EDIT = "/sc_xgs/people/savePicture.action";

    String MODEL_AUTHENTICATION = "/sc_xgs/model/modelAuthentication.action";

    //标签
    String LABEL_FIRST = "/sc_xgs/dictionar/getFirstLevelLabel.action";
    String LABEL_SECOND = "/sc_xgs/dictionar/getTwoLevelLabel.action";
    String LABEL_ADD = "/sc_xgs/dictionar/saveInterestDictionar.action";

    //收货地址
    String ADDRESS_LIST = "/sc_xgs/address/getPeopleAdress.action";
    String ADDRESS_DETAIL = "/sc_xgs/address/getAddressDetail.action";
    String ADDRESS_ADD = "/sc_xgs/address/saveAddress.action";
    String ADDRESS_EDITOR = "/sc_xgs/address/modifyAdress.action";
    String ADDRESS_DELETE = "/sc_xgs/address/delAddress.action";
    //参数：id：地址ID type：是否为默认地址：0：否，1：是
    String ADDRESS_DEFAULT = "/sc_xgs/address/setDefaultAdress.action";

    String MINE_QRCODE = "/sc_xgs/people/createShareUrl.action";
     String MOODVIEW = "/sc_xgs/moodview/getMoodViewList.action";

    //我的收益
    String EARNINGS_LIST = "/sc_xgs/inandex/getExRecordList.action";
    String EARNINGS = "/sc_xgs/inandex/saveInAndExOrder.action";

    //我的账户
    String ACCOUNT_LIST = "/sc_xgs/account/getAccountList.action";
    String ACCOUNT_APPEND = "/sc_xgs/account/saveAccount.action";

    //我的订单
    String ORDER_LIST = "/sc_xgs/order/getOrderList.action";

    //点赞，关注，评论数量
    String COUNT_LIKE = "/sc_xgs/dynamic/getDynamicPraiseCount.action";
    String COUNT_ATTENTION = "/sc_xgs/attention/getAttentionMyCount.action";
    String COUNT_COMMENT = "/sc_xgs/comment/getCommentCount.action";
    //我的产品
    String MINE_PRODUCT_LIST = "/sc_xgs/product/getPeopleProductList.action";
    String MINE_PRODUCT_PUBLISH = "/sc_xgs/product/saveProduct.action";
    //企业认证
    String AUTHENTICATION_ENTERPRISE_INFO = "/sc_xgs/enterprise/getEnterprise.action";
    String AUTHENTICATION_ENTERPRISE_ADD = "/sc_xgs/enterprise/saveEnterprise.action";
    String AUTHENTICATION_ENTERPRISE_EDITOR = "/sc_xgs/enterprise/modifyEnterprise.action";
    //店铺
    String STORE_INFO = "/sc_xgs/shop/getShopDetailByEnterpriseid.action";
    String STORE_ADD = "/sc_xgs/shop/saveShop.action";
    String STORE_EDITOR = "/sc_xgs/shop/modifyShop.action";
    //个人认证
    String AUTHENTICATION_PERSON_INFO = "/sc_xgs/people/getPeopleAuthentication.action";
    String AUTHENTICATION_PERSON_SAVE = "/sc_xgs/people/savePeopleAuthentication.action";

    String VIP = "/sc_xgs/vip/getVipDetail.action";
    String TEAM = "/sc_xgs/team/getTeamList.action";//我的团队

    String MINE_RECHARGE_LIST = "/sc_xgs/money/getRecharge.action";

    /**
     * 设置
     */
    String AGREEMENT = "/sc_xgs/install/getAgreement.action";
    String ABOUT_ME = "/sc_xgs/install/getAboutUs.action";
    String HELP = "/sc_xgs/install/getInstructions.action";
    String FEEDBACK = "/sc_xgs/feedback/saveFeedback.action";

    /**
     * 圈子
     */
    String TITLE_CIRCLE = "/sc_xgs/circle/getCircleList.action";
    String CIRCLE_LIST = "/sc_xgs/circle/getCircleList.action";
    String CIRCLE_LIST_MY_ATTENTION = "/sc_xgs/circle/getCircleListByPeopleId.action";
    String CIRCLE_MSG = "/sc_xgs/circle/getCircle.action";
    String CIRCLE_TYPE = "/sc_xgs/circle/getCircleDynamicList.action";
    String CIRCLE_JOIN = "/sc_xgs/circle/joinCircle.action";

  /**
   * 新作榜
   */
  String ALL_VOICE = "/sc_xgs/music/getAllMusicList.action";
  String ALL_VOICE_FRIEND = "/sc_xgs/music/getMyFriendMusic.action";
  String MUSICLIKE = "/sc_xgs/music/saveMusicLikes.action";
  String SAVEMUSIC = "/sc_xgs/music/saveMusic.action";
  String MUSICLABEL = "/sc_xgs/music/getMusicLabelList.action";
  String COMMENT_MUSIC = "/sc_xgs/music/saveMusicComment.action";
  String COMMENT_MUSICLIST = "/sc_xgs/music/getMusicCommentList.action";
  String COMMENT_MUSICTIME = "/sc_xgs/music/updateMusicTime.action";
  //  String CIRCLE_MSG = "/sc_xgs/circle/getCircle.action";
//  String CIRCLE_TYPE = "/sc_xgs/circle/getCircleDynamicList.action";
    /**
     * 订单处理
     */
    String ORDER_CREATE = "/sc_xgs/order/saveOrder.action";//创建订单
    String ORDER_VIP_CREATE = "/sc_xgs/order/saveVipOrder.action";//创建VIP订单--------------
    String ORDER_RECHARGE_CREATE = "/sc_xgs/order/saveRechargeOrder.action";//创建充值订单
    String ORDER_SHOP_DETAIL = "/sc_xgs/order/getOrderDetail.action";//商品订单详情
    String ORDER_SHOP_ADDRESS = "/sc_xgs/address/getDefaultAddress.action";


    String ORDER_GIFT_PAY_ALI = "/sc_xgs/pay/createRewardOrder.action";
    String ORDER_SHOP_PAY_ALI = "/sc_xgs/pay/createProductPayOrder.action";
    String ORDER_VIP_PAY_ALI = "/sc_xgs/pay/createVipPayOrder.action";  //------------
    String ORDER_RECHARGE_PAY_ALI = "/sc_xgs/pay/createRechargePayOrder.action";
    String ORDER_GIFT_PAY_WEIXIN= "/sc_xgs/pay/createWxRewardOrder.action";
    String ORDER_SHOP_PAY_WEIXIN = "/sc_xgs/pay/createProductWxPayOrder.action";
    String ORDER_VIP_PAY_WEIXIN = "/sc_xgs/pay/createVipWxPayOrder.action";  //------------
    String ORDER_RECHARGE_PAY_WEIXIN = "/sc_xgs/pay/createRechargeWxPayOrder.action";

}
