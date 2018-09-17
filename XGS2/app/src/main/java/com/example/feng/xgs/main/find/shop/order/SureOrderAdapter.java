package com.example.feng.xgs.main.find.shop.order;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/21 0021.
 */

public class SureOrderAdapter extends BaseEntityAdapter{

    public SureOrderAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.setText(R.id.tv_order_sure_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_order_sure_item_price, entity.getField(EntityKeys.PRICE))
                .setText(R.id.tv_order_sure_item_count, "x" + entity.getField(EntityKeys.COUNT));
        ImageView ivDetail = holder.getView(R.id.iv_order_sure_item);
        loadImg(entity.getField(EntityKeys.IMG_URL), ivDetail);
    }
// alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018053060325348&biz_content=%7B%22body%22%3A%22%E9%AA%86%E9%A9%BC%E7%94%B7%E5%87%89%E9%9E%8B*3%22%2C%22out_trade_no%22%3A%222018061218162837084%22%2C%22passback_params%22%3A%22%25C2%25E6%25CD%25D5%25C4%25D0%25C1%25B9%25D0%25AC*3%22%2C%22product_code%22%3A%22QUICK_WAP_PAY%22%2C%22subject%22%3A%22%E5%95%86%E5%93%81%E4%BB%98%E6%AC%BE%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flocalhost%3A8080%2Fpay%2FcheckProductPayResult.action&sign=evFkOFqwivhF9ZPpN6HPGEoXgy84E0xyCkcI%2FDWKPBpmnozE33zCx6qCu%2FYiBuxGteNaNM8id7gVilpUb76CGtIwVxYR%2BrjWTGpdS%2FHqF%2BhYgqKaGKSPWVzv7OHOWJK6R0iOzc9JEffLnYSS88vRL79eaS%2BVcDgf5uKm3Fzliqej9s9kLivNTYvsDr5QwPma7mKzaI%2FCCLaC8ubSIBktrZs49LjCd4oO4YIsidHwcVa6Leu1aYWCldD4Oco1LkhnygD%2BiJIP512aYkm8wzMlDLm773dpgm%2BsZudqiPRYDJR7IVZzEC%2F1Es35po2qxFT8dNCJPYer%2FqeJlQ5lU9%2FZiA%3D%3D&sign_type=RSA2&timestamp=2018-06-12+18%3A16%3A32&version=1.0&sign=evFkOFqwivhF9ZPpN6HPGEoXgy84E0xyCkcI%2FDWKPBpmnozE33zCx6qCu%2FYiBuxGteNaNM8id7gVilpUb76CGtIwVxYR%2BrjWTGpdS%2FHqF%2BhYgqKaGKSPWVzv7OHOWJK6R0iOzc9JEffLnYSS88vRL79eaS%2BVcDgf5uKm3Fzliqej9s9kLivNTYvsDr5QwPma7mKzaI%2FCCLaC8ubSIBktrZs49LjCd4oO4YIsidHwcVa6Leu1aYWCldD4Oco1LkhnygD%2BiJIP512aYkm8wzMlDLm773dpgm%2BsZudqiPRYDJR7IVZzEC%2F1Es35po2qxFT8dNCJPYer%2FqeJlQ5lU9%2FZiA%3D%3D
// alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018053060325348&biz_content=%7B%22body%22%3A%22%E5%B0%8F%E8%B0%83%E8%B0%83%E7%A4%BC%E7%89%A9%E8%B5%A0%E9%80%81%22%2C%22passback_params%22%3A%22%25D0%25A1%25B5%25F7%25B5%25F7%25C0%25F1%25CE%25EF%25D4%25F9%25CB%25CD%22%2C%22product_code%22%3A%22QUICK_WAP_PAY%22%2C%22subject%22%3A%22%E5%B0%8F%E8%B0%83%E8%B0%83%E7%A4%BC%E7%89%A9%E8%B5%A0%E9%80%81%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&sign=LDEEt8zhCgn%2BCNZst%2B9O244uZJS1o7K9gWwv0V9Pt2XtRGOUDnDqCRZnSIMhFnMetTXFtxL9HmramBPBswWLWUBX%2BzR001EHd8q3QvkbXxJAgXLHb7ge%2Fw8FW5lX71ebA89gVc5BsXCSAnc1YeIHhyiJ1agKb%2B%2BpnYLY7iZ2affQlF6T0x6ROmkzrLKa0jo5CJY42qPCytVrUxNCYNXQ53j2DBWlNxe3vRRQGtrmtG2U9pmMC6XL3UL1wO%2BlUKX1Rb4mxAdFcC7hK82KFO27yidtGLBrmUl0mnP8FpOD6HEP6tZmnC5l1jNA3K63mtoUfOP4Lt4%2BwX4ImFpJDpZqiw%3D%3D&sign_type=RSA2&timestamp=2018-06-12+18%3A17%3A18&version=1.0&sign=LDEEt8zhCgn%2BCNZst%2B9O244uZJS1o7K9gWwv0V9Pt2XtRGOUDnDqCRZnSIMhFnMetTXFtxL9HmramBPBswWLWUBX%2BzR001EHd8q3QvkbXxJAgXLHb7ge%2Fw8FW5lX71ebA89gVc5BsXCSAnc1YeIHhyiJ1agKb%2B%2BpnYLY7iZ2affQlF6T0x6ROmkzrLKa0jo5CJY42qPCytVrUxNCYNXQ53j2DBWlNxe3vRRQGtrmtG2U9pmMC6XL3UL1wO%2BlUKX1Rb4mxAdFcC7hK82KFO27yidtGLBrmUl0mnP8FpOD6HEP6tZmnC5l1jNA3K63mtoUfOP4Lt4%2BwX4ImFpJDpZqiw%3D%3D

}
