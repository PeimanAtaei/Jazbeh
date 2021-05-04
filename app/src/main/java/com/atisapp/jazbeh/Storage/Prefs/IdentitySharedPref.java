package com.atisapp.jazbeh.Storage.Prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by GHADIR PC on 29/04/2017.
 */

public class IdentitySharedPref {

    private static final String IDENTITY_SHARED_PREF_NAME = "identity_sharedpref";
    private static final String TOKEN_KEY = "token";
    private static final String LANGUAGE_KEY = "language";
    private static final String AGE_KEY = "age";
    private static final String CITY_KEY = "city";
    private static final String PHONE_NUMBER_KEY = "phone_number";
    private static final String PHONE_PASSWORD_KEY = "password";
    private static final String NAME_KEY = "name";
    private static final String INVITATION_CODE_KEY = "invitation_code";
    private static final String IS_REGISTERED_KEY = "is_registered";
    private static final String FREE_ACCOUNT_KEY = "free_account";
    private static final String WALLET_BALANCE_KEY = "wallet_balance";
    private static final String DOCUMENT_KEY = "document";
    private static final String AVATAR_KEY = "avatar";




    private SharedPreferences identity_SharedPreferences ;
    private Context context;
    private String  new_token = "";



    public IdentitySharedPref(Context context)
    {
        this.context = context;
        identity_SharedPreferences = context.getSharedPreferences(IDENTITY_SHARED_PREF_NAME,context.MODE_PRIVATE);
    }

    public void clearAllData()
    {
        identity_SharedPreferences.edit().clear().apply();
    }



    public void setAvatarKey(String avatarKey)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putString(AVATAR_KEY,avatarKey);
        editor.apply();
    }

    public void setPhoneNumber(String phoneNumber)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putString(PHONE_NUMBER_KEY,phoneNumber);
        editor.apply();
    }

    public void setPassword(String password)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putString(PHONE_PASSWORD_KEY,password);
        editor.apply();
    }

    public void setCity(String city)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putString(CITY_KEY,city);
        editor.apply();
    }

    public void setName(String city)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putString(NAME_KEY,city);
        editor.apply();
    }

    public void setAge(int age)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putInt(AGE_KEY,age);
        editor.apply();
    }

    public void setDocument(int state)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putInt(DOCUMENT_KEY,state);
        editor.apply();
    }

    public void setToken(String token)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putString(TOKEN_KEY,token);
        editor.apply();
    }

    public void setInvitationCode(int code)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putInt(INVITATION_CODE_KEY,code);
        editor.apply();
    }

    public void setLanguage(String lang)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putString(LANGUAGE_KEY,lang);
        editor.apply();
    }

    public void setIsRegistered(int code)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putInt(IS_REGISTERED_KEY,code);
        editor.apply();
    }

    public void setFreeAccount(int code)
    {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putInt(FREE_ACCOUNT_KEY,code);
        editor.apply();
    }


    public void setWalletBalance(int balance) {
        SharedPreferences.Editor editor = identity_SharedPreferences.edit();
        editor.putInt(WALLET_BALANCE_KEY, balance);
        editor.apply();
    }

    public int getWalletBalance()
    {
        return identity_SharedPreferences.getInt(WALLET_BALANCE_KEY,0);
    }

    public String getAvatarKey()
    {
        return identity_SharedPreferences.getString(AVATAR_KEY,"");
    }

    public String getPhoneNumber()
    {
        return identity_SharedPreferences.getString(PHONE_NUMBER_KEY,"98936***");
    }

    public String getPassword()
    {
        return identity_SharedPreferences.getString(PHONE_PASSWORD_KEY,"123456789");
    }

    public String getCity()
    {
        return identity_SharedPreferences.getString(CITY_KEY,"Tehran");
    }

    public String getName()
    {
        return identity_SharedPreferences.getString(NAME_KEY,"user");
    }

    public int getAge()
    {
        return identity_SharedPreferences.getInt(AGE_KEY,20);
    }

    public int getDocument()
    {
        return identity_SharedPreferences.getInt(DOCUMENT_KEY,0);
    }

    public String getToken()
    {
        return identity_SharedPreferences.getString(TOKEN_KEY,new_token);
        //return new_token;
    }

    public int getInvitationCode()
    {
        return identity_SharedPreferences.getInt(INVITATION_CODE_KEY,0);
        //return new_token;
    }

    public String getLanguage()
    {
        return identity_SharedPreferences.getString(LANGUAGE_KEY,"fa");
        //return new_token;
    }

    public int getIsRegistered()
    {
        return identity_SharedPreferences.getInt(IS_REGISTERED_KEY,0);
        //return new_token;
    }

    public int getIsFreeAccount()
    {
        return identity_SharedPreferences.getInt(FREE_ACCOUNT_KEY,0);
        //return new_token;
    }


}
