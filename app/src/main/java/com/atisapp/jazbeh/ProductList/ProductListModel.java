package com.atisapp.jazbeh.ProductList;

public class ProductListModel {

    private int         id;
    private String     product_msg;
    private String     product_id;
    private String     price;
    private String     group_id;
    private String  product_time;
    private String  product_name;
    private String  product_explain;
    private String  product_usage;
    private String  product_alarm;
    private String  product_url;
    private String  practice_url;
    private boolean bought;
    private boolean is_package;
    private boolean is_practice;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getProduct_time() {
        return product_time;
    }

    public void setProduct_time(String product_time) {
        this.product_time = product_time;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_explain() {
        return product_explain;
    }

    public void setProduct_explain(String product_explain) {
        this.product_explain = product_explain;
    }

    public String getProduct_usage() {
        return product_usage;
    }

    public void setProduct_usage(String product_usage) {
        this.product_usage = product_usage;
    }

    public String getProduct_alarm() {
        return product_alarm;
    }

    public void setProduct_alarm(String product_alarm) {
        this.product_alarm = product_alarm;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIs_package() {
        return is_package;
    }

    public void setIs_package(boolean is_package) {
        this.is_package = is_package;
    }

    public String getProduct_msg() {
        return product_msg;
    }

    public void setProduct_msg(String product_msg) {
        this.product_msg = product_msg;
    }

    public String getPracticeUrl() {
        return practice_url;
    }

    public void setPracticeUrl(String practice_url) {
        this.practice_url = practice_url;
    }

    public boolean getPractice() {
        return is_practice;
    }

    public void setPractice(boolean is_practice) {
        this.is_practice = is_practice;
    }
}
