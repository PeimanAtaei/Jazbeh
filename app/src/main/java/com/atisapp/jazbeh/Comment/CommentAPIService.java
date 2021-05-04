package com.atisapp.jazbeh.Comment;

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

public class CommentAPIService {

    private static final String TAG = "CommentAPIService";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private ProductPrefs productPrefs;

    public CommentAPIService(Context context)
    {
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);
    }

    public void GetComments(String productType,String id,final onGetComments getComments)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/"+productType+"/"+id+"/comments" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");

                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getComments.onGet(res,CommentParsResponse(response));
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
                            Log.e(TAG, error.getMessage() );
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

    public void LikeComment(String commentId,final onLikeComments likeComments)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/comments/"+commentId+"/like",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    likeComments.onGet(res);

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

    public void DeleteLikeComment(String commentId,final onDislikeComments dislikeComments)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/comments/"+commentId+"/dislike",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    dislikeComments.onGet(res);

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


    public void CreateComment(String productId,String productType,String commentText,final onCreateComment createComment)
    {

        JSONObject commentTextObj = new JSONObject();
        try {
            commentTextObj.put("text",commentText);
            //Log.i(TAG, "product info : "+identitySharedPref.getLanguage()+categoryId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL +"/v2/"+productType+"/"+productId+"/comments",commentTextObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    createComment.onGet(res);

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

    private List<CommentModel> CommentParsResponse(JSONObject response)
    {
        List<CommentModel> list_comments = new ArrayList<>();

        try {

            JSONArray array_comments =  response.getJSONArray("data");
            Log.i(TAG, "CommentParsResponse: "+array_comments.length());
            for (int i = 0; i < array_comments.length(); i++) {


                CommentModel commentsListModel = new CommentModel();
                JSONObject object_comment = array_comments.getJSONObject(i);

                Log.i(TAG, "CommentParsResponse: "+object_comment.getString("user"));

                commentsListModel.setCommentId(object_comment.getString("id"));
                commentsListModel.setCommentText(object_comment.getString("text"));
                commentsListModel.setHasLiked(object_comment.getBoolean("hasLiked"));
                commentsListModel.setLikeCount(object_comment.getInt("likeCount"));
                commentsListModel.setUserName(object_comment.getString("user"));

                JSONArray array_response =  object_comment.getJSONArray("response");
                if(array_response.length()>0)
                {
                    JSONObject object_response = array_response.getJSONObject(0);
                    commentsListModel.setResponseText(object_response.getString("text"));
                }

                commentsListModel.setCreateDate(object_comment.getString("createdAt"));
                commentsListModel.setUpdateDate(object_comment.getString("updatedAt"));

                list_comments.add(commentsListModel);

                //Log.i(TAG, "ProductParsResponse: "+object_products.getString("url"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_comments;
    }



    // Interfaces ----------------------------------------------------------------------------------

    public interface onGetComments
    {
        void onGet(boolean msg, List<CommentModel> list_comments);
    }

    public interface onLikeComments
    {
        void onGet(boolean msg);
    }

    public interface onDislikeComments
    {
        void onGet(boolean msg);
    }

    public interface onCreateComment
    {
        void onGet(boolean msg);
    }

}
