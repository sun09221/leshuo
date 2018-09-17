package com.example.feng.xgs.main.find.shop.search;

import java.util.List;

/**
 * Created by feng on 2018/6/11 0011.
 */

public class ShopSearchBean {

    private List<SearchBean> beanList;

    public List<SearchBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<SearchBean> beanList) {
        this.beanList = beanList;
    }

    public static class SearchBean{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
