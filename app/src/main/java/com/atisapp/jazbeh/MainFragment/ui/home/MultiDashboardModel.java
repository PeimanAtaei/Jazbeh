package com.atisapp.jazbeh.MainFragment.ui.home;

import com.atisapp.jazbeh.ProductList.ProductModel;

import java.util.List;

public class MultiDashboardModel {

    public static final int SLIDER_LAYOUT = 0;
    public static final int POPULAR_LAYOUT = 1;
    public static final int HEADER_LAYOUT = 2;
    public static final int CATEGORY_LAYOUT = 3;


    // SLIDER_LAYOUT -------------------------------------------------------------------------------

    private int viewType;
    private String sliderURL;
    private String sliderTitle;

    public MultiDashboardModel(int viewType, String sliderURL, String sliderTitle) {
        this.viewType = viewType;
        this.sliderURL = sliderURL;
        this.sliderTitle = sliderTitle;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getSliderURL() {
        return sliderURL;
    }

    public void setSliderURL(String sliderURL) {
        this.sliderURL = sliderURL;
    }

    public String getSliderTitle() {
        return sliderTitle;
    }

    public void setSliderTitle(String sliderTitle) {
        this.sliderTitle = sliderTitle;
    }



    // POPULAR_LAYOUT AND CATEGORIES  -----------------------------------------------------------------------------

    private List<ProductModel> popularModelList;

    public MultiDashboardModel(int viewType, List<ProductModel> popularModelList) {
        this.viewType = viewType;
        this.popularModelList = popularModelList;
    }


    public List<ProductModel> getPopularModelList() {
        return popularModelList;
    }

    public void setPopularModelList(List<ProductModel> popularModelList) {
        this.popularModelList = popularModelList;
    }



    // HEADER_LAYOUT -------------------------------------------------------------------------------

    private String headerTitle;

    public MultiDashboardModel(int viewType, String headerTitle) {
        this.viewType = viewType;
        this.headerTitle = headerTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

}
