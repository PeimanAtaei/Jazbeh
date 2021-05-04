package com.atisapp.jazbeh.Storage.Prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class CategoryPref {

    private static final String CATEGORY_SHARED_PREF_NAME = "category_pref";
    private static final String MONEY_KEY = "money";
    private static final String LOVE_KEY = "love";
    private static final String SUCCESS_KEY = "success";
    private static final String BEAUTY_KEY = "beauty";
    private static final String ENGLISH_KEY = "english";
    private static final String HEALTH_KEY = "health";
    private static final String SPORT_KEY = "sport";
    private static final String FOOD_KEY = "food";
    private static final String MEDITATION_KEY = "meditation";
    private static final String MIND_KEY = "mind";
    private static final String LIGHTS_KEY = "lights";
    private static final String BOOK_KEY = "book";
    private static final String ART_KEY = "art";
    private static final String GUIDE_KEY = "guide";
    private static final String METAPHYSIC_KEY = "metaphysic";
    private static final String GIFT_KEY = "gift";
    private static final String VIRTUAL_KEY = "virtual";
    private static final String MANAGEMENT_KEY = "management";
    private static final String EDUCATION_KEY = "education";
    private static final String AUDIOBOOKS_KEY = "audioBooks";

    private SharedPreferences category_prefs_SharedPreferences ;
    private Context context;

    public CategoryPref(Context context)
    {
        this.context = context;
        category_prefs_SharedPreferences = context.getSharedPreferences(CATEGORY_SHARED_PREF_NAME,context.MODE_PRIVATE);
    }

    public void clearAllData()
    {
        category_prefs_SharedPreferences.edit().clear().apply();
    }


    public void set_money(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(MONEY_KEY,id);
        editor.apply();
    }

    public void set_love(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(LOVE_KEY,id);
        editor.apply();
    }

    public void set_success(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(SUCCESS_KEY,id);
        editor.apply();
    }

    public void set_beauty(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(BEAUTY_KEY,id);
        editor.apply();
    }

    public void set_endglish(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(ENGLISH_KEY,id);
        editor.apply();
    }

    public void set_health(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(HEALTH_KEY,id);
        editor.apply();
    }

    public void set_sport(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(SPORT_KEY,id);
        editor.apply();
    }

    public void set_food(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(FOOD_KEY,id);
        editor.apply();
    }

    public void set_meditation(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(MEDITATION_KEY,id);
        editor.apply();
    }

    public void set_mind(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(MIND_KEY,id);
        editor.apply();
    }

    public void set_lights(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(LIGHTS_KEY,id);
        editor.apply();
    }

    public void set_book(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(BOOK_KEY,id);
        editor.apply();
    }

    public void set_art(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(ART_KEY,id);
        editor.apply();
    }

    public void set_guide(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(GUIDE_KEY,id);
        editor.apply();
    }

    public void set_metaphysic(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(METAPHYSIC_KEY,id);
        editor.apply();
    }

    public void set_gift(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(GIFT_KEY,id);
        editor.apply();
    }

    public void set_virtual(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(VIRTUAL_KEY,id);
        editor.apply();
    }

    public void set_management(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(MANAGEMENT_KEY,id);
        editor.apply();
    }

    public void set_education(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(EDUCATION_KEY,id);
        editor.apply();
    }

    public void set_audioBooks(String id)
    {
        SharedPreferences.Editor editor = category_prefs_SharedPreferences.edit();
        editor.putString(AUDIOBOOKS_KEY,id);
        editor.apply();
    }


    // Get =========================================================================================


    public String getMoneyKey()
    {
        return category_prefs_SharedPreferences.getString(MONEY_KEY,"");
    }

    public String getLoveKey()
    {
        return category_prefs_SharedPreferences.getString(LOVE_KEY,"");
    }

    public String getSuccessKey()
    {
        return category_prefs_SharedPreferences.getString(SUCCESS_KEY,"");
    }

    public String getBeautyKey()
    {
        return category_prefs_SharedPreferences.getString(BEAUTY_KEY,"");
    }

    public String getEnglishKey()
    {
        return category_prefs_SharedPreferences.getString(ENGLISH_KEY,"");
    }

    public String getHealthKey()
    {
        return category_prefs_SharedPreferences.getString(HEALTH_KEY,"");
    }

    public String getSportKey()
    {
        return category_prefs_SharedPreferences.getString(SPORT_KEY,"");
    }

    public String getFoodKey()
    {
        return category_prefs_SharedPreferences.getString(FOOD_KEY,"");
    }

    public String getMeditationKey()
    {
        return category_prefs_SharedPreferences.getString(MEDITATION_KEY,"");
    }
    public String getMindKey()
    {
        return category_prefs_SharedPreferences.getString(MIND_KEY,"");
    }
    public String getLightsKey()
    {
        return category_prefs_SharedPreferences.getString(LIGHTS_KEY,"");
    }

    public String getBookKey()
    {
        return category_prefs_SharedPreferences.getString(BOOK_KEY,"");
    }

    public String getGuideKey()
    {
        return category_prefs_SharedPreferences.getString(GUIDE_KEY,"");
    }

    public String getArtKey()
    {
        return category_prefs_SharedPreferences.getString(ART_KEY,"");
    }

    public String getMetaphysicKey()
    {
        return category_prefs_SharedPreferences.getString(METAPHYSIC_KEY,"");
    }

    public String getGiftKey()
    {
        return category_prefs_SharedPreferences.getString(GIFT_KEY,"");
    }

    public String getVirtualKey()
    {
        return category_prefs_SharedPreferences.getString(VIRTUAL_KEY,"");
    }

    public String getManagementKey()
    {
        return category_prefs_SharedPreferences.getString(MANAGEMENT_KEY,"");
    }

    public String getEducationKey()
    {
        return category_prefs_SharedPreferences.getString(EDUCATION_KEY,"");
    }

    public String getAudiobooksKey()
    {
        return category_prefs_SharedPreferences.getString(AUDIOBOOKS_KEY,"");
    }



}
