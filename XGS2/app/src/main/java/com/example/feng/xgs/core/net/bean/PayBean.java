package com.example.feng.xgs.core.net.bean;

import java.util.List;

/**
 * Created by feng on 2018/6/11 0011.
 */

public class PayBean {

    /**
     * peopleid : 6
     * type : 1
     * product : [{"productid":"1","num":"2", carid:"1"}]
     */

    private String peopleid;
    private String type;
    private String modelid;
    private String addressid;
    private List<ProductBean> product;

    public String getAddressid() {
        return addressid;
    }

    public void setAddressid(String addressid) {
        this.addressid = addressid;
    }

    public String getModelid() {
        return modelid;
    }

    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    public String getPeopleid() {
        return peopleid;
    }

    public void setPeopleid(String peopleid) {
        this.peopleid = peopleid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ProductBean> getProduct() {
        return product;
    }

    public void setProduct(List<ProductBean> product) {
        this.product = product;
    }

    public static class ProductBean {
        /**
         * productid : 1
         * num : 2
         */

        private String productid;
        private String num;
        private String rewardid;

        public String getRewardid() {
            return rewardid;
        }

        public void setRewardid(String rewardid) {
            this.rewardid = rewardid;
        }

        public String getProductid() {
            return productid;
        }

        public void setProductid(String productid) {
            this.productid = productid;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
