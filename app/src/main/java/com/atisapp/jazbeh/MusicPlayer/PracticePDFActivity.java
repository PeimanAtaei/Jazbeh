package com.atisapp.jazbeh.MusicPlayer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.atisapp.jazbeh.R;
import com.github.barteksc.pdfviewer.PDFView;


public class PracticePDFActivity extends AppCompatActivity {

    private static final String TAG = "PracticePDFActivity";
    private Context context;
    private String Url;
    private PDFView pdfView;
    private Toolbar pdfToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_pdf);
        SetupViews();
    }
    private void SetupViews()
    {
        context = PracticePDFActivity.this;
        Url = getIntent().getStringExtra("Url");
        pdfView = findViewById(R.id.pdfView);

        getBinaryData();
        setupToolBar();
    }

    private void getBinaryData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        BinaryRequest binaryRequest = new BinaryRequest(Request.Method.GET, Url, new Response.Listener<byte[]>() {

            @Override
            public void onResponse(byte[] response) {
                Log.i("JAZBE", ""+response.length);
                pdfView.fromBytes(response).load();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("JAZBE", error.getMessage());
            }
        });

        requestQueue.add(binaryRequest);
    }

    private void setupToolBar()
    {
        pdfToolbar     = (Toolbar) findViewById(R.id.pdf_activity_toolBar);
        setSupportActionBar(pdfToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        pdfToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        pdfToolbar.setTitle("");

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
