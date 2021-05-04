package com.atisapp.jazbeh.MainFragment.ui.home;

import android.content.Context;
import com.atisapp.jazbeh.ProductList.ProductModel;
import java.util.List;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {

    private static final String TAG = "MainSliderAdapter";
    private Context context;
    private DashboardAPIService dashboardAPIService;
    private int bannerSize;
    private List<ProductModel> myBannerList;

    public MainSliderAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        switch (position) {
            case 0:
                imageSlideViewHolder.bindImageSlide("http://cdn.jazbe.com/public/images/banners/banner1.png");
                break;
            case 1:
                imageSlideViewHolder.bindImageSlide("http://cdn.jazbe.com/public/images/banners/banner2.png");
                break;
        }
    }
}
