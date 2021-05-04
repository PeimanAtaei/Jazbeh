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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    private static final String TAG = "VideoListActivity";
    private Context context;
    private EpisodeAPIService episodeAPIService;
    private CourseAPIService courseAPIService;
    private List<VideoMultiViewModel> totalMultiViewModelList;
    private VideoMultiViewAdapter videoMultiViewAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayout emptyView;
    private String categoryId;
    private int courseSize;
    private Toolbar videoListToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        SetupViews();

    }

    private void SetupViews()
    {
        context = VideoListActivity.this;
        episodeAPIService = new EpisodeAPIService(context);
        courseAPIService = new CourseAPIService(context);

        emptyView = findViewById(R.id.video_empty_list_view);
        recyclerView = findViewById(R.id.video_list_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");

        setupToolBar();
        GetCoursesFromServer();

    }

    private void GetCoursesFromServer()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
            progressDialog.show();
        }

        totalMultiViewModelList = new ArrayList<>();

        Log.i(TAG, "getProductsFromServer: start getting courses -- category id : "+categoryId);
        courseAPIService.GetCoursesWithCategoryID(categoryId, new CourseAPIService.onGetCourses() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_courses) {

                Log.i(TAG, "onGet: "+list_courses.size());
                courseSize = list_courses.size();

                if(msg && list_courses.size()>0)
                {
                    if(totalMultiViewModelList.isEmpty()) {
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_HEADER_LAYOUT, "دوره ها"));
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_LIST_LAYOUT, list_courses,true));
                    }
                    Log.i(TAG, "onGet: course received"+totalMultiViewModelList.size());
//
                }
                else {
                    Log.i(TAG, "onGet: list is empty");
                }

                GetEpisodesFromServer();
            }
        });

    }

    private void GetEpisodesFromServer()
    {
        episodeAPIService.GetEpisodesWithCategoryID(categoryId, new EpisodeAPIService.onGetEpisodes() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_episodes) {
                if(msg && list_episodes.size()>0)
                {
                    if(courseSize > 0 && totalMultiViewModelList.size()<=2) {
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_HEADER_LAYOUT, "تک ویدئو ها"));
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_EPISODE_LIST_LAYOUT, list_episodes,true));
                    }else if(courseSize == 0 && totalMultiViewModelList.size()==0)
                    {
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_HEADER_LAYOUT, "تک ویدئو ها"));
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_EPISODE_LIST_LAYOUT, list_episodes,true));
                    }
                    Log.i(TAG, "onGet: product received"+totalMultiViewModelList.size());
                }


                if(totalMultiViewModelList.size()>0) {
                    emptyView.setVisibility(View.GONE);
                    videoMultiViewAdapter = new VideoMultiViewAdapter(totalMultiViewModelList,context);
                    recyclerView.setAdapter(videoMultiViewAdapter);
                    videoMultiViewAdapter.notifyDataSetChanged();
                    if (progressDialog != null) progressDialog.dismiss();
                }
                else {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        });

        if(progressDialog != null)
            progressDialog.dismiss();

    }

    // Setup toolbar -------------------------------------------------------------------------------

    private void setupToolBar()
    {
        videoListToolbar     = (Toolbar) findViewById(R.id.video_list_toolBar);
        setSupportActionBar(videoListToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        videoListToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        videoListToolbar.setTitle("");

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
