package com.example.feng.xgs.main.nearby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.BaseFragment;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.move.handler.AttentionHandler;
import com.example.feng.xgs.main.move.handler.LikeHandler;
import com.example.feng.xgs.main.nearby.search.NearbayScreen;
import com.example.feng.xgs.main.nearby.search.SearchPersonActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocatedCity;
import com.zc.swiple.SwipeFlingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by feng on 2018/4/24 0024.
 * icon_nearby_select
 */

public class NearbyFragment extends BaseFragment implements SwipeFlingView.OnItemClickListener, TextWatcher ,RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.radioGroupSex)
    RadioGroup radioGroupSex;
    @BindView(R.id.radioTwo)
    RadioButton radioTwo;
    @BindView(R.id.radioMan)
    RadioButton radioMan;
    @BindView(R.id.radioWoman)
    RadioButton radioWoman;
    @BindView(R.id.radioGroupXinZuo)
    RadioGroupEx radioGroupXinZuo;
    @BindView(R.id.radioEXTotal)
    RadioButton radioEXTotal;
    @BindView(R.id.radio1)
    RadioButton radio1;
    @BindView(R.id.radio2)
    RadioButton radio2;
    @BindView(R.id.radio3)
    RadioButton radio3;
    @BindView(R.id.radio4)
    RadioButton radio4;
    @BindView(R.id.radio5)
    RadioButton radio5;
    @BindView(R.id.radio6)
    RadioButton radio6;
    @BindView(R.id.radio7)
    RadioButton radio7;
    @BindView(R.id.radio8)
    RadioButton radio8;
    @BindView(R.id.radio9)
    RadioButton radio9;
    @BindView(R.id.radio10)
    RadioButton radio10;
    @BindView(R.id.radio11)
    RadioButton radio11;
    @BindView(R.id.radio12)
    RadioButton radio12;
    @BindView(R.id.txtCancle)
    TextView txtCancle;
    @BindView(R.id.txtSure)
    TextView txtSure;
    @BindView(R.id.tv_nearby_address)
    TextView mTvAddress;
    @BindView(R.id.iv_nearby_search)
    ImageView mIvSearch;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
//    @BindView(R.id.sex)
//    TextView sex_selection;
    @BindView(R.id.swipe_card)
    SwipeFlingView mSwipeFlingView;
    @BindView(R.id.editAgeMin)
    EditText editAgeMin;
    @BindView(R.id.editAgeMax)
    EditText editAgeMax;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.viewNull)
    View viewNull;
    @BindView(R.id.stub_nearby_empty)
    ViewStub viewStub;

    @BindView(R.id.et_nearby_search)
    EditText mEtSearch;
    private  String sex;
    public String getSex = "";
    public String getXinzuo = "";
    public String getAgeBegin = "", getAgeEnd = "";
    private RadioButton[] rbtnSexs;
    @OnClick(R.id.iv_nearby_search)
    void onClickSearch() {
        String searchInfo = mEtSearch.getText().toString();
        if (TextUtils.isEmpty(searchInfo)) {
            ToastUtils.showText(getContext(), "请输入搜索的标签");
            return;
        }

        if (isSearch) {

//            for (int i = mPosition; i < mData.size(); i++) {
//                mSwipeFlingView.selectLeft(false);
//            }

//            mSwipeFlingView.removeAllViewsInLayout();
            Intent intent = new Intent(getActivity(), SearchPersonActivity.class);

            intent.putExtra(ContentKeys.ACTIVITY_INFO, searchInfo);
            startActivity(intent);

            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
            }


        } else {
            Intent intent = new Intent(getActivity(), SearchPersonActivity.class);
            intent.putExtra(ContentKeys.ACTIVITY_INFO, searchInfo);
            startActivity(intent);
        }
    }

    private static final int TUDE_NORMAL = 0;
    private double mLongitude = TUDE_NORMAL;
    private double mLatitude = TUDE_NORMAL;
    private NearbyAdapter mAdapter;
    //    private NearbyDataConverter mDataConverter;
    private List<BaseEntity> mData = new ArrayList<>();
    private int mPosition = 0;
    private boolean isRecallCard = false;
    private LocationHandler mLocationHandler;
    private String mCity;
    private int mType;
    private String mSearchInfo;
    private String sex2;
    private String sportsList2="";
    private String musicList="";
    private String foodList="";
    private String movieList="";
    private String bookList="";
    private String travelList="";
    //是否加载搜索的数据
    private boolean isSearch = false;

    @OnClick(R.id.tv_nearby_address)
    void onClickLocation() {
        String type= SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_TYPE);
        if (type.equals("1")||type.equals("2")){
            LocatedCity locatedCity  =   new LocatedCity(mCity, mCity, "101280601");
            List<HotCity> hotCities = new ArrayList<>();
            hotCities.add(new HotCity("北京", "北京", "101010100"));
            hotCities.add(new HotCity("上海", "上海", "101020100"));
            hotCities.add(new HotCity("广州", "广东", "101280101"));
            hotCities.add(new HotCity("深圳", "广东", "101280601"));
            hotCities.add(new HotCity("杭州", "浙江", "101210101"));
            CityPicker.getInstance()
                    .setFragmentManager(getActivity().getSupportFragmentManager())
                    .enableAnimation(false)
                    .setLocatedCity(locatedCity)
                    .setHotCities(hotCities)
                    .setOnPickListener(new OnPickListener() {
                        @Override
                        public void onPick(int position, City data) {
                            mTvAddress.setText(data == null ? mCity : data.getName());
                            Geocoder geocoder = new Geocoder(getContext(), Locale.CHINA);
                            try {
                                List addressList = geocoder.getFromLocationName(data.getName(), 1);
                                Address address = (Address) addressList.get(0);
                                double latitude = address.getLatitude();
                                double longitude = address.getLongitude();
                                mLatitude=latitude;
                                mLongitude=longitude;
                                loadData();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLocate() {
                            //开始定位，这里模拟一下定位
//                        mLocationHandler = LocationHandler.create(getActivity());
//                        mLocationHandler.initLocation(new LocationHandler.ILocationResultListener() {
//                            @Override
//                            public void onLocationResult(AMapLocation location) {
//                                mCity = location.getCity();
//                                mLatitude = location.getLatitude();
//                                mLongitude = location.getLongitude();
//                                mTvAddress.setText(mCity);
//                            }
//                        });
                        }
                    })
                    .show();
        }else {
            ToastUtils.showText(getActivity(),"请升级VIP");
        }

    }



    //删除
    @OnClick(R.id.rl_nearby_bottom_error)
    void onClickError() {
        if (mSwipeFlingView.isAnimationRunning()) {
            return;
        }
        isRecallCard = false;
        mSwipeFlingView.selectLeft(false);
    }

    //点赞
    @OnClick(R.id.rl_nearby_bottom_like)
    void onClickLike() {

        String peopleId = getPeopleId();
        if (peopleId != null) {
            LikeHandler.create(getActivity()).like(peopleId, "", null);
        } else {
            LogUtils.d("like: id为null");
        }

        LogUtils.d("like: 当前position = " + mPosition);

    }

    //关注
    @OnClick(R.id.rl_nearby_bottom_star)
    void onClickStar() {

        String peopleId = getPeopleId();
        if (peopleId != null) {
            AttentionHandler.create(getActivity()).attention(peopleId, null);
        } else {
            LogUtils.d("like: id为null");
        }


    }
//    //性别选择
//    @OnClick(R.id.sex)
//    void onClickSex() {
//
//        SexSelectionDialog.create(getActivity())
//                .beginShow(new IDialogStringListener() {
//                    @Override
//                    public void onSure(String type) {
//                        sex_selection.setText(type);
//                        if (type.equals("男")){
//                            sex2="1";
//                            loadData();
//                        }else if (type.equals("女")){
//                            sex2="0";
//                            loadData();
//                        }else{
//                            sex="";
//                            loadData();
//                        }
//                        LogUtils.d("type: " + sex_selection + ", " + type);
//                    }
//                });
//
//
//    }
    //性别，星座选择
    @OnClick(R.id.imgSearch)
    void onClickSex() {
   //    llSearch.setVisibility(View.VISIBLE);
        Intent intent = new Intent(getContext(), NearbayScreen.class);
        startActivityForResult(intent, CodeKeys.NEARBAY);
    }
    //取消性别，星座选择
    @OnClick(R.id.txtCancle)
    void onClickCancle() {
        llSearch.setVisibility(View.GONE);
    }
    //取消性别，星座选择
    @OnClick(R.id.viewNull)
    void onClickviewNull() {
        llSearch.setVisibility(View.GONE);
    }
    @OnClick(R.id.txtSure)
    void onClickSure() {
       // llSearch.setVisibility(View.GONE);
        if( getSex == null){
            Toast.makeText(getActivity(), "请输入性别！", Toast.LENGTH_SHORT).show();
            return;
        }
        if( getXinzuo == null){
            Toast.makeText(getActivity(), "请选择星座！", Toast.LENGTH_SHORT).show();
            return;
        }
        getAgeBegin = editAgeMin.getText().toString();
        if(TextUtils.isEmpty(getAgeBegin)){
            ToastUtils.showText(getActivity(), "请输入年龄查询下限值");
            return;
        }
        getAgeEnd = editAgeMax.getText().toString();
        if(TextUtils.isEmpty(getAgeEnd)){
            ToastUtils.showText(getActivity(), "请输入年龄查询上限值");
            return;
        }
  if (getSex.equals("男")){
      sex2="1";
  }else if (getSex.equals("女")){
      sex2="0";
  }else{
      sex2="";
  }
        loadData();
        llSearch.setVisibility(View.GONE);
    }
    //撤回
    @OnClick(R.id.rl_nearby_bottom_refresh)
    void onClickRefresh() {

        isRecallCard = true;
        //参数决定动画开始位置是从左边还是右边出现
        if (mPosition > 1) {
            mPosition--;
        } else {
            LogUtils.d("onTopCardViewFinish已经是第一张图片");
        }
        mSwipeFlingView.selectComeBackCard(true);
    }

    public static NearbyFragment create(int type, String searchInfo ,String peoplesex) {
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_INFO, searchInfo);
        args.putInt(ContentKeys.ACTIVITY_TYPE, type);
        args.putString(ContentKeys.ACTIVITY_MUSIC_SEX, peoplesex);
        NearbyFragment fragment = new NearbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_nearby;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        Bundle args = getArguments();
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
         String a=bundle.getString("sexresult");
        }
        if (args != null) {
            mType = args.getInt(ContentKeys.ACTIVITY_TYPE);
            sex  = SharedPreferenceUtils.getCustomAppSex(sex);
            if (mType == CodeKeys.NEARBY_SEARCH) {
                mSearchInfo = args.getString(ContentKeys.ACTIVITY_INFO);
                isSearch = true;
            }
        }
        rbtnSexs = new RadioButton[] { radioTwo, radioMan, radioWoman };
        rbtnSexs = new RadioButton[] { radioEXTotal, radio1, radio2, radio3, radio4, radio5, radio6, radio7, radio8, radio9, radio10, radio11, radio12 };
        radioGroupSex.setOnCheckedChangeListener(this);
        radioGroupXinZuo.setOnCheckedChangeListener(this);
        SharedPreferences share_get=null;
        share_get=getActivity().getSharedPreferences("data", MODE_PRIVATE);
        //根据键获取数据，第二个参数为默认值，若没有指定的键，则返回默认值
       String sex_informal=share_get.getString("sex_section", null);
        if (sex_informal==null){
            sex2="";
         //   sex_selection.setText("范围选择");
        }else if (sex_informal.equals("")){
          //  sex_selection.setText("范围选择");
            sex2="";
        }else if (sex_informal.equals("1")){
          //  sex_selection.setText("男");
            sex2="1";
        }else {
          //  sex_selection.setText("女");
            sex2="0";
        }
      //  sex_selection.setVisibility(View.VISIBLE);
        initCardView();
        initSearch();

//        mEtSearch.addTextChangedListener(this);
    }

    private void initSearch() {

        if (isSearch) {
            mTvAddress.setVisibility(View.GONE);
            mEtSearch.setText(mSearchInfo);
            loadData();
        } else {
            startLocation();
        }

    }


    private void initCardView() {
//        mDataConverter = new NearbyDataConverter();
        mAdapter = new NearbyAdapter(getActivity(), mData);
        mSwipeFlingView.setAdapter(mAdapter);
        mSwipeFlingView.setFlingListener(mSwipeListener);//SimpleOnSwipeListener/OnSwipeListener
        mSwipeFlingView.setOnItemClickListener(this);

    }

    /**
     * 加载数据
     */
    private void loadData() {
        Map<String, String> map = new HashMap<>();
        String url = UrlKeys.NEARBY;
        if (isSearch) {
            String label = mEtSearch.getText().toString();
            if (TextUtils.isEmpty(label)) return;
            map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
            map.put(ParameterKeys.PAGE_NUMBER, "1");
            map.put(ParameterKeys.LABEL, label);
            map.put(ParameterKeys.LATITUDE, SharedPreferenceUtils.getCustomAppProfile(ParameterKeys.LATITUDE));
            map.put(ParameterKeys.LONGITUDE, SharedPreferenceUtils.getCustomAppProfile(ParameterKeys.LONGITUDE));
            url = UrlKeys.NEARBY_SEARCH;
        } else {
            if (mLatitude == TUDE_NORMAL || mLongitude==TUDE_NORMAL ) {
                startLocation();
                return;
            }
            map.put(ParameterKeys.LATITUDE, String.valueOf(mLatitude));
            map.put(ParameterKeys.LONGITUDE, String.valueOf(mLongitude));
            map.put(ParameterKeys.USER_SEX,sex2);
            map.put("age1",getAgeBegin);
            map.put("age2",getAgeEnd);
            map.put("starsign",getXinzuo);
            map.put("sports", sportsList2);
            map.put("music",musicList);
            map.put("food",foodList);
            map.put("film",movieList);
            map.put("books",bookList);
            map.put("travel",travelList);
            SharedPreferenceUtils.setCustomAppProfile(ParameterKeys.LATITUDE, String.valueOf(mLatitude));
            SharedPreferenceUtils.setCustomAppProfile(ParameterKeys.LONGITUDE, String.valueOf(mLongitude));
        }

        String raw = JSON.toJSONString(map);


        RestClient.builder()
                .url(url)
                .raw(raw)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {


                        NearbyDataConverter mDataConverter = new NearbyDataConverter();
                        mDataConverter.setJsonData(response).convert();
//                        if(mDataConverter.ENTITIES.size() > 0){
//                            while (true) {
//                                if (mDataConverter.ENTITIES.size() >= 4) {
//                                    break;
//                                }
//                                mDataConverter.setJsonData(response).convert();
//
//                            }
//                        }
                        mData=mDataConverter.ENTITIES;
                        mAdapter.notifyDataSetInvalidated();
                        mAdapter.setData(mDataConverter.ENTITIES);
                        setEmpty();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("onError: ");
                        mData.clear();
                        setEmpty();

                    }
                })
                .build().post();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        LogUtils.d(s.toString());
//        if (TextUtils.isEmpty(s.toString())) {
//            loadData(false);
//        }
    }


    /**
     * 空布局设置
     */
    private View emptyView = null;

    private void setEmpty() {

        int size = mData.size();
        LogUtils.d("setEmpty: " + size);

        if (size > 0) {
            if (emptyView != null) {
                emptyView.setVisibility(View.GONE);
            }
        } else {
            if (emptyView == null) {
                emptyView = viewStub.inflate();
            }
            emptyView.setVisibility(View.VISIBLE);
            TextView tvEmpty = emptyView.findViewById(R.id.tv_empty_no_data);
            tvEmpty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();
                }
            });

        }
    }


    /**
     * 定位成功后开始请求数据
     */
    private void startLocation() {
        mLocationHandler = LocationHandler.create(getActivity());
        mLocationHandler.initLocation(new LocationHandler.ILocationResultListener() {
            @Override
            public void onLocationResult(AMapLocation location) {
                mCity = location.getCity();
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                mTvAddress.setText(mCity);
                loadData();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CodeKeys.FOR_RESULT_CODE) {
            if (requestCode == CodeKeys.EDITOR_REQUEST && data != null) {
                String city = data.getStringExtra(ContentKeys.ACTIVITY_INFO);
                mTvAddress.setText(city);
            }
        }else if (resultCode == CodeKeys.NEARBAY){
            Bundle bundle=data.getBundleExtra("1");
            getAgeBegin=bundle.getString("startage");
            getAgeEnd=bundle.getString("endage");
            getXinzuo=bundle.getString("strginresult");
             sportsList2 =bundle.getStringArrayList("sportsList2").toString().substring(1, bundle.getStringArrayList("sportsList2").toString().length() - 1);
             musicList =bundle.getStringArrayList("musicList").toString().substring(1, bundle.getStringArrayList("musicList").toString().length() - 1);
             foodList =bundle.getStringArrayList("foodList").toString().substring(1, bundle.getStringArrayList("foodList").toString().length() - 1);
             movieList =bundle.getStringArrayList("movieList").toString().substring(1, bundle.getStringArrayList("movieList").toString().length() - 1);
             bookList =bundle.getStringArrayList("bookList").toString().substring(1, bundle.getStringArrayList("bookList").toString().length() - 1);
             travelList =bundle.getStringArrayList("travelList").toString().substring(1, bundle.getStringArrayList("travelList").toString().length() - 1);

            loadData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLocationHandler != null) {
            mLocationHandler.onDestroy();
        }

    }


    /*****************************  以下为卡片滑动事件回调 *******************************/
    private SwipeFlingView.onSwipeListener mSwipeListener = new SwipeFlingView.onSwipeListener() {
        @Override
        public void onStartDragCard() {
            LogUtils.d("onStartDragCard执行了");
        }

        @Override
        public boolean canLeftCardExit() {
//            LogUtils.d("canLeftCardExit执行了");
            return true;
        }

        @Override
        public boolean canRightCardExit() {
//            LogUtils.d("canRightCardExit执行了");
            return true;
        }

        @Override
        public void onPreCardExit() {
//            LogUtils.d("onPreCardExit执行了");
        }

        @Override
        public void onLeftCardExit(View view, Object o, boolean b) {
//            LogUtils.d("onLeftCardExit执行了");
        }

        @Override
        public void onRightCardExit(View view, Object o, boolean b) {
//            LogUtils.d("onRightCardExit执行了");
        }

        @Override
        public void onSuperLike(View view, Object o, boolean b) {
            LogUtils.d("onSuperLike执行了");
        }


        //卡片滑出界面时执行
        @Override
        public void onTopCardViewFinish() {
            LogUtils.d("onTopCardViewFinish执行了");
            if (isRecallCard) {
//                if(mPosition > 1){
//                    mPosition--;
//                }else {
//                    LogUtils.d("onTopCardViewFinish已经是第一张图片");
//                }
                isRecallCard = false;
            } else {
                mPosition++;
            }

//            setEmpty();


            LogUtils.d("onTopCardViewFinish position: " + mPosition + ", 数据长度: " + mData.size());
        }

        //全部卡片移除页面时执行
        @Override
        public void onAdapterAboutToEmpty(int i) {
            LogUtils.d("onAdapterAboutToEmpty cardItem:  = " + i);
//            mPosition = i;

//            setEmpty();
//            mDataConverter.ENTITIES.addAll(mDataConverter.ENTITIES);
//            mAdapter.setData(mDataConverter.ENTITIES);
        }

        //数据为空时执行
        @Override
        public void onAdapterEmpty() {
//            mPosition = 0;
//            mDataConverter.clearData();
//            setEmpty();
            loadData();
            LogUtils.d("onAdapterEmpty执行了");
        }

        @Override
        public void onScroll(View view, float v) {
//            LogUtils.d("onScroll执行了");
        }

        @Override
        public void onEndDragCard() {
//            LogUtils.d("onEndDragCard");
        }
    };


    private String getPeopleId() {
        int size = mAdapter.getCount();
        if (size >= mPosition && mPosition - 1 >= 0) {
            BaseEntity entity = (BaseEntity) mAdapter.getItem(mPosition - 1);
            String mIdOnly = entity.getField(EntityKeys.ID_ONLY);
            return mIdOnly;
        }

        return null;
    }

    @Override
    public void onItemClicked(int i, Object o) {
        LogUtils.d("onItemClicked cardItem: icon_find_activity_address = " + i);
    }
    // fragment : 点击切换
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        RadioButton radioButton = (RadioButton)group.findViewById(group.getCheckedRadioButtonId());
        String waterIn = radioButton.getText().toString();
//        MyLogUtils.debug("TAG", "--------选项: " + waterIn);

         if( group.equals(radioGroupSex) ){
             if(waterIn.equals("全部")){
                 getSex = "";
             }else{
                 getSex = waterIn;
             }
        }else if( group.equals(radioGroupXinZuo) ){
            if(waterIn.equals("全部")){
                getXinzuo = "";
            }else{
                getXinzuo = waterIn;
            }
        }

    }

}
