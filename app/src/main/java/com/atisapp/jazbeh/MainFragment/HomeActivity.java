package com.atisapp.jazbeh.MainFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.atisapp.jazbeh.AboutUs.AboutActivity;
import com.atisapp.jazbeh.Comment.CommentActivity;
import com.atisapp.jazbeh.Delegate.DelegateAPIService;
import com.atisapp.jazbeh.Delegate.DelegateActivity;
import com.atisapp.jazbeh.Delegate.FormActivity;
import com.atisapp.jazbeh.Login.LoginActivity;
import com.atisapp.jazbeh.Login.RegisterActivity;
import com.atisapp.jazbeh.MainFragment.ui.home.WalletApiService;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Search.SearchActivity;
import com.atisapp.jazbeh.Splash.SplashActivity;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
//import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private Toolbar main_toolbar;
    private DrawerLayout main_draw;
    private NavigationView main_navigation;
    private WalletApiService walletApiService;
    //private FirebaseAnalytics firebaseAnalytics;
    private HomeAPIService homeAPIService;
    private DelegateAPIService delegateAPIService;
    private ImageView searchButton;
    private Handler handler;
    private Uri location;
    private ProgressDialog updateProgressDialog,progressDialog;
    private String directDownloadURl = "";
    private UpdateModel newUpdateModel;
    String appName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_free, R.id.navigation_shop, R.id.navigation_jazbe_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        SetupViews();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    private void SetupViews()
    {
        context = HomeActivity.this;
        identitySharedPref = new IdentitySharedPref(context);
        delegateAPIService = new DelegateAPIService(context);
        main_draw = findViewById(R.id.container);
        main_navigation = findViewById(R.id.main_navigation);
        //firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        homeAPIService = new HomeAPIService(context);
        searchButton = findViewById(R.id.searchButton);
        handler = new Handler();
        updateProgressDialog = new ProgressDialog(context);
        updateProgressDialog.setTitle("در حال دانلود نسخه جدید (حجم تقریبی 29 مگابایت)");
        updateProgressDialog.setMax(100);
        updateProgressDialog.setCancelable(false);
        updateProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        if(isOnline())
        {
            setupToolBar();
            setupNavigationView();
            CheckToken();
            showBalance();
            //CheckLastUpdate();

            Bundle bundle = new Bundle();
            bundle.putString("UserEntered", "home");
            //firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

        }

    }

    private void setupToolBar()
    {
        main_toolbar     = (Toolbar) findViewById(R.id.main_toolBar);
        setSupportActionBar(main_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        main_toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white_trans));
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,main_draw,main_toolbar,0,0);
        main_draw.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        main_toolbar.setNavigationIcon(R.drawable.ic_dehaze);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
    private void setupNavigationView()

    {
        main_navigation.getMenu().setGroupCheckable(0,false,true);
        main_navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menu_navigation_explain :
                    {

                        Intent intent = new Intent(context, AboutActivity.class);
                        intent.putExtra("aboutType", "explain");
                        startActivity(intent);
                        break;
                    }
                    case R.id.menu_navigation_usage :
                    {
                        Intent intent = new Intent(context, AboutActivity.class);
                        intent.putExtra("aboutType", "usage");
                        startActivity(intent);
                        break;
                    }

                    case R.id.menu_navigation_sell :
                    {

                        if(CheckRegistration()) {
                            progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("در حال ورود به پنل نمایندگان");
                            progressDialog.show();


                            delegateAPIService.GetDelegateStatus(new DelegateAPIService.onGetDelegateStatus() {
                                @Override
                                public void onGet(String response) {


                                    if (response.equals("1")) {
                                        Intent intent = new Intent(context, DelegateActivity.class);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                    } else {
                                        Intent intent = new Intent(context, FormActivity.class);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                    }
                                }

                            });
                        }
                        break;
                    }

                    case R.id.menu_navigation_about :
                    {
                        Intent intent = new Intent(context, AboutActivity.class);
                        intent.putExtra("aboutType", "about");
                        startActivity(intent);
                        break;
                    }

                }

                return false;
            }
        });


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
            return true;
        }
        else
        {
//            openDialogOffline();
            Intent intent =new Intent(context, SplashActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }

    private void setBalanceToMenu() {
        main_navigation.getMenu().findItem(R.id.menu_navigation_wallet_balance).setTitle("اعتبار شما : " + convertNumberToBalance(identitySharedPref.getWalletBalance()) + " تومان ");
    }

    private void showBalance() {
        walletApiService = new WalletApiService(getApplicationContext());
        walletApiService.showBalance(new WalletApiService.OnGetBalanceListener() {

            @Override
            public void onBalance(int balance) {
                identitySharedPref.setWalletBalance(balance);
                setBalanceToMenu();
            }
        });
    }

    private String convertNumberToBalance(int balance) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(balance);
    }

    // check Token ---------------------------------------------------------------------------------

    private void CheckToken()
    {
        if(identitySharedPref.getToken().length()>0)
        {
            homeAPIService.CheckUserToken(new HomeAPIService.onCheckToken() {
                @Override
                public void onCheck(boolean isTokenVerified) {
                    if(!isTokenVerified)
                        RefreshToken();
                }
            });
        }
    }

    private void RefreshToken()
    {
        homeAPIService.RefreshToken(new HomeAPIService.onRefreshToken() {
            @Override
            public void onRefresh(boolean result) {
                Log.i(TAG, "onRefresh: "+result);
            }
        });
    }


//    public void GetAddress()
//    {
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        try {
//            startActivityForResult(builder.build(HomeActivity.this),PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//    }


    // download and install new update -------------------------------------------------------------

    /*private void PermissionCheck()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return;
            }
        }
        initialize();
    }


    private void CheckLastUpdate()
    {
        Log.i(TAG, "onGet: response ");
        homeAPIService.GetLastUpdateVersion(new HomeAPIService.onGetUpdate() {
            @Override
            public void onGet(boolean msg,UpdateModel updateModel) {
                Log.i(TAG, "onGet: response");

                try {
                    PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
                    int version = pInfo.versionCode;
                    Log.i(TAG, "onGet version code : "+version);
                    if(version<updateModel.getVersionCode())
                    {
                        newUpdateModel = updateModel;
                        directDownloadURl = updateModel.getDirectURL();
                        openDialogUpdate(updateModel);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void openDialogUpdate(final UpdateModel updateModel) {
        Log.i(TAG, "onGet: dialog");
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_update_dialog, viewGroup, false);

        TextView    updateTitle             = dialogView.findViewById(R.id.update_label);
        Button      marketBtn               = dialogView.findViewById(R.id.market_updateBtn);
        Button      directBtn               = dialogView.findViewById(R.id.direct_updateBtn);
        ListView    updateDialogListView    = dialogView.findViewById(R.id.update_dialog_listView);

        updateTitle.setText(" بروز رسانی نرم افزار به نسخه " + updateModel.getVersionName());

        List<String> itemsArrayList = updateModel.getFeaturesList(); // calls function to get items list
        FeatureAdapter adapter = new FeatureAdapter(this, itemsArrayList);
        updateDialogListView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);
        final AlertDialog updateDialog = builder.create();
        updateDialog.show();

        marketBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context,"ورود به صفحه جذبه برای بروز رسانی",Toast.LENGTH_LONG).show();
                updateDialog.dismiss();

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        directBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
                PermissionCheck();
            }
        });
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            initialize();
        }else {
            PermissionCheck();
        }
    }

    private void initialize()
    {
        Log.i(TAG, "initialize: start");
        appName = "Jazbeh-"+newUpdateModel.getVersionName()+".apk";

        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(newUpdateModel.getDirectURL()).build();
                Log.i(TAG, "run: "+directDownloadURl);
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    float file_size = response.body().contentLength();
                    Log.i(TAG, "run: file size :"+file_size);

                    final BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream());
                    final OutputStream stream = new FileOutputStream(Environment.getExternalStorageDirectory()+"/Download/"+appName);
                    byte[] data = new byte[8192];
                    float total = 0;
                    int read_bytes = 0;

                    updateProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "بیخیال", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            updateProgressDialog.dismiss();//dismiss dialog
                        }
                    });

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: post start");
                            updateProgressDialog.show();
                        }
                    });


                    while ((read_bytes = inputStream.read(data)) != -1)
                    {
                        total = total + read_bytes;
                        stream.write(data,0,read_bytes);
                        updateProgressDialog.setProgress((int) ((total/file_size)*100));
                    }
                    updateProgressDialog.dismiss();
                    stream.flush();
                    stream.close();
                    response.body().close();

                    InstallAPK();

                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        updateThread.start();
    }

    private void InstallAPK()
    {
        File file = new File(Environment.getExternalStorageDirectory()+"/Download/" +appName);
        location = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",file);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(location, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }else {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }*/


    // Create Account Dialog

    private void openAccountDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_account_dialog, viewGroup, false);

        Button createAccount = dialogView.findViewById(R.id.create_account);
        Button createAccountCancel = dialogView.findViewById(R.id.create_account_cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        final AlertDialog accountDialog = builder.create();
        accountDialog.show();
        accountDialog.setCanceledOnTouchOutside(false);
        accountDialog.setCancelable(false);

        createAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context,"ورود به بخش ثبت نام",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

        createAccountCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                accountDialog.dismiss();
            }
        });

    }

    public boolean CheckRegistration()
    {
        boolean isRegistered ;
        if(identitySharedPref.getToken().length()>0 || identitySharedPref.getIsRegistered() == 1)
        {
            isRegistered = true;
        }else {
            isRegistered = false;
            openAccountDialog();
        }
        return isRegistered;
    }



    // open browser and font -----------------------------------------------------------------------
    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
