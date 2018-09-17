package com.example.feng.xgs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.example.feng.xgs.base.activitys.BaseActivity;
import com.example.feng.xgs.config.IPerfectConfig;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.main.circle.CircleFragment;
import com.example.feng.xgs.main.find.FindFragment;
import com.example.feng.xgs.main.message.MessageFragment;
import com.example.feng.xgs.main.mine.MineFragment;
import com.example.feng.xgs.main.move.NaturalFragment;
import com.example.feng.xgs.main.nearby.NearbyFragment;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;
import com.example.feng.xgs.ui.weight.NoScrollViewPager;
import com.example.feng.xgs.utils.XRadioGroup;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements XRadioGroup.OnCheckedChangeListener {


    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    @BindView(R.id.rb_0)
    RadioButton rb0;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.iv_point)
    ImageView ivPoint;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rb_5)
    RadioButton rb5;
    private long TOUCH_TIME = 0;
    private String sex;
    private int mTarIndex = 0;
    private List<RadioButton> mTabs = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    @BindView(R.id.rg_main_bottom)
    XRadioGroup mBottomBar;
    @BindView(R.id.vp_main)
    NoScrollViewPager mViewPager;
    private NaturalFragment mNaturalFragment;
    @BindView(R.id.iv_test)
    ImageView mIvTest;

    @Override
    public Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        JMessageClient.registerEventReceiver(this);
        int tabCount = mBottomBar.getChildCount();
//        for (int i = 0; i < tabCount; i++) {
//            RadioButton tab = (RadioButton) mBottomBar.getChildAt(i);
//            mTabs.add(tab);
//        }
        Intent intent = getIntent();
        sex = intent.getStringExtra("sex");
        if (sex!=null){
            SharedPreferences share=null;
            //得到SharePreferences对象，第一个参数：指定文件名，第二个参数：操作模式
            share= getSharedPreferences("data", MODE_PRIVATE);
            //得到SharedPreferen.Edit对象
            SharedPreferences.Editor edit=share.edit();
            //用edit存储数据
            edit.putString("sex_section", sex);

            //提交数据，存储完成
            edit.commit();
        }

        mBottomBar.setOnCheckedChangeListener(this);

        mFragments.add(NearbyFragment.create(CodeKeys.NEARBY_SHOW, "",sex));
//        mNaturalFragment = NaturalFragment.create();
//        mFragments.add(mNaturalFragment);
        mFragments.add(CircleFragment.create());
        mFragments.add(MessageFragment.create());
        mFragments.add(FindFragment.create());
        mFragments.add(MineFragment.create());
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
//        mViewPager.setCurrentItem(0, false);
//        mTabs.get(0).setChecked(true);
        rb0.setChecked(true);
//        registerTest();
    }

    // TODO: 2018/6/14 0014 极光注册测试
    public void registerTest() {
        LogUtils.d("registerTest");
        JMessageClient.getUserInfo("15238691746", new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                LogUtils.d("gotResult: " + i);
                String imgPath = userInfo.getAvatar();
                String name = userInfo.getNickname();
                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int i, String s, Bitmap bitmap) {
                        LogUtils.d("gotResult: " + i + "错误信息：" + s);
                        if (bitmap != null) {
                            mIvTest.setImageBitmap(bitmap);
                        }
                    }
                });
//                String file = userInfo.getAvatarFile().getPath();
                LogUtils.d("gotResult: imgPath: " + imgPath + ",name: " + name + ", file: ");

            }
        });


    }

    /**
     * 接收消息，在主线程中，可以刷新UI
     */
    public void onEventMainThread(MessageEvent event) {
        LogUtils.d("onEventMainThread: 收到消息11");
        ivPoint.setVisibility(View.VISIBLE);
        Intent intent =new Intent("bro");
        sendBroadcast(intent);

    }
    @Override
    public void onCheckedChanged(XRadioGroup group, @IdRes int checkedId) {
        LogUtils.d(TAG, "onCheckedChanged: 点击事件");
//        for (int i = 0; i < mTabs.size(); i++) {
//            if (checkedId == mTabs.get(i).getId()) {
//                mTarIndex = i;
//                Log.d(TAG, "onCheckedChanged: " + mTarIndex);
//                break;
//            }
//        }
        switch (checkedId) {
            case R.id.rb_0:
                mTarIndex = 0;
                Log.d(TAG, "onCheckedChanged: " + mTarIndex);
                break;
            case R.id.rb_1:
                mTarIndex = 1;
                Log.d(TAG, "onCheckedChanged: " + mTarIndex);
                break;
            case R.id.rb_2:
                mTarIndex = 2;
                ivPoint.setVisibility(View.GONE);
                Log.d(TAG, "onCheckedChanged: " + mTarIndex);
                break;
            case R.id.rb_3:
                mTarIndex = 3;
                Log.d(TAG, "onCheckedChanged: " + mTarIndex);
                break;
            case R.id.rb_5:
                mTarIndex = 4;
                Log.d(TAG, "onCheckedChanged: " + mTarIndex);
                break;
        }
        mViewPager.setCurrentItem(mTarIndex, false);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(this, "双击退出" + IPerfectConfig.getApplicationContext().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        mNaturalFragment.onBackPress();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        SDKInitializer.initialize(getApplicationContext());
        ButterKnife.bind(this);
    }
}
