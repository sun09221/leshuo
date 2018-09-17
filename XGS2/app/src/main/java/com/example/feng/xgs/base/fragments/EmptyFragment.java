package com.example.feng.xgs.base.fragments;

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
 * Created by feng on 2017/12/18.
 */

public abstract class EmptyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView mEmptyText;
    private RecyclerView mRecyclerView;
    public View emptyView;

    public abstract RecyclerView getRecyclerView();
    public abstract void emptyLoadData();

//    BaseQuickAdapter


    public void baseInitEmptyView(){
        mRecyclerView = getRecyclerView();
        if(mRecyclerView!=null){
            Log.d("TYPE", "baseInitEmptyView: recyclerView != null");
            emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_no_data,
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
                R.color.pink,
                R.color.main,
                R.color.yellow_light
        );
        refreshLayout.setProgressViewOffset(true,0,200);
        refreshLayout.setOnRefreshListener(this);
    }

    public void addDivider(int size, int color){
        if(getContext() != null) {
            mRecyclerView.addItemDecoration(BaseDecoration.create(DensityUtils.dp2px(getContext(), size),
                    ContextCompat.getColor(getContext(), color)));
        }
    }

    public void addDivider(int size){
        if(getContext() != null){
            mRecyclerView.addItemDecoration(BaseDecoration.create(DensityUtils.dp2px(getContext(), size),
                    ContextCompat.getColor(getContext(), R.color.background_color)));
        }
    }

    public void addDivider(){
        if(getContext() != null){
            mRecyclerView.addItemDecoration(BaseDecoration.create(DensityUtils.dp2px(getContext(), 1),
                    ContextCompat.getColor(getContext(), R.color.background_color)));
        }

    }


}
