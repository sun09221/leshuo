package com.example.feng.xgs.main.message;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.BaseFragment;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.main.message.list.MessageListActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/3/15 0015.
 * icon_message_select
 */

public class MessageFragment extends BaseFragment {

    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;

    //评论我的
    @OnClick(R.id.tv_message_comment)
    void onClickComment() {
        startPersonListActivity(getString(R.string.message_comment), CodeKeys.MESSAGE_COMMENT_MY);

    }

    //关注我的
    @OnClick(R.id.tv_message_attention_me)
    void onClickAttentionMe() {
        startPersonListActivity(getString(R.string.message_attention_me), CodeKeys.MESSAGE_ATTENTION_MY);

    }

    //我关注的
    @OnClick(R.id.tv_message_my_attention)
    void onClickMyAttention() {
        startPersonListActivity(getString(R.string.message_my_attention), CodeKeys.MESSAGE_MY_ATTENTION);
    }


    //私信我的
    @OnClick(R.id.tv_message_private_letter_me)
    void onClickLetterMe() {
        iv_point.setVisibility(View.GONE);
        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, getString(R.string.message_private));
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.MESSAGE_PRIVATE_LATTER_ME);
        startActivity(intent);
    }

    //我私信的
    @OnClick(R.id.tv_message_my_private_letter)
    void onClickMyLetter() {
        iv_point.setVisibility(View.GONE);
        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, getString(R.string.message_private));
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.MESSAGE_MY_PRIVATE_LATTER);
        startActivity(intent);
    }

    //客服
    @OnClick(R.id.tv_message_customer_service)
    void onClickCustomerService() {
        showDialog();
    }

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    private void showDialog() {
        alert = null;
        builder = new AlertDialog.Builder(getActivity());
        alert = builder
                .setTitle("系统提示：")
                .setMessage("用户您好，很高兴为您服务,\n客服微信：mofan2258,\n客服电话：4008799697")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                    }
                })
                .create();             //创建AlertDialog对象
        alert.show();

    }

    public static MessageFragment create() {
        return new MessageFragment();
    }

    private ImageView iv_point;

    @Override
    public Object setLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        mTvTitle.setText(R.string.message);
        iv_point = rootView.findViewById(R.id.iv_point);
        IntentFilter filter = new IntentFilter("bro");
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            iv_point.setVisibility(View.VISIBLE);
        }
    };
    private void startPersonListActivity(String title, int type) {
        Intent intent = new Intent(getContext(), PersonListActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, title);
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, type);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onDestroyView();
    }
}
