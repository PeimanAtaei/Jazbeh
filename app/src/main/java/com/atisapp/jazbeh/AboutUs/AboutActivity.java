package com.atisapp.jazbeh.AboutUs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.atisapp.jazbeh.R;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";
    private TextView title,t1,t2,t3,t4,t5,t6,t7,t8,t9;
    private String type;
    private Toolbar aboutToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        SetupViews();

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            type = (String) bd.get("aboutType");
            if(type.equals("about"))
            {
                SetupAboutTexts();

            }else if(type.equals("explain"))
            {
                SetupExplainTexts();
            }else
            {
                SetupUsageTexts();
            }
        }
    }

    private void SetupViews()
    {
        title = (TextView) findViewById(R.id.about_title);
        t1 = (TextView) findViewById(R.id.about_text1);
        t2 = (TextView) findViewById(R.id.about_text2);
        t3 = (TextView) findViewById(R.id.about_text3);
        t4 = (TextView) findViewById(R.id.about_text4);
        t5 = (TextView) findViewById(R.id.about_text5);
        t6 = (TextView) findViewById(R.id.about_text6);
        t7 = (TextView) findViewById(R.id.about_text7);
        t8 = (TextView) findViewById(R.id.about_text8);
        t9 = (TextView) findViewById(R.id.about_text9);

        setupToolBar();
    }

    private void SetupAboutTexts()
    {
        t8.setVisibility(View.VISIBLE);
        t9.setVisibility(View.VISIBLE);

        title.setText(R.string.about_title3);

        t1.setText(R.string.about_t3_text1);
        t2.setText(R.string.about_t3_text2);
        t3.setText(R.string.about_t3_text3);
        t4.setText(R.string.about_t3_text4);
        t5.setText(R.string.about_t3_text5);
        t6.setText(R.string.about_t3_text6);
        t7.setText(R.string.about_t3_text7);
        t8.setText(R.string.about_t3_text8);
        t9.setText(R.string.about_t3_text9);

    }

    private void SetupExplainTexts()
    {
        t8.setVisibility(View.VISIBLE);
        t9.setVisibility(View.VISIBLE);

        title.setText(R.string.about_title1);

        t1.setText(R.string.about_t1_text1);
        t2.setText(R.string.about_t1_text2);
        t3.setText(R.string.about_t1_text3);
        t4.setText(R.string.about_t1_text4);
        t5.setText(R.string.about_t1_text5);
        t6.setText(R.string.about_t1_text6);
        t7.setText(R.string.about_t1_text7);
        t8.setText(R.string.about_t1_text8);
        t9.setText(R.string.about_t1_text9);

    }

    private void SetupUsageTexts()
    {
        t8.setVisibility(View.GONE);
        t9.setVisibility(View.GONE);

        title.setText(R.string.about_title2);

        t1.setText(R.string.about_t2_text1);
        t2.setText(R.string.about_t2_text2);
        t3.setText(R.string.about_t2_text3);
        t4.setText(R.string.about_t2_text4);
        t5.setText(R.string.about_t2_text5);
        t6.setText(R.string.about_t2_text6);
        t7.setText(R.string.about_t2_text7);


    }

    private void setupToolBar()
    {
        aboutToolbar     = (Toolbar) findViewById(R.id.about_toolBar);
        setSupportActionBar(aboutToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        aboutToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        aboutToolbar.setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
