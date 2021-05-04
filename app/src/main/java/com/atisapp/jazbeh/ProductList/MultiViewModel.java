package com.atisapp.jazbeh.ProductList;

import com.atisapp.jazbeh.Storage.Database.Tables.OfflineTable;

import java.util.List;

public class MultiViewModel {

    public static final int HEADER_LAYOUT = 0;
    public static final int COURSE_LIST_LAYOUT = 1;
    public static final int EPISODE_LIST_LAYOUT = 2;
    public static final int COURSE_HEADER_LAYOUT = 3;
    public static final int PRACTICE_IMAGE_LAYOUT = 4;
    public static final int PRACTICE_PDF_LAYOUT = 5;
    public static final int PRACTICE_VOICE_LAYOUT = 6;
    public static final int OFFLINE_PRODUCTS_LAYOUT = 7;

    private int viewType;
    private String headerTitle;


    public MultiViewModel(int viewType, String headerTitle) {
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

    public MultiViewModel(int viewType, List<ProductModel> productModelList,boolean clickable) {
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

    public MultiViewModel(int viewType, ProductModel courseInformation) {
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

    // Practice Model

    private List<PracticeModel> practiceModelList;

    public MultiViewModel(int viewType, List<PracticeModel> practiceModelList) {
        this.viewType = viewType;
        this.practiceModelList = practiceModelList;
    }


    public List<PracticeModel> getPracticeModelList() {
        return practiceModelList;
    }

    public void setPracticeModelList(List<PracticeModel> practiceModelList) {
        this.practiceModelList = practiceModelList;
    }


    // Offline List Model

    private List<OfflineTable> offlineProductList;

    public MultiViewModel(int viewType, List<OfflineTable> offlineProductList,String thing) {
        this.viewType = viewType;
        this.offlineProductList = offlineProductList;
    }

    public List<OfflineTable> getOfflineProductList() {
        return offlineProductList;
    }

    public void setOfflineProductList(List<OfflineTable> offlineProductList) {
        this.offlineProductList = offlineProductList;
    }
}
