package com.atisapp.jazbeh.PsycologicalTests;

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

import com.atisapp.jazbeh.PsycologicalTests.model.TestModel;
import com.atisapp.jazbeh.R;

import java.util.List;

public class TestListActivity extends AppCompatActivity {

    private static final String TAG = "TestListActivity";
    private Context context;
    private TestAPIService testAPIService;
    private List<TestModel> testModelList;
    private TestListAdapter testListAdapter;

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private Toolbar productListToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        SetupViews();
    }

    private void SetupViews()
    {
        context = TestListActivity.this;
        testAPIService = new TestAPIService(context);
        recyclerView = findViewById(R.id.test_list_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        setupToolBar();
        GetTestsList();

    }

    private void GetTestsList()
    {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت لیست آزمون ها");
            progressDialog.show();
        }

        testAPIService.GetTestsList(new TestAPIService.onGetTestsList() {
            @Override
            public void onGet(boolean msg, List<TestModel> list_tests) {
                if(list_tests.size()>0)
                {
                    testModelList = list_tests;
                    testListAdapter = new TestListAdapter(context,testModelList);
                    recyclerView.setAdapter(testListAdapter);
                    testListAdapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(context,"آختلال در برقراری ارتباط با سرور ، لطفا دوباره تلاش کنید",Toast.LENGTH_SHORT).show();
                }
                if(progressDialog != null)
                    progressDialog.dismiss();

            }
        });
    }

    private void setupToolBar()
    {
        productListToolbar     = (Toolbar) findViewById(R.id.test_list_toolBar);
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
}
