package com.example.feng.xgs.main.find.broadcast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.circle.RadioAdapter;
import com.example.feng.xgs.main.circle.RadioPlayActivity;
import com.example.feng.xgs.main.circle.VoiceTitleDataConverter;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewRadio extends AppCompatActivity implements  BaseQuickAdapter.OnItemChildClickListener ,SwipeRefreshLayout.OnRefreshListener  {
    private TextView tvToolbarTitle;
    private SwipeRefreshLayout refreshBase;
    private ImageView iv_toolbar_back;
    private ImageView radio_title_picture;
    private ImageView radio_herald;
    private TextView radio_anchor,radio_time;
    private CircleImageView radio_anchor_one_head;
    private CircleImageView radio_anchor_two_head;
    private CircleImageView radio_anchor_three_head;
    private CircleImageView radio_anchor_four_head;
    private CircleImageView radio_anchor_five_head;
    private CircleImageView radio_anchor_six_head;
    private CircleImageView radio_anchor_seven_head;
    private TextView radio_anchor_one_date,radio_anchor_one_name;
    private TextView radio_anchor_two_date,radio_anchor_two_name;
    private TextView radio_anchor_three_date,radio_anchor_three_name;
    private TextView radio_anchor_four_date,radio_anchor_four_name;
    private TextView radio_anchor_five_date,radio_anchor_five_name;
    private TextView radio_anchor_six_date,radio_anchor_six_name;
    private TextView radio_anchor_seven_date,radio_anchor_seven_name;
    private LinearLayoutManager mManager;
    private RecyclerView recData;
    VoiceTitleDataConverter mDataConverter;
    private RadioAdapter mCircleAdapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
//需要执行的代码放这里
                    radio_herald.setVisibility(View.GONE);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_new_radio);
        init();
        tvToolbarTitle.setText("七仙女");
        initRefreshLayout(refreshBase);
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initRecViews();
        loadData();
        herald();

    }
    private void herald(){
        SimpleDateFormat   formatter   =   new   SimpleDateFormat ("HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        String strBeginTime = "00:00:00";
        String strEndTime = "21:30:00";
        Date strbeginDate = null;//起始时间
        Date strendDate = null;//结束时间
        Date systime = null;
        try {
            systime = formatter.parse(str.toString());//将时间转化成相同格式的Date类型
            strbeginDate = formatter.parse(strBeginTime.toString());//将时间转化成相同格式的Date类型
            strendDate = formatter.parse(strEndTime.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (systime.getTime()-strendDate.getTime()<0){
            radio_herald.setVisibility(View.VISIBLE);
        }else{
            radio_herald.setVisibility(View.GONE);
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");// 可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        Timer timer = new Timer(true);
        timer.schedule(task,strToDateLong(hehe+" 21:30:00"));

    }
    private void init(){
        refreshBase = findViewById(R.id.refresh_base);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        iv_toolbar_back = findViewById(R.id.iv_toolbar_back);
        radio_title_picture = findViewById(R.id.radio_title_picture);
        radio_anchor = findViewById(R.id.radio_anchor);
        radio_time = findViewById(R.id.radio_time);
        radio_anchor_one_head = findViewById(R.id.radio_anchor_one_head);
        radio_anchor_two_head = findViewById(R.id.radio_anchor_two_head);
        radio_anchor_three_head = findViewById(R.id.radio_anchor_three_head);
        radio_anchor_four_head = findViewById(R.id.radio_anchor_four_head);
        radio_anchor_five_head = findViewById(R.id.radio_anchor_five_head);
        radio_anchor_six_head = findViewById(R.id.radio_anchor_six_head);
        radio_anchor_seven_head = findViewById(R.id.radio_anchor_seven_head);
        radio_anchor_one_date = findViewById(R.id.radio_anchor_one_date);
        radio_anchor_one_name = findViewById(R.id.radio_anchor_one_name);
        radio_anchor_two_date = findViewById(R.id.radio_anchor_two_date);
        radio_anchor_two_name = findViewById(R.id.radio_anchor_two_name);
        radio_anchor_three_date = findViewById(R.id.radio_anchor_three_date);
        radio_anchor_three_name = findViewById(R.id.radio_anchor_three_name);
        radio_anchor_four_date = findViewById(R.id.radio_anchor_four_date);
        radio_anchor_four_name = findViewById(R.id.radio_anchor_four_name);
        radio_anchor_five_date = findViewById(R.id.radio_anchor_five_date);
        radio_anchor_five_name = findViewById(R.id.radio_anchor_five_name);
        radio_anchor_six_date = findViewById(R.id.radio_anchor_six_date);
        radio_anchor_six_name = findViewById(R.id.radio_anchor_six_name);
        radio_anchor_seven_date = findViewById(R.id.radio_anchor_seven_date);
        radio_anchor_seven_name = findViewById(R.id.radio_anchor_seven_name);
        recData = findViewById(R.id.rec_data);
        radio_herald = findViewById(R.id.radio_herald);
    }
    private void initRecViews() {
        mManager = new LinearLayoutManager(this);
        recData.setLayoutManager(mManager);
        //  addDivider(10);
        mDataConverter = new VoiceTitleDataConverter();
        mCircleAdapter = new RadioAdapter(R.layout.item_all_natural_radio, mDataConverter.ENTITIES);
        recData.setAdapter(mCircleAdapter);
        mCircleAdapter.setOnItemChildClickListener(this);
    }
    private void loadData() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(1));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        mUrl = UrlKeys.ALL_VOICE;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.clearData();
                        mCircleAdapter.setNewData(mDataConverter.setJsonData(response).convert());

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");


                    }
                })
                .build().post();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        final BaseEntity entity2 = mCircleAdapter.getData().get(position);
        String peopleId2 = entity2.getField(EntityKeys.PEOPLE_ID);
        switch (view.getId()) {
            case R.id.radio_anchor_relayout:
                Intent intent_message3 = new Intent(NewRadio.this, RadioPlayActivity.class);
                intent_message3.putExtra(ContentKeys.ACTIVITY_ID_ONLY, peopleId2);
                intent_message3.putExtra(ContentKeys.ACTIVITY_LAMEIMG, entity2.getField("labelimg"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_ID, entity2.getField("id"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_HEAD, entity2.getField("header"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_TITLE,entity2.getField("title"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_CREATTIME, entity2.getField("createtime"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_SEX, entity2.getField("sex"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_CONSTELLATION, entity2.getField("starsign"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_MUSICPATH, entity2.getField("musicpath"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_LENGTH, entity2.getField("lengthtime"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_PEOPLEID, entity2.getField("peopleid"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_STATE, entity2.getField("state"));
                startActivityForResult(intent_message3, CodeKeys.FOLLOW);
                break;
            default:
                break;
        }

    }
    /**
     * string类型时间转换为date
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;

}
    public void initRefreshLayout(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeResources(
                R.color.pink,
                R.color.main,
                R.color.yellow_light
        );
        refreshLayout.setProgressViewOffset(true, 0, 200);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {

    }
}
