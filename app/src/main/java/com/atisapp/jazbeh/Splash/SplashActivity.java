package com.atisapp.jazbeh.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.atisapp.jazbeh.Login.LoginActivity;
import com.atisapp.jazbeh.MainFragment.HomeActivity;
import com.atisapp.jazbeh.Offline.OfflineListActivity;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private Button reloadBtn,databaseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SetupViews();

        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline())
                {
                    if(identitySharedPref.getIsRegistered() == 1)
                    {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        databaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, OfflineListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void SetupViews()
    {
        context = SplashActivity.this;
        identitySharedPref = new IdentitySharedPref(context);
        reloadBtn = findViewById(R.id.splash_reload_btn);
        databaseBtn = findViewById(R.id.splash_database_btn);

        Log.i(TAG, "SetupViews: splash started");

        if(isOnline())
        {
            if(identitySharedPref.getIsRegistered() == 1)
            {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
