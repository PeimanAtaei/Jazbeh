package com.atisapp.jazbeh.Sharing;

import android.app.ProgressDialog;
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
import com.atisapp.jazbeh.Questionnaire.QuestionSharedPref;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class SharingAPIService {

    private static final String TAG = "ProfileApiService";
    private Context context;
    private ProgressDialog progressDialog;
    private IdentitySharedPref identitySharedPref;
    private QuestionSharedPref questionSharedPref;

    public SharingAPIService(Context context)
    {
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        questionSharedPref = new QuestionSharedPref(context);
    }


    public void ReceiveShareInfo(final onGetShareLink shareLink)
    {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL + "/v2/users/referrals/token",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean res = response.getBoolean("success");

                    if(res)
                    {
                        Log.i(TAG, "onResponse: user recieved");
                        shareLink.onGet(res,LinkParsResponse(response));

                    }else {
                        Toast.makeText(context,"اختلال در بروز رسانی پروفایل",Toast.LENGTH_SHORT).show();
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





    private String LinkParsResponse(JSONObject response)
    {
        String link = "";

        try {
            JSONObject object_info = response.getJSONObject("data");
            link =object_info.getString("token");
            Log.i(TAG, "ProfileParsResponse: "+link);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return link;
    }






    public interface onGetShareLink
    {
        void onGet(boolean response, String link);
    }


}
