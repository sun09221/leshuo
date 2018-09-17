package com.example.feng.xgs.main.nearby;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.core.GlideApp;
import com.example.feng.xgs.main.nearby.detail.NearbyDetailActivity;

import java.util.List;

/**
 * Created by feng on 2018/4/25 0025.
 */

public class NearbyAdapter extends BaseAdapter {

    private Context mContext;
    private List<BaseEntity> mData;
    private LayoutInflater mInflater;

    public NearbyAdapter(Context context, List<BaseEntity> data) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mData = data;
    }


    @Override
    public int getCount() {
        return mData.size();
    }


    @Override
    public Object getItem(int index) {
        return mData.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_nearby, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (position < mData.size()) {
            BaseEntity entity = mData.get(position);
            holder.bindData(entity, position);
        }
        return convertView;
    }


    class ViewHolder {

        ImageView mIVDetail, mIVSex, mIVFriend;
        TextView mTVImgCount, mTVName, mTVAge, mTVStarSign, mTvVIP, mTVIndustry;
        LinearLayout mLyAge;

        public ViewHolder(View view) {
            mIVDetail = (ImageView) view.findViewById(R.id.iv_nearby_item);
            mIVFriend = view.findViewById(R.id.iv_nearby_item_friend);
            mTVImgCount = view.findViewById(R.id.tv_nearby_item_img_count);
            mTVName = view.findViewById(R.id.tv_nearby_item_name);
            mTVAge = view.findViewById(R.id.tv_nearby_item_age);
            mIVSex = view.findViewById(R.id.iv_nearby_item_sex);
            mLyAge = view.findViewById(R.id.ly_nearby_item_age);
            mTVStarSign = view.findViewById(R.id.tv_nearby_item_star_sign);
            mTVIndustry = view.findViewById(R.id.tv_nearby_item_industry);
            mTvVIP = view.findViewById(R.id.tv_nearby_item_vip);
        }

        public void bindData(BaseEntity entity, final int position) {
            GlideApp.with(mContext)
                    .load(entity.getField(EntityKeys.IMG_URL)).
                    centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mIVDetail);

//            int screenHeight = DensityUtils.getScreenHeight(mContext);
//            int imgHeight = screenHeight / 2;
//            mIVDetail.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, imgHeight));

            mIVDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NearbyDetailActivity.class);
                    intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, mData.get(position).getField(EntityKeys.ID_ONLY));
                    mContext.startActivity(intent);
                }
            });

            String sex = entity.getField(EntityKeys.SEX);
            if (ContentKeys.SEX_WOMEN.equals(sex)) {
                mLyAge.setBackgroundResource(R.drawable.pink_light_solid_radius_shape);
                mIVSex.setImageResource(R.mipmap.icon_sex_women);
            } else {
                mLyAge.setBackgroundResource(R.drawable.blue_solid_radius_shape);
                mIVSex.setImageResource(R.mipmap.icon_sex_men);
            }

            mTVName.setText(entity.getField(EntityKeys.NAME));
            if (entity.getField(EntityKeys.AGE) == null || entity.getField(EntityKeys.AGE).isEmpty()) {
                mLyAge.setVisibility(View.GONE);
            } else {
                mTVAge.setText(entity.getField(EntityKeys.AGE));
            }
            if (entity.getField("ismodel") == null || entity.getField("ismodel").isEmpty()) {
                mTVStarSign.setVisibility(View.GONE);
            } else {
                if (entity.getField("ismodel").equals("1")) {
                    mTVStarSign.setText("好声音");
                } else if (entity.getField("ismodel").equals("2")) {
                    mTVStarSign.setText("唱作人");
                } else if (entity.getField("ismodel").equals("3")) {
                    mTVStarSign.setText("音乐老师");
                } else if (entity.getField("ismodel").equals("4")) {
                    mTVStarSign.setText("主持人");
                }else {
                    mTVStarSign.setVisibility(View.GONE);
                }
            }
            String info = entity.getField(EntityKeys.HOMETOWN) + "(" + entity.getField(EntityKeys.DISTANCE) + ")";
            mTVIndustry.setText(info);
            mTVImgCount.setText(entity.getField(EntityKeys.COUNT));
            String type = entity.getField(EntityKeys.TYPE);
            if (ContentKeys.USER_TYPE_VIP.equals(type) || ContentKeys.USER_TYPE_VIP_SUPER.equals(type)) {
                mTvVIP.setVisibility(View.VISIBLE);
            } else {
                mTvVIP.setVisibility(View.GONE);
            }

        }
    }

    public void setData(List<BaseEntity> entities) {
        mData = entities;
        notifyDataSetChanged();
    }
}
