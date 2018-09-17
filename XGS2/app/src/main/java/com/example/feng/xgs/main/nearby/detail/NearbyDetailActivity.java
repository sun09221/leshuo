package com.example.feng.xgs.main.nearby.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.chatui.ui.activity.ChatUIActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.ShareUtils;
import com.example.feng.xgs.core.banner.BannerCreator;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.mine.dynamic.MineDynamicActivity;
import com.example.feng.xgs.main.nearby.complaint.ComplaintDialog;
import com.example.feng.xgs.ui.weight.LabelLayout;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/4/27 0027.
 * 附近详情
 */

public class NearbyDetailActivity extends ToolbarActivity implements OnItemClickListener {


    @BindView(R.id.iv_toolbar_right)
    ImageView mIVToolbarRight;  //分享
    @BindView(R.id.banner_nearby_detail)
    ConvenientBanner<String> mBanner;
    @BindView(R.id.tv_nearby_detail_name)
    TextView mTVHead;   //姓名
    @BindView(R.id.tv_nearby_item_age)
    TextView mTVAge;    //年龄
    @BindView(R.id.iv_nearby_item_sex)
    ImageView mIVSex;    //性别
    @BindView(R.id.ly_nearby_item_age)
    LinearLayout mLyAge;    //年龄
    @BindView(R.id.tv_nearby_item_star_sign)
    TextView mTVStarSign;   //星座
    @BindView(R.id.tv_nearby_item_vip)
    TextView mTvVIP;   //是否是VIP
    @BindView(R.id.tv_nearby_detail_head_address)
    TextView mTVlHeadAddress;   //地址
    @BindView(R.id.tv_nearby_detail_sign)
    TextView mTVSign;   //签名

    @BindView(R.id.tv_nearby_detail_classify)
    TextView mTVIndustry;   //我的信息--行业
    @BindView(R.id.tv_nearby_detail_occupation)
    TextView mTVOccupation;     //职业
    @BindView(R.id.tv_nearby_detail_company)
    TextView mTVCompany;        //公司
    @BindView(R.id.tv_nearby_detail_hometown)
    TextView mTVHometown;       //家乡
    @BindView(R.id.tv_nearby_detail_id)
    TextView mTVID;       //乐说ID
    @BindView(R.id.tv_nearby_detail_address)
    TextView mTVAddress;        //地址

    @BindView(R.id.tv_nearby_detail_question)
    TextView mTVQuestion;       //问题
    @BindView(R.id.tv_nearby_detail_respond)
    TextView mTVRespond;        //回答n

    @BindView(R.id.label_nearby_detail_tag)
    LabelLayout mLabelTag;//我的标签
    @BindView(R.id.rv_nearby_detail_like)
    RecyclerView mRvlLike;      //兴趣
    private String mIdOnly;
    private NearbyDetailLabelDataConverter mDataConverter;
    private NearbyDetailLabelAdapter mAdapterLike;
    private String mImgUrl;
    private String mMobile;
    private String mNickName;
    private String idOnly;
    private String id;

    //私信
    @OnClick(R.id.tv_nearby_detail_message)void onClickPrivateMessage(){
//        if(!TextUtils.isEmpty(mMobile)){
//
//        }
        if (idOnly.equals(SharedPreferenceUtils.getPeopleId())){

        }else{
            Intent intent = new Intent(this, ChatUIActivity.class);
            intent.putExtra(ContentKeys.ACTIVITY_TITLE, mNickName);
            intent.putExtra(ContentKeys.ACTIVITY_IMG_URL, mImgUrl);
            intent.putExtra(ContentKeys.ACTIVITY_MOBILE, mMobile);
            startActivity(intent);

        }

    }

    //分享
    @OnClick(R.id.iv_toolbar_right)
    void onClickShare() {

        ShareUtils.create(this)
                .setText("个人详情")
                .setImgUrl(mImgUrl)
                .setTitleUrl(UrlKeys.BASE_NAME+UrlKeys.PERSON_SHARE+"peopleid="+mIdOnly)
                .setUrl(UrlKeys.BASE_NAME+UrlKeys.PERSON_SHARE+"peopleid="+mIdOnly)
                .showDialog();
    }

    //投诉
    @OnClick(R.id.iv_nearby_detail_complaint)
    void onClickComplaint() {
        ComplaintDialog.create(this).beginShow(mIdOnly);
    }

    //相册
    @OnClick(R.id.tv_nearby_Detail_photo)
    void onClickPhoto() {

    }

    //个人动态
    @OnClick(R.id.tv_nearby_detail_dynamic)
    void onClickDynamic() {
//        Intent intent = new Intent(this, NearbyDynamicActivity.class);
//        intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, mIdOnly);
//        startActivity(intent);
        Intent intent = new Intent(this, MineDynamicActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, getString(R.string.dynamic));
        intent.putExtra(ContentKeys.ACTIVITY_PEOPLE_ID, mIdOnly);
        startActivity(intent);//我的动态
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_nearby_detail;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        setTitle(title);

        mIdOnly = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);

        mIVToolbarRight.setImageResource(R.mipmap.icon_share);
        initLabel();
        initLikeRecyclerView();

        loadData();
    }


    private void initBanner(String imgUrls) {
        if(!TextUtils.isEmpty(imgUrls)){
            String[] imageArray = imgUrls.split(",");
            List<String> imageList = new ArrayList<>();
            for (int i = 0; i < imageArray.length; i++) {
                imageList.add(imageArray[i]);
            }

            BannerCreator.setDefault(mBanner, imageList, this);
        }

    }

    /**
     * 初始化兴趣
     */
    private void initLikeRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRvlLike.setLayoutManager(manager);
        mDataConverter = new NearbyDetailLabelDataConverter();
        mAdapterLike = new NearbyDetailLabelAdapter(R.layout.item_neayby_detail_label, mDataConverter.convert());
        mRvlLike.setAdapter(mAdapterLike);
        mRvlLike.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化我的标签
     */
    private void initLabel() {
        mLabelTag.setTextColor(R.color.label_writer_dark);
        mLabelTag.setBackground(R.drawable.label_label_solid_radius_shape);
//        mLabelTag.setLabelData(labelTagList);
    }


    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ID_ONLY, mIdOnly);
        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.USER_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        bindData(response);

                    }
                })
                .build().post();

    }

    /**
     * 为控件绑定数据
     */
    private void bindData(String response) {
        JSONObject object = JSON.parseObject(response);

        String mBirthday = object.getString("birthday");//出生日期
        String sex = object.getString("sex");//性别
        String autograph = object.getString("autograph");
        //电话
        mMobile = object.getString("jgusername");
        //昵称
        mNickName = object.getString("nickname");
        //头像
        mImgUrl = object.getString("header");
        //个人id
        idOnly = object.getString("id");
        id = object.getString("idcode");
        String message = object.getString("message");
        String mEmail = object.getString("email");//邮箱
        String userId = object.getString("userId");
        String photoUrls = object.getString("imagepath");
        String starsign = object.getString("starsign");//星座
        String type = object.getString("type");//0普通用户 1VIP 2超级VIP

        // ---------- 我的信息数据
        String industry = object.getString("industry");//行业信息
        String jobArea = object.getString("jobarea");//工作领域
        String company = object.getString("companyname");//公司
        String hometown = object.getString("hometown");//家乡
        String address = object.getString("constantaddress");//常去地址
        String label = object.getString("lable");//我的个性标签

        String question = object.getString("question");
        String respond = object.getString("answer");
        String age = object.getString("age");

        // ------------- 我的兴趣
        String sports = object.getString("sports");//运动
        String music = object.getString("music");//音乐
        String food = object.getString("food");//食物
        String movie = object.getString("film");//电影
        String books = object.getString("books");//阅读
        String travel = object.getString("travel");//旅行
        List<String> likeList = new ArrayList<>();
        likeList.add(sports);
        likeList.add(music);
        likeList.add(food);
        likeList.add(movie);
        likeList.add(books);
        likeList.add(travel);

        mTVHead.setText(mNickName);
        if (!id.equals("")&&id!=null){
            mTVID.setVisibility(View.VISIBLE);
            mTVID.setText("乐说ID:"+id);
        }else{
            mTVID.setVisibility(View.GONE);
        }
        if (age!=null&&!starsign.equals("")){
            mTVAge.setText(age);
            mLyAge.setVisibility(View.VISIBLE);
            if(ContentKeys.SEX_WOMEN.equals(sex)){
                mLyAge.setBackgroundResource(R.drawable.pink_light_solid_radius_shape);
                mIVSex.setImageResource(R.mipmap.icon_sex_women);
            }else {
                mLyAge.setBackgroundResource(R.drawable.blue_solid_radius_shape);
                mIVSex.setImageResource(R.mipmap.icon_sex_men);
            }
        }
        else{
            mLyAge.setVisibility(View.GONE);
        }
        if(ContentKeys.USER_TYPE_VIP.equals(type) || ContentKeys.USER_TYPE_VIP_SUPER.equals(type)){
            mTvVIP.setVisibility(View.VISIBLE);
        }else {
            mTvVIP.setVisibility(View.GONE);
        }
//        mTVSign.setText(autograph);
        setTextViewEmpty(mTVSign, autograph);
        if (starsign!=null&&!starsign.equals("")){
            mTVStarSign.setText(starsign);
            mTVStarSign.setVisibility(View.VISIBLE);
        }
        else{
            mTVStarSign.setVisibility(View.GONE);
        }

        mTVlHeadAddress.setText(address);


        setTextViewEmpty(mTVIndustry, industry);
        setTextViewEmpty(mTVOccupation, jobArea);
        setTextViewEmpty(mTVCompany, company);
        setTextViewEmpty(mTVHometown, hometown);
        setTextViewEmpty(mTVAddress, address);


        mLabelTag.setLabelData(label);

        mDataConverter.clearData();
        mAdapterLike.setNewData(mDataConverter.setTitleList(likeList).convert());

        if (!TextUtils.isEmpty(question)) {
            mTVQuestion.setText(question);
            mTVRespond.setText(respond);
            mTVRespond.setVisibility(View.VISIBLE);
        }

        initBanner(photoUrls);

    }

    @Override
    public void onItemClick(int position) {

    }


    private void setTextViewEmpty(TextView textView, String text){
        if(TextUtils.isEmpty(text)){
            textView.setText(getString(R.string.nothing_left));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setTextColor(ContextCompat.getColor(this, R.color.gray_light));
        }else {
            textView.setText(text);
        }


    }
}
