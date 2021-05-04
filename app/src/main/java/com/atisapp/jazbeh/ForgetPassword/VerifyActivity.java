package com.atisapp.jazbeh.ForgetPassword;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

public class VerifyActivity extends AppCompatActivity {

    private static final String TAG = "VerifyActivity";
    private Context context;
    private EditText verify_code_edit;
    private Button verify_next_btn;
    private ForgetApiService forgetApiService;
    private IdentitySharedPref identitySharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify2);

        SetupVIew();

        verify_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(verify_code_edit.getText().equals(""))
                {
                    Toast.makeText(context,"کد تایید پیامک شده به موبایل خود را وارد نمایید",Toast.LENGTH_LONG).show();
                }
                else {
                    forgetApiService.SendVerifyCode(verify_code_edit.getText().toString(),new ForgetApiService.onSetCode() {
                        @Override
                        public void onSet(boolean code_response,String data) {
                            if(code_response == true)
                            {
                                Intent intent = new Intent(VerifyActivity.this, PasswordActivity.class);
                                intent.putExtra("data", data);
                                startActivity(intent);
                                Toast.makeText(context,"رمز عبور جدید خود را وارد نمایید",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void SetupVIew()
    {
        context = VerifyActivity.this;
        verify_code_edit = (EditText) findViewById(R.id.verify_code_edit_text);
        verify_next_btn  = (Button) findViewById(R.id.verify_next_btn);
        forgetApiService = new ForgetApiService(context);
        identitySharedPref = new IdentitySharedPref(context);
    }
}
