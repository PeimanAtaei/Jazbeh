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

import com.atisapp.jazbeh.ForgetPassword.PhoneActivity;
import com.atisapp.jazbeh.MainFragment.HomeActivity;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
//import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Context     context;
    private EditText    login_phoneNumber_edit,login_password_edit;
    private Button      login_next_btn;
    private LoginApiService loginApiService;
    private IdentitySharedPref identitySharedPref;
    private TextView new_account_txt,forget_password_txt;
    //private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SetupVIew();

//        if(identitySharedPref.getIsRegistered() == 1)
//        {
//            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//            Log.i(TAG, "onCreate: enter");
//
//            Bundle bundle = new Bundle();
//            bundle.putString("UserEntered", "Login");
//            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
//
//            startActivity(intent);
//            finish();
//        }



        login_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(login_phoneNumber_edit.getText().equals("")
                        && login_password_edit.getText().equals(""))
                {
                    Toast.makeText(context,"اطلاعات اکانت را به ضورت کامل وارد نمایید",Toast.LENGTH_LONG).show();

                }
                else if(login_password_edit.getText().length()<4)
                {
                    Toast.makeText(context,"رمز عبور باید بیش از 4 کاراکتر داشته باشد",Toast.LENGTH_LONG).show();
                }
                else {
                    loginApiService.LoginUser(login_phoneNumber_edit.getText().toString(),login_password_edit.getText().toString(),new LoginApiService.onUserLogin() {
                        @Override
                        public void onLogin(boolean login_response) {

                            Log.i(TAG, "onLogin: "+login_response);
                            if(login_response == true)
                            {
                                identitySharedPref.setPhoneNumber(login_phoneNumber_edit.getText().toString());
                                identitySharedPref.setPassword(login_password_edit.getText().toString());
                                identitySharedPref.setIsRegistered(1);

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(context,"ورود با موفقیت انجام شد",Toast.LENGTH_LONG).show();
                                finish();

                                Bundle bundle = new Bundle();
                                bundle.putString("UserEntered", "Login");
                                //firebaseAnalytics.logEvent(FirebaseAnalytics.Event.JOIN_GROUP, bundle);
                            }
                        }
                    });
                }

            }
        });

        new_account_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this
                        , RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forget_password_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this
                        , PhoneActivity.class);
                startActivity(intent);
            }
        });


    }

    @SuppressLint("WrongViewCast")
    private void SetupVIew()
    {
        context = LoginActivity.this;
        login_phoneNumber_edit = (EditText) findViewById(R.id.login_user_edit_text);
        login_password_edit = (EditText) findViewById(R.id.login_password_edit_text);
        login_next_btn  = (Button) findViewById(R.id.login_next_btn);
        loginApiService = new LoginApiService(context);
        identitySharedPref = new IdentitySharedPref(context);
        new_account_txt = (TextView) findViewById(R.id.login_new_account_text);
        new_account_txt.setPaintFlags(new_account_txt.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);

        forget_password_txt = (TextView) findViewById(R.id.login_forget_text);
        forget_password_txt.setPaintFlags(forget_password_txt.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);


        //firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());



    }

}
