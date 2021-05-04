package com.atisapp.jazbeh.Delegate.CheckOut;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atisapp.jazbeh.Delegate.DelegateAPIService;

import com.atisapp.jazbeh.R;

import java.util.List;

public class CheckOutActivity extends AppCompatActivity {

    private static final String TAG = "CheckOutActivity";
    private Context context;
    private DelegateAPIService delegateAPIService;
    private RecyclerView checkRecyclerView;
    private CheckOutAdapter checkOutAdapter;
    private Button checkRequestBtn;
    private ProgressDialog progressDialog;
    private LinearLayout emptyView;
    private boolean isChecking = true;
    private Toolbar checkListToolbar;
    private int currentBalance = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        SetupViews();

        checkRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: "+isChecking);

                if(currentBalance<50000) {
                    openDialogCheckOut("همکار عزیز طبق قوانین مشارکت در فروش جذبه حداقل موجودی شما برای ثبت درخواست واریز باید 50 هزار تومان باشد");
                }else if(!isChecking)
                {
                    openDialogCheckOut("همکار عزیز شما درخواست واریز خود را ثبت نموده اید و باید تا هفته اول ماه پیش رو صبر نمایید تا درخواست شما تایید و پورسانت شما واریز گردد");
                }else {
                    SendCheckOutRequest();
                }
            }
        });
    }

    private void SetupViews()
    {
        context = CheckOutActivity.this;
        delegateAPIService = new DelegateAPIService(context);
        checkRecyclerView = findViewById(R.id.checkout_recyclerView);
        checkRequestBtn = findViewById(R.id.check_request_btn);
        emptyView = findViewById(R.id.check_empty_list_view);

        Intent intent = getIntent();
        currentBalance = intent.getExtras().getInt("balance");
        Log.i(TAG, "SetupViews: "+currentBalance);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        checkRecyclerView.setLayoutManager(layoutManager);

        setupToolBar();
        GetCheckOuts();
    }

    private void GetCheckOuts()
    {

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت لیست تسویه حساب ها");
            progressDialog.show();



        delegateAPIService.GetCheckOuts(new DelegateAPIService.onGetCheckOuts() {
            @Override
            public void onGet(boolean inChecking, List<CheckOutModel> checkOutModels) {
                isChecking = inChecking;

                if(checkOutModels!=null || !checkOutModels.isEmpty())
                {
                    checkOutAdapter = new CheckOutAdapter(context,checkOutModels);
                    checkRecyclerView.setAdapter(checkOutAdapter);
                    checkOutAdapter.notifyDataSetChanged();
                }

                else
                {
                    emptyView.setVisibility(View.VISIBLE);
                }

                if(progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }

    private  void SendCheckOutRequest()
    {
        delegateAPIService.CreateCheckOuts(new DelegateAPIService.onCreateCheckOuts() {
            @Override
            public void onGet(boolean msg) {
                if(msg)
                {
                    GetCheckOuts();
                    openDialogCheckOut("همکار گرامی درخواست تسویه حساب شما در سیستم ثبت گردید و در هفته اول ماه پیش رو تایید و به حساب شما واریز خواهد شد");
                }else {
                    Toast.makeText(context,"مشکل در برقراری ارتباط با سرور ، لطفا دوباره تلاش کنید",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openDialogCheckOut(String dialogText) {
        Log.i(TAG, "onGet: dialog");
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_create_check_dialog, viewGroup, false);

        Button      commentDialogBtn               = dialogView.findViewById(R.id.check_sent_btn);
        TextView    commentDialogText              = dialogView.findViewById(R.id.check_dialog_text);

        commentDialogText.setText(dialogText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        final AlertDialog updateDialog = builder.create();
        updateDialog.show();

        commentDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
            }
        });
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);



    }

    private void setupToolBar()
    {
        checkListToolbar     = (Toolbar) findViewById(R.id.check_list_toolBar);
        setSupportActionBar(checkListToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        checkListToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        checkListToolbar.setTitle("");

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
