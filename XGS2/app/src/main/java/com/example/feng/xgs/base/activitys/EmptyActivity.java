package com.example.feng.xgs.base.activitys;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.BaseDecoration;
import com.example.feng.xgs.utils.DensityUtils;


/**
 * Created by feng on 2017/12/22.
 */

public abstract class EmptyActivity extends ToolbarActivity implements SwipeRefreshLayout.OnRefreshListener{

    private TextView mEmptyText;
    private RecyclerView mRecyclerView;
    public View emptyView;

    public abstract RecyclerView getRecyclerView();
    public abstract void emptyLoadData();



    public void baseInitEmptyView(){
        mRecyclerView = getRecyclerView();
        if(mRecyclerView!=null){
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

    }

    public void initRefreshLayout(SwipeRefreshLayout refreshLayout){
        refreshLayout.setColorSchemeResources(
                R.color.main,
                R.color.main
        );
        refreshLayout.setProgressViewOffset(true,0,200);
        refreshLayout.setOnRefreshListener(this);
    }

    public void addDivider(int size, int color){
        mRecyclerView.addItemDecoration(BaseDecoration.create(DensityUtils.dp2px(this, size),
                ContextCompat.getColor(this, color)));
    }

    public void addDivider(int size){
        mRecyclerView.addItemDecoration(BaseDecoration.create(DensityUtils.dp2px(this, size),
                ContextCompat.getColor(this, R.color.background_color)));
    }

    public void addDivider(){
        mRecyclerView.addItemDecoration(BaseDecoration.create(DensityUtils.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.background_color)));
    }
}
