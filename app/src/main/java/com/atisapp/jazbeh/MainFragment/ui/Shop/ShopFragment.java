package com.atisapp.jazbeh.MainFragment.ui.Shop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.MainFragment.HomeActivity;
import com.atisapp.jazbeh.MainFragment.ui.Shop.ui.main.SectionsPagerAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private static final String TAG = "ProductListActivity";
    private Context context;
    private View shopRoot;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabs;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        shopRoot = inflater.inflate(R.layout.activity_shop, container, false);
        Log.i(TAG, "onCreateView:  shop");
        SetupViews();
        return shopRoot;
    }

    private void SetupViews()
    {
        context = getContext();
        CheckRegister();
    }

    private void CheckRegister()
    {
        HomeActivity homeActivity = (HomeActivity)getActivity();
        if(homeActivity.CheckRegistration())
        {
            sectionsPagerAdapter = new SectionsPagerAdapter(context, getChildFragmentManager());
            viewPager = shopRoot.findViewById(R.id.shop_view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            tabs = shopRoot.findViewById(R.id.shop_tabs);
            tabs.setupWithViewPager(viewPager);
        }
    }


}
