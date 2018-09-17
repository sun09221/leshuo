package com.example.feng.xgs.main.area.model.list;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.adapter.BaseMultipleEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.utils.DensityUtils;

import java.util.List;

/**
 * Created by feng on 2018/5/31 0031.
 */

public class StudentAdapter extends BaseMultipleEntityAdapter{


    public StudentAdapter(List<BaseEntity> data) {
        super(data);
        addItemType(ItemTypeKeys.LIST_HEAD, R.layout.head_find_area_student_list);
        addItemType(ItemTypeKeys.LIST_ITEM, R.layout.item_find_area_student_item);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {

        switch (holder.getItemViewType()){
            case ItemTypeKeys.LIST_HEAD:
                ImageView imageViewHead = holder.getView(R.id.iv_student_list);
                holder.setText(R.id.tv_student_list_count, entity.getField(EntityKeys.COUNT))
                        .setText(R.id.tv_student_list_popularity_count, entity.getField(EntityKeys.COUNT_POPULARITY))
                        .setText(R.id.tv_student_list_visit_count, entity.getField(EntityKeys.COUNT_VISIT));
                break;
            case ItemTypeKeys.LIST_ITEM:

                int position = holder.getLayoutPosition();
                ImageView imageView = holder.getView(R.id.iv_student_item);
                int margin = DensityUtils.dp2px(mContext, 10);
                loadImg(entity.getField(EntityKeys.IMG_URL), imageView);
                holder.setText(R.id.tv_student_item_name, entity.getField(EntityKeys.NAME))
                        .setText(R.id.tv_student_item_info, entity.getField(EntityKeys.INFO));

                LinearLayout layoutItem = holder.getView(R.id.ly_student_item);
                if(position % 2 == 0){
                    layoutItem.setPadding(margin / 2, 0, margin, margin);
                }else {
                    layoutItem.setPadding(margin, 0, margin / 2, margin);
                }
                break;
        }

    }
}
