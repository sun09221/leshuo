package com.example.feng.xgs.base.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.feng.xgs.core.GlideApp;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by feng on 2017/12/18.
 */

public abstract class BaseFragment extends Fragment {


    @SuppressWarnings("SpellCheckingInspection")
    private Unbinder mUnbinder = null;

    public abstract Object setLayout();

    public abstract void onBindView(@Nullable Bundle saveInstanceState, View rootView);

    public void baseInitEmptyView() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = null;
        if (setLayout() instanceof Integer) {
            rootView = inflater.inflate((Integer) setLayout(), container, false);
        } else if (setLayout() instanceof View) {
            rootView = (View) setLayout();
        } else {
            throw new ClassCastException("setLayout() must be int or emptyView");
        }

        if (rootView != null) {
            mUnbinder = ButterKnife.bind(this, rootView);
            baseInitEmptyView();
            onBindView(savedInstanceState, rootView);
        }
        return rootView;
    }

    public void loadImg(String imgUrl, ImageView view) {
        if (getContext() == null) return;
        GlideApp.with(getContext())
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .centerCrop()
                .dontAnimate()
                .into(view);
    }

    public void loadImg(String imgUrl, ImageView view, int drawable) {
        if (getContext() == null) return;
        GlideApp.with(getContext())
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .error(drawable)
                .centerCrop()
                .dontAnimate()
                .into(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }


    /**
     * 以下为解决fragment重叠问题
     * */
//    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
//
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            if (isSupportHidden) {
//                transaction.hide(this);
//            } else {
//                transaction.show(this);
//            }
//            transaction.commit();
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putBoolean(STATE_SAVE_IS_HIDDEN, true);
//    }

}
