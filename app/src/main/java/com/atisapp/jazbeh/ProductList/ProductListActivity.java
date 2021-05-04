package com.atisapp.jazbeh.ProductList;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.LinearLayout;
import android.widget.Toast;
import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Courses.CourseListAdapter;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;
import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private static final String TAG = "ProductListActivity";
    private Context context;
    private RecyclerView productRecyclerView;
    private MultiViewAdapter multiViewAdapter;
    private EpisodeAPIService episodeAPIService;
    private CourseAPIService courseAPIService;
    private ProductPrefs productPrefs;
    private int courseSize;
    private List<MultiViewModel> totalMultiViewModelList;
    private Toolbar productListToolbar;
    private ProgressDialog progressDialog;
    private LinearLayout emptyView;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SetupViews();

        if(isOnline())
        {
            GetCoursesFromServer();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        SetupViews();

        if(isOnline()) {
            GetCoursesFromServer();
        }
    }

    private void SetupViews()
    {
        context = ProductListActivity.this;

        productRecyclerView =  findViewById(R.id.course_recyclerView);
        courseAPIService = new CourseAPIService(context);
        episodeAPIService = new EpisodeAPIService(context);
        productPrefs = new ProductPrefs(context);
        emptyView = findViewById(R.id.product_empty_list_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        productRecyclerView.setLayoutManager(layoutManager);

        setupToolBar();
    }

    private void GetCoursesFromServer()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
            progressDialog.show();
        }

        totalMultiViewModelList = new ArrayList<>();

        Log.i(TAG, "getProductsFromServer: start getting courses -- category id : "+productPrefs.get_product_group_id());
        courseAPIService.GetCoursesWithCategoryID(productPrefs.get_product_group_id(), new CourseAPIService.onGetCourses() {
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
        episodeAPIService.GetEpisodesWithCategoryID(productPrefs.get_product_group_id(), new EpisodeAPIService.onGetEpisodes() {
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
                    productRecyclerView.setAdapter(multiViewAdapter);
                    multiViewAdapter.notifyDataSetChanged();
                    if (progressDialog != null) progressDialog.hide();
                }
                else {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        });

        if(progressDialog != null)
            progressDialog.dismiss();

    }

    private void setupToolBar()
    {
        productListToolbar     = (Toolbar) findViewById(R.id.product_list_toolBar);
        setSupportActionBar(productListToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        productListToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white_trans));
        productListToolbar.setTitle("");

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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        else
        {
            Toast.makeText(context,"اتصال به اینترنت خود را بررسی نمایید",Toast.LENGTH_LONG).show();
        }
        return false;
    }



}
