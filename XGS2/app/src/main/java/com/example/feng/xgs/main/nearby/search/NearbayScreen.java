package com.example.feng.xgs.main.nearby.search;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.nearby.NearbyFragment;
import com.example.feng.xgs.main.nearby.RadioGroupEx;
import com.example.feng.xgs.ui.weight.LabelLayout2;
import com.example.feng.xgs.utils.PickerView;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbayScreen extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private String sexresult;
    private String startage;
    private String endage;
    private String strginresult;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private LabelLayout2 label_item;
    private LabelLayout2 label_item_music;
    private LabelLayout2 label_item_food;
    private LabelLayout2 label_item_movie;
    private LabelLayout2 label_item_book;
    private LabelLayout2 label_item_travel;
    private TagFlowLayout id_flowlayout;
    private TagFlowLayout sports_flowlayout;
    private TagFlowLayout music_flowlayout;
    private TagFlowLayout food_flowlayout;
    private TagFlowLayout movie_flowlayout;
    private TagFlowLayout book_flowlayout;
    private View  viewNulltravel,viewNullbook,viewNullmovie,viewNullfood,viewNullmusic,viewNullsports
                    ,viewNullstarign,viewNullsex;
    private List<String> sportsListmessage;
    private List<String> musicListmessage;
    private List<String> foodListmessage;
    private List<String> movieListmessage;
    private List<String> bookListmessage;
    private List<String> travelListmessage;
    private RelativeLayout ages_relayout,ages_layout;
    private View viewNullage;
    private LinearLayout ages_choice_relative;
    private PickerView minute_pv,second_pv;
    private TextView age_over;
    private TextView sex;
    private RelativeLayout sex_relayout;
    private RelativeLayout stargin_relayout;
    private TextView sex_choice_over;
    private TextView stargin;
    private TextView music_over;
    private TextView food_over;
    private TextView movie_over;
    private TextView book_over;
    private TextView travel_over;
    private TextView stargin_choice_over;
    private TextView nearbay_screen_age_start;
    private TextView nearbay_screen_age_end;
    private TextView tv_toolbar_title;
    private TextView nearbay_screen_submit;
    private ImageView iv_toolbar_back;
    private TextView sports_over;
    private String getSex;
    private String getXinzuo;
    private String selectText;
    private String selectText2;
    private String age_strt="",age_end="";
    private String selectText_left;
    private String selectText_right;
    private RadioGroup radioGroupSex;
    private RadioButton radioTwo;
    ;
    private RadioButton radioMan;
    private RadioButton radioWoman;
    private RadioGroupEx radioGroupXinZuo;
    private RelativeLayout sex_layout;
    private RelativeLayout stargin_layout;
    private RelativeLayout sports_layout;
    private LinearLayout sports_relayout;
    private LinearLayout music_relayout;
    private LinearLayout food_relayout;
    private LinearLayout movie_relayout;
    private LinearLayout book_relayout;
    private LinearLayout travel_relayout;
    private RelativeLayout music_layout;
    private RelativeLayout food_layout;
    private RelativeLayout movie_layout;
    private RelativeLayout book_layout;
    private RelativeLayout travel_layout;
    private  LayoutInflater mInflater;
    private TextView iv_travel_label_item_hint,
                      iv_book_label_item_hint,
                      iv_movie_label_item_hint,
                      iv_food_label_item_hint,
                      iv_music_label_item_hint,
                      iv_sports_label_item_hint;
    private List<String> sportsList = new ArrayList<>();
    private List<String> sportsList2 = new ArrayList<>();
    private List<String> musicList = new ArrayList<>();
    private List<String> foodList = new ArrayList<>();
    private List<String> movieList = new ArrayList<>();
    private List<String> bookList = new ArrayList<>();
    private List<String> travelList = new ArrayList<>();
    private ArrayList<String> heightList = new ArrayList<String>();
    private ArrayList<String> zeroToNineList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearbay_screen);
        mInflater = LayoutInflater.from(getApplicationContext());
        publishImage();
        init();
        initData();
        radioGroupSex.setOnCheckedChangeListener(this);
        radioGroupXinZuo.setOnCheckedChangeListener(this);
        sex_relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex_layout.setVisibility(View.VISIBLE);
            }
        });
        viewNullsex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex_layout.setVisibility(View.GONE);
            }
        });
        sex_choice_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex_layout.setVisibility(View.GONE);
                if (getSex == null) {
                    sex.setText("全部");
                } else {
                    sex.setText(getSex);
                }
            }
        });
        stargin_relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stargin_layout.setVisibility(View.VISIBLE);
            }
        });
        viewNullstarign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stargin_layout.setVisibility(View.GONE);
            }
        });
        stargin_choice_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stargin_layout.setVisibility(View.GONE);
                if (getXinzuo == null) {
                    stargin.setText("全部");
                } else {
                    stargin.setText(getXinzuo);
                }

            }
        });
        List<String> data = new ArrayList<String>();
        List<String> seconds = new ArrayList<String>();
        for (int i = 1; i <100; i++)
        {
            data.add(String.valueOf(i));
        }
        for (int i = 0; i <100; i++)
        {
            seconds.add(String.valueOf(i));
        }
        minute_pv.setData(data);
        second_pv.setData(seconds);
        ages_relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ages_layout.setVisibility(View.VISIBLE);
                minute_pv.setOnSelectListener(new PickerView.onSelectListener()
                {

                    @Override
                    public void onSelect(String text)
                    {
                        age_strt= text;
                    }
                });
                second_pv.setOnSelectListener(new PickerView.onSelectListener()
                {

                    @Override
                    public void onSelect(String text)
                    {
                        age_end = text;
                    }
                });
            }
        });
        viewNullage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ages_layout.setVisibility(View.GONE);
            }
        });
        age_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!age_strt.equals("")&&age_strt!=null){
                    nearbay_screen_age_start.setText(age_strt);
                } if (!age_end.equals("")&&age_end!=null){
                    nearbay_screen_age_end.setText(age_end);
                }
                ages_layout.setVisibility(View.GONE);
            }
        });
//        nearbay_screen_age_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showChoiceDialog(R.id.wheel_view_wv, heightList, nearbay_screen_age_start,
//                        new WheelView.OnWheelViewListener() {
//                            @Override
//                            public void onSelected(int selectedIndex, String item) {
//                                selectText = item;
//                                Float height = Float.parseFloat(item) / 100.0f;
//                            }
//                        });
//            }
//        });
//        nearbay_screen_age_end.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showChoiceDialog2(R.id.wheel_view_wv, heightList, nearbay_screen_age_end,
//                        new WheelView.OnWheelViewListener() {
//                            @Override
//                            public void onSelected(int selectedIndex, String item) {
//                                selectText2 = item;
//                                Float height = Float.parseFloat(item) / 100.0f;
//                            }
//                        });
//            }
//        });
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sports_relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sports_layout.setVisibility(View.VISIBLE);
            }
        });
        viewNullsports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sports_flowlayout.getSelectedList().size()>0){
                    iv_sports_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_sports_label_item_hint.setVisibility(View.VISIBLE);
                }
                sports_layout.setVisibility(View.GONE);
            }
        });
        sports_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sports_flowlayout.getSelectedList().size()>0){
                    iv_sports_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_sports_label_item_hint.setVisibility(View.VISIBLE);
                }
                sports_layout.setVisibility(View.GONE);
                List<String> labelList = new ArrayList<>();
                String info = sports_flowlayout.getSelectedList().toString().substring(1, sports_flowlayout.getSelectedList().toString().length() - 1).replace(" ","");
                String[] labelArray = info.split(",");
                if (!info.equals("")){
                    for (int i = 0; i < labelArray.length; i++) {
                        labelList.add(sportsListmessage.get(Integer.parseInt(labelArray[i])));
                    }
                }
                if (!info.equals("")&&info!=null){
                label_item.setLabelData(labelList);
                }else{
                    label_item.removeAllViews();
                }
                sportsList2=labelList;
            }
        });



        music_relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music_layout.setVisibility(View.VISIBLE);
            }
        });
        viewNullmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (music_flowlayout.getSelectedList().size()>0){
                    iv_music_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_music_label_item_hint.setVisibility(View.VISIBLE);
                }
                music_layout.setVisibility(View.GONE);
            }
        });
        music_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (music_flowlayout.getSelectedList().size()>0){
                    iv_music_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_music_label_item_hint.setVisibility(View.VISIBLE);
                }
                music_layout.setVisibility(View.GONE);
                List<String> labelList = new ArrayList<>();
                String info = music_flowlayout.getSelectedList().toString().substring(1, music_flowlayout.getSelectedList().toString().length() - 1).replace(" ","");
                String[] labelArray = info.split(",");
                if (!info.equals("")){
                    for (int i = 0; i < labelArray.length; i++) {
                        labelList.add(musicListmessage.get(Integer.parseInt(labelArray[i])));
                    }
                }

                if (!info.equals("")&&info!=null){
                    label_item_music.setLabelData(labelList);
                }else{
                    label_item_music.removeAllViews();
                }
                   musicList=labelList;
            }
        });



        food_relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                food_layout.setVisibility(View.VISIBLE);
            }
        });
        viewNullfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food_flowlayout.getSelectedList().size()>0){
                    iv_food_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_food_label_item_hint.setVisibility(View.VISIBLE);
                }
                food_layout.setVisibility(View.GONE);
            }
        });
        food_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food_flowlayout.getSelectedList().size()>0){
                    iv_food_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_food_label_item_hint.setVisibility(View.VISIBLE);
                }
                food_layout.setVisibility(View.GONE);
                List<String> labelList = new ArrayList<>();
                String info = food_flowlayout.getSelectedList().toString().substring(1, food_flowlayout.getSelectedList().toString().length() - 1).replace(" ","");
                String[] labelArray = info.split(",");
                if (!info.equals("")){
                    for (int i = 0; i < labelArray.length; i++) {
                        labelList.add(foodListmessage.get(Integer.parseInt(labelArray[i])));
                    }
                }

                if (!info.equals("")&&info!=null){
                    label_item_food.setLabelData(labelList);
                }else{
                    label_item_food.removeAllViews();
                }
                foodList=labelList;
            }
        });



        movie_relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movie_layout.setVisibility(View.VISIBLE);
            }
        });
        viewNullmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie_flowlayout.getSelectedList().size()>0){
                    iv_movie_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_movie_label_item_hint.setVisibility(View.VISIBLE);
                }
                movie_layout.setVisibility(View.GONE);
            }
        });
        movie_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie_flowlayout.getSelectedList().size()>0){
                    iv_movie_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_movie_label_item_hint.setVisibility(View.VISIBLE);
                }
                movie_layout.setVisibility(View.GONE);
                List<String> labelList = new ArrayList<>();
                String info = movie_flowlayout.getSelectedList().toString().substring(1, movie_flowlayout.getSelectedList().toString().length() - 1).replace(" ","");
                String[] labelArray = info.split(",");
                if (!info.equals("")){
                    for (int i = 0; i < labelArray.length; i++) {
                        labelList.add(movieListmessage.get(Integer.parseInt(labelArray[i])));
                    }
                }
                if (!info.equals("")&&info!=null){
                    label_item_movie.setLabelData(labelList);
                }else {
                    label_item_movie.removeAllViews();
                }
                 movieList=labelList;
            }
        });



        book_relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              book_layout.setVisibility(View.VISIBLE);
            }
        });
        viewNullbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (book_flowlayout.getSelectedList().size()>0){
                    iv_book_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_book_label_item_hint.setVisibility(View.VISIBLE);
                }
                book_layout.setVisibility(View.GONE);
            }
        });
        book_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (book_flowlayout.getSelectedList().size()>0){
                    iv_book_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_book_label_item_hint.setVisibility(View.VISIBLE);
                }
                book_layout.setVisibility(View.GONE);
                List<String> labelList = new ArrayList<>();
                String info = book_flowlayout.getSelectedList().toString().substring(1, book_flowlayout.getSelectedList().toString().length() - 1).replace(" ","");
                String[] labelArray = info.split(",");
                if (!info.equals("")){
                    for (int i = 0; i < labelArray.length; i++) {
                        labelList.add(bookListmessage.get(Integer.parseInt(labelArray[i])));
                    }
                }
                if (!info.equals("")&&info!=null){
                    label_item_book.setLabelData(labelList);
                }else {
                    label_item_book.removeAllViews();
                }
                     bookList=labelList;
            }
        });


        travel_relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travel_layout.setVisibility(View.VISIBLE);
            }
        });
        viewNulltravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_flowlayout.getSelectedList().size()>0){
                    iv_travel_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_travel_label_item_hint.setVisibility(View.VISIBLE);
                }
                travel_layout.setVisibility(View.GONE);
            }
        });
        travel_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_flowlayout.getSelectedList().size()>0){
                    iv_travel_label_item_hint.setVisibility(View.GONE);
                }else {
                    iv_travel_label_item_hint.setVisibility(View.VISIBLE);
                }
                travel_layout.setVisibility(View.GONE);
                List<String> labelList = new ArrayList<>();
                String info = id_flowlayout.getSelectedList().toString().substring(1, id_flowlayout.getSelectedList().toString().length() - 1).replace(" ","");

                String[] labelArray = info.split(",");
                if (!info.equals("")){
                    for (int i = 0; i < labelArray.length; i++) {
                        labelList.add(travelListmessage.get(Integer.parseInt(labelArray[i])));
                    }
                }
                if (!info.equals("")&&info!=null){
                    label_item_travel.setLabelData(labelList);
                }else {
                    label_item_travel.removeAllViews();
                }
                   travelList=labelList;
            }
        });
        nearbay_screen_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexresult= String.valueOf(sex.getText());
                if (sexresult.equals("全部")){
                    sexresult="";
                }else{
                    sexresult= String.valueOf(sex.getText());
                }
                startage= String.valueOf(nearbay_screen_age_start.getText());
                endage = String.valueOf(nearbay_screen_age_end.getText());
                strginresult =String.valueOf(stargin.getText());
                if (strginresult.equals("全部")){
                    strginresult="";
                }else{
                    strginresult =String.valueOf(stargin.getText());
                }
                if (Integer.parseInt(startage)>Integer.parseInt(endage)){
                    ToastUtils.showText(NearbayScreen.this,"年龄区间设置有误");
                }else {
                    NearbyFragment myFragment = new NearbyFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("sexresult",sexresult);
                    bundle.putString("startage",startage);
                    bundle.putString("endage", endage);
                    bundle.putString("strginresult", strginresult);
                    bundle.putStringArrayList("sportsList2", (ArrayList<String>) sportsList2);
                    bundle.putStringArrayList("musicList", (ArrayList<String>) musicList);
                    bundle.putStringArrayList("foodList", (ArrayList<String>) foodList);
                    bundle.putStringArrayList("movieList", (ArrayList<String>) movieList);
                    bundle.putStringArrayList("bookList", (ArrayList<String>) bookList);
                    bundle.putStringArrayList("travelList", (ArrayList<String>) travelList);
                    myFragment.setArguments(bundle);
                    Intent intent = new Intent();
                    intent.putExtra("1",bundle);
                    setResult(CodeKeys.NEARBAY,intent);

//                    sportsList2.clear();
//                    musicList.clear();
//                    foodList.clear();
//                    movieList.clear();
//                    bookList.clear();
//                    travelList.clear();
                    finish();
                }
            }
        });

    }
    private  void init(){
        sex = findViewById(R.id.nearbay_screen_sex);
        sex_relayout = findViewById(R.id.sex_relayout);
        stargin_relayout = findViewById(R.id.stargin_relayout);
        sex_choice_over = findViewById(R.id.sex_choice_over);
        stargin = findViewById(R.id.nearbay_screen_stargin);
        stargin_choice_over = findViewById(R.id.stargin_choice_over);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        radioTwo = findViewById(R.id.radioTwo);
        radioMan = findViewById(R.id.radioMan);
        radioWoman = findViewById(R.id.radioWoman);
        sex_layout = findViewById(R.id.sex_layout);
        radioGroupXinZuo = findViewById(R.id.radioGroupXinZuo);
        stargin_layout = findViewById(R.id.starign_layout);
        nearbay_screen_age_start = findViewById(R.id.nearbay_screen_age_start);
        nearbay_screen_age_end = findViewById(R.id.nearbay_screen_age_end);
        tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("条件筛选");
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        sports_over = findViewById(R.id.sports_over);
        sports_layout = findViewById(R.id.sports_layout);
        sports_relayout = findViewById(R.id.sports_relayout);
        nearbay_screen_submit = findViewById(R.id.nearbay_screen_submit);
        music_relayout = findViewById(R.id.music_relayout);
        food_relayout = findViewById(R.id.food_relayout);
        movie_relayout = findViewById(R.id.movie_relayout);
        book_relayout = findViewById(R.id.book_relayout);
        travel_relayout = findViewById(R.id.travel_relayout);
        music_layout = findViewById(R.id.music_layout);
        food_layout = findViewById(R.id.food_layout);
        movie_layout = findViewById(R.id.movie_layout);
        book_layout = findViewById(R.id.book_layout);
        travel_layout = findViewById(R.id.travel_layout);
        music_over = findViewById(R.id.music_over);
        food_over = findViewById(R.id. food_over);
        movie_over = findViewById(R.id.movie_over);
        book_over = findViewById(R.id.book_over);
        travel_over = findViewById(R.id.travel_over);
        label_item = findViewById(R.id.label_dating_label_item);
        label_item_music = findViewById(R.id.label_music_label_item);
        label_item_food = findViewById(R.id.label_food_label_item);
        label_item_movie = findViewById(R.id.label_movie_label_item);
        label_item_book = findViewById(R.id.label_book_label_item);
        label_item_travel = findViewById(R.id.label_travel_label_item);
        id_flowlayout = findViewById(R.id.id_flowlayout);
        sports_flowlayout = findViewById(R.id.sports_flowlayout);
        music_flowlayout = findViewById(R.id.music_flowlayout);
        food_flowlayout = findViewById(R.id.food_flowlayout);
        movie_flowlayout = findViewById(R.id.movie_flowlayout);
        book_flowlayout = findViewById(R.id.book_flowlayout);
        viewNulltravel = findViewById(R.id.viewNulltravel);
        viewNullbook = findViewById(R.id.viewNullbook);
        viewNullmovie = findViewById(R.id.viewNullmovie);
        viewNullfood = findViewById(R.id.viewNullfood);
        viewNullmusic = findViewById(R.id.viewNullmusic);
        viewNullsports = findViewById(R.id.viewNullsports);
        viewNullstarign = findViewById(R.id.viewNullstarign);
        viewNullsex = findViewById(R.id.viewNullsex);
        ages_relayout = findViewById(R.id.ages_relayout);
        ages_layout = findViewById(R.id.ages_layout);
        viewNullage = findViewById(R.id.viewNullage);
        ages_choice_relative = findViewById(R.id.ages_choice_relative);
        minute_pv = findViewById(R.id.minute_pv);
        second_pv = findViewById(R.id.second_pv);
        age_over = findViewById(R.id.age_over);
        iv_travel_label_item_hint = findViewById(R.id.iv_travel_label_item_hint);
        iv_book_label_item_hint = findViewById(R.id.iv_book_label_item_hint);
        iv_movie_label_item_hint = findViewById(R.id.iv_movie_label_item_hint);
        iv_food_label_item_hint = findViewById(R.id.iv_food_label_item_hint);
        iv_music_label_item_hint = findViewById(R.id.iv_music_label_item_hint);
        iv_sports_label_item_hint = findViewById(R.id.iv_sports_label_item_hint);


    }
    // fragment : 点击切换
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        RadioButton radioButton = (RadioButton)group.findViewById(group.getCheckedRadioButtonId());
        String waterIn = radioButton.getText().toString();
//        MyLogUtils.debug("TAG", "--------选项: " + waterIn);

        if( group.equals(radioGroupSex) ){
            if(waterIn.equals("全部")){
                getSex = "全部";
            }else{
                getSex = waterIn;
            }
        }else if( group.equals(radioGroupXinZuo) ){
            if(waterIn.equals("全部")){
                getXinzuo = "全部";
            }else{
                getXinzuo = waterIn;
            }
        }

    }
    private void initData(){
        // 填充列表
        heightList.clear();
        zeroToNineList.clear();
        for (int i = 1; i <= 100; i++){
            heightList.add(i+""); // height: 150-180
        }

            zeroToNineList.add("张三");
        zeroToNineList.add("李四");
        zeroToNineList.add("王五");
        zeroToNineList.add("张三");
        zeroToNineList.add("张三");

    }
    private void showChoiceDialog(int wheelviewId, ArrayList<String> dataList,
                                  final TextView infoBtn, WheelView.OnWheelViewListener listener){
        selectText = "";
        View outerView = LayoutInflater.from(this).inflate(R.layout.date_picker_dialog2, null);
        final WheelView wv = outerView.findViewById(wheelviewId);
        wv.setOffset(2);// 对话框中当前项上面和下面的项数
        wv.setItems(dataList);// 设置数据源
        wv.setSeletion(20);// 默认选中第三项
        wv.setOnWheelViewListener(listener);

// 显示对话框，点击确认后将所选项的值显示到Button上
        new AlertDialog.Builder(this)
                .setView(outerView)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (selectText.equals("")){
                                    infoBtn.setText("20"+"岁");
                                }else{
                                    infoBtn.setText(selectText+"岁");
                                }


                            }
                        })
                .setNegativeButton("取消",null)
                .show();
    }
    private void showChoiceDialog2(int wheelviewId, ArrayList<String> dataList,
                                   final TextView infoBtn, WheelView.OnWheelViewListener listener){
        selectText2 = "";
        View outerView = LayoutInflater.from(this).inflate(R.layout.date_picker_dialog2, null);
        final WheelView wv = outerView.findViewById(wheelviewId);
        wv.setOffset(2);// 对话框中当前项上面和下面的项数
        wv.setItems(dataList);// 设置数据源
        wv.setSeletion(20);// 默认选中第三项
        wv.setOnWheelViewListener(listener);

// 显示对话框，点击确认后将所选项的值显示到Button上
        new AlertDialog.Builder(this)
                .setView(outerView)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (selectText2.equals("")){
                                    infoBtn.setText("25"+"岁");
                                }else{
                                    infoBtn.setText(selectText2+"岁");
                                }

                            }
                        })
                .setNegativeButton("取消",null)
                .show();
    }
    private void publishImage(){
        sportsListmessage = new ArrayList<>();
        musicListmessage = new ArrayList<>();
        foodListmessage = new ArrayList<>();
        movieListmessage = new ArrayList<>();
        bookListmessage = new ArrayList<>();
        travelListmessage = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.GETLOVE)
                .toast()
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONArray arraysports = JSON.parseObject(response).getJSONArray("sportslist");
                        JSONArray arraymusic = JSON.parseObject(response).getJSONArray("musiclist");
                        JSONArray arrayfood = JSON.parseObject(response).getJSONArray("foodlist");
                        JSONArray arraymovie = JSON.parseObject(response).getJSONArray("filmlist");
                        JSONArray arraybook = JSON.parseObject(response).getJSONArray("booklist");
                        JSONArray arraytravel = JSON.parseObject(response).getJSONArray("travellist");
                        if (arraysports != null) {
                            int size = arraysports.size();
                            for (int i = 0; i < size; i++) {
                                JSONObject object = JSON.parseObject(arraysports.getString(i));
                                String name = object.getString("name");
                                sportsListmessage.add(name);
                            }
                        }
                        sports_flowlayout.setAdapter(new TagAdapter<String>(sportsListmessage) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                        sports_flowlayout, false);
                                tv.setText(s);
                                return tv;
                            }

                        });
                        if (arraymusic != null) {
                            int size = arraymusic.size();
                            for (int i = 0; i < size; i++) {
                                JSONObject object = JSON.parseObject(arraymusic.getString(i));
                                String name = object.getString("name");
                                musicListmessage.add(name);
                            }
                        }
                        music_flowlayout.setAdapter(new TagAdapter<String>(musicListmessage) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                        music_flowlayout, false);
                                tv.setText(s);
                                return tv;
                            }

                        });
                        if (arrayfood != null) {
                            int size = arrayfood.size();
                            for (int i = 0; i < size; i++) {
                                JSONObject object = JSON.parseObject(arrayfood.getString(i));
                                String name = object.getString("name");
                                foodListmessage.add(name);
                            }
                        }
                        food_flowlayout.setAdapter(new TagAdapter<String>(foodListmessage) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                        food_flowlayout, false);
                                tv.setText(s);
                                return tv;
                            }

                        });
                        if (arraymovie != null) {
                            int size = arraymovie.size();
                            for (int i = 0; i < size; i++) {
                                JSONObject object = JSON.parseObject(arraymovie.getString(i));
                                String name = object.getString("name");
                                movieListmessage.add(name);
                            }
                        }
                        movie_flowlayout.setAdapter(new TagAdapter<String>(movieListmessage) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                        movie_flowlayout, false);
                                tv.setText(s);
                                return tv;
                            }

                        });
                        if (arraybook != null) {
                            int size = arraybook.size();
                            for (int i = 0; i < size; i++) {
                                JSONObject object = JSON.parseObject(arraybook.getString(i));
                                String name = object.getString("name");
                                bookListmessage.add(name);
                            }
                        }
                        book_flowlayout.setAdapter(new TagAdapter<String>(bookListmessage) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                        book_flowlayout, false);
                                tv.setText(s);
                                return tv;
                            }

                        });
                        if (arraytravel != null) {
                            int size = arraytravel.size();
                            for (int i = 0; i < size; i++) {
                                JSONObject object = JSON.parseObject(arraytravel.getString(i));
                                String name = object.getString("name");
                                travelListmessage.add(name);
                            }
                        }
                        id_flowlayout.setAdapter(new TagAdapter<String>(travelListmessage) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                        id_flowlayout, false);
                                tv.setText(s);
                                return tv;
                            }

                        });
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        NaturalLoader.stopLoading();
                    }
                })
                .build().post();

    }

}
