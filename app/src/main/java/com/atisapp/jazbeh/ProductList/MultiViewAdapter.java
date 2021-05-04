package com.atisapp.jazbeh.ProductList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atisapp.jazbeh.Comment.CommentActivity;
import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Courses.CourseListAdapter;
import com.atisapp.jazbeh.Courses.CoursesActivity;
import com.atisapp.jazbeh.Login.RegisterActivity;
import com.atisapp.jazbeh.MainFragment.ui.home.WalletApiService;
import com.atisapp.jazbeh.MainFragment.ui.profile.ProfileInfoApiService;
import com.atisapp.jazbeh.MainFragment.ui.profile.UserModel;
import com.atisapp.jazbeh.MusicPlayer.PracticeAdapter;
import com.atisapp.jazbeh.Offline.OfflineListAdapter;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Database.Tables.OfflineTable;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

import java.util.List;

import static com.atisapp.jazbeh.ProductList.MultiViewModel.EPISODE_LIST_LAYOUT;
import static com.atisapp.jazbeh.ProductList.MultiViewModel.HEADER_LAYOUT;
import static com.atisapp.jazbeh.ProductList.MultiViewModel.COURSE_LIST_LAYOUT;
import static com.atisapp.jazbeh.ProductList.MultiViewModel.COURSE_HEADER_LAYOUT;
import static com.atisapp.jazbeh.ProductList.MultiViewModel.OFFLINE_PRODUCTS_LAYOUT;
import static com.atisapp.jazbeh.ProductList.MultiViewModel.PRACTICE_IMAGE_LAYOUT;
import static com.atisapp.jazbeh.ProductList.MultiViewModel.PRACTICE_PDF_LAYOUT;
import static com.atisapp.jazbeh.ProductList.MultiViewModel.PRACTICE_VOICE_LAYOUT;

public class MultiViewAdapter extends RecyclerView.Adapter{

    private static final String TAG = "MultiViewAdapter";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private ProfileInfoApiService profileInfoApiService;
    private List<MultiViewModel> multiViewModelList;
    private CourseAPIService courseAPIService;
    private int lastPosition = -1;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private CourseListAdapter productAdapter;
    private OfflineListAdapter offlineListAdapter;
    private PracticeAdapter practiceAdapter;
    private WalletApiService walletApiService;
    private int myBalance = 0 , coursePrice , likeCount;
    private String courseID;
    private boolean hasLiked,hasFavorite;
    private boolean myClickable = true;
    private ViewGroup viewGroup;
    private Button payButton;


    public MultiViewAdapter(List<MultiViewModel> multiViewModelList, Context context) {
        //Log.i(TAG, "MultiViewAdapter: make");
        this.multiViewModelList = multiViewModelList;
        //Log.i(TAG, "MultiViewAdapter size : "+multiViewModelList.size());
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);
        profileInfoApiService = new ProfileInfoApiService(context);
    }

    @Override
    public int getItemViewType(int position)
    {
        switch (multiViewModelList.get(position).getViewType())
        {
            case 0:{
                return HEADER_LAYOUT;
            }
            case 1:{
                return COURSE_LIST_LAYOUT;
            }
            case 2:{
                return EPISODE_LIST_LAYOUT;
            }
            case 3:{
                return COURSE_HEADER_LAYOUT;
            }
            case 4:{
                return PRACTICE_IMAGE_LAYOUT;
            }
            case 5:{
                return PRACTICE_PDF_LAYOUT;
            }
            case 6:{
                return PRACTICE_VOICE_LAYOUT;
            }
            case 7:{
                return OFFLINE_PRODUCTS_LAYOUT;
            }
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType)
        {
            case HEADER_LAYOUT: {
                //Log.i(TAG, "onCreateViewHolder: USER_INFO_LAYOUT");
                View userInfoLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.multiview_header_layout, parent, false);
                return new HeaderLayout(userInfoLayout);
            }
            case COURSE_LIST_LAYOUT:
            {
                //Log.i(TAG, "onCreateViewHolder: AD_INFO_LAYOUT");
                View adLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.multiview_subrecycler_view_layout, parent, false);
                return new ProductListLayout(adLayout);
            }

            case EPISODE_LIST_LAYOUT:
            {
                View adLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.multiview_subrecycler_view_layout, parent, false);
                return new EpisodeListLayout(adLayout);
            }

            case OFFLINE_PRODUCTS_LAYOUT:
            {
                View adLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.multiview_subrecycler_view_layout, parent, false);
                return new OfflineListLayout(adLayout);
            }

            case COURSE_HEADER_LAYOUT:
            {
                View headerLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_course_activity_detail, parent, false);
                return new CourseHeaderLayout(headerLayout);
            }

            case PRACTICE_IMAGE_LAYOUT:
            {
                View headerLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.multiview_subrecycler_view_layout, parent, false);
                return new PracticeImageListLayout(headerLayout);
            }

            case PRACTICE_PDF_LAYOUT:
            {
                View headerLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.multiview_subrecycler_view_layout, parent, false);
                return new PracticePDFListLayout(headerLayout);
            }

            case PRACTICE_VOICE_LAYOUT:
            {
                View headerLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.multiview_subrecycler_view_layout, parent, false);
                return new CourseHeaderLayout(headerLayout);
            }



            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (multiViewModelList.get(position).getViewType())
        {
            case HEADER_LAYOUT: {

                String title = multiViewModelList.get(position).getHeaderTitle();
                ((HeaderLayout)holder).setData(title);
                break;

            }
            case COURSE_LIST_LAYOUT:
            {
                List<ProductModel> productModelList = multiViewModelList.get(position).getProductModelList();
                ((ProductListLayout)holder).setData(productModelList);
                break;
            }

            case EPISODE_LIST_LAYOUT:
            {
                List<ProductModel> productModelList = multiViewModelList.get(position).getProductModelList();
                boolean clickable = multiViewModelList.get(position).isClickable();
                ((EpisodeListLayout)holder).setData(productModelList,clickable);
                break;
            }

            case OFFLINE_PRODUCTS_LAYOUT:
            {
                List<OfflineTable> productModelList = multiViewModelList.get(position).getOfflineProductList();
                ((OfflineListLayout)holder).setData(productModelList,true);
                break;
            }

            case COURSE_HEADER_LAYOUT:
            {

                ProductModel courseInfo = multiViewModelList.get(position).getCourseInformation();
                ((CourseHeaderLayout)holder).setData(courseInfo);
                break;
            }

            case PRACTICE_IMAGE_LAYOUT:
            {
                List<PracticeModel> practiceModelList = multiViewModelList.get(position).getPracticeModelList();
                ((PracticeImageListLayout)holder).setData(practiceModelList);
                break;
            }

            case PRACTICE_PDF_LAYOUT:
            {
                List<PracticeModel> practiceModelList = multiViewModelList.get(position).getPracticeModelList();
                ((PracticePDFListLayout)holder).setData(practiceModelList);
                break;
            }
        }

        if (lastPosition < position)
        {
            Animation animation = AnimationUtils.loadAnimation
                    (holder.itemView.getContext(),android.R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return multiViewModelList.size();
    }

    class HeaderLayout extends RecyclerView.ViewHolder
    {

        private TextView title;
        private TextView  body;

        public HeaderLayout(@NonNull View itemView) {
            super(itemView);

            //Log.i(TAG, "UserInfoLayout: UserInfoLayout");
            title = itemView.findViewById(R.id.multiView_header_text);

        }

        private void setData(String title)
        {
            this.title.setText(title);
        }
    }

    class ProductListLayout extends RecyclerView.ViewHolder
    {
        private RecyclerView subRecyclerView;

        public ProductListLayout(@NonNull View itemView) {
            super(itemView);
            subRecyclerView = itemView.findViewById(R.id.product_list_subRecyclerView);
            //Log.i(TAG, "ProductListLayout: "+itemView.getContext());

        }

        private void setData(List<ProductModel> productModelList)
        {
            //Log.i(TAG, "setData: "+productModelList.size());

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    itemView.getContext(),LinearLayoutManager.VERTICAL,false
            );
            layoutManager.setInitialPrefetchItemCount(productModelList.size());
            productAdapter = new CourseListAdapter(itemView.getContext(),productModelList,1,true);
            subRecyclerView.setLayoutManager(layoutManager);
            subRecyclerView.setAdapter(productAdapter);
            subRecyclerView.setRecycledViewPool(viewPool);

        }
    }

    class EpisodeListLayout extends RecyclerView.ViewHolder
    {
        private RecyclerView subRecyclerView;

        public EpisodeListLayout(@NonNull View itemView) {
            super(itemView);
            subRecyclerView = itemView.findViewById(R.id.product_list_subRecyclerView);
            //Log.i(TAG, "EpisodeListLayout: "+itemView.getContext());

        }

        private void setData(List<ProductModel> productModelList,boolean clickable)
        {

            //Log.i(TAG, "setData: "+productModelList.size());

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    itemView.getContext(),LinearLayoutManager.VERTICAL,false
            );
            layoutManager.setInitialPrefetchItemCount(productModelList.size());
            productAdapter = new CourseListAdapter(itemView.getContext(),productModelList,2,myClickable);
            subRecyclerView.setLayoutManager(layoutManager);
            subRecyclerView.setAdapter(productAdapter);
            subRecyclerView.setRecycledViewPool(viewPool);

        }
    }

    class OfflineListLayout extends RecyclerView.ViewHolder
    {
        private RecyclerView subRecyclerView;

        public OfflineListLayout(@NonNull View itemView) {
            super(itemView);
            subRecyclerView = itemView.findViewById(R.id.product_list_subRecyclerView);
            //Log.i(TAG, "EpisodeListLayout: "+itemView.getContext());

        }

        private void setData(List<OfflineTable> productModelList,boolean clickable)
        {

            Log.i(TAG, "setData: "+productModelList.size());

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    itemView.getContext(),LinearLayoutManager.VERTICAL,false
            );
            layoutManager.setInitialPrefetchItemCount(productModelList.size());
            offlineListAdapter = new OfflineListAdapter(itemView.getContext(),productModelList);
            subRecyclerView.setLayoutManager(layoutManager);
            subRecyclerView.setAdapter(offlineListAdapter);
            subRecyclerView.setRecycledViewPool(viewPool);

        }
    }

    class CourseHeaderLayout extends RecyclerView.ViewHolder
    {
        private TextView title;
        private TextView  description;
        private TextView  price;
        private TextView  time;
        private TextView  viewCount;
        private TextView  like;
        private TextView  favorite;
        private TextView  commentText;
        private LinearLayout favoriteBox,likeBox,commentBox;
        private ImageView likeImage,favoriteImage;

        public CourseHeaderLayout(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.course_card_title);
            description = itemView.findViewById(R.id.course_card_description);
            price = itemView.findViewById(R.id.course_card_price);
            time = itemView.findViewById(R.id.course_card_time);
            viewCount = itemView.findViewById(R.id.course_card_view_count);
            like = itemView.findViewById(R.id.course_card_like);
            favorite = itemView.findViewById(R.id.course_card_favorite);
            payButton = itemView.findViewById(R.id.course_card_herder_payBtn);
            likeImage = itemView.findViewById(R.id.course_card_like_image);
            favoriteImage = itemView.findViewById(R.id.course_card_favorite_image);
            favoriteBox = itemView.findViewById(R.id.course_card_favorite_box);
            likeBox = itemView.findViewById(R.id.course_card_like_box);
            commentBox = itemView.findViewById(R.id.course_card_comment_box);
            commentText = itemView.findViewById(R.id.course_card_comment_text);
            viewGroup = itemView.findViewById(android.R.id.content);

            courseAPIService = new CourseAPIService(itemView.getContext());
            //Log.i(TAG, "CourseHeaderLayout: "+itemView.getContext());



        }

        private void setData(ProductModel courseInfo)
        {
            this.title.setText(courseInfo.getTitle());
            this.description.setText(courseInfo.getDescription());

            if (courseInfo.getPrice() > 0) {
                this.price.setText(courseInfo.getPrice()+" تومان");
                payButton.setText("پرداخت و خرید محصول");
            }else{
                this.price.setText("رایگان");
                payButton.setText("دریافت محصول (رایگان)");
            }

            //this.time.setText(courseInfo.getTime());
            this.viewCount.setText(courseInfo.getViewCount()+"");
            this.like.setText(courseInfo.getLikeCount()+"");
            this.commentText.setText(courseInfo.getCommentCount()+" نظر");

            hasLiked = courseInfo.isHasLiked();
            hasFavorite = courseInfo.isFavorite();
            courseID = courseInfo.getProduct_id();
            likeCount = courseInfo.getLikeCount();
            myClickable = courseInfo.isPayCourse();
            coursePrice = courseInfo.getPrice();

           // Log.i(TAG, "setData: is favorite "+hasFavorite);

            if(courseInfo.isHasLiked())
               likeImage.setImageResource(R.drawable.ic_like);

            if(courseInfo.isFavorite())
                favoriteImage.setImageResource(R.drawable.ic_archive);

            if(courseInfo.isPayCourse())
            {
                payButton.setVisibility(View.GONE);
            }

            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(CheckRegistration())
                    {
                        openDialogPayment();
                    }
                }
            });

            likeBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Log.i(TAG, "SetLike: "+hasLiked);
                    if(CheckRegistration())
                    {
                        if(hasLiked)
                        {
                            likeImage.setImageResource(R.drawable.ic_favorite_border);
                            likeCount--;
                            like.setText((likeCount)+"");
                            hasLiked = false;

                            courseAPIService.DeleteLikeCourse(courseID, new CourseAPIService.onLikeCourse() {

                                @Override
                                public void onGet(boolean msg) {
                                    //  Log.i(TAG, "onGet: course liked "+msg);
                                }
                            });

                        }
                        else {

                            likeImage.setImageResource(R.drawable.ic_like);
                            likeCount++;
                            like.setText((likeCount)+"");
                            hasLiked = true;

                            courseAPIService.LikeCourse(courseID, new CourseAPIService.onLikeCourse() {
                                @Override
                                public void onGet(boolean msg) {
                                    //  Log.i(TAG, "onGet: course liked "+msg);
                                }
                            });

                        }
                    }

                }
            });

            favoriteBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(CheckRegistration())
                    {
                        if(hasFavorite)
                        {
                            favoriteImage.setImageResource(R.drawable.ic_unarchive);

                            courseAPIService.DeleteFavoriteCourse(courseID, new CourseAPIService.onFavorite() {
                                @Override
                                public void onGet(boolean msg) {
                                    if (msg) {
                                        Toast.makeText(context, "این محصول از لیست علاقمندی های شما حذف گردید", Toast.LENGTH_SHORT).show();;
                                        hasFavorite = false;
                                        //    Log.i(TAG, "onGet: deleted from favorite");
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "اختلال در لیست علاقمندی ها", Toast.LENGTH_SHORT).show();;
                                        favoriteImage.setImageResource(R.drawable.ic_archive);
                                    }
                                }
                            });

                        }
                        else {

                            favoriteImage.setImageResource(R.drawable.ic_archive);

                            courseAPIService.FavoriteCourse(courseID, new CourseAPIService.onFavorite() {
                                @Override
                                public void onGet(boolean msg) {
                                    if (msg) {
                                        hasFavorite = true;
                                        //  Log.i(TAG, "onGet: added to favorite");
                                        Toast.makeText(context, "این محصول به لیست علاقمندی های شما افزوده شد", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context, "اختلال در افزودن به لیست علاقمندی ها", Toast.LENGTH_SHORT).show();
                                        favoriteImage.setImageResource(R.drawable.ic_unarchive);
                                    }
                                }
                            });

                        }
                    }
                }
            });

            commentBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(CheckRegistration())
                    {
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("productType","courses");
                        intent.putExtra("productId",courseID);
                        context.startActivity(intent);
                    }
                }
            });

            //this.like.setText(courseInfo.get());
            //this.favorite.setText(courseInfo.getTitle());
        }
    }

    class PracticeImageListLayout extends RecyclerView.ViewHolder
    {
        private RecyclerView subRecyclerView;

        public PracticeImageListLayout(@NonNull View itemView) {
            super(itemView);
            subRecyclerView = itemView.findViewById(R.id.product_list_subRecyclerView);
           // Log.i(TAG, "PracticeImageListLayout: ");

        }

        private void setData(List<PracticeModel> practiceModelList)
        {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    itemView.getContext(),LinearLayoutManager.VERTICAL,false
            );
            layoutManager.setInitialPrefetchItemCount(practiceModelList.size());
            practiceAdapter = new PracticeAdapter(itemView.getContext(),practiceModelList,4);
            subRecyclerView.setLayoutManager(layoutManager);
            subRecyclerView.setAdapter(practiceAdapter);
            subRecyclerView.setRecycledViewPool(viewPool);

        }
    }

    class PracticePDFListLayout extends RecyclerView.ViewHolder
    {
        private RecyclerView subRecyclerView;

        public PracticePDFListLayout(@NonNull View itemView) {
            super(itemView);
            subRecyclerView = itemView.findViewById(R.id.product_list_subRecyclerView);
            //Log.i(TAG, "PracticeImageListLayout: ");

        }

        private void setData(List<PracticeModel> practiceModelList)
        {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    itemView.getContext(),LinearLayoutManager.VERTICAL,false
            );
            layoutManager.setInitialPrefetchItemCount(practiceModelList.size());
            practiceAdapter = new PracticeAdapter(itemView.getContext(),practiceModelList,5);
            subRecyclerView.setLayoutManager(layoutManager);
            subRecyclerView.setAdapter(practiceAdapter);
            subRecyclerView.setRecycledViewPool(viewPool);

        }


    }

    private void showBalance() {
        walletApiService = new WalletApiService(context);
        walletApiService.showBalance(new WalletApiService.OnGetBalanceListener() {

            @Override
            public void onBalance(int balance) {
                //identitySharedPref.setWalletBalance(balance);
                myBalance = balance;

            }
        });

    }

    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    private void openDialogPayment() {
        Log.i(TAG, "onGet: dialog");
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

    private void StartPayment()
    {
        walletApiService = new WalletApiService(context);
        walletApiService.showBalance(new WalletApiService.OnGetBalanceListener() {

            @Override
            public void onBalance(int balance) {
                //identitySharedPref.setWalletBalance(balance);
                myBalance = balance;

                // Log.i(TAG, "onClick: "+myBalance+" price : "+coursePrice);

                boolean useWallet = false;
                if(coursePrice<=myBalance) {
                    //  Log.i(TAG, "onBalance: use wallet");
                    useWallet = true;
                }

                courseAPIService.GetSingleCoursePerches(courseID, useWallet, new CourseAPIService.onGetPerchesSingleCourse() {
                    @Override
                    public void onGet(boolean msg, String URL) {

                        // Log.i(TAG, "onGet: "+URL);

                        if(URL.equals("1")) {
                            Toast.makeText(context, "پرداخت از طریق کیف پول شما با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                            //  Log.i(TAG, "onGet: payed");
                            payButton.setVisibility(View.GONE);
                            Intent intent = new Intent(context,CoursesActivity.class);
                            intent.putExtra("courseID",courseID);
                            context.startActivity(intent);
                            ((CoursesActivity)context).finish();
                        }
                        else if(URL.equals("0")) {
                            Toast.makeText(context, "این محصول به صورت رایگان فعال گردید", Toast.LENGTH_LONG).show();

                            payButton.setVisibility(View.GONE);
                            Intent intent = new Intent(context,CoursesActivity.class);
                            intent.putExtra("courseID",courseID);
                            context.startActivity(intent);
                            ((CoursesActivity)context).finish();
                        }
                        else if(URL.equals("-1"))
                            Toast.makeText(context,"شارژ کیف پول شما کافی نمی باشد",Toast.LENGTH_LONG).show();
                        else
                            openBrowser(URL);
                    }
                });

            }
        });
    }

    private void openAccountDialog() {
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
                context.startActivity(intent);
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
