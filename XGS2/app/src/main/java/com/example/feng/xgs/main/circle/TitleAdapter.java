package com.example.feng.xgs.main.circle;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;

import java.util.List;

/**
 * 描述：添加类的描述
 *
 * @author 金源
 * @time 2018/7/23
 */
public class TitleAdapter extends BaseEntityAdapter {

    public TitleAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BaseEntity baseEntity) {
        baseViewHolder.addOnClickListener(R.id.tv_title_1);
        baseViewHolder.setText(R.id.tv_title_1,baseEntity.getField("name"));
        if(baseEntity.getField("type").equals("0")){
            ((TextView)baseViewHolder.getView(R.id.tv_title_1)).setTextColor(mContext.getResources().getColor(R.color.text_black));
        }else if(baseEntity.getField("type").equals("1")){
            ((TextView)baseViewHolder.getView(R.id.tv_title_1)).setTextColor(mContext.getResources().getColor(R.color.blue_original));
        }

    }
}
