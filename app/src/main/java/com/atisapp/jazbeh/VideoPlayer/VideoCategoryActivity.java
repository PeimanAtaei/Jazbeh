package com.atisapp.jazbeh.VideoPlayer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.atisapp.jazbeh.R;

import java.util.List;

public class VideoCategoryActivity extends AppCompatActivity {

    private static final String TAG = "VideoCategoryActivity";
    private Context context;
    private VideoAPIService videoAPIService;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private List<VideoCategoryModel> categoryModelList;
    private Toolbar categoryToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_category);

        SetupViews();
    }

    private void SetupViews()
    {
        context = VideoCategoryActivity.this;
        videoAPIService = new VideoAPIService(context);
        recyclerView = findViewById(R.id.video_category_activity_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        setupToolBar();
        GetVideoCategory();

    }

    private void GetVideoCategory()
    {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت لیست محصولات");
            progressDialog.show();
        }

        videoAPIService.GetCategories(new VideoAPIService.onGetVideoCategories() {
            @Override
            public void onGet(boolean msg, List<VideoCategoryModel> list_category) {

                if(msg && list_category!=null)
                {
                    categoryModelList = list_category;
                    categoryAdapter = new CategoryAdapter(context,list_category);
                    recyclerView.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                }

                if(list_category == null || list_category.isEmpty())
                {
                    Toast.makeText(context,"دسته بندی یافت نشد",Toast.LENGTH_SHORT).show();
                }

                if(progressDialog != null)
                    progressDialog.dismiss();

            }
        });
    }

    private void setupToolBar()
    {
        categoryToolbar     = (Toolbar) findViewById(R.id.category_toolBar);
        setSupportActionBar(categoryToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        categoryToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        categoryToolbar.setTitle("");

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
