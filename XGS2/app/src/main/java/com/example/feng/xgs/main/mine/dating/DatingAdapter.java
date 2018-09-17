package com.example.feng.xgs.main.mine.dating;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.adapter.BaseMultipleEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.ui.weight.LabelLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/5/10 0010.
 * 我的交友资料
 */

public class DatingAdapter extends BaseMultipleEntityAdapter {

    public DatingAdapter(List<BaseEntity> data) {
        super(data);
        addItemType(ItemTypeKeys.MINE_DATING_PHOTO_LAYOUT, R.layout.head_mine_dating);
        addItemType(ItemTypeKeys.MINE_DATING_DIVIDER, R.layout.item_mine_dating_divider);
        addItemType(ItemTypeKeys.MINE_DATING_TITLE, R.layout.item_mine_dating_title);
        addItemType(ItemTypeKeys.MINE_DATING_APPEND, R.layout.item_mine_dating_append);
        addItemType(ItemTypeKeys.MINE_DATING_IMG_WITH_TEXT, R.layout.item_mine_dating_image_with_text);
        addItemType(ItemTypeKeys.MINE_DATING_LABEL, R.layout.item_mine_dating_label);
        addItemType(ItemTypeKeys.MINE_DATING_QUESTION, R.layout.item_mine_dating_question);
        addItemType(ItemTypeKeys.MINE_DATING_QUESTION_APPEND, R.layout.item_mine_dating_append);
    }

    /**
     * 由于布局是固定的，可以根据position判断 是什么功能
     *
     *icon_natural_publish_address = 4： 个性签名
     *icon_natural_publish_address = 7-11：我的信息
     *icon_natural_publish_address = 14：我的标签
     *icon_natural_publish_address = 17-23：我的兴趣
     *icon_natural_publish_address = 26： 星座
     *icon_natural_publish_address = 29： 问题
     * */
    @Override
    protected void convert(final BaseViewHolder holder, BaseEntity entity) {
        switch (holder.getItemViewType()) {
            //顶部图片布局
            case ItemTypeKeys.MINE_DATING_PHOTO_LAYOUT:
                DatingPhotoLayout photoLayout = holder.getView(R.id.photo_layout_dating_head);
                photoLayout.setData(entity.getField(EntityKeys.IMG_URL_S));
                photoLayout.setOnLayoutPhotoItemListener(new DatingPhotoLayout.IDatingLayoutPhotoListener() {
                    @Override
                    public void onItemClick(int position, ImageView imageView) {
                        if(mListener != null){
                            mListener.onItemClick(position, imageView);
                        }
                    }
                });
                break;
                //分割线
            case ItemTypeKeys.MINE_DATING_DIVIDER:
                break;
                //带加号的布局
            case ItemTypeKeys.MINE_DATING_APPEND:
                holder.setText(R.id.tv_dating_append_item, entity.getField(EntityKeys.NAME));
                holder.setImageResource(R.id.iv_dating_add_item,
                        (Integer) entity.getFieldObject(EntityKeys.IMG_URL))
                        .setImageResource(R.id.iv_dating_add_item_add,
                                (Integer) entity.getFieldObject(EntityKeys.IMG_RESOURCE));


                break;
            //带加号的布局 隐藏左边的图片
            case ItemTypeKeys.MINE_DATING_QUESTION_APPEND:
                holder.setText(R.id.tv_dating_append_item, entity.getField(EntityKeys.NAME));
                holder.setImageResource(R.id.iv_dating_add_item,
                        R.color.white)
                        .setImageResource(R.id.iv_dating_add_item_add,
                                (Integer) entity.getFieldObject(EntityKeys.IMG_RESOURCE));

                break;

            //分类title
            case ItemTypeKeys.MINE_DATING_TITLE:
                holder.setText(R.id.tv_dating_title_item, entity.getField(EntityKeys.NAME));
                break;

            //图片文字混合布局
            case ItemTypeKeys.MINE_DATING_IMG_WITH_TEXT:
                holder.setText(R.id.tv_dating_image_with_text_item, entity.getField(EntityKeys.INFO));
                holder.setImageResource(R.id.iv_dating_image_with_text_item, (Integer) entity.getFieldObject(EntityKeys.IMG_URL));
                break;

            //带不固定标签的布局
            case ItemTypeKeys.MINE_DATING_LABEL:
                holder.setImageResource(R.id.iv_dating_label_item, (Integer) entity.getFieldObject(EntityKeys.IMG_URL));
                LabelLayout labelLayout = holder.getView(R.id.label_dating_label_item);
                labelLayout.setVerticalMargin(8);
                labelLayout.setTextColor((Integer) entity.getFieldObject(EntityKeys.DATING_COLOR_RESOURCE));
                labelLayout.setBackground((Integer) entity.getFieldObject(EntityKeys.DATING_BACKGROUND_RESOURCE));

                String info = entity.getField(EntityKeys.INFO);
                List<String> labelList = new ArrayList<>();
                String[] labelArray = info.split(",");
                for (int i = 0; i < labelArray.length; i++) {
                    labelList.add(labelArray[i]);
                }
                labelLayout.setLabelData(labelList);
                break;
            //我的问答
            case ItemTypeKeys.MINE_DATING_QUESTION:
                holder.setText(R.id.tv_dating_question_name, entity.getField(EntityKeys.NAME))
                        .setText(R.id.tv_dating_question_info, entity.getField(EntityKeys.INFO));
//                holder.addOnClickListener(R.id.tv_dating_question_name);
//                holder.addOnClickListener(R.id.tv_dating_question_info);
                break;
            default:
                break;
        }
    }

    private IPhotoLayoutItemListener mListener;

    public void setPhotoLayoutItemListener(IPhotoLayoutItemListener listener){
        this.mListener = listener;
    }

    public interface IPhotoLayoutItemListener{
        void onItemClick(int position, ImageView imageView);
    }
}
