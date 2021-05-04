package com.atisapp.jazbeh.Offline;

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
import android.view.View;
import android.widget.LinearLayout;

import com.activeandroid.query.Select;
import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Courses.CourseListAdapter;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.ProductList.ProductListActivity;
import com.atisapp.jazbeh.ProductList.ProductListAdapter;
import com.atisapp.jazbeh.ProductList.ProductListApiService;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Database.Tables.OfflineTable;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.util.ArrayList;
import java.util.List;

public class OfflineListActivity extends AppCompatActivity {

    private static final String TAG = "OfflineListActivity";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private RecyclerView productRecyclerView;
    private MultiViewAdapter multiViewAdapter;
    private List<MultiViewModel> totalMultiViewModelList;
    private Toolbar productListToolbar;
    private ProgressDialog progressDialog;
    private List<OfflineTable> offlineModelList;
    private LinearLayout emptyView;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SetupViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_list);

        SetupViews();
    }

    private void SetupViews()
    {
        context = OfflineListActivity.this;

        productRecyclerView =  findViewById(R.id.offline_recyclerView);
        identitySharedPref = new IdentitySharedPref(context);
        emptyView = findViewById(R.id.offline_empty_list_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        productRecyclerView.setLayoutManager(layoutManager);

        setupToolBar();
        SetData();
    }

    private void SetData()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات");
            progressDialog.show();
        }

        totalMultiViewModelList = new ArrayList<>();

        offlineModelList = GetAll();
        if(offlineModelList.size()>0)
        {
            if(totalMultiViewModelList.isEmpty()) {
                totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "لیست محصولات"));
                totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.OFFLINE_PRODUCTS_LAYOUT, offlineModelList,""));
            }

            if(totalMultiViewModelList.size()>0) {
                emptyView.setVisibility(View.GONE);
                multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList,context);
                productRecyclerView.setAdapter(multiViewAdapter);
                multiViewAdapter.notifyDataSetChanged();
                if (progressDialog != null) progressDialog.hide();
            }
        }
        else {
            emptyView.setVisibility(View.VISIBLE);
        }
        progressDialog.dismiss();
    }

    public static List<OfflineTable> GetAll( ) {
        return new Select()
                .from(OfflineTable.class)
                //.where("product_id = ?", product_id)
                .orderBy("Id")
                .execute();
    }

    private void setupToolBar()
    {
        productListToolbar     = (Toolbar) findViewById(R.id.product_list_toolBar);
        setSupportActionBar(productListToolbar);

        productListToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white_trans));
        productListToolbar.setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
}
