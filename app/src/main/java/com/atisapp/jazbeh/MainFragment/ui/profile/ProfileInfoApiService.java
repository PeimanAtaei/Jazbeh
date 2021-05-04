package com.atisapp.jazbeh.MainFragment.ui.profile;

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

public class ProfileInfoApiService {

    private static final String TAG = "ProfileApiService";
    private Context context;
    private ProgressDialog progressDialog;
    private IdentitySharedPref identitySharedPref;
    private QuestionSharedPref questionSharedPref;

    public ProfileInfoApiService(Context context)
    {

        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        questionSharedPref = new QuestionSharedPref(context);

    }

    public void SendPersonalInfo(UserModel userModel,final onGetAddPersonalInfoResponse getAddPersonalInfoResponse)
    {
        if(progressDialog== null)
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات");
            progressDialog.show();
        }


        JSONObject user_object = new JSONObject();
        try {
            if(userModel.getFullName() != null)
                user_object.put("fullName",userModel.getFullName());
            if(userModel.getAge()>0)
                user_object.put("age",userModel.getAge());
            if(userModel.getInvitationCode()>0)
                user_object.put("invitationCode",userModel.getInvitationCode());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "SendPersonalInfo: start req");


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, ClientConfigs.REST_API_BASE_URL + "/v1/auth/updateDetails",user_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    boolean res = response.getBoolean("success");

                    Log.i(TAG, "onResponse: "+res);
                    getAddPersonalInfoResponse.onGet(res,SendProfileParsResponse(response));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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


    public void ReceivePersonalInfo(final onReceivePersonalInfoResponse receivePersonalInfoResponse)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات");
        progressDialog.show();


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL + "/v1/auth/me",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    boolean res = response.getBoolean("success");

                    if(res)
                    {
                        Log.i(TAG, "onResponse: user recieved");
                        receivePersonalInfoResponse.onGet(res,ProfileParsResponse(response));

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
                progressDialog.dismiss();
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


    public void UpdatePassword(String currentPassword,String newPassword,final onUpdatePasswordResponse passwordResponse)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال بروز رسانی رمز عبور");
        progressDialog.show();


        JSONObject password_object = new JSONObject();
        try {
            password_object.put("currentPassword",currentPassword);
            password_object.put("newPassword",newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, ClientConfigs.REST_API_BASE_URL + "/v1/auth/updatePassword",password_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    boolean res = response.getBoolean("success");
                    if (res)
                    {
                        String token = response.getString("token");
                        identitySharedPref.setToken("Bearer "+token);
                    }
                    passwordResponse.onGet(res);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, "onErrorResponse: erore"+error.getMessage());

                passwordResponse.onGet(false);


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



    private UserModel SendProfileParsResponse(JSONObject response)
    {
        UserModel userModel = new UserModel();
        try {

            JSONObject object_info = response.getJSONObject("data");

            userModel.setRole(object_info.getString("role"));
            userModel.setActive(object_info.getBoolean("active"));
            userModel.setUser_id(object_info.getString("_id"));
            userModel.setPhoneNumber(object_info.getString("phoneNumber"));
            if(object_info.getString("invitationCode") == null)
            {
                userModel.setInvitationCode(0);
            }else {
                userModel.setInvitationCode(object_info.getInt("invitationCode"));
            }
            userModel.setAge(object_info.getInt("age"));
            userModel.setFullName(object_info.getString("fullName"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userModel;
    }

    private UserModel ProfileParsResponse(JSONObject response)
    {
        UserModel userModel5 = new UserModel();

        try {
            JSONObject object_info = response.getJSONObject("data");

            userModel5.setUser_id(object_info.getString("id"));
            userModel5.setFullName(object_info.getString("fullName"));
            userModel5.setPhoneNumber(object_info.getString("phoneNumber"));
            userModel5.setActive(object_info.getBoolean("active"));
            userModel5.setAge(object_info.getInt("age"));
            userModel5.setInvitationCode(object_info.getInt("invitationCode"));
            userModel5.setRole(object_info.getString("role"));

            JSONObject object_file = response.getJSONObject("file");

            Log.i(TAG, "ProfileParsResponse: "+userModel5.getFullName());

            questionSharedPref.setAge(object_file.getBoolean("age"));
            questionSharedPref.setHeartDisease(object_file.getBoolean("heartDisease"));
            questionSharedPref.setEpilepsy(object_file.getBoolean("epilepsy"));
            questionSharedPref.setHysteria(object_file.getBoolean("hysteria"));
            questionSharedPref.setBipolar(object_file.getBoolean("bipolar"));
            questionSharedPref.setHearing(object_file.getBoolean("hearing"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userModel5;
    }







    public interface onGetAddPersonalInfoResponse
    {
        void onGet(boolean response,UserModel userModel);
    }

    public interface onReceivePersonalInfoResponse
    {
        void onGet(boolean response,UserModel userModel);
    }

    public interface onUpdatePasswordResponse
    {
        void onGet(boolean response);
    }


}
