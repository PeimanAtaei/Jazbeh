package com.atisapp.jazbeh.PsycologicalTests;

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
import com.atisapp.jazbeh.PsycologicalTests.model.AnswerModel;
import com.atisapp.jazbeh.PsycologicalTests.model.QuestionModel;
import com.atisapp.jazbeh.PsycologicalTests.model.ResultModel;
import com.atisapp.jazbeh.PsycologicalTests.model.TestModel;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;
import com.atisapp.jazbeh.VideoPlayer.VideoCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAPIService {

    private static final String TAG = "TestAPIService";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private ProductPrefs productPrefs;
    private String myUrl;
    private int    productType;

    public TestAPIService(Context context)
    {
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        productPrefs = new ProductPrefs(context);
    }

    public void GetTestsList(final onGetTestsList getTestsList)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/psychologist" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getTestsList.onGet(res,TestsListParsResponse(response));
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

    public void GetSingleTest(String id,final onGetSingleTest getSingleTest)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/psychologist/"+id ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        getSingleTest.onGet(res,SingleTestParsResponse(response));
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

    public void AddTestCount(String id,final onAddTestCount addTestCount)
    {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, ClientConfigs.REST_API_BASE_URL +"/v2/psychologist/"+id+"/addCount" ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: product list received");
                try {
                    boolean res = response.getBoolean("success");
                    //productPrefs.set_product_msg(response.getString("message"));
                    if(res)
                    {
                        addTestCount.onGet(res);
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


    // Parsers ----------------------------------------------------------------------------------


    private List<TestModel> TestsListParsResponse(JSONObject response)
    {
        List<TestModel> list_tests = new ArrayList<>();

        try {
            JSONArray array_tests =  response.getJSONArray("data");
            for (int i = 0; i < array_tests.length(); i++) {

                TestModel testsListModel = new TestModel();
                JSONObject object_tests = array_tests.getJSONObject(i);

                testsListModel.setTestId(object_tests.getString("id"));
                testsListModel.setName(object_tests.getString("name"));
                testsListModel.setImage(object_tests.getString("image"));
                testsListModel.setQuestionCount(object_tests.getInt("questionsCount"));

                list_tests.add(testsListModel);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "TestParsResponse: "+list_tests.size());
        return list_tests;
    }

    private TestModel SingleTestParsResponse(JSONObject response)
    {
        TestModel testsListModel = new TestModel();

        try {


            JSONObject object_tests = response.getJSONObject("data");

            testsListModel.setTestId(object_tests.getString("id"));
            testsListModel.setName(object_tests.getString("name"));
            testsListModel.setDescription(object_tests.getString("description"));
            testsListModel.setImage(object_tests.getString("image"));

            List<QuestionModel> questionModelList = new ArrayList<>();
            JSONArray array_questions =  object_tests.getJSONArray("properties");
            for (int i = 0; i < array_questions.length(); i++) {

               QuestionModel questionModel = new QuestionModel();
               JSONObject object_question = array_questions.getJSONObject(i);


                List<AnswerModel> answerModelList  = new ArrayList<>();
                JSONArray array_answers =  object_question.getJSONArray("answers");

                for (int k = 0; k < array_answers.length(); k++) {

                    AnswerModel answerModel = new AnswerModel();
                    JSONObject object_answer = array_answers.getJSONObject(k);

                    answerModel.setAnswerId(object_answer.getString("_id"));
                    answerModel.setAnswerName(object_answer.getString("name"));
                    answerModel.setAnswerScore(object_answer.getInt("score"));

                    answerModelList.add(answerModel);


                }

                questionModel.setQuestionId(object_question.getString("_id"));
                questionModel.setQuestionTitle(object_question.getString("question"));
                questionModel.setAnswerModelList(answerModelList);
                Log.i(TAG, "SingleTestParsResponse: answerModelList size "+answerModelList.size());


                questionModelList.add(questionModel);


            }

            testsListModel.setQuestionModelList(questionModelList);
            Log.i(TAG, "SingleTestParsResponse: qusetion size "+questionModelList.size());

            List<ResultModel> resultModelList = new ArrayList<>();
            JSONArray array_results =  object_tests.getJSONArray("results");

            for (int i = 0; i < array_results.length(); i++) {

                ResultModel resultModel = new ResultModel();
                JSONObject object_result = array_results.getJSONObject(i);

                resultModel.setResultId(object_result.getString("_id"));
                resultModel.setResultText(object_result.getString("text"));
                resultModel.setResultTotal(object_result.getInt("total"));

                resultModelList.add(resultModel);

            }
            testsListModel.setResultModelList(resultModelList);
            testsListModel.setUserCount(object_tests.getInt("usersCount"));
            testsListModel.setQuestionCount(object_tests.getInt("questionsCount"));
            Log.i(TAG, "SingleTestParsResponse: resultModelList size "+resultModelList.size());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return testsListModel;
    }



    // Interfaces ----------------------------------------------------------------------------------

    public interface onGetTestsList
    {
        void onGet(boolean msg, List<TestModel> list_tests);
    }

    public interface onGetSingleTest
    {
        void onGet(boolean msg, TestModel testModel);
    }

    public interface onAddTestCount
    {
        void onGet(boolean msg);
    }

}


