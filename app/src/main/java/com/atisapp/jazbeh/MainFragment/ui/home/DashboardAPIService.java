package com.atisapp.jazbeh.MainFragment.ui.home;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atisapp.jazbeh.Core.ClientConfigs;
import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardAPIService {


    private static final String TAG = "DashboardAPIService";
    private Context context;
    private ProgressDialog progressDialog;
    private IdentitySharedPref identitySharedPref;
    private ProductPrefs productPrefs;
    private String myUrl;
    private int    productType;

    public DashboardAPIService(Context context)
    {
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات دوره ها");
    }

    public void GetPopularProducts(final onGetPopularProducts getPopularProducts)
    {

        Log.i(TAG, "GetPopularProducts: API started");
        if(!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
            progressDialog.show();
        }



        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v1/categories/populars" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: popular list received");
                progressDialog.dismiss();

                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getPopularProducts.onGet(res,popularParsResponse(response));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    public void GetCategories(final onGetCategories getCategories)
    {

        Log.i(TAG, "GetCoursesWithCategoryID: start course API");
        if(!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
            progressDialog.show();
        }



        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v1/categories" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");
                progressDialog.dismiss();

                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getCategories.onGet(res,popularParsResponse(response));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    public void GetBanners(final onGetBanners getBanners)
    {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/banners" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");

                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getBanners.onGet(res,bannerParsResponse(response));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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


    // Parsers -------------------------------------------------------------------------------------

    private List<ProductModel> popularParsResponse(JSONObject response)
    {
        List<ProductModel> list_popular = new ArrayList<>();

        try {

            JSONArray array_popular =  response.getJSONArray("data");
            for (int i = 0; i < array_popular.length(); i++) {

                ProductModel popularListModel = new ProductModel();
                JSONObject object_popular = array_popular.getJSONObject(i);

                popularListModel.setProduct_id(object_popular.getString("id"));
                popularListModel.setTitle(object_popular.getString("title"));
                popularListModel.setBrief(object_popular.getString("brief"));
                popularListModel.setImage(object_popular.getString("image"));
                //Log.i(TAG, "popularParsResponse: "+object_popular.getString("image"));

                list_popular.add(popularListModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list_popular;
    }

    private List<ProductModel> bannerParsResponse(JSONObject response)
    {
        List<ProductModel> list_banner = new ArrayList<>();

        try {

            JSONArray array_popular =  response.getJSONArray("data");
            for (int i = 0; i < array_popular.length(); i++) {

                ProductModel bannerListModel = new ProductModel();
                JSONObject object_popular = array_popular.getJSONObject(i);

                bannerListModel.setProduct_id(object_popular.getString("id"));
                bannerListModel.setTitle(object_popular.getString("image"));
                bannerListModel.setPodcastUrl(object_popular.getString("url"));
                //Log.i(TAG, "popularParsResponse: "+object_popular.getString("image"));

                list_banner.add(bannerListModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list_banner;
    }



    // Interfaces ----------------------------------------------------------------------------------

    public interface onGetPopularProducts
    {
        void onGet(boolean msg, List<ProductModel> popularProducts);
    }

    public interface onGetCategories
    {
        void onGet(boolean msg, List<ProductModel> categoryList);
    }

    public interface onGetBanners
    {
        void onGet(boolean msg, List<ProductModel> bannerList);
    }

}
