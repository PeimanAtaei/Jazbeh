package com.atisapp.jazbeh.MainFragment;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atisapp.jazbeh.Core.ClientConfigs;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeAPIService {

    private static final String TAG = "HomeAPIService";
    private Context context;
    private IdentitySharedPref identitySharedPref;

    public HomeAPIService(Context context)
    {
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
    }

    public void CheckUserToken(final onCheckToken checkToken)
    {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v1/auth/checkAccessToken" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean res = response.getBoolean("success");
                    Log.i(TAG, "token verification: "+res);

                    checkToken.onCheck(res);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "onErrorResponse: erore"+error.getMessage());
                //checkToken.onCheck(false);

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

    public void RefreshToken(final onRefreshToken refreshToken)
    {
        JSONObject login_object = new JSONObject();
        try {
            login_object.put("phoneNumber",identitySharedPref.getPhoneNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL + "/v1/auth/refreshToken",login_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean res = response.getBoolean("success");
                    Log.i(TAG, "onResponse: "+res);
                    //int code = response.getInt("code");
                    if(res)
                    {

                        String token = response.getString("token");
                        identitySharedPref.setToken("Bearer "+token);

                    }
                    refreshToken.onRefresh(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: erore"+error.getMessage());
            }
        });

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(objectRequest);

    }

    public void GetLastUpdateVersion(final onGetUpdate getUpdate)
    {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/apps/lastAppPublished" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");

                try {
                    boolean res = response.getBoolean("success");
                    Log.i(TAG, "onResponse: "+res);
                    if(res)
                    {
                        getUpdate.onGet(res,FeatureParsResponse(response));
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

    private UpdateModel FeatureParsResponse(JSONObject response)
    {
        List<String> list_features = new ArrayList<>();
        UpdateModel updateModel = new UpdateModel();

        try {

            JSONObject object_data =  response.getJSONObject("data");

            int code = object_data.getInt("versionCode");
            String name = object_data.getString("versionName");
            boolean published = object_data.getBoolean("published");
            String marketUrl = object_data.getString("marketUrl");
            String directUrl = object_data.getString("directUrl");

            JSONArray array_features =  object_data.getJSONArray("releaseNotes");

            for (int i = 0; i < array_features.length(); i++) {

                String feature;
                JSONObject object_course = array_features.getJSONObject(i);

                feature = object_course.getString("text");
                list_features.add(feature);

            }

            updateModel.setVersionCode(code);
            updateModel.setVersionName(name);
            updateModel.setPublished(published);
            updateModel.setMarketURL(marketUrl);
            updateModel.setDirectURL(directUrl);
            updateModel.setFeaturesList(list_features);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return updateModel;
    }

    public interface onGetUpdate
    {
        void onGet(boolean msg,UpdateModel updateModel);
    }

    public interface onCheckToken
    {
        void onCheck(boolean isTokenVerified);
    }

    public interface onRefreshToken
    {
        void onRefresh(boolean result);
    }


}
