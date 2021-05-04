package com.atisapp.jazbeh.MainFragment.ui.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.StatsLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.atisapp.jazbeh.MainFragment.HomeActivity;
import com.atisapp.jazbeh.ProductList.ProductListActivity;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.PsycologicalTests.TestListActivity;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Sharing.SharingActivity;
import com.atisapp.jazbeh.Splash.SplashActivity;
import com.atisapp.jazbeh.Storage.Prefs.CategoryPref;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;
import com.atisapp.jazbeh.VideoPlayer.VideoCategoryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
//import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final int    ACTIVITYNUM = 0;
    private Context context;
    private ProductPrefs productPrefs;
    private IdentitySharedPref identitySharedPref;
    private CategoryPref categoryPref;
    private ImageView    sharing,audioBooks,videoCategories,testCategories;
    private LinearLayout love,money,success,beauty,english,health,sport,food,meditation,mind,lights,book,art,guide,metaphysic,gift,virtual,management,education;
    private LinearLayout homeBtn,shopBtn,profileBtn;
    private Toolbar main_toolbar;
    private DrawerLayout dashboard_draw;
    private NavigationView menu_navigation;
    private FloatingActionButton fab, walletFab;
    private BottomNavigationView bottomNavigationView;
    private WalletApiService walletApiService;
    private List<MultiDashboardModel> totalMultiViewModelList;
    private List<ProductModel> myBannerList;
    private DashboardAPIService dashboardAPIService;
    private MultiDashboardAdapter multiDashboardAdapter;
    private RecyclerView dashboardRecyclerView;
    private Slider slider;
    private View dashboardRoot;
    private HomeActivity homeActivity;
    private HomeViewModel homeViewModel;
    //private FirebaseAnalytics firebaseAnalytics;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardRoot = inflater.inflate(R.layout.activity_dashboard, container, false);

        SetupViews();

        return dashboardRoot;


    }

    private void SetupViews()
    {
        context = getContext();
        productPrefs = new ProductPrefs(context);
        identitySharedPref = new IdentitySharedPref(context);
        categoryPref = new CategoryPref(context);
        //firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        homeActivity = (HomeActivity)getActivity();

        love = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_love);
        money = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_money);
        success = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_success);
        beauty = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_beauty);
        english = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_english);
        health = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_health);
        sport = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_sport);
        food = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_food);
        meditation = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_meditation);
        mind = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_mind);
        lights = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_lights);
        book = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_book);
        art = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_art);
        guide = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_guide);
        metaphysic = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_metaphysic);
        gift = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_gift);
        virtual = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_virtual);
        management = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_management);
        education = (LinearLayout) dashboardRoot.findViewById(R.id.dashboard_education);
        fab = (FloatingActionButton) dashboardRoot.findViewById(R.id.dashboard_float_action_btn);
        walletFab = (FloatingActionButton) dashboardRoot.findViewById(R.id.dashboard_wallet_float_action_btn);

        audioBooks = dashboardRoot.findViewById(R.id.dashboard_audioBook);
        sharing = dashboardRoot.findViewById(R.id.dashboard_sharing);
        videoCategories = dashboardRoot.findViewById(R.id.dashboard_video_categories);
        testCategories = dashboardRoot.findViewById(R.id.dashboard_test_categories);

        if(isOnline()) {
            Log.i(TAG, "SetupViews: is online");
            dashboardAPIService = new DashboardAPIService(context);
            slider = dashboardRoot.findViewById(R.id.banner_slider1);
            MenuItemController();
            GetPopulars();
            GetCategories();
            GetBanners();
        }

        sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeActivity.CheckRegistration()) {
                    Intent intent = new Intent(context, SharingActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void GetPopulars()
    {
        totalMultiViewModelList = new ArrayList<>();


        dashboardAPIService.GetPopularProducts(new DashboardAPIService.onGetPopularProducts() {
            @Override
            public void onGet(boolean msg, List<ProductModel> popularProducts) {



                if(msg && popularProducts.size()>0)
                {

                    for (int i = 0 ; i < popularProducts.size() ; i ++)
                    {

                        switch (popularProducts.get(i).getImage())
                        {
                            case "money":
                            {
                                categoryPref.set_money(popularProducts.get(i).getProduct_id());
                                break;
                            }
                            case "love":
                            {
                                categoryPref.set_love(popularProducts.get(i).getProduct_id());
                                break;
                            }
                            case "success":
                            {
                                categoryPref.set_success(popularProducts.get(i).getProduct_id());
                                break;
                            }
                            case "beauty":
                            {
                                categoryPref.set_beauty(popularProducts.get(i).getProduct_id());
                                break;
                            }
                            case "english":
                            {
                                categoryPref.set_endglish(popularProducts.get(i).getProduct_id());
                                break;
                            }

                        }
                    }
                }
                else {
                    Log.i(TAG, "onGet: list is empty");
                }

            }
        });
    }

    private void GetCategories()
    {

        dashboardAPIService.GetCategories(new DashboardAPIService.onGetCategories() {
            @Override
            public void onGet(boolean msg, List<ProductModel> categoryList) {

                if(msg && categoryList.size()>0)
                {
                    for (int i = 0 ; i < categoryList.size() ; i ++)
                    {
                        switch (categoryList.get(i).getImage())
                        {
                            case "health":
                            {
                                categoryPref.set_health(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "sport":
                            {
                                categoryPref.set_sport(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "food":
                            {
                                categoryPref.set_food(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "meditation":
                            {
                                categoryPref.set_meditation(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "mind":
                            {
                                categoryPref.set_mind(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "lights":
                            {
                                categoryPref.set_lights(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "books":
                            {
                                categoryPref.set_book(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "art":
                            {
                                categoryPref.set_art(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "guide":
                            {
                                categoryPref.set_guide(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "metaphysic":
                            {
                                categoryPref.set_metaphysic(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "gift":
                            {
                                categoryPref.set_gift(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "virtual":
                            {
                                categoryPref.set_virtual(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "management":
                            {
                                categoryPref.set_management(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "education":
                            {
                                categoryPref.set_education(categoryList.get(i).getProduct_id());
                                break;
                            }
                            case "audiobooks":
                            {
                                categoryPref.set_audioBooks(categoryList.get(i).getProduct_id());
                                break;
                            }

                        }
                    }
//                    if(totalMultiViewModelList.size()<=2) {
//                        totalMultiViewModelList.add(new MultiDashboardModel(MultiDashboardModel.HEADER_LAYOUT, "دسته بندی محصولات"));
//                        totalMultiViewModelList.add(new MultiDashboardModel(MultiDashboardModel.CATEGORY_LAYOUT, categoryList));
//                    }
//                    Log.i(TAG, "onGet: product received"+totalMultiViewModelList.size());
                }

            }
        });
    }

    private String convertNumberToBalance(int balance) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(balance);
    }

    private void openDialogSupport() {
        ViewGroup viewGroup = dashboardRoot.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_soppurt_dialog, viewGroup, false);

        Button atisBtn = dialogView.findViewById(R.id.support_atis_btn);
        Button jazbeBtn = dialogView.findViewById(R.id.support_jazbe_btn);

        atisBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context,"برقراری ارتباط با پشتیبانی فنی نرم افزار از طریق تلگرام",Toast.LENGTH_LONG).show();
                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/atisapp_support"));
                startActivity(telegram);
            }
        });

        jazbeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context,"برقراری ارتباط با کارشناسان جذبه از طریق تلگرام",Toast.LENGTH_LONG).show();
                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/JAZ_B_E"));
                startActivity(telegram);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void openDialogWallet() {

        Bundle bundle = new Bundle();
        bundle.putString("UserEntered", "home");
        //firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, bundle);

        ViewGroup viewGroup = dashboardRoot.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_wallet, viewGroup, false);

        TextView balanceLabel = dialogView.findViewById(R.id.balanceLabel);
        final EditText balanceEditText = dialogView.findViewById(R.id.balanceEditText);
        final Button chargeButtonOne = dialogView.findViewById(R.id.chargeButtonOne);
        final Button chargeButtonTwo = dialogView.findViewById(R.id.chargeButtonTwo);
        final Button chargeButtonThree = dialogView.findViewById(R.id.chargeButtonThree);
        Button balanceChargeButton = dialogView.findViewById(R.id.balanceChargeButton);

        String balanceValue = "اعتبار فعلی شما : " + convertNumberToBalance(identitySharedPref.getWalletBalance()) + " تومان ";
        balanceLabel.setText(balanceValue);

        chargeButtonOne.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                //view.setBackground(getResources().getDrawable(R.drawable.selector_input_selected));
                //chargeButtonTwo.setBackground(getResources().getDrawable(R.drawable.selector_input));
                //chargeButtonThree.setBackground(getResources().getDrawable(R.drawable.selector_input));
                balanceEditText.setText(getString(R.string.wallet_charge_1));
            }
        });

        chargeButtonTwo.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                //view.setBackground(getResources().getDrawable(R.drawable.selector_input_selected));
                //chargeButtonOne.setBackground(getResources().getDrawable(R.drawable.selector_input));
                //chargeButtonThree.setBackground(getResources().getDrawable(R.drawable.selector_input));
                balanceEditText.setText(getString(R.string.wallet_charge_2));
            }
        });

        chargeButtonThree.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                //view.setBackground(getResources().getDrawable(R.drawable.selector_input_selected));
                //chargeButtonTwo.setBackground(getResources().getDrawable(R.drawable.selector_input));
                //chargeButtonOne.setBackground(getResources().getDrawable(R.drawable.selector_input));
                balanceEditText.setText(getString(R.string.wallet_charge_3));
            }
        });

        balanceChargeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String balance = balanceEditText.getText().toString().replaceAll(",", "");
                if (!TextUtils.isEmpty(balance)) {
                    hideKeyboard(balanceEditText);
                    walletBalancePay(balance);
                } else {
                    Toast.makeText(context, "لطفا مقدار اعتبار خود را وارد نمایید", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void hideKeyboard(EditText myEditText) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
        }
    }

    private void walletBalancePay(String balance) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال اتصال به درگاه");
        progressDialog.show();

        walletApiService = new WalletApiService(context);
        walletApiService.ChargeWallet(balance, new WalletApiService.OnPayListener() {

            @Override
            public void onPay(String url) {
                progressDialog.dismiss();
                openBrowser(url);
            }

            @Override
            public void onError(int statusCode) {
                progressDialog.dismiss();
            }
        });
    }

    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void GetBanners()
    {
        dashboardAPIService.GetBanners(new DashboardAPIService.onGetBanners() {
            @Override
            public void onGet(boolean msg, List<ProductModel> bannerList) {
                myBannerList = bannerList;
                SetupSlide();
            }
        });
    }

    private void SetupSlide() {
        if (myBannerList.size() > 0) {
            Slider.init(new PicassoImageLoadingService(context));
            slider.postDelayed(new Runnable() {
                @Override
                public void run() {

                    slider.setAdapter(new MainSliderAdapter(context));
                    slider.setSelectedSlide(0);
                    slider.setLoopSlides(true);
                    slider.setInterval(5000);
                    slider.setAnimateIndicators(true);
                    slider.setOnSlideClickListener(new OnSlideClickListener() {
                        @Override
                        public void onSlideClick(int position) {
                            Log.i(TAG, "onSlideClick: " + position);
                            openBrowser(myBannerList.get(position).getPodcastUrl());
                        }
                    });

                }
            }, 1500);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
            return true;
        }
        else
        {
            /*Intent intent = new Intent(getContext(), SplashActivity.class);
            getContext().startActivity(intent);
            getActivity().finish();
            Log.i(TAG, "isOnline: offline");*/
        }
        return false;
    }

    private void MenuItemController()
    {

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openDialogSupport();
            }
        });

        walletFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(homeActivity.CheckRegistration()) {
                    openDialogWallet();
                }
            }
        });

        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getMoneyKey());
            }
        });

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getLoveKey());

            }
        });

        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getSuccessKey());


            }
        });

        beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getBeautyKey());

            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getEnglishKey());

            }
        });

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getHealthKey());


            }
        });

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getSportKey());


            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getFoodKey());


            }
        });

        meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getMeditationKey());

            }
        });

        mind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getMindKey());

            }
        });

        lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getLightsKey());


            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getBookKey());


            }
        });

        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getArtKey());

            }
        });

        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getGuideKey());

            }
        });

        metaphysic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getMetaphysicKey());


            }
        });

        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getGiftKey());

            }
        });

        virtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getVirtualKey());


            }
        });

        management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getManagementKey());


            }
        });

        education.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ManagerOnClick(categoryPref.getEducationKey());

        }
    });

        audioBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManagerOnClick(categoryPref.getAudiobooksKey());

            }
        });

        videoCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, VideoCategoryActivity.class);
                startActivity(intent);

            }
        });

        testCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TestListActivity.class);
                startActivity(intent);

            }
        });

    }

    private void ManagerOnClick(String id)
    {

        productPrefs.set_product_group_id(id);
        Intent intent = new Intent(context, ProductListActivity.class);
        startActivity(intent);

    }

}