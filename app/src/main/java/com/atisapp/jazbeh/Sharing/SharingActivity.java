package com.atisapp.jazbeh.Sharing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.R;

import java.util.ArrayList;
import java.util.List;

public class SharingActivity extends AppCompatActivity {

    private static final String TAG = "SharingActivity";
    private Context context;
    private SharingAPIService sharingAPIService;
    private List<SharingMultiViewModel> totalMultiViewModelList;
    private SharingMultiViewAdapter multiViewAdapter;
    private SharingAdapter sharingAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private Toolbar sharingToolbar;
    private String shareLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        SetupViews();



    }

    private void SetupViews()
    {
        context = SharingActivity.this;
        recyclerView = findViewById(R.id.sharing_activity_recyclerView);
        sharingAPIService = new SharingAPIService(context);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        setupToolBar();
        GetInvitationLink();
    }

    private void GetInvitationLink()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات");
            progressDialog.show();
        }

        totalMultiViewModelList = new ArrayList<>();

        sharingAPIService.ReceiveShareInfo(new SharingAPIService.onGetShareLink() {
            @Override
            public void onGet(boolean response, String link) {
                if(response)
                {
                    shareLink = link;
                    Log.i(TAG, "onGet: "+shareLink);
                    if(totalMultiViewModelList.isEmpty()) {
                        totalMultiViewModelList.add(new SharingMultiViewModel(SharingMultiViewModel.SHARE_HEADER_LAYOUT,shareLink,""));
                        GetApplications();
                    }
                }

            }
        });
    }

    private void GetApplications()
    {

        totalMultiViewModelList.add(new SharingMultiViewModel(SharingMultiViewModel.HEADER_LAYOUT, "لینک خود را با دوستان به اشتراک بگزارید"));

        PackageManager pm = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_SEND, null);
        mainIntent.setType("text/plain");
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);// returns all applications which can listen to the SEND Intent

        List<SharingModel> sharingModels = new ArrayList<>();

        for (ResolveInfo info : resolveInfos) {
            ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
            Log.i(TAG, "GetApplications: "+applicationInfo.loadIcon(pm).toString());

            SharingModel sharingModel = new SharingModel();
            sharingModel.setIcon(applicationInfo.loadIcon(pm));
            sharingModel.setTitle(applicationInfo.loadLabel(pm).toString());
            sharingModel.setPackageName(applicationInfo.packageName);
            sharingModels.add(sharingModel);

            //get package name, icon and label from applicationInfo object and display it in your custom layout

            //App icon = applicationInfo.loadIcon(pm);
            //App name  = applicationInfo.loadLabel(pm).toString();
            //App package name = applicationInfo.packageName;
        }


        totalMultiViewModelList.add(new SharingMultiViewModel(SharingMultiViewModel.APPS_LIST_LAYOUT, sharingModels));

        multiViewAdapter = new SharingMultiViewAdapter(totalMultiViewModelList,context);
        recyclerView.setAdapter(multiViewAdapter);
        multiViewAdapter.notifyDataSetChanged();

        progressDialog.dismiss();


    }

    private void setupToolBar()
    {
        sharingToolbar     = (Toolbar) findViewById(R.id.course_list_toolBar);
        setSupportActionBar(sharingToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        sharingToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        sharingToolbar.setTitle("");

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
