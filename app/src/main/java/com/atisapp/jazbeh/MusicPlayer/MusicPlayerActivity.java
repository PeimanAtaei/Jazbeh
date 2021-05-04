package com.atisapp.jazbeh.MusicPlayer;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.atisapp.jazbeh.Comment.CommentActivity;
import com.atisapp.jazbeh.Login.RegisterActivity;
import com.atisapp.jazbeh.MainFragment.ui.home.WalletApiService;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.MainFragment.ui.profile.ProfileInfoApiService;
import com.atisapp.jazbeh.MainFragment.ui.profile.UserModel;
import com.atisapp.jazbeh.ProductList.PracticeModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Database.Tables.OfflineTable;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicPlayerActivity extends AppCompatActivity{

    private static final String TAG = "MusicPlayerActivity";
    private Context     context;
    private IdentitySharedPref identitySharedPref;
    private TextView    musicTimer,productName,productExplain,productUsage,productAlarm,prodactTime,music_title
            ,productPrice,packagePrice,playModeText;
    private SeekBar     musicSeekBar;
    private ImageButton playBtn , repetBtn,stopBtn;
    private Button      payBtn,fileBtn,downloadBtn;
    private MediaPlayer mediaPlayer;
    private ProgressBar musicProgress;
    private int         playNum = 0;
    private int         repeatCount = 0;
    private int         likeCount = 0;
    private int         myBalance = 0,price = 50;
    private TextView    musicRepeatCounter,playCounter,viewCounter,likeCounter, favoriteText,commentText;
    private boolean     hasLiked,hasFavorite;
    private ProductPrefs productPrefs;
    private BoughtApiService boughtApiService;
    private DownloadManager downloadManager;
    private WalletApiService walletApiService;
    private ProfileInfoApiService profileInfoApiService;
    private ProgressDialog consumptionProgressDialog;
    private Intent intent;
    private Bundle bundle;
    private ProductModel episodeModel;
    private String packagePriceValue = "0";
    private String episodeId;
    private String podcastURL;
    private boolean isLocked = true,playCounted = false,loop = false;
    private WebView webView;
    private EpisodeAPIService episodeAPIService;
    private AlertDialog alertDialog;
    private ImageView likeImage,favoriteImage;
    private Toolbar musicPlayerToolbar;
    private ProgressDialog progressDialog;
    private LinearLayout likeBox,favoriteBox,commentBox;
    private int totalTime;
    private String fileName,downloadPath,playUrl;
    private ProgressDialog mProgressDialog;
    private Handler uiHandler;
    private boolean isOfflineMode = false;
    private AlertDialog myProgressDialog;
    private boolean isRegistered = false;

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
        setContentView(R.layout.activity_music_player);

        SetupViews();

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isLocked)
                {
                    Toast.makeText(context,"برای استفاده از محصول ابتدا آن را دریافت نمایید",Toast.LENGTH_LONG).show();
                }else if(!isLocked){

                    if(!mediaPlayer.isPlaying())
                    {
                        mediaPlayer.start();

                        playBtn.setImageResource(R.drawable.ic_pause);
                    }else {
                        mediaPlayer.pause();
                        playBtn.setImageResource(R.drawable.ic_play_arrow);
                    }
                    if(!playCounted)
                    {
                        episodeAPIService.EpisodePlayCount(episodeId, new EpisodeAPIService.onPlayEpisode() {
                            @Override
                            public void onGet(boolean msg) {
                                playCounted = true;
                            }
                        });
                    }

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
                playNum = 0;
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

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckRegistration())
                {
                    openDialogPayment();
                }
            }
        });

        fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context,"این محصول دارای فایل تمرین نمی باشد",Toast.LENGTH_SHORT).show();

                try {
                    if(episodeModel.getPracticeModelList() != null && episodeModel.getPracticeModelList().size() > 0)
                    {
                        List<PracticeModel> practiceModelList = episodeModel.getPracticeModelList();
                        Intent intent = new Intent(MusicPlayerActivity.this,PracticeActivity.class);
                        intent.putExtra("practiceList",(Serializable) practiceModelList);
                        startActivity(intent);
                        Toast.makeText(context,"این محصول دارای فایل تمرین نمی باشد",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(context,"این محصول دارای فایل تمرین نمی باشد",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    Log.i(TAG, "onClick: "+e);
                }

            }
        });

        likeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckRegistration())
                {
                    SetLike();
                }
            }
        });

        favoriteBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckRegistration())
                {
                    SetFavorite();
                }
            }
        });

        commentBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckRegistration()) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("productType", "episodes");
                    intent.putExtra("productId", episodeId);
                    startActivity(intent);
                }
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDownloadDialog();
            }
        });
    }


    private void SetupViews()
    {
        context = MusicPlayerActivity.this;
        identitySharedPref = new IdentitySharedPref(context);
        boughtApiService = new BoughtApiService(context);
        profileInfoApiService = new ProfileInfoApiService(context);

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
        likeImage = findViewById(R.id.episode_card_like_image);
        favoriteImage = findViewById(R.id.episode_card_favorite_image);
        playCounter = findViewById(R.id.episode_card_play_count);
        viewCounter = findViewById(R.id.episode_card_view_count);
        likeCounter = findViewById(R.id.episode_card_like_text);
        favoriteText = findViewById(R.id.episode_card_favorite_text);
        commentText = findViewById(R.id.episode_card_comment_text);
        likeBox = findViewById(R.id.episode_card_like);
        favoriteBox = findViewById(R.id.episode_card_favorite);
        commentBox = findViewById(R.id.episode_card_comment);
        musicRepeatCounter = findViewById(R.id.music_repet_counter);
        musicSeekBar = findViewById(R.id.music_seek_bar);
        playBtn = findViewById(R.id.music_play_btn);
        downloadBtn = findViewById(R.id.music_palyer_download_btn);
        repetBtn = findViewById(R.id.repet_btn);
        stopBtn = findViewById(R.id.stop_btn);
        payBtn = findViewById(R.id.music_palyer_pay_btn);
        fileBtn = findViewById(R.id.music_player_file_btn);
        musicProgress = findViewById(R.id.music_play_progress);
        playModeText = findViewById(R.id.music_play_mode_text);

        musicSeekBar.setMax(99);
        productPrefs = new ProductPrefs(context);
        episodeAPIService = new EpisodeAPIService(context);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("در حال دانلود محصول ...");
        mProgressDialog.setMax(100);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        uiHandler = new Handler();

        setupToolBar();
        GetSingleEpisodeFromServer();
        if(identitySharedPref.getToken().length()>0 || identitySharedPref.getIsRegistered() == 1)
        {
            showBalance();
        }

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("مایل به دریافت این محصول هستید ؟");
        builder.setCancelable(true);

        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                StartPayment();
                dialog.cancel();
            }
        });

        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void openDialogPayment() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_payment_dialog, viewGroup, false);

        Button      startPayment                = dialogView.findViewById(R.id.music_pay_btn);
        final Button      cancelPayment               = dialogView.findViewById(R.id.music_cancel_btn);
        final       EditText    invitationBox   = dialogView.findViewById(R.id.music_invitation_edit);
        
        int code = identitySharedPref.getInvitationCode();
        if(code>0)
        {
            invitationBox.setText(code+"");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        final AlertDialog updateDialog = builder.create();
        updateDialog.show();

        startPayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int code;
                if(invitationBox.getText().length()>0)
                    code = Integer.parseInt(invitationBox.getText().toString());
                else
                    code = 0;
                SendInvitationCode(code);
                //StartPayment();
                updateDialog.dismiss();
            }
        });
        cancelPayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                updateDialog.dismiss();
            }
        });
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);



    }

    private void SendInvitationCode(int InvitationCode)
    {
        if(InvitationCode>0){

            UserModel userModel = new UserModel();
            userModel.setInvitationCode(InvitationCode);

            profileInfoApiService.SendPersonalInfo(userModel, new ProfileInfoApiService.onGetAddPersonalInfoResponse() {
                @Override
                public void onGet(boolean response, UserModel userModel) {
                    Log.i(TAG, "onGet: use invitation Code");
                    }
            });

            StartPayment();

        }else {
            StartPayment();
        }
    }

    // get episode data and set them  --------------------------------------------------------------



    private void GetSingleEpisodeFromServer()
    {

        OpenProgressDialog();

        EpisodeAPIService episodeAPIService = new EpisodeAPIService(context);
        episodeAPIService.GetSingleEpisode(episodeId, new EpisodeAPIService.onGetSingleEpisode() {
            @Override
            public void onGet(boolean msg, ProductModel single_episodes) {
                if(msg) {
                    episodeModel = single_episodes;
                    SetProductData(episodeModel);
                }
            }
        });
    }

    private void SetProductData(ProductModel Model)
    {
        if(Model!= null)
        {
            music_title.setText(Model.getTitle());
            productName.setText(Model.getTitle());
            fileName = Model.getProduct_id();
            downloadPath = getFilesDir()+"/"+fileName+".mp3";

            if(Model.getPrice()>=0)
                price = Model.getPrice();

            if(price == 0) {
                productPrice.setText("رایگان");
                payBtn.setText("دریافت محصول (رایگان)");
            }
            else if(price > 0) {
                productPrice.setText(price + " تومان ");
                payBtn.setText("پرداخت و خرید محصول");
            }

            productExplain.setText(Model.getExplain());
            productUsage.setText(Model.getUsage());
            productAlarm.setText(Model.getAlarm());
            prodactTime.setText(Model.getTime());
            commentText.setText(Model.getCommentCount()+" نظر");
            if(Model.isLocked() == true || Model.isLocked() == false)
                isLocked = Model.isLocked();
            playCounter.setText(Model.getPlayCount()+"");
            viewCounter.setText(Model.getViewCount()+"");
            likeCounter.setText(Model.getLikeCount()+"");
            likeCount = Model.getLikeCount();
            hasLiked = Model.isHasLiked();
            hasFavorite = Model.isFavorite();

            if(hasLiked)
            {
                likeImage.setImageResource(R.drawable.ic_like);
            }

            if(hasFavorite)
            {
                favoriteImage.setImageResource(R.drawable.ic_archive);
            }

            if(!isLocked)
            {
                podcastURL = Model.getPodcastUrl();
                playUrl = podcastURL;
            }

            CheckIsLocked();
        }
    }

    private void CheckIsLocked()
    {
        if (isLocked)
        {
            payBtn.setVisibility(View.VISIBLE);
            fileBtn.setVisibility(View.GONE);
            myProgressDialog.dismiss();

        }else {
            payBtn.setVisibility(View.GONE);
            fileBtn.setVisibility(View.VISIBLE);

            playUrl = podcastURL;
            Log.i(TAG, "CheckIsLocked: "+playUrl);

            CheckFileSize();
        }

    }

    //Pay button -----------------------------------------------------------------------------------

    private void StartPayment()
    {
        if(price>=0)
        {
            boolean useWallet = false;
            if(price<=myBalance)
                useWallet = true;

            episodeAPIService.GetSingleEpisodePerches(episodeId,useWallet, new EpisodeAPIService.onGetEpisodesPerches() {
                @Override
                public void onGet(boolean msg, String URL) {

                    if(URL.equals("1")) {
                        Toast.makeText(context, "پرداخت از طریق کیف پول شما با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                        SetupViews();
                    }
                    else if(URL.equals("-1"))
                        Toast.makeText(context,"شارژ کیف پول شما کافی نمی باشد",Toast.LENGTH_LONG).show();
                    else if(URL.equals("0"))
                    {
                        Toast.makeText(context,"محصول به صورت رایگان برای شما فعال گردید",Toast.LENGTH_LONG).show();
                        SetupViews();
                    }
                    else
                        openBrowser(URL);
                }
            });
        }
    }

    // Like button ---------------------------------------------------------------------------------

    private void SetLike()
    {
        if(hasLiked)
        {
            likeImage.setImageResource(R.drawable.ic_favorite_border);
            likeCount--;
            likeCounter.setText((likeCount)+"");
            hasLiked = false;

            episodeAPIService.DisLikeEpisode(episodeId, new EpisodeAPIService.onLikeEpisode() {
                @Override
                public void onGet(boolean msg) {
                    Log.i(TAG, "onGet: episode liked "+msg);
                }
            });

        }else {

            likeImage.setImageResource(R.drawable.ic_like);
            likeCount++;
            likeCounter.setText((likeCount)+"");
            hasLiked = true;

            episodeAPIService.LikeEpisode(episodeId, new EpisodeAPIService.onLikeEpisode() {
                @Override
                public void onGet(boolean msg) {
                    Log.i(TAG, "onGet: episode liked "+msg);
                }
            });

        }
    }

    // Favorite button -----------------------------------------------------------------------------

    private void SetFavorite()
    {
        if(hasFavorite)
        {
            favoriteImage.setImageResource(R.drawable.ic_unarchive);

            episodeAPIService.DeleteFavoriteEpisode(episodeId, new EpisodeAPIService.onFavorite() {
                @Override
                public void onGet(boolean msg) {
                    if (msg) {
                        Toast.makeText(context, "این محصول از لیست علاقمندی های شما حذف گردید", Toast.LENGTH_SHORT).show();;
                        hasFavorite = false;
                    }
                    else
                    {
                        Toast.makeText(context, "اختلال در لیست علاقمندی ها", Toast.LENGTH_SHORT).show();;
                        favoriteImage.setImageResource(R.drawable.ic_archive);
                    }
                }
            });

        }else {

            favoriteImage.setImageResource(R.drawable.ic_archive);

            episodeAPIService.FavoriteEpisode(episodeId, new EpisodeAPIService.onFavorite() {
                @Override
                public void onGet(boolean msg) {
                    if (msg) {
                        hasFavorite = true;
                        Toast.makeText(context, "این محصول به لیست علاقمندی های شما افزوده شد", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "اختلال در افزودن به لیست علاقمندی ها", Toast.LENGTH_SHORT).show();
                        favoriteImage.setImageResource(R.drawable.ic_unarchive);
                    }
                }
            });

        }
    }

    // Setup toolbar -------------------------------------------------------------------------------

    private void setupToolBar()
    {
        musicPlayerToolbar     = (Toolbar) findViewById(R.id.music_player_list_toolBar);
        setSupportActionBar(musicPlayerToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        musicPlayerToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        musicPlayerToolbar.setTitle("");

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


    // Setup Media Player --------------------------------------------------------------------------

    private void SetupPlayer()
    {

        if(isOfflineMode)
        {
            playUrl = downloadPath;

            downloadBtn.post(new Runnable() {
                @Override
                public void run() {
                    downloadBtn.setVisibility(View.GONE);
                }
            });

            playModeText.post(new Runnable() {
                @Override
                public void run() {
                    playModeText.setText("اجرا از حافظه دستگاه");
                }
            });
        }else {
            playUrl = podcastURL;

            downloadBtn.post(new Runnable() {
                @Override
                public void run() {
                    downloadBtn.setVisibility(View.VISIBLE);
                }
            });

            playModeText.post(new Runnable() {
                @Override
                public void run() {
                    playModeText.setText("اجرا آنلاین");
                }
            });
        }

        if(isOnline() && playUrl!=null)
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
                    myProgressDialog.dismiss();
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
            Toast.makeText(context,"اتصال به اینترنت خود را بررسی نمایید",Toast.LENGTH_SHORT).show();
        }

        myProgressDialog.dismiss();
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
            musicRepeatCounter.setText(repeatCount+" rep ");
            playNum = 0;
            mediaPlayer.start();
        }
    }


    private void OpenProgressDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_progress_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);
        myProgressDialog = builder.create();
        myProgressDialog.show();

        myProgressDialog.setCancelable(false);
        myProgressDialog.setCanceledOnTouchOutside(false);

    }

    // Download Product ----------------------------------------------------------------------------

    private void OpenDownloadDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_download_dialog, viewGroup, false);

        Button      startDownload               = dialogView.findViewById(R.id.download_start_btn);
        Button      cancelDownload               = dialogView.findViewById(R.id.download_cancel_btn);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);
        final AlertDialog downloadDialog = builder.create();
        downloadDialog.show();

        startDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DownloadProduct();
                downloadDialog.dismiss();
            }
        });

        cancelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadDialog.dismiss();
            }
        });
        downloadDialog.setCancelable(true);
        downloadDialog.setCanceledOnTouchOutside(true);

    }

    private void DownloadProduct()
    {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(podcastURL).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    float file_size = response.body().contentLength();

                    final BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream());
                    final OutputStream stream = new FileOutputStream(downloadPath);
                    byte[] data = new byte[8192];
                    float total = 0;
                    int read_bytes = 0;

                    mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "بیخیال", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            File proFile = new File(downloadPath);
                            if(proFile.exists())
                                proFile.delete();

                            mProgressDialog.dismiss();//dismiss dialog
                        }
                    });

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.show();
                        }
                    });


                    while ((read_bytes = inputStream.read(data)) != -1)
                    {
                        total = total + read_bytes;
                        stream.write(data,0,read_bytes);
                        mProgressDialog.setProgress((int) ((total/file_size)*100));
                    }
                    mProgressDialog.dismiss();
                    stream.flush();
                    stream.close();
                    response.body().close();

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            SetupViews();
                        }
                    });

                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void CheckFileSize()
    {
        final File proFile = new File(downloadPath);

        if(proFile.exists()) {

            Thread sizeThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(podcastURL).build();
                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                        Long file_size = response.body().contentLength();

                        if(proFile.length() == file_size)
                        {
                            isOfflineMode = true;
                            Log.i(TAG, "run: offline mode is ready");
                            SaveInDatabase();
                            SetupPlayer();

                        }
                        else
                        {
                            playUrl = podcastURL;
                            proFile.delete();
                            isOfflineMode = false;
                            Log.i(TAG, "run: online mode is ready");
                            SetupPlayer();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
            sizeThread.start();
        }
        else {
            playUrl = podcastURL;
            isOfflineMode = false;
            Log.i(TAG, "run: online mode is ready");
            SetupPlayer();
        }

    }

    // Balance -------------------------------------------------------------------------------------

    private void showBalance() {
        walletApiService = new WalletApiService(getApplicationContext());
        walletApiService.showBalance(new WalletApiService.OnGetBalanceListener() {

            @Override
            public void onBalance(int balance) {
                identitySharedPref.setWalletBalance(balance);
                myBalance = balance;

            }
        });
    }

    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void SaveInDatabase()
    {

        try {
            OfflineTable offlineModel  = new OfflineTable();
            offlineModel = GetSingleRecord(episodeModel.getProduct_id());

            if(offlineModel == null) {

                offlineModel = new OfflineTable();
                offlineModel.setOffline_id(episodeModel.getProduct_id());
                offlineModel.setOffline_title(episodeModel.getTitle());
                offlineModel.setOffline_url(downloadPath);
                offlineModel.setOffline_alarm(episodeModel.getAlarm());
                offlineModel.setOffline_explain(episodeModel.getExplain());
                offlineModel.setOffline_usage(episodeModel.getUsage());
                offlineModel.setOffline_price(episodeModel.getPrice());
                offlineModel.setOffline_time(episodeModel.getTime());

                offlineModel.save();
                Log.i(TAG, "SaveInDatabase: product saved in database id : "+offlineModel.getOffline_id());

            }
            else {
                Log.i(TAG, "SaveInDatabase: this product exists in database"+offlineModel.getOffline_id());
            }
        }catch (Exception e)
        {
            Log.i(TAG, "SaveInDatabase: "+e);
        }

    }

    public static OfflineTable GetSingleRecord(String product_id) {
        return new Select()
                .from(OfflineTable.class)
                .where("product_id = ?", product_id)
                .executeSingle();
    }

    private void openAccountDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_account_dialog, viewGroup, false);

        Button createAccount = dialogView.findViewById(R.id.create_account);
        Button createAccountCancel = dialogView.findViewById(R.id.create_account_cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        final AlertDialog accountDialog = builder.create();
        accountDialog.show();
        accountDialog.setCanceledOnTouchOutside(false);
        accountDialog.setCancelable(false);

        createAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context,"ورود به بخش ثبت نام",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

        createAccountCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                accountDialog.dismiss();
            }
        });

    }

    public boolean CheckRegistration()
    {
        if(identitySharedPref.getToken().length()>0 || identitySharedPref.getIsRegistered() == 1)
        {
            isRegistered = true;
        }else {
            isRegistered = false;
            openAccountDialog();
        }
        return isRegistered;
    }
}
