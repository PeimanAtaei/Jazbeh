package com.atisapp.jazbeh.Login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atisapp.jazbeh.MainFragment.HomeActivity;
import com.atisapp.jazbeh.Questionnaire.QuestionActivity;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
//import com.google.firebase.analytics.FirebaseAnalytics;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Context context;
    private EditText register_phoneNumber_edit,register_password_edit;
    private Button register_next_btn;
    private RegisterApiService registerApiService;
    private IdentitySharedPref identitySharedPref;
    private TextView hasAccountTxt;
    //private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SetupVIew();

//        if(identitySharedPref.getIsRegistered() == 1)
//        {
//            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
//            Log.i(TAG, "onCreate: enter");
//
//            Bundle bundle = new Bundle();
//            bundle.putString("UserEntered", "Login");
//            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
//
//            startActivity(intent);
//            finish();
//        }

        register_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(register_phoneNumber_edit.getText().toString().equals("") && register_password_edit.getText().toString().equals(""))
                {
                    Toast.makeText(context,"اطلاعات اکانت را به ضورت کامل وارد نمایید",Toast.LENGTH_LONG).show();
                }
                else if(register_phoneNumber_edit.getText().length()<11)
                {
                    Toast.makeText(context,"شماره موبایل خود را به شکل صحیح وارد نمایید",Toast.LENGTH_LONG).show();
                }
                else if(register_password_edit.getText().length()<6)
                {
                    Toast.makeText(context,"رمز عبور باید حداقل 6 کاراکتر داشته باشد",Toast.LENGTH_LONG).show();
                }
                else {
                    registerApiService.RegisterUser(register_phoneNumber_edit.getText().toString(),register_password_edit.getText().toString() , new RegisterApiService.onRegisterUser() {
                        @Override
                        public void onRegister(boolean login_response) {
                            if(login_response == true)
                            {
                                identitySharedPref.setPhoneNumber(register_phoneNumber_edit.getText().toString());
                                identitySharedPref.setPassword(register_password_edit.getText().toString());
                                identitySharedPref.setIsRegistered(1);

                                Intent intent = new Intent(RegisterActivity.this, QuestionActivity.class);
                                startActivity(intent);

                                Bundle bundle = new Bundle();
                                bundle.putString("UserEntered", "Register");
                                //firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);

                                Toast.makeText(context,"ثبت نام با موفقیت انجام شد",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

        hasAccountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void SetupVIew()
    {
        context = RegisterActivity.this;
        register_phoneNumber_edit = (EditText) findViewById(R.id.register_phone_number_edit_text);
        register_password_edit = (EditText) findViewById(R.id.register_password_edit_text);
        register_next_btn  = (Button) findViewById(R.id.register_next_btn);
        registerApiService = new RegisterApiService(context);
        identitySharedPref = new IdentitySharedPref(context);
        hasAccountTxt = (TextView)findViewById(R.id.register_has_account_text);
        hasAccountTxt.setPaintFlags(hasAccountTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
    }


}
