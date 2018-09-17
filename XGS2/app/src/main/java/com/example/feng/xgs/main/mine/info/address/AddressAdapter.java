package com.example.feng.xgs.main.mine.info.address;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.ui.dialog.DeleteHintDialog;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/12/22.
 * 收货地址
 */

public class AddressAdapter extends BaseEntityAdapter {

    private boolean isSelect = false;
    private int mOldSelectPosition = 0;
    private int mType;

    public AddressAdapter(@LayoutRes int layoutResId, @Nullable List<BaseEntity> data, int type) {
        super(layoutResId, data);
        this.mType = type;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final BaseEntity entity) {
        final String name = entity.getField(EntityKeys.NAME);
        final String mobile = entity.getField(EntityKeys.MOBILE);
        final String city = entity.getField(EntityKeys.ADDRESS_ALL);
        final int position = holder.getLayoutPosition();
        holder.setText(R.id.tv_address_item_name, name)
                .setText(R.id.tv_address_item_mobile, mobile)
                .setText(R.id.tv_address_item_address, city);

        if(mType == CodeKeys.ADDRESS_SELECT){
            LinearLayout layout = holder.getView(R.id.ly_address_item_manager);
            layout.setVisibility(View.GONE);
        }

        //默认地址
        final TextView tvSelector = holder.getView(R.id.tv_address_item_selector);
        final String type = entity.getField(EntityKeys.TYPE);
        if(type.equals(ContentKeys.ADDRESS_DEFAULT)){
            isSelect = true;
            mOldSelectPosition = holder.getLayoutPosition();
            //添加默认地址文字
            if(mType == CodeKeys.ADDRESS_SELECT){
                String address = "【默认地址】\r" + city;
                SpannableStringBuilder builder = new SpannableStringBuilder(address);
                CharacterStyle style = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.main));
                builder.setSpan(style,0,address.indexOf("\r"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.setText(R.id.tv_address_item_address, builder);
            }
        }else {
            isSelect = false;
        }
        tvSelector.setSelected(isSelect);

        tvSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showText(mContext, "改变默认地址按钮点击了");
                //当不是默认地址时，点击变为默认地址
                int position = holder.getLayoutPosition();
                String addressId = entity.getField(EntityKeys.ID_ONLY);
                String type = entity.getField(EntityKeys.TYPE);
                //改变type的状态
                type = ContentKeys.ADDRESS_DEFAULT.equals(type) ?
                        ContentKeys.ADDRESS_DEFAULT_NO : ContentKeys.ADDRESS_DEFAULT;

                Map<String, String> map = new HashMap<>();
                map.put(ParameterKeys.ID_ONLY, addressId);
                map.put(ParameterKeys.TYPE, type);
                map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());

                String raw = JSON.toJSONString(map);
                if(mOldSelectPosition != position){
                    RestClient.builder()
                            .url(UrlKeys.ADDRESS_DEFAULT)
                            .loader(mContext)
                            .toast()
                            .raw(raw)
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    /**
                                     * 先取消上个默认地址选中状态
                                     * 再选中要设置的默认地址
                                     * */
                                    int position = holder.getLayoutPosition();
                                    List<BaseEntity> entities = getData();
                                    entities.get(position).setField(EntityKeys.TYPE,
                                            ContentKeys.ADDRESS_DEFAULT);
                                    entities.get(mOldSelectPosition).setField(EntityKeys.TYPE,
                                            ContentKeys.ADDRESS_DEFAULT_NO);
                                    notifyItemChanged(mOldSelectPosition);
                                    tvSelector.setSelected(true);

                                    mOldSelectPosition = position;
                                }
                            })
                            .build().post();
                }
            }
        });

        //编辑
        holder.addOnClickListener(R.id.tv_address_item_editor);

        //删除地址
        TextView tvDelete = holder.getView(R.id.tv_address_item_delete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = entity.getField(EntityKeys.TYPE);
                if(type.equals(ContentKeys.ADDRESS_DEFAULT)){
                    ToastUtils.showText(mContext, "不能删除默认地址");
                    return;
                }

                DeleteHintDialog.create(mContext)
                        .showDialog(new DeleteHintDialog.IOnDialogDeleteListener(){
                            @Override
                            public void onSure() {
                                final String addressId = entity.getField(EntityKeys.ID_ONLY);
                                final int position = holder.getLayoutPosition();
                                Map<String, String> map = new HashMap<>();
                                map.put(ParameterKeys.ID_ONLY, addressId);
                                String raw = JSON.toJSONString(map);

                                RestClient.builder()
                                        .url(UrlKeys.ADDRESS_DELETE)
                                        .raw(raw)
                                        .loader(mContext)
                                        .toast()
                                        .success(new ISuccess() {
                                            @Override
                                            public void onSuccess(String response) {
                                                ToastUtils.showText(mContext, "删除成功");
                                                remove(position);
                                            }
                                        })
                                        .build().post();


                            }
                        });

            }
        });
    }


}
