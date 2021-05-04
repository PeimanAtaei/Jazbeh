package com.atisapp.jazbeh.Storage.Prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class ProductPrefs {

    private static final String PRODUCT_SHARED_PREF_NAME = "product_prefs";
    private static final String PRODUCT_NAME_KEY = "product_name";
    private static final String PRODUCT_ID_KEY = "product_id";
    private static final String PRODUCT_MSG_KEY = "product_msg";
    private static final String PRODUCT_PRICE_KEY = "product_price";
    private static final String PRODUCT_EXPLAIN_KEY= "product_explain";
    private static final String PRODUCT_USAGE_KEY = "product_usage";
    private static final String PRODUCT_ALARM_KEY = "product_alarm";
    private static final String PRODUCT_GROUP_ID_KEY = "product_group_id";
    private static final String PRODUCT_PACKAGE_ID_KEY = "product_package_id";
    private static final String PRODUCT_TIME_KEY = "product_time";
    private static final String PRODUCT_URL_KEY = "product_url";
    private static final String PRODUCT_BOUGHT_KEY = "product_Bought";
    private static final String PRODUCT_IS_PACKAGE_KEY = "product_is_package";
    private static final String PRODUCT_TYPE_KEY = "product_Bought";
    private static final String PRACTICE_URL_KEY = "practice_url";
    private static final String IS_PRACTICE_KEY = "is_practice";
    private static final String PRODUCT_MASSAGE_KEY = "is_practice";
    private static final String PACKAGE_PRICE = "package_price";
    private static final String PRODUCTS_IS_PACKAGE = "products_is_package";


    private SharedPreferences product_prefs_SharedPreferences ;
    private Context context;

    public ProductPrefs(Context context)
    {
        this.context = context;
        product_prefs_SharedPreferences = context.getSharedPreferences(PRODUCT_SHARED_PREF_NAME,context.MODE_PRIVATE);
    }

    public void clearAllData()
    {
        product_prefs_SharedPreferences.edit().clear().apply();
    }





    // set date in prefs ---------------------------------------------------------------------------

    public void set_practice(boolean res)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putBoolean(IS_PRACTICE_KEY,res);
        editor.apply();
    }

    public void set_practice_url(String url)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRACTICE_URL_KEY,url);
        editor.apply();
    }


    public void set_product_name(String name)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_NAME_KEY,name);
        editor.apply();
    }

    public void set_product_msg(String name)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_MSG_KEY,name);
        editor.apply();
    }

    public void set_product_explain(String ex)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_EXPLAIN_KEY,ex);
        editor.apply();
    }

    public void set_product_usage(String usage)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_USAGE_KEY,usage);
        editor.apply();
    }

    public void set_product_alarm(String alarm)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_ALARM_KEY,alarm);
        editor.apply();
    }

    public void set_product_time(String time)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_TIME_KEY,time);
        editor.apply();
    }

    public void set_product_url(String url)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_URL_KEY,url);
        editor.apply();
    }

    public void set_product_id(String id)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_ID_KEY,id);
        editor.apply();
    }



    public void set_product_group_id(String group_d)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_GROUP_ID_KEY,group_d);
        editor.apply();
    }

    public void set_product_package_id(String package_d)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_PACKAGE_ID_KEY,package_d);
        editor.apply();
    }

    public void set_product_price(String price)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_PRICE_KEY,price);
        editor.apply();
    }

    public void set_product_massage(String msg)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PRODUCT_MASSAGE_KEY,msg);
        editor.apply();
    }

    public void set_product_bought(boolean res)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putBoolean(PRODUCT_BOUGHT_KEY,res);
        editor.apply();
    }

    public void set_is_package(boolean res)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putBoolean(PRODUCT_IS_PACKAGE_KEY,res);
        editor.apply();
    }

    public void set_product_type(int type)
    {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putInt(PRODUCT_TYPE_KEY,type);
        editor.apply();
    }

    public void set_product_package_price(String price) {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putString(PACKAGE_PRICE, price);
        editor.apply();
    }

    public void set_products_is_package(boolean productsIsPackage) {
        SharedPreferences.Editor editor = product_prefs_SharedPreferences.edit();
        editor.putBoolean(PRODUCTS_IS_PACKAGE, productsIsPackage);
        editor.apply();
    }



    // get date from prefs -------------------------------------------------------------------------

    public boolean get_products_is_package() {
        return product_prefs_SharedPreferences.getBoolean(PRODUCTS_IS_PACKAGE,false);
    }

    public boolean get_practice()
    {
        return product_prefs_SharedPreferences.getBoolean(IS_PRACTICE_KEY,false);
    }

    public String get_product_package_price()
    {
        return product_prefs_SharedPreferences.getString(PACKAGE_PRICE,"0");
    }

    public String get_practice_url()
    {
        return product_prefs_SharedPreferences.getString(PRACTICE_URL_KEY,"0");
    }

    public String get_product_name()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_NAME_KEY,"product name");
    }

    public String get_product_msg()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_MSG_KEY,"product name");
    }


    public String get_product_explain()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_EXPLAIN_KEY,"product explain");
    }

    public String get_product_usage()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_USAGE_KEY,"product usage");
    }

    public String get_product_alarm()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_ALARM_KEY,"product alarm");
    }

    public String get_product_time()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_TIME_KEY,"1 hour");
    }

    public String get_product_url()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_URL_KEY,"product url");
    }

    public String get_product_id()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_ID_KEY,"null");
    }

    public String get_product_group_id()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_GROUP_ID_KEY,"null");
    }

    public String get_product_package_id()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_PACKAGE_ID_KEY,"null");
    }

    public String get_product_price()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_PRICE_KEY,"0");
    }

    public String get_product_massage()
    {
        return product_prefs_SharedPreferences.getString(PRODUCT_MASSAGE_KEY,"0");
    }

    public boolean get_product_bought()
    {
        return product_prefs_SharedPreferences.getBoolean(PRODUCT_BOUGHT_KEY,false);
    }

    public boolean get_is_package()
    {
        return product_prefs_SharedPreferences.getBoolean(PRODUCT_IS_PACKAGE_KEY,false);
    }

    public int get_product_type()
    {
        return product_prefs_SharedPreferences.getInt(PRODUCT_TYPE_KEY,0);
    }
}
