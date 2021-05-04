package com.atisapp.jazbeh.ProductList;

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
import com.atisapp.jazbeh.Courses.CourseModel;
import com.atisapp.jazbeh.Episodes.EpisodeModel;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListApiService {

    private static final String TAG = "ProductListApiService";
    private Context context;
    private ProgressDialog progressDialog;
    private IdentitySharedPref identitySharedPref;
    private ProductPrefs productPrefs;
    private String myUrl;
    private int    productType;

    public ProductListApiService(Context context)
    {

        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);


    }

    /*public void updateProducts(boolean isPackage,String categoryId,final onGetProducts getProducts)
    {


        JSONObject list_object = new JSONObject();
        String url ="showByCategoryId";
        try {
            if(isPackage)
            {
                url ="showByPackageId";
                list_object.put("packageId",categoryId);
            }else {
                list_object.put("categoryId",categoryId);
            }

            list_object.put("lang",identitySharedPref.getLanguage());

            //Log.i(TAG, "product info : "+identitySharedPref.getLanguage()+categoryId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL + url,list_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    getProducts.onGet(res,ProductParsResponse(response));

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

    }*/

    public void getCoursesWithCategoryID(String categoryId,final onGetCourses getCourses)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
        progressDialog.show();


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v1/categories/"+ categoryId+"/courses" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();

                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getCourses.onGet(res,CourseParsResponse(response));
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

    public void getEpisodesWithCategoryID(String categoryId,final onGetEpisodes getEpisodes)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
        progressDialog.show();


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v1/categories/"+ categoryId +"/episodes",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();

                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getEpisodes.onGet(res,EpisodeParsResponse(response));
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


    private List<ProductModel> CourseParsResponse(JSONObject response)
    {
        List<ProductModel> list_courses = new ArrayList<>();

        try {

            JSONArray array_courses =  response.getJSONArray("data");
            for (int i = 0; i < array_courses.length(); i++) {

                ProductModel courseListModel = new ProductModel();
                JSONObject object_course = array_courses.getJSONObject(i);

                courseListModel.setTime(object_course.getString("time"));
                courseListModel.setViewCount(object_course.getInt("viewCount"));
                courseListModel.setCommentCount(object_course.getInt("commentCount"));
                courseListModel.setRating(object_course.getInt("rating"));
                courseListModel.setCategory(productPrefs.get_product_group_id());
                courseListModel.setProduct_id(object_course.getString("_id"));
                courseListModel.setTitle(object_course.getString("title"));
                courseListModel.setDescription(object_course.getString("description"));
                courseListModel.setType(object_course.getString("type"));
                courseListModel.setPrice(object_course.getInt("price"));
                courseListModel.setVersion(object_course.getInt("__v"));
                courseListModel.setProduct_id(object_course.getString("id"));

                list_courses.add(courseListModel);

                //Log.i(TAG, "ProductParsResponse: "+object_products.getString("url"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list_courses;
    }


    private List<ProductModel> EpisodeParsResponse(JSONObject response)
    {
        List<ProductModel> list_episode = new ArrayList<>();

        try {

            JSONArray array_episodes =  response.getJSONArray("data");
            for (int i = 0; i < array_episodes.length(); i++) {

                ProductModel episodeListModel = new ProductModel();
                JSONObject object_episode = array_episodes.getJSONObject(i);

                episodeListModel.setTime(object_episode.getString("time"));
                episodeListModel.setPrice(object_episode.getInt("price"));
                episodeListModel.setPlayCount(object_episode.getInt("playCount"));
                episodeListModel.setViewCount(object_episode.getInt("viewCount"));
                episodeListModel.setCommentCount(object_episode.getInt("commentCount"));
                episodeListModel.setCategory(productPrefs.get_product_group_id());
                episodeListModel.setProduct_id(object_episode.getString("_id"));
                episodeListModel.setTitle(object_episode.getString("title"));
                episodeListModel.setExplain(object_episode.getString("explain"));
                episodeListModel.setUsage(object_episode.getString("usage"));
                episodeListModel.setAlarm(object_episode.getString("alarm"));
                episodeListModel.setType(object_episode.getString("type"));
                episodeListModel.setNumber(object_episode.getInt("number"));
                episodeListModel.setPodcastUrl(object_episode.getString("podcastUrl"));
                episodeListModel.setVersion(object_episode.getInt("__v"));


                list_episode.add(episodeListModel);

                //Log.i(TAG, "ProductParsResponse: "+object_products.getString("url"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list_episode;
    }







    public interface onGetProducts
    {
        void onGet(boolean msg,List<ProductListModel> list_products);
    }

    public interface onGetCourses
    {
        void onGet(boolean msg,List<ProductModel> list_courses);
    }

    public interface onGetEpisodes
    {
        void onGet(boolean msg,List<ProductModel> list_episodes);
    }

    public interface onGetSingleCourses
    {
        void onGet(boolean msg,ProductModel single_courses);
    }


}
