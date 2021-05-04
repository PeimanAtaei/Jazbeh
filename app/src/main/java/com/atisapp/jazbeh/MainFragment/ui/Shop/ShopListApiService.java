package com.atisapp.jazbeh.MainFragment.ui.Shop;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atisapp.jazbeh.Core.ClientConfigs;
import com.atisapp.jazbeh.ProductList.ProductListModel;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopListApiService {

    private static final String TAG = "ProductListApiService";
    private Context context;
    private ProgressDialog progressDialog;
    private IdentitySharedPref identitySharedPref;
    private ProductPrefs productPrefs;
    private String myUrl;
    private int    productType;

    public ShopListApiService(Context context)
    {

        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);
    }

    public void getProducts(final onGetShops getProducts)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات محصولات خریداری شده");
        progressDialog.show();


        JSONObject list_object = new JSONObject();
        try {
            list_object.put("lang",identitySharedPref.getLanguage());

        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL + "products/bought", list_object , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                try {
                    boolean res = response.getBoolean("success");
                    getProducts.onGet(res, ProductParsResponse(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                //Log.e(TAG, "onErrorResponse: erore"+error.getMessage());


                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 401:
                        {
                            //Log.e(TAG, "onErrorResponse: Token Expire" );
                            break;
                        }


                        case 204:
                        {
                            //Log.e(TAG, "onErrorResponse: No New Message" );
                            break;
                        }

                    }
                }
            }
        }){

            @Override
            public Map< String, String > getHeaders() throws AuthFailureError {
                HashMap< String, String > headers = new HashMap < String, String > ();

                headers.put("Authorization",identitySharedPref.getToken());

                return headers;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError){
                if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                }

                return volleyError;
            }


        };

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(objectRequest);

    }






    private List<ProductListModel> ProductParsResponse(JSONObject response)
    {
        List<ProductListModel> list_product = new ArrayList<>();

        try {

            JSONArray array_product =  response.getJSONArray("products");
            for (int i = 0; i < array_product.length(); i++) {

                ProductListModel productListModel = new ProductListModel();
                JSONObject object_products = array_product.getJSONObject(i);

                productListModel.setProduct_id(object_products.getString("id"));
                productListModel.setProduct_name(object_products.getString("name"));
                productListModel.setProduct_explain(object_products.getString("explain"));
                productListModel.setProduct_usage(object_products.getString("usage"));
                productListModel.setProduct_alarm(object_products.getString("alarm"));
                productListModel.setPrice(object_products.getString("price"));
                //productListModel.setProduct_url(object_products.getString("url"));
                JSONObject object_time = object_products.getJSONObject("time");
                productListModel.setProduct_time(object_time.getString("hours")+":"+object_time.getString("minutes")+":"+object_time.getString("seconds"));


                list_product.add(productListModel);

                //Log.i(TAG, "ProductParsResponse: "+object_products.getString("name"));

            }

            JSONArray array_package =  response.getJSONArray("packages");
            for (int i = 0; i < array_package.length(); i++) {

                ProductListModel productListModel = new ProductListModel();
                JSONObject object_package = array_package.getJSONObject(i);

                productListModel.setProduct_id(object_package.getString("id"));
                productListModel.setProduct_name(object_package.getString("name"));
                productListModel.setIs_package(true);

                list_product.add(productListModel);

                //Log.i(TAG, "ProductParsResponse: "+object_products.getString("name"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list_product;
    }








    public interface onGetShops
    {
        void onGet(boolean msg, List<ProductListModel> list_products);
    }


}
