package com.atisapp.jazbeh.VideoPlayer;

import com.atisapp.jazbeh.ProductList.PracticeModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.Storage.Database.Tables.OfflineTable;

import java.util.List;

public class VideoMultiViewModel {

    public static final int VIDEO_HEADER_LAYOUT = 0;
    public static final int VIDEO_LIST_LAYOUT = 1;
    public static final int VIDEO_COURSE_HEADER_LAYOUT = 2;
    public static final int VIDEO_EPISODE_LIST_LAYOUT = 3;

    private int viewType;
    private String headerTitle;


    public VideoMultiViewModel(int viewType, String headerTitle) {
        this.viewType = viewType;
        this.headerTitle = headerTitle;
    }


    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }


    // List Model

    private List<ProductModel> productModelList;
    private boolean clickable;

    public VideoMultiViewModel(int viewType, List<ProductModel> productModelList, boolean clickable) {
        this.viewType = viewType;
        this.productModelList = productModelList;
        this.clickable = clickable;
    }

    public List<ProductModel> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
    }

    // Course Header Model

    private ProductModel courseInformation;

    public VideoMultiViewModel(int viewType, ProductModel courseInformation) {
        this.viewType = viewType;
        this.courseInformation = courseInformation;
    }


    public ProductModel getCourseInformation() {
        return courseInformation;
    }

    public void setCourseInformation(ProductModel courseInformation) {
        this.courseInformation = courseInformation;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

}
