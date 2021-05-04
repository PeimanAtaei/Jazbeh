package com.atisapp.jazbeh.MusicPlayer;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.ProductList.PracticeModel;
import com.atisapp.jazbeh.R;

import java.util.ArrayList;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {

    private static final String TAG = "PracticeActivity";
    private Context context;
    private List<PracticeModel> practiceModelList;
    private List<MultiViewModel> totalPracticeMultiViewModelList;
    private List<PracticeModel> imageList,pdfList,voiceList;
    private MultiViewAdapter multiViewAdapter2;
    private RecyclerView practiceRecyclerView;
    private Toolbar practiceToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        SetupViews();

    }

    private void SetupViews()
    {
        context = PracticeActivity.this;
        practiceModelList = (List<PracticeModel>)getIntent().getSerializableExtra("practiceList") ;
        practiceRecyclerView = (RecyclerView) findViewById(R.id.practice_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        practiceRecyclerView.setLayoutManager(layoutManager);

        SetData();
        setupToolBar();
    }

    private void SetData()
    {
        imageList = new ArrayList<>();
        pdfList = new ArrayList<>();
        voiceList = new ArrayList<>();

        for (PracticeModel practiceModel :
                practiceModelList) {

            switch (practiceModel.getPracticeType())
            {
                case 0:
                {
                    Log.i(TAG, "SetData: add image");
                    imageList.add(practiceModel);
                    break;
                }
                case 1:
                {
                    Log.i(TAG, "SetData: add pdf");
                    pdfList.add(practiceModel);
                    break;
                }
                case 2:
                {
                    Log.i(TAG, "SetData: add voice");
                    voiceList.add(practiceModel);
                    break;
                }
            }
        }

        SetupPractices();
    }

    private void SetupPractices()
    {
        Log.i(TAG, "SetupPractices: ");
        totalPracticeMultiViewModelList = new ArrayList<>();
        if(totalPracticeMultiViewModelList.isEmpty()) {
            Log.i(TAG, "SetupPractices: start multi");
            totalPracticeMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "فایل های تصویری"));
            totalPracticeMultiViewModelList.add(new MultiViewModel(MultiViewModel.PRACTICE_IMAGE_LAYOUT, imageList));

            totalPracticeMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "فایل های متنی (PDF)"));
            totalPracticeMultiViewModelList.add(new MultiViewModel(MultiViewModel.PRACTICE_PDF_LAYOUT, pdfList));

            Log.i(TAG, "SetupPractices size : "+totalPracticeMultiViewModelList.size());
            multiViewAdapter2 = new MultiViewAdapter(totalPracticeMultiViewModelList,context);
            practiceRecyclerView.setAdapter(multiViewAdapter2);
            multiViewAdapter2.notifyDataSetChanged();
        }

    }

    private void setupToolBar()
    {
        practiceToolbar     = (Toolbar) findViewById(R.id.practice_toolBar);
        setSupportActionBar(practiceToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        practiceToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.trans));
        practiceToolbar.setTitle("");

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
