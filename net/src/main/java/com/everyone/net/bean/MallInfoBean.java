package com.everyone.net.bean;

public class MallInfoBean {
    private String goods_cover;
    private int goods_id;
    private String goods_name;
    private String goods_price;
    private String goods_src_price;
    private int goods_total_sales;

    public int getGoods_id() {
        return goods_id;
    }

    public String getGoods_cover() {
        return goods_cover;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public int getGoods_total_sales() {
        return goods_total_sales;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public String getGoods_src_price() {
        return goods_src_price;
    }

    public void setGoods_cover(String goods_cover) {
        this.goods_cover = goods_cover;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public void setGoods_src_price(String goods_src_price) {
        this.goods_src_price = goods_src_price;
    }

    public void setGoods_total_sales(int goods_total_sales) {
        this.goods_total_sales = goods_total_sales;
    }
}

