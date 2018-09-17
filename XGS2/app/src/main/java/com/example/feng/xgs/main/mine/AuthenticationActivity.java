package com.example.feng.xgs.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.PermissionActivity;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.mine.authentication.AuthenticationEnterpriseActivity;
import com.example.feng.xgs.main.mine.authentication.AuthenticationHonorActivity;
import com.example.feng.xgs.main.mine.authentication.AuthenticationPersonActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：添加类的描述
 *
 * @author 金源
 * @time 2018/8/2
 */
public class AuthenticationActivity extends PermissionActivity {
    @BindView(R.id.include_toolbar)
    RelativeLayout includeToolbar;
    @BindView(R.id.rl_person_auth)
    RelativeLayout rlPersonAuth;
    @BindView(R.id.rl_company_auth)
    RelativeLayout rlCompanyAuth;
    @BindView(R.id.rl_music_teacher)
    RelativeLayout rlMusicTeacher;
    @BindView(R.id.rl_music_honor)
    RelativeLayout honor;
    @BindView(R.id.rl_singer_auth)
    RelativeLayout rlSingerAuth;
    @BindView(R.id.rl_host_auth)
    RelativeLayout rlHostAuth;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.persion_authentication_state)
    TextView persion_state;
    @BindView(R.id.company_authentication_state)
    TextView company_state;
    @BindView(R.id.occupation_authentication_state)
    TextView occupation_state;
    @BindView(R.id.honor_authentication_state)
    TextView honor_state;

    @Override
    public Object setLayout() {
        return R.layout.activity_authentication_layout;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        tvToolbarTitle.setText("认证");
        loadData_persion();
        loadDataCompany();
        loadDataoccupation();
        loadDatahonor();
    }

    private void loadData_persion() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.AUTHENTICATION_PERSON_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {


                        JSONObject object = JSON.parseObject(response);

                        String state = object.getString("state");
                        if (state.equals("0")){
                            persion_state.setText("审核中");
                        } else if (state.equals("1")){
                            persion_state.setText("认证通过");
                        }
                        else if (state.equals("2")){
                            persion_state.setText("认证未通过");
                        }
                    }
                })
                .build().post();
    }
    private void loadDataCompany() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());

        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.AUTHENTICATION_ENTERPRISE_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        //企业ID
                        String state = object.getString("state");
                        if (state.equals("0")){
                            company_state.setText("审核中");
                        } else if (state.equals("1")){
                            company_state.setText("认证通过");
                        }
                        else if (state.equals("2")){
                            company_state.setText("认证未通过");
                        }


                    }
                })
                .build().post();
    }
    private void loadDataoccupation() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.AUTHENTICATION_PERSON_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {


                        JSONObject object = JSON.parseObject(response);
                        //企业ID
                        String state = object.getString("state");
                        if (state.equals("0")){
                            occupation_state.setText("审核中");
                        } else if (state.equals("1")){
                            occupation_state.setText("认证通过");
                        }
                        else if (state.equals("2")){
                            occupation_state.setText("认证未通过");
                        }


                    }
                })
                .build().post();
    }
    private void loadDatahonor() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.AUTHENTICATION_PERSON_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {


                        JSONObject object = JSON.parseObject(response);

                        //企业ID
                        String state = object.getString("state");
                        if (state.equals("0")){
                            honor_state.setText("审核中");
                        } else if (state.equals("1")){
                            honor_state.setText("认证通过");
                        }
                        else if (state.equals("2")){
                            honor_state.setText("认证未通过");
                        }

                    }
                })
                .build().post();
    }
    @OnClick({R.id.iv_toolbar_back, R.id.include_toolbar, R.id.rl_person_auth, R.id.rl_company_auth, R.id.rl_music_teacher,R.id.rl_music_honor, R.id.rl_singer_auth, R.id.rl_host_auth})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.rl_person_auth:
                intent.setClass(this, AuthenticationPersonActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("title", "个人认证");
                startActivity(intent);
                break;
            case R.id.rl_company_auth:
                intent.setClass(this, AuthenticationEnterpriseActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_music_teacher:
                intent.setClass(this, AuthenticationPersonActivity.class);
                intent.putExtra("title", "职业认证");
                intent.putExtra("type", 1);
                startActivity(intent);

                break;
            case R.id.rl_music_honor:
                intent.setClass(this, AuthenticationHonorActivity.class);
                intent.putExtra("title", "荣誉认证");
                intent.putExtra("type", 4);
                startActivity(intent);

                break;
//            case R.id.rl_singer_auth:
//                intent.setClass(this, AuthenticationPersonActivity.class);
//                intent.putExtra("title", "唱作人认证");
//                intent.putExtra("type", 1);
//                startActivity(intent);
//                break;
//            case R.id.rl_host_auth:
//                intent.setClass(this, AuthenticationPersonActivity.class);
//                intent.putExtra("title", "主持人认证");
//                intent.putExtra("type", 3);
//                startActivity(intent);
//                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
