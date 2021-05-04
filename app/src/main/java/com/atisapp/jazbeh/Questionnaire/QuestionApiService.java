package com.atisapp.jazbeh.Questionnaire;

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
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuestionApiService {

    private static final String TAG = "ProductListApiService";
    private Context context;
    private ProgressDialog progressDialog;
    private IdentitySharedPref identitySharedPref;
    private QuestionSharedPref questionSharedPref;

    public QuestionApiService(Context context)
    {

        this.context = context;
        questionSharedPref = new QuestionSharedPref(context);
        identitySharedPref = new IdentitySharedPref(context);

    }

    public void sendFile(QuestionModel questionModel,final onGetFileResponse fileResponse)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
        progressDialog.show();

        JSONObject file = new JSONObject();

        try {
            file.put("age",questionModel.getAge());
            file.put("heartDisease",questionModel.isHeartDisease());
            file.put("epilepsy",questionModel.isEpilepsy());
            file.put("hysteria",questionModel.isHysteria());
            file.put("bipolar",questionModel.isBipolar());
            file.put("hearing",questionModel.isHearing());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, ClientConfigs.REST_API_BASE_URL + "/v1/auth/updateDetails/file",file, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();

                try {
                    boolean res = response.getBoolean("success");
                    fileResponse.onGet(res);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Log.e(TAG, "onErrorResponse: erore"+error.getMessage());


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


    public interface onGetFileResponse
    {
        void onGet(boolean response);
    }


}
