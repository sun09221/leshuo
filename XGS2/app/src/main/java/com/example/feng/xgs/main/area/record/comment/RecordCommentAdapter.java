package com.example.feng.xgs.main.area.record.comment;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/6/7 0007.
 */

public class RecordCommentAdapter extends BaseEntityAdapter{

    public RecordCommentAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
      //  try {
            holder.setText(R.id.tv_record_comment_item_name, entity.getField(EntityKeys.NAME))
                    .setText(R.id.tv_record_comment_item_time, entity.getField(EntityKeys.TIME))
                   // .setText(R.id.tv_record_comment_item_info, URLDecoder.decode(entity.getField(EntityKeys.INFO),"UTF-8"));
                    .setText(R.id.tv_record_comment_item_info,entity.getField(EntityKeys.INFO));
//        }
//        catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        ImageView imageView = holder.getView(R.id.iv_record_comment_item);
        loadImg(entity.getField(EntityKeys.IMG_URL), imageView);

    }
}
