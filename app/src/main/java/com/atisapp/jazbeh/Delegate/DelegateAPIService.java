package com.atisapp.jazbeh.Delegate;

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
import com.atisapp.jazbeh.Comment.CommentModel;
import com.atisapp.jazbeh.Core.ClientConfigs;
import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Delegate.CheckOut.CheckOutModel;
import com.atisapp.jazbeh.Delegate.ui.balance.BalanceModel;
import com.atisapp.jazbeh.Delegate.ui.sell.PurchasesModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelegateAPIService {

    private static final String TAG = "DelegateAPIService";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private ProductPrefs productPrefs;



    public DelegateAPIService(Context context)
    {
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);
    }

    public void GetDelegateStatus(final onGetDelegateStatus getDelegateStatus)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/delegates/status" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean res = response.getBoolean("success");
                    if(res)
                    {
                        getDelegateStatus.onGet(response.getString("data"));
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

    public void GetDelegateDashboard(final onGetDelegateDashboard getDelegateDashboard)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/delegates/dashboard" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean res = response.getBoolean("success");
                    if(res)
                    {
                        getDelegateDashboard.onGet(DashboardParsResponse(response));
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

    public void CreateNewDelegate(DelegateModel delegateModel,final onCreateNewDelegate createNewDelegate)
    {

        JSONObject delegate_object = new JSONObject();
        try {
            delegate_object.put("name",delegateModel.getName());
            delegate_object.put("city",delegateModel.getCity());
            delegate_object.put("education",delegateModel.getEducation());
            delegate_object.put("cardNumber",delegateModel.getCardNumber());
            delegate_object.put("collaborate",delegateModel.getCollaborate());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL +"/v2/delegates" ,delegate_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean res = response.getBoolean("success");
                    if(res)
                    {
                        createNewDelegate.onGet(res);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                Log.e(TAG, "onErrorResponse: erore"+error.getMessage()+error.networkResponse);
                Toast.makeText(context,"اطلاعات شما قبلا ثبت شده و در حال بررسی می باشد",Toast.LENGTH_LONG).show();


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 401:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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
                            break;
                        }
                        case 500:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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

    public void GetPurchase(final onGetParchases getParchases)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/delegates/transactions" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean res = response.getBoolean("success");
                    if(res)
                    {
                        getParchases.onGet(PurchaseParsResponse(response));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                Log.e(TAG, "onErrorResponse: erore"+error.getMessage()+error.networkResponse);
                Toast.makeText(context,"اطلاعات شما قبلا ثبت شده و در حال بررسی می باشد",Toast.LENGTH_LONG).show();


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 401:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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
                            break;
                        }
                        case 500:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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

    public void GetDelegateInfo(final onGetInfo getInfo)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/delegates/me" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean res = response.getBoolean("success");
                    if(res)
                    {
                        getInfo.onGet(InformationParsResponse(response));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                Log.e(TAG, "onErrorResponse: erore"+error.getMessage()+error.networkResponse);
                Toast.makeText(context,"مشکل در دریافت اطلاعات",Toast.LENGTH_LONG).show();


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 401:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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
                            break;
                        }
                        case 500:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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

    public void ModifyDelegateInformation(DelegateModel delegateModel,final onCreateNewDelegate createNewDelegate)
    {

        JSONObject delegate_object = new JSONObject();
        try {
            delegate_object.put("name",delegateModel.getName());
            delegate_object.put("city",delegateModel.getCity());
            delegate_object.put("education",delegateModel.getEducation());
            delegate_object.put("cardNumber",delegateModel.getCardNumber());
            delegate_object.put("collaborate",delegateModel.getCollaborate());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, ClientConfigs.REST_API_BASE_URL +"/v2/delegates/updateDetails" ,delegate_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean res = response.getBoolean("success");
                    if(res)
                    {
                        createNewDelegate.onGet(res);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                Log.e(TAG, "onErrorResponse: erore"+error.getMessage()+error.networkResponse);
                Toast.makeText(context,"اطلاعات شما قبلا ثبت شده و در حال بررسی می باشد",Toast.LENGTH_LONG).show();


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 401:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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
                            break;
                        }
                        case 500:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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

    public void GetCheckOuts(final onGetCheckOuts getCheckOuts)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/delegates/checkouts" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean res = response.getBoolean("success");
                    if(res)
                    {
                        boolean isChecking = response.getBoolean("isCheckout");
                        getCheckOuts.onGet(isChecking,CheckOutsParsResponse(response));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                Log.e(TAG, "onErrorResponse: erore"+error.getMessage()+error.networkResponse);
                Toast.makeText(context,"اطلاعات شما قبلا ثبت شده و در حال بررسی می باشد",Toast.LENGTH_LONG).show();


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 401:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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
                            break;
                        }
                        case 500:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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

    public void CreateCheckOuts(final onCreateCheckOuts createCheckOuts)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, ClientConfigs.REST_API_BASE_URL +"/v2/delegates/checkouts" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean res = response.getBoolean("success");
                    createCheckOuts.onGet(res);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                Log.e(TAG, "onErrorResponse: erore"+error.getMessage()+error.networkResponse);
                Toast.makeText(context,"اطلاعات شما قبلا ثبت شده و در حال بررسی می باشد",Toast.LENGTH_LONG).show();


                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 401:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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
                            break;
                        }
                        case 500:
                        {
                            Log.i(TAG, "onErrorResponse: start 500");
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

    private BalanceModel DashboardParsResponse(JSONObject response)
    {
        BalanceModel dashboard_model = new BalanceModel();

        try {

            JSONObject object_course =  response.getJSONObject("data");

            dashboard_model.setName(object_course.getString("name"));

            dashboard_model.setInvitationCode(object_course.getInt("invitationCode"));
            dashboard_model.setBalance(object_course.getInt("balance"));
            dashboard_model.setPurchasesCount(object_course.getInt("purchasesCount"));
            dashboard_model.setUsersCount(object_course.getInt("usersCount"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return dashboard_model;
    }

    private List<PurchasesModel> PurchaseParsResponse(JSONObject response)
    {
        List<PurchasesModel> list_purchase = new ArrayList<>();

        try {

            JSONArray array_purchase =  response.getJSONArray("data");
            Log.i(TAG, "CommentParsResponse: "+array_purchase.length());
            for (int i = 0; i < array_purchase.length(); i++) {


                PurchasesModel purchaseListModel = new PurchasesModel();
                JSONObject object_purchase = array_purchase.getJSONObject(i);

                purchaseListModel.setId(object_purchase.getString("id"));
                purchaseListModel.setBalance(object_purchase.getInt("balance"));
                purchaseListModel.setPaid(object_purchase.getBoolean("paid"));
                purchaseListModel.setUsed(object_purchase.getBoolean("used"));
                purchaseListModel.setType(object_purchase.getString("type"));
                purchaseListModel.setName(object_purchase.getString("productName"));
                purchaseListModel.setTrackNumber(object_purchase.getString("trackNumber"));
                purchaseListModel.setPrice(object_purchase.getInt("price"));
                purchaseListModel.setDate(object_purchase.getString("createdAt"));

                list_purchase.add(purchaseListModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_purchase;
    }

    private DelegateModel InformationParsResponse(JSONObject response)
    {
        DelegateModel info_model = new DelegateModel();

        try {

            JSONObject object_info =  response.getJSONObject("data");

            info_model.setName(object_info.getString("name"));
            info_model.setCity(object_info.getString("city"));
            info_model.setEducation(object_info.getString("education"));
            info_model.setCardNumber(object_info.getString("cardNumber"));
            info_model.setCollaborate(object_info.getString("collaborate"));
            info_model.setInvitationCode(object_info.getInt("invitationCode"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return info_model;
    }

    private List<CheckOutModel> CheckOutsParsResponse(JSONObject response)
    {
        List<CheckOutModel> list_checkOuts = new ArrayList<>();

        try {

            JSONArray array_checkOuts =  response.getJSONArray("data");
            Log.i(TAG, "CommentParsResponse: "+array_checkOuts.length());
            for (int i = 0; i < array_checkOuts.length(); i++) {


                CheckOutModel checkOutModel = new CheckOutModel();
                JSONObject object_checkOuts = array_checkOuts.getJSONObject(i);

                checkOutModel.setId(object_checkOuts.getString("id"));
                checkOutModel.setBalance(object_checkOuts.getInt("balance"));
                checkOutModel.setPaid(object_checkOuts.getBoolean("paid"));
                checkOutModel.setInvoiceNumber(object_checkOuts.getString("invoiceNumber"));
                checkOutModel.setRequestDate(object_checkOuts.getString("requestDate"));
                checkOutModel.setCheckOutDate(object_checkOuts.getString("checkoutDate"));

                Log.i(TAG, "CheckOutsParsResponse: "+object_checkOuts.getString("invoiceNumber")+" "+object_checkOuts.getString("requestDate"));


                list_checkOuts.add(checkOutModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_checkOuts;
    }

    // Interfaces ----------------------------------------------------------------------------------

    public interface onGetDelegateDashboard
    {
        void onGet(BalanceModel dashboardModel);
    }

    public interface onGetDelegatePurchases
    {
        void onGet(List<BalanceModel> dashboardModel);
    }

    public interface onCreateNewDelegate
    {
        void onGet(boolean response);
    }

    public interface onGetDelegateStatus
    {
        void onGet(String response);
    }

    public interface onGetParchases
    {
        void onGet(List<PurchasesModel> purchasesModelList);
    }

    public interface onGetInfo
    {
        void onGet(DelegateModel delegateModelList);
    }

    public interface onGetCheckOuts
    {
        void onGet(boolean inChecking,List<CheckOutModel> checkOutModels);
    }

    public interface onCreateCheckOuts
    {
        void onGet(boolean msg);
    }

}
