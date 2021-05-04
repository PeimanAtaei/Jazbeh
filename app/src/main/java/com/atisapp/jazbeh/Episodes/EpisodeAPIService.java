package com.atisapp.jazbeh.Episodes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Debug;
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
import com.atisapp.jazbeh.ProductList.PracticeModel;
import com.atisapp.jazbeh.ProductList.ProductListApiService;
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

public class EpisodeAPIService {

    private static final String TAG = "EpisodeApiService";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private ProductPrefs productPrefs;
    private String myUrl;
    private int    productType;
    private boolean wallet;


    public EpisodeAPIService(Context context)
    {

        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);


    }

    public void GetEpisodesWithCategoryID(String categoryId,final onGetEpisodes getEpisodes)
    {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v1/categories/"+ categoryId +"/episodes",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    //Log.i(TAG, "onResponse Episode : "+res);
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getEpisodes.onGet(res,EpisodeParserResponse(response));
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

    public void GetEpisodesWithSearchKey(String Key,final onGetSearchedEpisodes searchedEpisodes)
    {
        Log.i(TAG, "GetCoursesWithCategoryID: start search episode API "+Key);

        String word =Key;
        try {
            word = URLEncoder.encode(Key, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/episodes/search?keyword="+word,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    //Log.i(TAG, "onResponse Episode : "+res);
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        searchedEpisodes.onGet(res,EpisodeParserResponse(response));
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

    public void GetEpisodesWithCourseID(String courseId,final onGetEpisodesWithCourse getEpisodes)
    {



        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/courses/"+ courseId +"/episodes",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getEpisodes.onGet(res,EpisodeParserResponse(response));
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

    public void GetSingleEpisode(String episodeId,final onGetSingleEpisode getEpisodes)
    {



        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/episodes/"+ episodeId +"/?isRegistered=true",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getEpisodes.onGet(res,SingleEpisodeParserResponse(response));
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

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,0 , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(objectRequest);

    }

    public void GetSingleEpisodePerches(String episodeId, boolean useWallet, final onGetEpisodesPerches getEpisodes)
    {


        this.wallet = useWallet;
        String urlType = "/purchase" ;
        if(wallet)
        {
            urlType = "/purchase?wallet=true";
            Log.i(TAG, "GetSingleEpisodePerches: use wallet");
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/episodes/"+ episodeId+urlType ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getEpisodes.onGet(res,response.getString("data"));
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

    public void GetMyEpisodes(String type,final onGetEpisodes getEpisodes)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/users/purchases/episodes?type="+type,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    Log.i(TAG, "onResponse Episode : "+res);
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getEpisodes.onGet(res,EpisodeParserResponse(response));
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

    public void GetFreeEpisodes(final onGetEpisodes getEpisodes)
    {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/episodes/free",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    Log.i(TAG, "onResponse Episode : "+res);
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getEpisodes.onGet(res,EpisodeParserResponse(response));
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

    public void LikeEpisode(String episodeID,final onLikeEpisode likeEpisode)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL +"/v2/episodes/"+episodeID+"/like",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    likeEpisode.onGet(res);

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

    public void DisLikeEpisode(String episodeID,final onLikeEpisode likeEpisode)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.DELETE, ClientConfigs.REST_API_BASE_URL +"/v2/episodes/"+episodeID+"/like",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    likeEpisode.onGet(res);

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

    public void FavoriteEpisode(String episodeID,final onFavorite favoriteEpisode)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL +"/v2/episodes/"+episodeID+"/favorite",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.i(TAG, "onResponse: favorite response received ");
                try {
                    boolean res = response.getBoolean("success");
                    Log.i(TAG, "onResponse: favorite response received "+res);

                    favoriteEpisode.onGet(res);

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

    public void DeleteFavoriteEpisode(String episodeID,final onFavorite favoriteEpisode)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.DELETE, ClientConfigs.REST_API_BASE_URL +"/v2/episodes/"+episodeID+"/favorite",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Log.i(TAG, "onResponse: product list recived ");
                try {
                    boolean res = response.getBoolean("success");
                    favoriteEpisode.onGet(res);

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

    public void EpisodePlayCount(String episodeID,final onPlayEpisode playEpisode)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/episodes/"+episodeID+"/addPlayCount",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.i(TAG, "onResponse: favorite response received ");
                try {
                    boolean res = response.getBoolean("success");
                    //Log.i(TAG, "onResponse: favorite response received "+res);

                    playEpisode.onGet(res);

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

    private List<ProductModel> EpisodeParserResponse(JSONObject response)
    {
        List<ProductModel> list_episode = new ArrayList<>();

        try {

            JSONArray array_episodes =  response.getJSONArray("data");
            for (int i = 0; i < array_episodes.length(); i++) {

                ProductModel episodeListModel = new ProductModel();
                JSONObject object_episode = array_episodes.getJSONObject(i);

                episodeListModel.setProduct_id(object_episode.getString("id"));
                episodeListModel.setTitle(object_episode.getString("title"));
                episodeListModel.setNumber(object_episode.getInt("number"));
                episodeListModel.setTime(object_episode.getString("time"));

                if(object_episode.getString("content").equals("video"))
                {
                    episodeListModel.setImage(object_episode.getString("imageUrl"));
                }

                //Log.i(TAG, "EpisodeParserResponse: "+object_episode.getString("title"));

                /*
                episodeListModel.setPrice(object_episode.getInt("price"));
                episodeListModel.setPlayCount(object_episode.getInt("playCount"));
                episodeListModel.setViewCount(object_episode.getInt("viewCount"));
                episodeListModel.setCommentCount(object_episode.getInt("commentCount"));
                episodeListModel.setCategory(productPrefs.get_product_group_id());

                episodeListModel.setExplain(object_episode.getString("explain"));
                episodeListModel.setUsage(object_episode.getString("usage"));
                episodeListModel.setAlarm(object_episode.getString("alarm"));
                episodeListModel.setType(object_episode.getString("type"));

                episodeListModel.setPodcastUrl(object_episode.getString("podcastUrl"));
                episodeListModel.setVersion(object_episode.getInt("__v"));*/


                list_episode.add(episodeListModel);

                //Log.i(TAG, "ProductParsResponse: "+object_products.getString("url"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "EpisodeParserResponse: "+list_episode.size());
        return list_episode;
    }

    private ProductModel SingleEpisodeParserResponse(JSONObject response)
    {
        ProductModel single_episode = new ProductModel();
        List<PracticeModel> practiceModelList = new ArrayList<>();

        try {

            JSONObject object_episode =  response.getJSONObject("data");

            single_episode.setProduct_id(object_episode.getString("id"));
            single_episode.setTitle(object_episode.getString("title"));
            single_episode.setExplain(object_episode.getString("explain"));
            single_episode.setUsage(object_episode.getString("usage"));
            single_episode.setAlarm(object_episode.getString("alarm"));
            single_episode.setType(object_episode.getString("type"));
            single_episode.setNumber(object_episode.getInt("number"));
            single_episode.setTime(object_episode.getString("time"));
            single_episode.setContent(object_episode.getString("content"));
            single_episode.setPrice(object_episode.getInt("price"));
            single_episode.setPlayCount(object_episode.getInt("playCount"));
            single_episode.setViewCount(object_episode.getInt("viewCount"));
            single_episode.setLikeCount(object_episode.getInt("likeCount"));
            single_episode.setHasLiked(object_episode.getBoolean("hasLiked"));
            single_episode.setFavorite(object_episode.getBoolean("isFavorite"));
            single_episode.setCommentCount(object_episode.getInt("commentCount"));
            single_episode.setLocked(object_episode.getBoolean("isEpisodeLocked"));
            single_episode.setCategory(productPrefs.get_product_group_id());
            if(!object_episode.getBoolean("isEpisodeLocked")) {

                Log.i(TAG, "SingleEpisodeParserResponse: start url");

                if(single_episode.getContent().equals("video"))
                {
                    single_episode.setVideoUrl(object_episode.getString("videoUrl"));
                    Log.i(TAG, "SingleEpisodeParserResponse:  url "+single_episode.getVideoUrl());
                }else
                {
                    single_episode.setPodcastUrl(object_episode.getString("podcastUrl"));
                }
            }
            if (object_episode.getJSONObject("practices") != null)
            {
                JSONObject object_practice = object_episode.getJSONObject("practices");

                JSONArray array_image =  object_practice.getJSONArray("images");
                for (int i = 0; i < array_image.length(); i++) {
                    PracticeModel practiceModel = new PracticeModel();
                    JSONObject object_image = array_image.getJSONObject(i);
                    practiceModel.setPracticeId(object_image.getString("id"));
                    practiceModel.setPracticeType(PracticeModel.IMAGE);
                    practiceModel.setPracticeURL(object_image.getString("url"));

                    practiceModelList.add(practiceModel);
                    //Log.i(TAG, "SingleEpisodeParserResponse: "+object_image.getString("url"));
                }

                JSONArray array_pdf =  object_practice.getJSONArray("documents");
                for (int i = 0; i < array_pdf.length(); i++) {
                    PracticeModel practiceModel = new PracticeModel();
                    JSONObject object_pdf = array_pdf.getJSONObject(i);
                    practiceModel.setPracticeId(object_pdf.getString("id"));
                    practiceModel.setPracticeType(PracticeModel.PDF);
                    practiceModel.setPracticeURL(object_pdf.getString("url"));

                    practiceModelList.add(practiceModel);
                    //Log.i(TAG, "SingleEpisodeParserResponse: "+object_pdf.getString("url"));
                }

                JSONArray array_voice =  object_practice.getJSONArray("voices");
                for (int i = 0; i < array_voice.length(); i++) {
                    PracticeModel practiceModel = new PracticeModel();
                    JSONObject object_voice = array_voice.getJSONObject(i);
                    practiceModel.setPracticeId(object_voice.getString("id"));
                    practiceModel.setPracticeType(PracticeModel.VOICE);
                    practiceModel.setPracticeURL(object_voice.getString("url"));

                    practiceModelList.add(practiceModel);
                    //Log.i(TAG, "SingleEpisodeParserResponse: "+object_voice.getString("url"));
                }

                //Log.i(TAG, "SingleEpisodeParserResponse: practice list size : "+practiceModelList.size());
                single_episode.setPracticeModelList(practiceModelList);

            }


                //Log.i(TAG, "ProductParsResponse: "+object_products.getString("url"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return single_episode;
    }



    // Interfaces ----------------------------------------------------------------------------------


    public interface onGetEpisodes
    {
        void onGet(boolean msg, List<ProductModel> list_episodes);
    }

    public interface onGetSearchedEpisodes
    {
        void onGet(boolean msg, List<ProductModel> list_episodes);
    }

    public interface onGetSingleEpisode
    {
        void onGet(boolean msg, ProductModel single_episodes);
    }

    public interface onGetEpisodesWithCourse
    {
        void onGet(boolean msg, List<ProductModel> list_episodes);
    }

    public interface onGetEpisodesPerches
    {
        void onGet(boolean msg, String URL);
    }

    public interface onLikeEpisode
    {
        void onGet(boolean msg);
    }

    public interface onFavorite
    {
        void onGet(boolean msg);
    }

    public interface onPlayEpisode
    {
        void onGet(boolean msg);
    }
}
