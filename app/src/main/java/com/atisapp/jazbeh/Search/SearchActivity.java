package com.atisapp.jazbeh.Search;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    private Context context;
    private Toolbar searchToolbar;
    private EditText searchEditText;
    private TextView searchButton;
    private RecyclerView searchRecyclerView;
    private ProgressDialog progressDialog;
    private CourseAPIService courseAPIService;
    private EpisodeAPIService episodeAPIService;
    private List<MultiViewModel> totalMultiViewModelList;
    private int courseSize,episodeSize;
    private LinearLayout emptyView;
    private MultiViewAdapter multiViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SetupView();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline() && searchEditText.getText().length()>0)
                {
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
                        progressDialog.show();
                    }
                    GetCoursesFromServer();
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(progressDialog!= null){
            progressDialog.dismiss();
        }
    }

    private void SetupView()
    {
        context = SearchActivity.this;
        courseAPIService = new CourseAPIService(context);
        episodeAPIService = new EpisodeAPIService(context);
        emptyView = findViewById(R.id.search_empty_list_view);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.sreachButton);
        searchRecyclerView = findViewById(R.id.search_activity_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchRecyclerView.setLayoutManager(layoutManager);


        setupToolBar();
    }

    private void GetCoursesFromServer()
    {
        Log.i(TAG, "GetCoursesFromServer: "+searchEditText.getText().toString());
        totalMultiViewModelList = new ArrayList<>();
        courseAPIService.GetCoursesWithSearchKey(searchEditText.getText().toString(), new CourseAPIService.onGetCoursesSearch() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_courses) {

                Log.i(TAG, "onGet: "+list_courses.size());
                courseSize = list_courses.size();

                if(msg && list_courses.size()>0)
                {
                    if(totalMultiViewModelList.isEmpty()) {
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "دوره ها"));
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.COURSE_LIST_LAYOUT, list_courses,true));
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
        episodeAPIService.GetEpisodesWithSearchKey(searchEditText.getText().toString(), new EpisodeAPIService.onGetSearchedEpisodes() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_episodes) {
                if(msg && list_episodes.size()>0)
                {
                    if(courseSize > 0 && totalMultiViewModelList.size()<=2) {
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "محصولات"));
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.EPISODE_LIST_LAYOUT, list_episodes,true));
                    }else if(courseSize == 0 && totalMultiViewModelList.size()==0)
                    {
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "محصولات"));
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.EPISODE_LIST_LAYOUT, list_episodes,true));
                    }
                    Log.i(TAG, "onGet: product received"+totalMultiViewModelList.size());
                }


                if(totalMultiViewModelList.size()>0) {
                    emptyView.setVisibility(View.GONE);
                    multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList,context);
                    searchRecyclerView.setAdapter(multiViewAdapter);
                    multiViewAdapter.notifyDataSetChanged();
                }
                else {
                    multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList,context);
                    searchRecyclerView.setAdapter(multiViewAdapter);
                    multiViewAdapter.notifyDataSetChanged();
                    emptyView.setVisibility(View.VISIBLE);
                }
                if(progressDialog!= null){
                    progressDialog.dismiss();
                }
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void setupToolBar()
    {
        searchToolbar     = (Toolbar) findViewById(R.id.search_toolBar);
        setSupportActionBar(searchToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        searchToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        searchToolbar.setTitle("");

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
