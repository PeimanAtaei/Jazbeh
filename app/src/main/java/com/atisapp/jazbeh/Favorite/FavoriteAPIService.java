package com.atisapp.jazbeh.Favorite;

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

public class FavoriteAPIService {

    private static final String TAG = "FavoriteAPIService";
    private Context context;
    private ProgressDialog progressDialog;
    private IdentitySharedPref identitySharedPref;
    private ProductPrefs productPrefs;
    private String myUrl;
    private int    productType;



    public FavoriteAPIService(Context context)
    {
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت لیست علاقمندی ها");
    }

    public void GetFavoriteCourses(String type,final onGetCourses getCourses)
    {

        Log.i(TAG, "GetCoursesWithCategoryID: start course API");
        if(!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
            progressDialog.show();
        }



        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/users/favorites?course=true&type="+type ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");
                progressDialog.hide();


                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getCourses.onGet(res,favoriteCourseParsResponse(response));
                    }


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

    public void GetFavoriteEpisodes(String type,final onGetEpisodes getEpisodes)
    {

        Log.i(TAG, "GetCoursesWithCategoryID: start course API");
        if(!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
            progressDialog.show();
        }



        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/users/favorites?episode=true&type="+type ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");
                progressDialog.hide();


                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getEpisodes.onGet(res,favoriteEpisodeParsResponse(response));
                    }


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


    private List<ProductModel> favoriteCourseParsResponse(JSONObject response)
    {

        List<ProductModel> list_courses = new ArrayList<>();
        Log.i(TAG, "favoriteCourseParsResponse: start response"+list_courses.size() );

        try {

            JSONArray array_courses =  response.getJSONArray("data");
            Log.i(TAG, "favoriteCourseParsResponse: "+array_courses.length());
            for (int i = 0; i < array_courses.length(); i++) {

                ProductModel courseListModel = new ProductModel();
                JSONObject object_course = array_courses.getJSONObject(i);

                    courseListModel.setProduct_id(object_course.getString("id"));
                    courseListModel.setTitle(object_course.getString("title"));
                    courseListModel.setTime(object_course.getString("time"));

                if(object_course.getString("content").equals("video"))
                {
                    courseListModel.setImage(object_course.getString("imageUrl"));
                }

                    list_courses.add(courseListModel);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list_courses;
    }

    private List<ProductModel> favoriteEpisodeParsResponse(JSONObject response)
    {
        List<ProductModel> list_courses = new ArrayList<>();

        try {

            JSONArray array_courses =  response.getJSONArray("data");
            for (int i = 0; i < array_courses.length(); i++) {

                ProductModel courseListModel = new ProductModel();
                JSONObject object_course = array_courses.getJSONObject(i);
                    courseListModel.setProduct_id(object_course.getString("id"));
                    courseListModel.setTitle(object_course.getString("title"));
                    courseListModel.setNumber(object_course.getInt("number"));
                    courseListModel.setTime(object_course.getString("time"));

                if(object_course.getString("content").equals("video"))
                {
                    courseListModel.setImage(object_course.getString("imageUrl"));
                }

                    list_courses.add(courseListModel);
                }



            } catch (JSONException ex) {
        }

        return list_courses;
    }



    public interface onGetCourses
    {
        void onGet(boolean msg, List<ProductModel> list_courses);
    }

    public interface onGetEpisodes
    {
        void onGet(boolean msg,List<ProductModel> list_episodes);
    }

}
