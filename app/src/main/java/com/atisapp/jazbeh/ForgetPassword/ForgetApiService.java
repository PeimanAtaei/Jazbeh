package com.atisapp.jazbeh.ForgetPassword;

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
import com.atisapp.jazbeh.Login.LoginApiService;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ForgetApiService {

    private static final String TAG = "ForgetApiService";
    private Context context;
    private ProgressDialog progressDialog;
    private IdentitySharedPref identitySharedPref;

    public ForgetApiService(Context context)
    {

        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);

    }

    public void SendPhoneNumber(String phone_number,final onSetPhoneNumber onSetPhoneNumber)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات");
        progressDialog.show();


        JSONObject phone_object = new JSONObject();
        try {
            phone_object.put("phoneNumber",phone_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL + "/v1/auth/forgotPassword/sendActiveCode",phone_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();

                try {
                    boolean res = response.getBoolean("success");
                    onSetPhoneNumber.onSet(res);


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

                        case 500:
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
                            Log.e(TAG, "onErrorResponse: invalid code" );
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

    public void SendVerifyCode(String code,final onSetCode setCode)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات");
        progressDialog.show();


        JSONObject code_object = new JSONObject();
        try {
            code_object.put("phoneNumber",identitySharedPref.getPhoneNumber());
            code_object.put("code",code);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL + "/v1/auth/forgotPassword/checkActiveCode",code_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();

                try {
                    boolean res = response.getBoolean("success");
                    if(res)
                    {
                        String data = response.getString("data");
                        setCode.onSet(res,data);
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

                        case 500:
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
                            Log.e(TAG, "onErrorResponse: invalid code" );
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

    public void ChangePassword(String password,String data,final onSetPassword setPassword)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات");
        progressDialog.show();


        JSONObject password_object = new JSONObject();
        try {
            password_object.put("token",data);
            password_object.put("phoneNumber",identitySharedPref.getPhoneNumber());
            password_object.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL + "/v1/auth/resetPassword",password_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();

                try {
                    boolean res = response.getBoolean("success");
                    if (res)
                    {
                        String token = response.getString("token");
                        identitySharedPref.setToken("Bearer "+token);

                    }
                    setPassword.onSet(res);


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

                        case 500:
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
                            Log.e(TAG, "onErrorResponse: invalid code" );
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






    public interface onSetPhoneNumber
    {
        void onSet(boolean phone_response);
    }

    public interface onSetCode
    {
        void onSet(boolean code_response,String data);
    }

    public interface onSetPassword
    {
        void onSet(boolean password_response);
    }

}
