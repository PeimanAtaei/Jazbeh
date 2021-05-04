package com.atisapp.jazbeh.VideoPlayer;

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

public class VideoAPIService {

    private static final String TAG = "CourseApiService";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private ProductPrefs productPrefs;
    private String myUrl;
    private int    productType;

    public VideoAPIService(Context context)
    {
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);
    }


    public void GetCategories(final onGetVideoCategories getCategories)
    {

        Log.i(TAG, "GetCoursesWithCategoryID: start course API");

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v1/categories?type=video" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getCategories.onGet(res,VideoCategoryParsResponse(response));
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

    // Parsers ----------------------------------------------------------------------------------


    private List<VideoCategoryModel> VideoCategoryParsResponse(JSONObject response)
    {
        List<VideoCategoryModel> list_category = new ArrayList<>();

        try {
            JSONArray array_category =  response.getJSONArray("data");
            for (int i = 0; i < array_category.length(); i++) {

                VideoCategoryModel categoryListModel = new VideoCategoryModel();
                JSONObject object_category = array_category.getJSONObject(i);

                categoryListModel.setCategoryId(object_category.getString("id"));
                categoryListModel.setCategoryTitle(object_category.getString("title"));
                categoryListModel.setCategoryBrief(object_category.getString("brief"));
                categoryListModel.setCategoryImage(object_category.getString("image"));
                categoryListModel.setCategoryType(object_category.getString("type"));

                /*courseListModel.setViewCount(object_course.getInt("viewCount"));
                courseListModel.setCommentCount(object_course.getInt("commentCount"));
                courseListModel.setRating(object_course.getInt("rating"));
                courseListModel.setCategory(productPrefs.get_product_group_id());


                courseListModel.setDescription(object_course.getString("description"));
                courseListModel.setType(object_course.getString("type"));
                courseListModel.setPrice(object_course.getInt("price"));
                courseListModel.setVersion(object_course.getInt("__v"));
                courseListModel.setProduct_id(object_course.getString("id"));*/

                list_category.add(categoryListModel);

                //Log.i(TAG, "ProductParsResponse: "+object_products.getString("url"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "CourseParsResponse: "+list_category.size());
        return list_category;
    }

    // Interfaces ----------------------------------------------------------------------------------

    public interface onGetVideoCategories
    {
        void onGet(boolean msg, List<VideoCategoryModel> list_category);
    }

}
