package com.atisapp.jazbeh.NewProductList;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.atisapp.jazbeh.ProductList.ProductListAdapter;
import com.atisapp.jazbeh.ProductList.ProductListModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.util.List;

public class NewProductListActivity extends AppCompatActivity {

    private static final String TAG = "ProductListActivity";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private ListView productListView;
    private ProductListAdapter productListAdapter;
    private NewProductListApiService newProductListApiService;
    private ProductPrefs productPrefs;
    private String type = "free";

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            if(isOnline()) {
                type = (String) bd.get("type");
                updateProductsFromServer();
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product_list);

        SetupViews();
        //getProductsFromDB();

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            type = (String) bd.get("type");
            //Log.i(TAG, "onCreate: "+type);
            getNewProductsFromServer();
        }
    }

    private void SetupViews()
    {
        context = NewProductListActivity.this;
        productListAdapter = new ProductListAdapter(context);
        productListView = (ListView) findViewById(R.id.new_product_list_listView);
        newProductListApiService = new NewProductListApiService(context);
        productPrefs = new ProductPrefs(context);
        identitySharedPref = new IdentitySharedPref(context);
    }

    void getNewProductsFromServer()
    {
        newProductListApiService.getNewProducts(type, new NewProductListApiService.onGetNewProducts() {
            @Override
            public void onGet(boolean msg, List<ProductListModel> list_products) {

                if(msg)
                {
                    productListView.setAdapter(productListAdapter);
                    productListView.requestFocus();
                    productListView.setSelectionFromTop(productListAdapter.getCount(), 0);
                    productListAdapter.updateAdapterData(list_products, type);
                    productListAdapter.notifyDataSetChanged();
                    //productListDatabase.saveProductListData(list_products);
                }else {
                    Toast.makeText(context,"اطلاعاتی برای این بخش وجود ندارد",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    void updateProductsFromServer()
    {

        newProductListApiService.updateNewProducts(type, new NewProductListApiService.onGetNewProducts() {
            @Override
            public void onGet(boolean msg, List<ProductListModel> list_products) {
                if(msg)
                {
                    productListView.setAdapter(productListAdapter);
                    productListView.requestFocus();
                    productListView.setSelectionFromTop(productListAdapter.getCount(), 0);
                    productListAdapter.updateAdapterData(list_products, type);
                    productListAdapter.notifyDataSetChanged();
                    //productListDatabase.saveProductListData(list_products);
                }else {
                    Toast.makeText(context,"اطلاعاتی برای این بخش وجود ندارد",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        else
        {
            if(identitySharedPref.getLanguage().equals("fa"))
            {
                Toast.makeText(context,"اتصال به اینترنت خود را بررسی نمایید",Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(context,"check your internet connection",Toast.LENGTH_LONG).show();

            }
        }
        return false;
    }
}
