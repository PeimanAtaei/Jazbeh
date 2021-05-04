package com.atisapp.jazbeh.Questionnaire;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by GHADIR PC on 29/04/2017.
 */

public class QuestionSharedPref {

    private static final String FILE_SHARED_PREF_NAME = "file_sharedpref";
    private static final String AGE_KEY = "age";
    private static final String HEARTDISEASE_KEY = "heartDisease";
    private static final String EPILEPSY_KEY = "epilepsy";
    private static final String HYSTERIA_KEY = "hysteria";
    private static final String BIPOLAR_KEY = "bipolar";
    private static final String HEARING_KEY = "hearing";



    private SharedPreferences file_SharedPreferences ;
    private Context context;


    public QuestionSharedPref(Context context)
    {
        this.context = context;
        file_SharedPreferences = context.getSharedPreferences(FILE_SHARED_PREF_NAME,context.MODE_PRIVATE);
    }

    public void clearAllData()
    {
        file_SharedPreferences.edit().clear().apply();
    }






    public void setAge(boolean fullName)
    {
        SharedPreferences.Editor editor = file_SharedPreferences.edit();
        editor.putBoolean(AGE_KEY,fullName);
        editor.apply();
    }

    public void setHeartDisease(boolean fullName)
    {
        SharedPreferences.Editor editor = file_SharedPreferences.edit();
        editor.putBoolean(HEARTDISEASE_KEY,fullName);
        editor.apply();
    }

    public void setEpilepsy(boolean phoneNumber)
    {
        SharedPreferences.Editor editor = file_SharedPreferences.edit();
        editor.putBoolean(EPILEPSY_KEY,phoneNumber);
        editor.apply();
    }

    public void setHysteria(boolean city)
    {
        SharedPreferences.Editor editor = file_SharedPreferences.edit();
        editor.putBoolean(HYSTERIA_KEY,city);
        editor.apply();
    }

    public void setBipolar(boolean age)
    {
        SharedPreferences.Editor editor = file_SharedPreferences.edit();
        editor.putBoolean(BIPOLAR_KEY,age);
        editor.apply();
    }

    public void setHearing(boolean token)
    {
        SharedPreferences.Editor editor = file_SharedPreferences.edit();
        editor.putBoolean(HEARING_KEY,token);
        editor.apply();
    }








    public boolean getAge()
    {
        return file_SharedPreferences.getBoolean(AGE_KEY,false);
    }

    public boolean getHeartDisease()
    {
        return file_SharedPreferences.getBoolean(HEARTDISEASE_KEY,false);
    }

    public boolean getEpilepsy()
    {
        return file_SharedPreferences.getBoolean(EPILEPSY_KEY,false);
    }

    public boolean getHysteria()
    {
        return file_SharedPreferences.getBoolean(HYSTERIA_KEY,false);
    }

    public boolean getBipolar()
    {
        return file_SharedPreferences.getBoolean(BIPOLAR_KEY,false);
    }

    public boolean getHearing()
    {
        return file_SharedPreferences.getBoolean(HEARING_KEY,false);
        //return new_token;
    }

}
