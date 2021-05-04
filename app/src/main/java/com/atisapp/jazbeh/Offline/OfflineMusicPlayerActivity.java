package com.atisapp.jazbeh.Offline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.MainFragment.ui.home.WalletApiService;
import com.atisapp.jazbeh.MainFragment.ui.profile.ProfileInfoApiService;
import com.atisapp.jazbeh.MusicPlayer.BoughtApiService;
import com.atisapp.jazbeh.MusicPlayer.MusicPlayerActivity;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Database.Tables.OfflineTable;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.io.IOException;

public class OfflineMusicPlayerActivity extends AppCompatActivity {

    private static final String TAG = "OfflineMusicPlayer";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private OfflineTable offlineModel;
    private TextView musicTimer,productName,productExplain,productUsage,productAlarm,prodactTime,music_title
            ,productPrice,packagePrice,playModeText;
    private SeekBar musicSeekBar;
    private ImageButton playBtn , repetBtn,stopBtn;
    private Button fileBtn;
    private MediaPlayer mediaPlayer;
    private ProgressBar musicProgress;
    private int         repeatCount = 0;
    private int         myBalance = 0,price = 50;
    private TextView    musicRepeatCounter;
    private ProductPrefs productPrefs;
    private ProfileInfoApiService profileInfoApiService;
    private ProgressDialog consumptionProgressDialog;
    private Intent intent;
    private Bundle bundle;
    private ProductModel episodeModel;
    private String packagePriceValue = "0";
    private String episodeId;
    private String podcastURL;
    private WebView webView;
    private EpisodeAPIService episodeAPIService;
    private AlertDialog alertDialog;
    private Toolbar musicPlayerToolbar;
    private ProgressDialog progressDialog;
    private int totalTime;
    private String fileName,downloadPath,playUrl;
    private ProgressDialog mProgressDialog;
    private Handler uiHandler;
    private boolean isOfflineMode = false,loop = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_music_player);

        SetupViews();


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(!mediaPlayer.isPlaying())
                    {
                        mediaPlayer.start();
                        playBtn.setImageResource(R.drawable.ic_pause);
                    }else {
                        mediaPlayer.pause();
                        playBtn.setImageResource(R.drawable.ic_play_arrow);
                    }
                }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                playBtn.setImageResource(R.drawable.ic_play_arrow);
                musicTimer.setText("00:00");
            }
        });

        repetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    repetBtn = findViewById(R.id.repet_btn);
                    if(loop)
                    {
                        repetBtn.setImageResource(R.drawable.ic_autorenew);
                        loop = false;
                        mediaPlayer.setLooping(loop);
                    }else {
                        repetBtn.setImageResource(R.drawable.ic_repet);
                        loop = true;
                        mediaPlayer.setLooping(loop);
                    }
                }catch (Exception e)
                {
                    Log.i(TAG, "onClick: ");
                }
            }
        });

    }

    private void SetupViews()
    {
        context = OfflineMusicPlayerActivity.this;
        identitySharedPref = new IdentitySharedPref(context);
        profileInfoApiService = new ProfileInfoApiService(context);
        offlineModel = new OfflineTable();

        Intent episodeIntent = getIntent();
        episodeId = episodeIntent.getExtras().getString("episodeID");

        musicTimer = findViewById(R.id.music_timer);
        productName = findViewById(R.id.music_product_name);
        productPrice = findViewById(R.id.music_product_price);
        productExplain = findViewById(R.id.music_product_description);
        productUsage = findViewById(R.id.music_product_use_way);
        prodactTime = findViewById(R.id.music_product_time);
        productAlarm = findViewById(R.id.music_product_alarm);
        music_title = findViewById(R.id.music_title);
        musicRepeatCounter = findViewById(R.id.music_repet_counter);
        musicSeekBar = findViewById(R.id.music_seek_bar);
        playBtn = findViewById(R.id.music_play_btn);
        repetBtn = findViewById(R.id.repet_btn);
        stopBtn = findViewById(R.id.stop_btn);
        fileBtn = findViewById(R.id.music_player_file_btn);
        musicProgress = findViewById(R.id.music_play_progress);
        playModeText = findViewById(R.id.music_play_mode_text);

        musicSeekBar.setMax(99);
        productPrefs = new ProductPrefs(context);
        episodeAPIService = new EpisodeAPIService(context);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //episodeId = "5c3b26be04bad144b19d7e53";

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال دریافت اطلاعات محصول");
        progressDialog.show();

        setupToolBar();
        offlineModel = GetSingleRecord(episodeId);
        SetProductData(offlineModel);
    }

    private void setupToolBar()
    {
        musicPlayerToolbar     = (Toolbar) findViewById(R.id.offline_music_player_list_toolBar);
        setSupportActionBar(musicPlayerToolbar);

        musicPlayerToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        musicPlayerToolbar.setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    public static OfflineTable GetSingleRecord(String product_id) {
        return new Select()
                .from(OfflineTable.class)
                .where("product_id = ?", product_id)
                .executeSingle();
    }

    private void SetProductData(OfflineTable Model)
    {
        if(Model!= null)
        {
            music_title.setText(Model.getOffline_title());
            productName.setText(Model.getOffline_title());
            downloadPath = Model.getOffline_url();

            if(Model.getOffline_price()>=0)
                price = Model.getOffline_price();

            if(price == 0)
                productPrice.setText("رایگان");
            else if(price > 0)
                productPrice.setText(price+" تومان ");

            productExplain.setText(Model.getOffline_explain());
            productUsage.setText(Model.getOffline_usage());
            productAlarm.setText(Model.getOffline_alarm());
            prodactTime.setText(Model.getOffline_time());

            SetupPlayer();
        }
    }

    private void SetupPlayer()
    {


        playUrl = downloadPath;
        if(playUrl!=null)
        {
            try {
                mediaPlayer.setDataSource(playUrl);
                mediaPlayer.prepare();
                totalTime = mediaPlayer.getDuration();

            } catch (IOException e) {
                e.printStackTrace();
            }
            musicProgress.setVisibility(View.GONE);
            mediaPlayer.seekTo(0);
            musicSeekBar.setMax(totalTime);


            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                }
            });

            musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser)
                    {
                        mediaPlayer.seekTo(progress);
                        musicSeekBar.setProgress(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mediaPlayer != null) {
                        try {
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);

                        } catch (InterruptedException e) {
                        }
                    }
                }
            }).start();
        }else {
            Toast.makeText(context,"اختلال در بازیابی محصول",Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();

    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int currentPosition = msg.what;
            musicSeekBar.setProgress(currentPosition);
            String elapsedTime = createTimeLabel(currentPosition);
            musicTimer.setText(elapsedTime);
        }
    };

    private String createTimeLabel(int time)
    {
        String timeLabel = "";
        int min = time/1000/60;
        int sec = time/1000%60;

        timeLabel = min + ":";
        if(sec<10)
            timeLabel += "0";
        timeLabel+=sec;

        return timeLabel;
    }

    public void RepeatMusic()
    {
        if(repeatCount>0)
        {
            repeatCount--;
            musicRepeatCounter.setText(repeatCount+"");
            mediaPlayer.start();
        }
    }
}
