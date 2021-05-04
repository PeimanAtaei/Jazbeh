package com.atisapp.jazbeh.Courses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.atisapp.jazbeh.MainFragment.ui.home.WalletApiService;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.Episodes.EpisodeAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import java.util.ArrayList;
import java.util.List;

public class CoursesActivity extends AppCompatActivity {

    private static final String TAG = "CoursesActivity";
    private WalletApiService walletApiService;
    private IdentitySharedPref identitySharedPref;
    private Context context;
    private TextView courseTitle,courseDescription,courseTime,courseViewCount;
    private RecyclerView episodeListView;
    private EpisodeAdapter episodeAdapter;
    private EpisodeAPIService episodeAPIService;
    private CourseAPIService courseAPIService;
    private String courseID;
    private List<MultiViewModel> totalMultiViewModelList;
    private MultiViewAdapter multiViewAdapter;
    private boolean isPaidCourse;
    private Button PayBtn;
    private int myBalance = 0 , coursePrice;
    private Toolbar courseListToolbar;
    private ProgressDialog progressDialog;
    private int episodeSize = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Intent intent = getIntent();
        courseID = intent.getExtras().getString("courseID");

        SetupView();
    }

    public void SetupView()
    {

        //Log.i(TAG, "SetupView: cours id "+courseID);

        context = CoursesActivity.this;
        courseTitle = (TextView)findViewById(R.id.course_card_title);
        courseDescription = (TextView)findViewById(R.id.course_card_description);
        courseTime = (TextView)findViewById(R.id.course_card_time);
        courseViewCount = (TextView)findViewById(R.id.course_card_view_count);
        episodeListView = (RecyclerView) findViewById(R.id.course_activity_recyclerView);
        identitySharedPref = new IdentitySharedPref(context);
        courseAPIService = new CourseAPIService(context);
        episodeAPIService = new EpisodeAPIService(context);
        episodeAdapter = new EpisodeAdapter(context);
        walletApiService = new WalletApiService(context);

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
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.COURSE_HEADER_LAYOUT,single_courses));
                    }
                    //Log.i(TAG, "onGet: course received"+totalMultiViewModelList.size());
//                    multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList,context);
//                    episodeListView.setAdapter(multiViewAdapter);
//                    multiViewAdapter.notifyDataSetChanged();

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
                    totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "لیست محصولات این دوره"));
                    totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.EPISODE_LIST_LAYOUT, list_episodes,isPaidCourse));
                    //Log.i(TAG, "onGet: product received"+totalMultiViewModelList.size());
                }

                multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList,context);
                episodeListView.setAdapter(multiViewAdapter);
                multiViewAdapter.notifyDataSetChanged();
            }
        });

//        if(episodeSize>0 && totalMultiViewModelList.size() == 1)
//        {
//            GetEpisodesWithCourseId();
//        }
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
        courseListToolbar     = (Toolbar) findViewById(R.id.course_list_toolBar);
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
