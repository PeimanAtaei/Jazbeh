package com.atisapp.jazbeh.Delegate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atisapp.jazbeh.MainFragment.FeatureAdapter;
import com.atisapp.jazbeh.MainFragment.UpdateModel;
import com.atisapp.jazbeh.R;

import java.util.List;

public class FormActivity extends AppCompatActivity {

    private static final String TAG = "FormActivity";
    private Context context;
    private DelegateAPIService delegateAPIService;
    private DelegateModel delegateModel;
    private TextView name,city,education,cardNumber,collaboration;
    private Button rulesBtn,sendBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        SetupViews();

        rulesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RulesActivity.class);
                startActivity(intent);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length()>0 && city.getText().length()>0 && education.getText().length()>0 && cardNumber.getText().length()>0 && collaboration.getText().length()>0)
                {
                    CreateNewDelegate();
                }else {
                    Toast.makeText(context,"تمام اطلاعات فرم را تکمیل نمایید",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void SetupViews()
    {
        context = FormActivity.this;
        delegateAPIService = new DelegateAPIService(context);
        name = findViewById(R.id.delegate_form_fullName_edit);
        city = findViewById(R.id.delegate_form_city_edit);
        education = findViewById(R.id.delegate_form_education_edit);
        cardNumber = findViewById(R.id.delegate_form_card_edit);
        collaboration = findViewById(R.id.delegate_form_channel_edit);
        rulesBtn = findViewById(R.id.delegate_form_rules_btn);
        sendBtn = findViewById(R.id.delegate_form_send_btn);

    }

    private void CreateNewDelegate()
    {
        delegateModel = new DelegateModel();

        delegateModel.setName(name.getText().toString());
        delegateModel.setCity(city.getText().toString());
        delegateModel.setEducation(education.getText().toString());
        delegateModel.setCardNumber(cardNumber.getText().toString());
        delegateModel.setCollaborate(collaboration.getText().toString());

        delegateAPIService.CreateNewDelegate(delegateModel, new DelegateAPIService.onCreateNewDelegate() {
            @Override
            public void onGet(boolean response) {
                if(response)
                {
                    openDialogForm();
                }
            }
        });
    }

    private void openDialogForm() {
        Log.i(TAG, "onGet: dialog");
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_create_delegate_dialog, viewGroup, false);

        Button      formDialogBtn               = dialogView.findViewById(R.id.form_dialog_btn);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);
        final AlertDialog updateDialog = builder.create();
        updateDialog.show();

        formDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
            }
        });
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);



    }
}
