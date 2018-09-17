package com.example.feng.xgs.base.activitys;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feng.xgs.R;

import butterknife.BindView;

/**
 * Created by feng on 2018/1/4.
 */

public abstract class ListActivity extends ToolbarActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;
    public RecyclerView mRv;
    public View emptyView;
    private TextView mEmptyText;

    public abstract void emptyLoadData();

    @Override
    public Object setLayout() {
        return R.layout.activity_list;
    }


    public void baseInitEmptyView(){
        if(mRecyclerView==null){
           return;
        }
        Log.d("TYPE", "baseInitEmptyView: recyclerView != null");
        emptyView = LayoutInflater.from(this).inflate(R.layout.empty_no_data,
                (ViewGroup) mRecyclerView.getParent(), false);
        mEmptyText = (TextView) emptyView.findViewById(R.id.tv_empty_no_data);

        mEmptyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLoadData();
            }
        });

    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    public SwipeRefreshLayout getRefresh(){
        return mRefresh;
    }

    public void initRefreshLayout(){
        mRefresh.setColorSchemeResources(
                R.color.main,
                R.color.main,
                android.R.color.holo_red_light
        );
        mRefresh.setProgressViewOffset(true,0,200);
        mRefresh.setOnRefreshListener(this);
    }

//    public void addDivider(){
//        mRecyclerView.addItemDecoration(
//                new GridItemDecoration(getResources()
//                        .getDrawable(R.drawable.divider_shape)));
//    }
//
//    public void addDivider(@DrawableRes int drawable){
//        mRecyclerView.addItemDecoration(
//                new GridItemDecoration(getResources()
//                        .getDrawable(drawable)));
//    }

}
