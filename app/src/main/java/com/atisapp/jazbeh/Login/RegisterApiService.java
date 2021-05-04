package com.atisapp.jazbeh.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;

public class RegisterApiService {

    private static final String TAG = "RegisterApiService";
    private Context context;
    private ProgressDialog progressDialog;
    private IdentitySharedPref identitySharedPref;

    public RegisterApiService(Context context)
    {

        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);

    }


    public void RegisterUser(String phone_number,String password,final onRegisterUser registerUser)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات");
        progressDialog.show();


        JSONObject login_object = new JSONObject();
        try {
            login_object.put("phoneNumber",phone_number);
            login_object.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL + "/v1/auth/register",login_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();

                try {
                    boolean res = response.getBoolean("success");
                    //int code = response.getInt("code");
                    if(res)
                    {
                        String token = response.getString("token");
                        identitySharedPref.setToken("Bearer "+token);
                        registerUser.onRegister(res);
                    }


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
                        case 400:
                        {
                            try {
                                String body = new String(error.networkResponse.data,"UTF-8");
                                JSONObject obj = new JSONObject(body);
                                Toast.makeText(context,obj.getString("error"),Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case 401:
                        {
                            try {
                                String body = new String(error.networkResponse.data,"UTF-8");
                                JSONObject obj = new JSONObject(body);
                                Log.i(TAG, "onErrorResponse: "+obj.getString("error"));
                                Toast.makeText(context,obj.getString("error"),Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        });

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(objectRequest);

    }

    public interface onRegisterUser
    {
        void onRegister(boolean login_response);
    }
}
