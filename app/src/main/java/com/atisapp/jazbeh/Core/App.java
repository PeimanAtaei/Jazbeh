package com.atisapp.jazbeh.Core;

import android.content.Context;
import androidx.multidex.MultiDexApplication;
import com.activeandroid.ActiveAndroid;

public class App extends MultiDexApplication {

    public static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ActiveAndroid.initialize(this);
    }
}