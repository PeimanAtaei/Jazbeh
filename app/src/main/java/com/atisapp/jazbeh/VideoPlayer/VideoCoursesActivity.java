package com.atisapp.jazbeh.VideoPlayer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.MainFragment.ui.home.WalletApiService;
import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

import java.util.ArrayList;
import java.util.List;

public class VideoCoursesActivity extends AppCompatActivity {

    private static final String TAG = "VideoCoursesActivity";
    private Context context;
    private CourseAPIService courseAPIService;
    private EpisodeAPIService episodeAPIService;
    private List<VideoMultiViewModel> totalMultiViewModelList;
    private VideoMultiViewAdapter multiViewAdapter;
    private WalletApiService walletApiService;
    private IdentitySharedPref identitySharedPref;

    private RecyclerView episodeListView;
    private String courseID;
    private Toolbar courseListToolbar;
    private ProgressDialog progressDialog;
    private boolean isPaidCourse;
    private int myBalance = 0 , coursePrice;
    private int episodeSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_courses);

        SetupView();
    }

    private void SetupView()
    {
        context = VideoCoursesActivity.this;
        walletApiService = new WalletApiService(context);
        courseAPIService = new CourseAPIService(context);
        episodeAPIService = new EpisodeAPIService(context);
        identitySharedPref = new IdentitySharedPref(context);


        episodeListView = findViewById(R.id.video_course_activity_recyclerView);

        Intent intent = getIntent();
        courseID = intent.getExtras().getString("courseID");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        episodeListView.setLayoutManager(layoutManager);

        setupToolBar();
        GetCourseInformation();
        if(identitySharedPref.getToken().length()>0 || identitySharedPref.getIsRegistered() == 1)
        {
            showBalance();
        }
    }

    public void GetCourseInformation()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
            progressDialog.show();
        }
        totalMultiViewModelList = new ArrayList<>();
        courseAPIService.GetSingleCourses(courseID, new CourseAPIService.onGetSingleCourses() {
            @Override
            public void onGet(boolean msg, ProductModel single_courses) {
                if(msg && single_courses!= null)
                {

                    isPaidCourse = single_courses.isPayCourse();
                    coursePrice = single_courses.getPrice();

                    if(totalMultiViewModelList.isEmpty()) {
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_COURSE_HEADER_LAYOUT,single_courses));
                    }
                }

                GetEpisodesWithCourseId();
            }
        });
    }

    private void GetEpisodesWithCourseId()
    {
        if (totalMultiViewModelList.isEmpty())
        {
            GetCourseInformation();
        }

        episodeAPIService.GetEpisodesWithCourseID(courseID, new EpisodeAPIService.onGetEpisodesWithCourse() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_episodes) {

                episodeSize = list_episodes.size();
                if(msg && list_episodes.size()>0)
                {
                    totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_HEADER_LAYOUT, "لیست ویدئو این دوره"));
                    totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_EPISODE_LIST_LAYOUT, list_episodes,isPaidCourse));
                    //Log.i(TAG, "onGet: product received"+totalMultiViewModelList.size());
                }

                multiViewAdapter = new VideoMultiViewAdapter(totalMultiViewModelList,context);
                episodeListView.setAdapter(multiViewAdapter);
                multiViewAdapter.notifyDataSetChanged();
            }
        });

        if(progressDialog != null)
            progressDialog.dismiss();

    }

    private void showBalance() {
        walletApiService = new WalletApiService(getApplicationContext());
        walletApiService.showBalance(new WalletApiService.OnGetBalanceListener() {

            @Override
            public void onBalance(int balance) {
                identitySharedPref.setWalletBalance(balance);
                myBalance = balance;

            }
        });
    }

    private void setupToolBar()
    {
        courseListToolbar     = (Toolbar) findViewById(R.id.video_course_list_toolBar);
        setSupportActionBar(courseListToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        courseListToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        courseListToolbar.setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
