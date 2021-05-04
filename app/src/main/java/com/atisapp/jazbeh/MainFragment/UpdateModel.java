package com.atisapp.jazbeh.MainFragment;

import java.util.List;

public class UpdateModel {

    private int             id;
    private int             versionCode;
    private String          versionName;
    private String          marketURL;
    private String          directURL;
    private boolean         published;
    private List<String>    featuresList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<String> getFeaturesList() {
        return featuresList;
    }

    public void setFeaturesList(List<String> featuresList) {
        this.featuresList = featuresList;
    }

    public String getMarketURL() {
        return marketURL;
    }

    public void setMarketURL(String marketURL) {
        this.marketURL = marketURL;
    }

    public String getDirectURL() {
        return directURL;
    }

    public void setDirectURL(String directURL) {
        this.directURL = directURL;
    }
}
