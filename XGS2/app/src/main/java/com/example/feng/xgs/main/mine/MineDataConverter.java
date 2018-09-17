package com.example.feng.xgs.main.mine;

import android.content.Context;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/3/19 0019.
 * 我的数据处理类
 * 注: 使用recyclerView有些小问题，暂时放弃该方法
 */

public class MineDataConverter extends BaseDataConverter {


    private Context mContext;

    @Override
    public ArrayList<BaseEntity> convert() {


        addHeadData();

        int[] imgArray = new int[]{ R.mipmap.icon_mine_item_0,R.mipmap.icon_mine_item_1,
               R.mipmap.icon_mine_item_3, R.mipmap.icon_mine_item_5, R.mipmap.icon_mine_item_4, R.mipmap.icon_mine_item_6,R.mipmap.icon_mine_item_7,

                R.mipmap.icon_mine_item_8,  R.mipmap.icon_mine_item_10,
                R.mipmap.icon_mine_item_11, R.mipmap.icon_mine_item_13,
                R.mipmap.icon_mine_item_14,R.mipmap.img_new};
        String[] nameArray = mContext.getResources().getStringArray(R.array.mine_array);

        for (int i = 0; i < imgArray.length; i++) {
            addItemData(imgArray[i], nameArray[i]);

            if(i == 7 || i == 11){
                addDividerData();
            }

        }
        addItemData(0, "");
        addItemData(0, "");

        return ENTITIES;
    }

    //添加头布局
    private void addHeadData(){
        BaseEntity entity = BaseEntity.builder()
                .setField(EntityKeys.NAME, "")
                .setField(EntityKeys.IMG_URL, "")
                .setField(EntityKeys.COUNT_LIKE, "0")
                .setField(EntityKeys.COUNT_COMMENT, "0")
                .setField(EntityKeys.COUNT_ATTENTION, "0")
                .setField(EntityKeys.SPAN_SIZE, 4)
                .setItemType(ItemTypeKeys.MINE_HEAD)
                .build();
        ENTITIES.add(entity);
    }


    //添加分割线布局
    private void addDividerData(){
        BaseEntity entity = BaseEntity.builder()
                .setField(EntityKeys.SPAN_SIZE, 4)
                .setItemType(ItemTypeKeys.DIVIDER_10_DP)
                .build();
        ENTITIES.add(entity);
    }

    //添加item布局
    private void addItemData(int drawable, String name){
        BaseEntity entity = BaseEntity.builder()
                .setField(EntityKeys.IMG_URL, drawable)
                .setField(EntityKeys.NAME, name)
                .setField(EntityKeys.SPAN_SIZE, 1)
                .setItemType(ItemTypeKeys.MINE_ITEM)
                .build();

        ENTITIES.add(entity);
    }

    public MineDataConverter setContext(Context context){
        this.mContext = context;
        return this;
    }
}
