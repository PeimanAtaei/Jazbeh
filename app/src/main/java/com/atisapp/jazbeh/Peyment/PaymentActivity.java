package com.atisapp.jazbeh.Peyment;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.atisapp.jazbeh.Core.ClientConfigs;
import com.atisapp.jazbeh.MusicPlayer.BoughtApiService;
import com.atisapp.jazbeh.MusicPlayer.MusicPlayerActivity;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    Context context;
    IdentitySharedPref identitySharedPref;
    ProductPrefs productPrefs;
    private BoughtApiService boughtApiService;
    private WebView webView;
    ProgressBar progressBar;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setupViews();

        //Log.i(TAG, "onCreate: "+productPrefs.get_product_msg());

        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        if (productPrefs.get_product_msg().equals("Fetch products by package")) {
            //Log.i(TAG, "onCreate: " + ClientConfigs.REST_API_BASE_URL + "products/buy/" + productPrefs.get_product_id() + "/" + productPrefs.get_product_package_id());

            webView.loadUrl(ClientConfigs.REST_API_BASE_URL + "products/buy/" + productPrefs.get_product_id() + "/" + productPrefs.get_product_package_id(),
                    getCustomHeaders());
        }

        else
        {
            //Log.i(TAG, "onCreate: "+ClientConfigs.REST_API_BASE_URL + "products/buy/" + productPrefs.get_product_id() + "/undefined");

            webView.loadUrl(ClientConfigs.REST_API_BASE_URL + "products/buy/" + productPrefs.get_product_id() + "/undefined",
                    getCustomHeaders());
        }


        progressBar.setVisibility(View.VISIBLE);
        //Log.i(TAG,"https://api.atisapp.com/v1/payment/buy/"+plan_id+"/"+token );

    }

    private void setupViews()
    {
        context= getApplicationContext();
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);
        boughtApiService = new BoughtApiService(context);
        webView = (WebView) findViewById(R.id.payment_web_view);
        progressBar = (ProgressBar) findViewById(R.id.web_progress_bar);

    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            if (url.equals("jazbe://payment/unpaid")) {
                startActivity(new Intent(PaymentActivity.this, MusicPlayerActivity.class));
                finish();
                return true;
            }
            else if (url.equals("jazbe://payment/paid")) {
                productPrefs.set_product_bought(true);
                startActivity(new Intent(PaymentActivity.this, MusicPlayerActivity.class));
                finish();
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    private Map<String, String> getCustomHeaders()
    {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", identitySharedPref.getToken());
        //headers.put("Mobile", identitySharedPref.getMyNumber());
        return headers;
    }

}
