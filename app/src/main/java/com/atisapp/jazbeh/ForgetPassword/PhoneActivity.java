package com.atisapp.jazbeh.ForgetPassword;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

public class PhoneActivity extends AppCompatActivity {

    private static final String TAG = "PhoneActivity";
    private Context context;
    private EditText forget_phoneNumber_edit;
    private Button forget_next_btn;
    private ForgetApiService forgetApiService;
    private IdentitySharedPref identitySharedPref;
    private TextView supportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        SetupVIew();


        forget_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(forget_phoneNumber_edit.getText().length()<11)
                {
                    Toast.makeText(context,"شماره موبایل خود را به شکل صحیح وارد نمایید",Toast.LENGTH_LONG).show();
                }
                else {
                    forgetApiService.SendPhoneNumber(forget_phoneNumber_edit.getText().toString(),new ForgetApiService.onSetPhoneNumber() {
                        @Override
                        public void onSet(boolean login_response) {
                            if(login_response == true)
                            {
                                identitySharedPref.setPhoneNumber(forget_phoneNumber_edit.getText().toString());
                                Intent intent = new Intent(PhoneActivity.this, VerifyActivity.class);
                                startActivity(intent);
                                Toast.makeText(context,"کد تایید برای موبایل شما پیامک گردید",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

        supportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogSupport();
            }
        });


    }


    @SuppressLint("WrongViewCast")
    private void SetupVIew()
    {
        context = PhoneActivity.this;
        forget_phoneNumber_edit = (EditText) findViewById(R.id.forget_phone_edit_text);
        forget_next_btn  = (Button) findViewById(R.id.forget_next_btn);
        forgetApiService = new ForgetApiService(context);
        identitySharedPref = new IdentitySharedPref(context);
        supportText = findViewById(R.id.phone_support_text);
        supportText.setPaintFlags(supportText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void openDialogSupport() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_soppurt_dialog, viewGroup, false);

        Button atisBtn = dialogView.findViewById(R.id.support_atis_btn);
        Button jazbeBtn = dialogView.findViewById(R.id.support_jazbe_btn);

        atisBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context,"برقراری ارتباط با پشتیبانی فنی نرم افزار از طریق تلگرام",Toast.LENGTH_LONG).show();
                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/atisapp_support"));
                startActivity(telegram);
            }
        });

        jazbeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context,"برقراری ارتباط با کارشناسان جذبه از طریق تلگرام",Toast.LENGTH_LONG).show();
                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/JAZ_B_E"));
                startActivity(telegram);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
