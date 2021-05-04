package com.atisapp.jazbeh.MainFragment.ui.profile;

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

public class UpdatePasswordActivity extends AppCompatActivity {

    private static final String TAG = "UpdatePasswordActivity";
    private Context context;
    private EditText password_current_edit,password_firstPass_edit,password_secondPass_edit;
    private Button password_next_btn;
    private ProfileInfoApiService profileInfoApiService;
    private IdentitySharedPref identitySharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        SetupVIew();

        password_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(password_current_edit.getText().length()<4)
                {
                    Toast.makeText(context,"رمز عبور رمز عبور فعلی خود را به شکل صحیح وارد نمایید",Toast.LENGTH_LONG).show();

                }
                else if(password_firstPass_edit.getText().length()<4)
                {
                    Toast.makeText(context,"رمز عبور باید حداقل 4 کاراکتر داشته باشد",Toast.LENGTH_LONG).show();
                }
                else if(!password_firstPass_edit.getText().toString().equals(password_secondPass_edit.getText().toString()))
                {
                    Toast.makeText(context,"رمز عبور جدید و تکرار رمز عبور باید یکسان باشند",Toast.LENGTH_LONG).show();
                }
                else {
                    profileInfoApiService.UpdatePassword(password_current_edit.getText().toString(), password_firstPass_edit.getText().toString(), new ProfileInfoApiService.onUpdatePasswordResponse() {
                        @Override
                        public void onGet(boolean response) {
                            if(response)
                            {
                                identitySharedPref.setIsRegistered(1);
                                finish();
                                Toast.makeText(context,"رمز عبور با موفقیت تغییر کرد",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(context,"اختلال در بروز رسانی رمز عبور ، اطلاعات خود را دوباره بررسی نمایید",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });
    }

    private void SetupVIew()
    {
        context = UpdatePasswordActivity.this;
        password_current_edit = (EditText) findViewById(R.id.update_password_current_edit_text);
        password_firstPass_edit = (EditText) findViewById(R.id.update_password_firstPass_edit_text);
        password_secondPass_edit = (EditText) findViewById(R.id.update_password_secondPass_edit_text);
        password_next_btn  = (Button) findViewById(R.id.update_password_next_btn);
        profileInfoApiService = new ProfileInfoApiService(context);
        identitySharedPref = new IdentitySharedPref(context);
    }
}
