package com.atisapp.jazbeh.Courses;

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
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseAPIService {

    private static final String TAG = "CourseApiService";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private ProductPrefs productPrefs;
    private String myUrl;
    private int    productType;



    public CourseAPIService(Context context)
    {
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);
    }

    public void GetCoursesWithCategoryID(String categoryId,final onGetCourses getCourses)
    {

        Log.i(TAG, "GetCoursesWithCategoryID: start course API");

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v1/categories/"+ categoryId+"/courses" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");

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

    public void GetCoursesWithSearchKey(String Key,final onGetCoursesSearch coursesSearch)
    {
        Log.i(TAG, "GetCoursesWithCategoryID: start search course API "+Key);
        String word =Key;
        try {
            word = URLEncoder.encode(Key, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/courses/search?keyword="+word ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");

                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        coursesSearch.onGet(res,CourseParsResponse(response));
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

    public void GetSingleCourses(String courseId,final onGetSingleCourses getSingleCourse)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/courses/"+ courseId+"/?isRegistered=true" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressDialog.hide();

                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getSingleCourse.onGet(res,SingleCourseParsResponse(response));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.hide();
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

    public void GetMyCourses(String type,final onGetCourses getCourses)
    {

        Log.i(TAG, "GetCoursesWithCategoryID: start course API");
//        if(!progressDialog.isShowing())
//        {
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
//            progressDialog.show();
//        }



        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/users/purchases/courses?type="+type ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");


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
                //progressDialog.hide();
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

    public void GetFreeCourses(final onGetCourses getCourses)
    {

        Log.i(TAG, "GetCoursesWithCategoryID: start course API");
//        if(!progressDialog.isShowing())
//        {
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
//            progressDialog.show();
//        }



        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/courses/free" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");



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
                //progressDialog.hide();
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

    public void GetSingleCoursePerches(String courseId,boolean useWallet,final onGetPerchesSingleCourse perchesSingleCourse)
    {
        String urlType = "/purchase" ;
        if(useWallet)
        {
            urlType = "/purchase?wallet=true";
            //Log.i(TAG, "GetSingleCoursePerches: true");
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/courses/"+ courseId+urlType ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        perchesSingleCourse.onGet(res,response.getString("data"));
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

    public void LikeCourse(String courseId,final onLikeCourse likeCourse)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL +"/v2/courses/"+courseId+"/like",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    likeCourse.onGet(res);

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

    public void DeleteLikeCourse(String courseId,final onLikeCourse likeCourse)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.DELETE, ClientConfigs.REST_API_BASE_URL +"/v2/courses/"+courseId+"/like",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    likeCourse.onGet(res);

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

    public void FavoriteCourse(String courseId,final onFavorite favorite)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL +"/v2/courses/"+courseId+"/favorite",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.i(TAG, "onResponse: favorite response received ");
                try {
                    boolean res = response.getBoolean("success");
                    Log.i(TAG, "onResponse: favorite response received "+res);

                    favorite.onGet(res);

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
                            Log.e(TAG, "onErrorResponse: Token Expire" );
                            break;
                        }


                        case 204:
                        {
                            Log.e(TAG, "onErrorResponse: No New Message" );
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

    public void DeleteFavoriteCourse(String courseId,final onFavorite favorite)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.DELETE, ClientConfigs.REST_API_BASE_URL +"/v2/courses/"+courseId+"/favorite",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.i(TAG, "onResponse: favorite response received ");
                try {
                    boolean res = response.getBoolean("success");
                    Log.i(TAG, "onResponse: favorite response received "+res);

                    favorite.onGet(res);

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
                            Log.e(TAG, "onErrorResponse: Token Expire" );
                            break;
                        }


                        case 204:
                        {
                            Log.e(TAG, "onErrorResponse: No New Message" );
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


    private List<ProductModel> CourseParsResponse(JSONObject response)
    {
        List<ProductModel> list_courses = new ArrayList<>();

        try {

            JSONArray array_courses =  response.getJSONArray("data");
            for (int i = 0; i < array_courses.length(); i++) {

                ProductModel courseListModel = new ProductModel();
                JSONObject object_course = array_courses.getJSONObject(i);

                courseListModel.setProduct_id(object_course.getString("id"));
                courseListModel.setTitle(object_course.getString("title"));
                courseListModel.setTime(object_course.getString("time"));
                if(object_course.getString("content").equals("video"))
                {
                    courseListModel.setImage(object_course.getString("imageUrl"));
                    Log.i(TAG, "CourseParsResponse: "+object_course.getString("imageUrl"));
                }
                /*courseListModel.setViewCount(object_course.getInt("viewCount"));
                courseListModel.setCommentCount(object_course.getInt("commentCount"));
                courseListModel.setRating(object_course.getInt("rating"));
                courseListModel.setCategory(productPrefs.get_product_group_id());


                courseListModel.setDescription(object_course.getString("description"));
                courseListModel.setType(object_course.getString("type"));
                courseListModel.setPrice(object_course.getInt("price"));
                courseListModel.setVersion(object_course.getInt("__v"));
                courseListModel.setProduct_id(object_course.getString("id"));*/

                list_courses.add(courseListModel);

                //Log.i(TAG, "ProductParsResponse: "+object_products.getString("url"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "CourseParsResponse: "+list_courses.size());
        return list_courses;
    }


    private ProductModel SingleCourseParsResponse(JSONObject response)
    {
        ProductModel single_courses = new ProductModel();

        try {

            JSONObject object_course =  response.getJSONObject("data");

            single_courses.setProduct_id(object_course.getString("id"));
            single_courses.setTitle(object_course.getString("title"));
            single_courses.setDescription(object_course.getString("description"));
            single_courses.setPrice(object_course.getInt("price"));
            single_courses.setType(object_course.getString("type"));
            single_courses.setViewCount(object_course.getInt("viewCount"));
            single_courses.setLikeCount(object_course.getInt("likeCount"));
            single_courses.setHasLiked(object_course.getBoolean("hasLiked"));
            single_courses.setFavorite(object_course.getBoolean("isFavorite"));
            single_courses.setCommentCount(object_course.getInt("commentCount"));
            single_courses.setTime(object_course.getString("time"));
            single_courses.setPayCourse(object_course.getBoolean("isPaidCourse"));

            single_courses.setCategory(productPrefs.get_product_group_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return single_courses;
    }

    // Interfaces ----------------------------------------------------------------------------------

    public interface onGetCourses
    {
        void onGet(boolean msg,List<ProductModel> list_courses);
    }

    public interface onGetCoursesSearch
    {
        void onGet(boolean msg,List<ProductModel> list_courses);
    }

    public interface onGetSingleCourses
    {
        void onGet(boolean msg,ProductModel single_courses);
    }

    public interface onGetPerchesSingleCourse
    {
        void onGet(boolean msg,String URL);
    }

    public interface onLikeCourse
    {
        void onGet(boolean msg);
    }

    public interface onFavorite
    {
        void onGet(boolean msg);
    }
}
