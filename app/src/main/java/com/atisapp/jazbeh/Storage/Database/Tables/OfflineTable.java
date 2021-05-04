package com.atisapp.jazbeh.Storage.Database.Tables;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "offlineTable")
public class OfflineTable extends Model {

    @Column(name = "product_id")
    private String  offline_id;

    @Column(name = "title")
    private String  offline_title;

    @Column(name = "url")
    private String  offline_url;

    @Column(name = "price")
    private int     offline_price;

    @Column(name = "time")
    private String  offline_time;

    @Column(name = "category")
    private String  offline_category;

    @Column(name = "description")
    private String  offline_description;

    @Column(name = "type")
    private String  offline_type;

    @Column(name = "explain")
    private String  offline_explain;

    @Column(name = "usage")
    private String  offline_usage;

    @Column(name = "alarm")
    private String  offline_alarm;

    public String getOffline_id() {
        return offline_id;
    }

    public void setOffline_id(String offline_id) {
        this.offline_id = offline_id;
    }

    public String getOffline_title() {
        return offline_title;
    }

    public void setOffline_title(String offline_title) {
        this.offline_title = offline_title;
    }

    public String getOffline_url() {
        return offline_url;
    }

    public void setOffline_url(String offline_url) {
        this.offline_url = offline_url;
    }

    public int getOffline_price() {
        return offline_price;
    }

    public void setOffline_price(int offline_price) {
        this.offline_price = offline_price;
    }

    public String getOffline_time() {
        return offline_time;
    }

    public void setOffline_time(String offline_time) {
        this.offline_time = offline_time;
    }

    public String getOffline_category() {
        return offline_category;
    }

    public void setOffline_category(String offline_category) {
        this.offline_category = offline_category;
    }

    public String getOffline_description() {
        return offline_description;
    }

    public void setOffline_description(String offline_description) {
        this.offline_description = offline_description;
    }

    public String getOffline_type() {
        return offline_type;
    }

    public void setOffline_type(String offline_type) {
        this.offline_type = offline_type;
    }

    public String getOffline_explain() {
        return offline_explain;
    }

    public void setOffline_explain(String offline_explain) {
        this.offline_explain = offline_explain;
    }

    public String getOffline_usage() {
        return offline_usage;
    }

    public void setOffline_usage(String offline_usage) {
        this.offline_usage = offline_usage;
    }

    public String getOffline_alarm() {
        return offline_alarm;
    }

    public void setOffline_alarm(String offline_alarm) {
        this.offline_alarm = offline_alarm;
    }
}
