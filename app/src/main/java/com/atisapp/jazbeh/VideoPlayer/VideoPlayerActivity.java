package com.atisapp.jazbeh.VideoPlayer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.atisapp.jazbeh.Comment.CommentActivity;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.Login.RegisterActivity;
import com.atisapp.jazbeh.MainFragment.ui.home.WalletApiService;
import com.atisapp.jazbeh.MainFragment.ui.profile.ProfileInfoApiService;
import com.atisapp.jazbeh.MainFragment.ui.profile.UserModel;
import com.atisapp.jazbeh.MusicPlayer.MusicPlayerActivity;
import com.atisapp.jazbeh.MusicPlayer.PracticeActivity;
import com.atisapp.jazbeh.ProductList.PracticeModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class VideoPlayerActivity extends AppCompatActivity {

    private static final String TAG = "VideoPlayer";
    private Context     context;
    private EpisodeAPIService episodeAPIService;
    private IdentitySharedPref identitySharedPref;
    private ProfileInfoApiService profileInfoApiService;
    private WalletApiService walletApiService;

    private VideoView   videoView;
    private Button      fullScreen,minimize;
    private MediaController mediaController;
    private ViewGroup   mAnchor;
    private ProgressBar progressBar;
    private String      episodeId,videoUrl="";
    private AlertDialog myProgressDialog;
    private ProductModel episodeModel;
    private TextView    musicTimer,productName,productExplain,productUsage,productAlarm,prodactTime,music_title
                        ,productPrice,packagePrice,playModeText;
    private TextView    musicRepeatCounter,playCounter,viewCounter,likeCounter, favoriteText,commentText;
    private String      fileName,downloadPath,playUrl;
    private LinearLayout likeBox,favoriteBox,commentBox;
    private Button      payBtn,fileBtn,downloadBtn;
    private int         myBalance = 0,price = 50,likeCount = 0;;
    private boolean     hasLiked,hasFavorite;
    private boolean     isLocked = true,playCounted = false;
    private ImageView   likeImage,favoriteImage;
    private String      videoURL;
    private Toolbar videoPlayerToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        SetupViews();

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

                try {
                    if(episodeModel.getPracticeModelList() != null && episodeModel.getPracticeModelList().size() > 0)
                    {
                        List<PracticeModel> practiceModelList = episodeModel.getPracticeModelList();
                        Intent intent = new Intent(VideoPlayerActivity.this, PracticeActivity.class);
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
                if(CheckRegistration())
                {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("productType","episodes");
                    intent.putExtra("productId",episodeId);
                    startActivity(intent);
                }

            }
        });


        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: fullscreen");

                fullScreen.setVisibility(View.GONE);
                minimize.setVisibility(View.VISIBLE);

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
                params.width = FrameLayout.LayoutParams.MATCH_PARENT;
                params.height = FrameLayout.LayoutParams.MATCH_PARENT;
                params.leftMargin = 0;
                videoView.setLayoutParams(params);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                CheckOrientation();

            }
        });

        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: minimize");

                fullScreen.setVisibility(View.VISIBLE);
                minimize.setVisibility(View.GONE);

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
                params.width = FrameLayout.LayoutParams.MATCH_PARENT;
                params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                //params.leftMargin = 30;
                videoView.setLayoutParams(params);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                CheckOrientation();

            }
        });

    }

    private void SetupViews()
    {
        context = VideoPlayerActivity.this;
        episodeAPIService = new EpisodeAPIService(context);
        identitySharedPref = new IdentitySharedPref(context);
        profileInfoApiService = new ProfileInfoApiService(context);

        productName = findViewById(R.id.video_product_name);
        productPrice = findViewById(R.id.video_product_price);
        productExplain = findViewById(R.id.video_product_description);
        productUsage = findViewById(R.id.video_product_use_way);
        prodactTime = findViewById(R.id.video_product_time);
        productAlarm = findViewById(R.id.video_product_alarm);

        likeImage = findViewById(R.id.video_card_like_image);
        favoriteImage = findViewById(R.id.video_card_favorite_image);
        playCounter = findViewById(R.id.video_card_play_count);
        viewCounter = findViewById(R.id.video_card_view_count);
        likeCounter = findViewById(R.id.video_card_like_text);
        favoriteText = findViewById(R.id.video_card_favorite_text);
        commentText = findViewById(R.id.video_card_comment_text);
        likeBox = findViewById(R.id.video_card_like);
        favoriteBox = findViewById(R.id.video_card_favorite);
        commentBox = findViewById(R.id.video_card_comment);
        downloadBtn = findViewById(R.id.video_palyer_download_btn);
        payBtn = findViewById(R.id.video_palyer_pay_btn);
        fileBtn = findViewById(R.id.video_player_file_btn);

//        likeImage = findViewById(R.id.episode_card_like_image);
//        favoriteImage = findViewById(R.id.episode_card_favorite_image);
//        playCounter = findViewById(R.id.episode_card_play_count);
//        viewCounter = findViewById(R.id.episode_card_view_count);
//        likeCounter = findViewById(R.id.episode_card_like_text);
//        favoriteText = findViewById(R.id.episode_card_favorite_text);
//        commentText = findViewById(R.id.episode_card_comment_text);
//        musicRepeatCounter = findViewById(R.id.music_repet_counter);
//        downloadBtn = findViewById(R.id.music_palyer_download_btn);
//        payBtn = findViewById(R.id.music_palyer_pay_btn);
//        fileBtn = findViewById(R.id.music_player_file_btn);
//        playModeText = findViewById(R.id.music_play_mode_text);



        videoView = findViewById(R.id.videoView);
        fullScreen = findViewById(R.id.video_fullScreen);
        minimize = findViewById(R.id.video_minimize);
        progressBar = findViewById(R.id.videoProgressBar);

        Intent episodeIntent = getIntent();
        episodeId = episodeIntent.getExtras().getString("episodeID");

        setupToolBar();
        GetSingleEpisodeFromServer();

        if(identitySharedPref.getToken().length()>0 || identitySharedPref.getIsRegistered() == 1)
        {
            showBalance();
        }

    }

    private void GetSingleEpisodeFromServer()
    {

        OpenProgressDialog();

        EpisodeAPIService episodeAPIService = new EpisodeAPIService(context);
        episodeAPIService.GetSingleEpisode(episodeId, new EpisodeAPIService.onGetSingleEpisode() {
            @Override
            public void onGet(boolean msg, ProductModel single_episodes) {
                if(msg) {
                    episodeModel = single_episodes;
                    Log.i(TAG, "onGet: URL is : "+episodeModel.getVideoUrl());
                    SetProductData(episodeModel);
                }
            }
        });
    }

    private void SetProductData(ProductModel Model)
    {
        if(Model!= null)
        {
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

            productExplain.setText(Model.getExplain());
            if(!Model.isLocked())
            {
                productUsage.setText(Model.getUsage());
                productAlarm.setText(Model.getAlarm());
                prodactTime.setText(Model.getTime());

                try {
                    Log.i(TAG, "SetProductData URI: "+Model.getVideoUrl());
                    payBtn.setVisibility(View.GONE);
                    videoUrl = Model.getVideoUrl();
                    Uri uri = Uri.parse(videoUrl);
                    videoView.setVideoURI(uri);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                MediaPlayer.TrackInfo[] trackInfoArray = mp.getTrackInfo();
                                for (int i = 0; i < trackInfoArray.length; i++) {
                                    // you can switch out the language comparison logic to whatever works for you
                                    Log.i(TAG, "onInfo: "+trackInfoArray[i].getLanguage());
                                    if (trackInfoArray[i].getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO
                                            && trackInfoArray[i].getLanguage().startsWith("per")) {
                                        mp.selectTrack(i);
                                        break;
                                    }
                                }
                                return true;
                            }
                        });
                    }

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            SetupPlayer();
                            CheckOrientation();
                        }
                    });
                }catch (Exception e){
                    Log.i(TAG, "SetProductData: "+e.getMessage());
                    Toast.makeText(context,"اختلال در بارگذاری ویدئو ، لطفا دوباره تلاش نمایید",Toast.LENGTH_LONG).show();
                    myProgressDialog.dismiss();
                }

            }else {
                Toast.makeText(context,"محصول دریافت نشده است",Toast.LENGTH_LONG).show();
                myProgressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
            }

            //CheckIsLocked();
        }
    }

    // Player --------------------------------------------------------------------------------------

    private void SetupPlayer()
    {

        try {

            progressBar.setVisibility(View.GONE);
            mediaController = new MediaController(context);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.requestFocus();
            videoView.start();
            PlayCounter();

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            //params.leftMargin = 30;
            videoView.setLayoutParams(params);
            CheckOrientation();

            myProgressDialog.dismiss();

        }catch (Exception e){
            Log.i(TAG, "SetupPlayer: "+e);
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

    private void CheckOrientation()
    {
        Log.i(TAG, "CheckOrientation: start"+context.getResources().getConfiguration().orientation);
        if(context.getResources().getConfiguration().orientation == 2)
        {
            Log.i(TAG, "CheckOrientation: done");
            fullScreen.setVisibility(View.GONE);
            minimize.setVisibility(View.VISIBLE);
        }
    }

    // payment -------------------------------------------------------------------------------------

    private void openDialogPayment() {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_payment_dialog, viewGroup, false);

        Button      startPayment                = dialogView.findViewById(R.id.music_pay_btn);
        final Button      cancelPayment               = dialogView.findViewById(R.id.music_cancel_btn);
        final EditText invitationBox   = dialogView.findViewById(R.id.music_invitation_edit);

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

    //Pay button -----------------------------------------------------------------------------------

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

    // Setup toolbar -------------------------------------------------------------------------------

    private void setupToolBar()
    {
        videoPlayerToolbar     = (Toolbar) findViewById(R.id.video_player_list_toolBar);
        setSupportActionBar(videoPlayerToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        videoPlayerToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.trans));
        videoPlayerToolbar.setTitle("");

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

    private void PlayCounter()
    {
        episodeAPIService.EpisodePlayCount(episodeId, new EpisodeAPIService.onPlayEpisode() {
            @Override
            public void onGet(boolean msg) {
                playCounted = true;
            }
        });
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
        boolean isRegistered ;
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
