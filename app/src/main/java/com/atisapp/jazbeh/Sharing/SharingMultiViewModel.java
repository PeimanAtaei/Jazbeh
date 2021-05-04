package com.atisapp.jazbeh.Sharing;

import com.atisapp.jazbeh.ProductList.PracticeModel;
import com.atisapp.jazbeh.ProductList.ProductModel;

import java.util.List;

public class SharingMultiViewModel {

    public static final int HEADER_LAYOUT = 0;
    public static final int APPS_LIST_LAYOUT = 1;
    public static final int SHARE_HEADER_LAYOUT = 2;

    private int viewType;
    private String headerTitle;


    public SharingMultiViewModel(int viewType, String headerTitle) {
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

    private List<SharingModel> appsModelList;

    public SharingMultiViewModel(int viewType, List<SharingModel> appsModelList) {
        this.viewType = viewType;
        this.appsModelList = appsModelList;
    }

    public List<SharingModel> getAppsModelList() {
        return appsModelList;
    }

    public void setProductModelList(List<SharingModel> appsModelList) {
        this.appsModelList = appsModelList;
    }

    // Course Header Model

    private String invitationLink;

    public SharingMultiViewModel(int viewType, String invitationLink,String str) {
        this.viewType = viewType;
        this.invitationLink = invitationLink;
    }


    public String getShareLink() {
        return invitationLink;
    }

    public void setShareLink(String invitationLink) {
        this.invitationLink = invitationLink;
    }

}
