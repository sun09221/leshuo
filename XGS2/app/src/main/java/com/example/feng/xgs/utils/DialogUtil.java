package com.example.feng.xgs.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.feng.xgs.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by JY on 2018/4/11.
 */

public class DialogUtil {


    private Dialog dialog;
    private RecyclerView rec;
    private DialogAdapter mAdapter;
    private int pos;

    private static class SingletonHolder {
        private static final DialogUtil INSTANCE = new DialogUtil();
    }


    public interface OnItemClickListener {
        void onClick(View v, int position);


    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    //获取单例
    public static DialogUtil getInstance() {
        return DialogUtil.SingletonHolder.INSTANCE;
    }

    public void showDialog(Activity activity, final List<String> list, final TextView tv) {
        dialog = new Dialog(activity, R.style.Theme_Light_Dialog_1);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_list, null);
        rec = dialogView.findViewById(R.id.rec_text);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        rec.setLayoutManager(linearLayoutManager);
        rec.setNestedScrollingEnabled(false);
        rec.addItemDecoration(new DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new DialogAdapter(list, activity);
        mAdapter.setOnItemClickListener(new DialogAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
//                mOnItemClickListener.onClick(v, position);
                tv.setText(list.get(position));
                dialog.dismiss();

            }


        });
        rec.setAdapter(mAdapter);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        WindowManager wm = activity.getWindowManager();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        dialog.show();


    }

    public View showFullDialog(Activity activity,Dialog fullDialog,int layout) {
        View dialogView = LayoutInflater.from(activity).inflate(layout, null);
        //获得dialog的window窗口
        Window window = fullDialog.getWindow();
        WindowManager wm = activity.getWindowManager();
        //设置dialog在屏幕底部
        //window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        //window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        fullDialog.setContentView(dialogView);

        return dialogView;
    }

    public void showDatePicker(Activity activity, final TextView tv) {
        Calendar cale1 = Calendar.getInstance();
        new DatePickerDialog(activity, DatePickerDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                //这里获取到的月份需要加上1哦~
                tv.setText(+year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
            }
        }
                , cale1.get(Calendar.YEAR)
                , cale1.get(Calendar.MONTH)
                , cale1.get(Calendar.DAY_OF_MONTH)).show();
    }

}
