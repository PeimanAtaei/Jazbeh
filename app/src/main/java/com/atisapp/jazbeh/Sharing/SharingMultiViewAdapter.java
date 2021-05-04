package com.atisapp.jazbeh.Sharing;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import java.util.List;

import static com.atisapp.jazbeh.Sharing.SharingMultiViewModel.APPS_LIST_LAYOUT;
import static com.atisapp.jazbeh.Sharing.SharingMultiViewModel.HEADER_LAYOUT;
import static com.atisapp.jazbeh.Sharing.SharingMultiViewModel.SHARE_HEADER_LAYOUT;


public class SharingMultiViewAdapter extends RecyclerView.Adapter{

    private static final String TAG = "SharingMultiViewAdapter";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private List<SharingMultiViewModel> multiViewModelList;
    private int lastPosition = -1;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ViewGroup viewGroup;
    private String shareLink;


    public SharingMultiViewAdapter(List<SharingMultiViewModel> multiViewModelList, Context context) {
        this.multiViewModelList = multiViewModelList;
        this.context = context;
        identitySharedPref = new IdentitySharedPref(context);

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
                return APPS_LIST_LAYOUT;
            }
            case 2:{
                return SHARE_HEADER_LAYOUT;
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
            case APPS_LIST_LAYOUT:
            {
                //Log.i(TAG, "onCreateViewHolder: AD_INFO_LAYOUT");
                View adLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_share_activity_grid_view, parent, false);
                return new AppsListLayout(adLayout);
            }

            case SHARE_HEADER_LAYOUT:
            {
                View adLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_share_activity_detail, parent, false);
                return new ShareHeaderLayout(adLayout);
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
            case APPS_LIST_LAYOUT:
            {
                List<SharingModel> appsModelList = multiViewModelList.get(position).getAppsModelList();
                ((AppsListLayout)holder).setData(appsModelList);
                break;
            }

            case SHARE_HEADER_LAYOUT:
            {
                String link = multiViewModelList.get(position).getShareLink();
                ((ShareHeaderLayout)holder).setData(link);
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

    class AppsListLayout extends RecyclerView.ViewHolder
    {
        private RecyclerView subRecyclerView;

        public AppsListLayout(@NonNull View itemView) {
            super(itemView);
            subRecyclerView = itemView.findViewById(R.id.apps_list_subRecyclerView);
            //Log.i(TAG, "EpisodeListLayout: "+itemView.getContext());

        }

        private void setData(List<SharingModel> productModelList)
        {

            //Log.i(TAG, "setData: "+productModelList.size());

            GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(),3);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
            gridLayoutManager.setReverseLayout(true);
            gridLayoutManager.setInitialPrefetchItemCount(productModelList.size());

            SharingAdapter sharingAdapter = new SharingAdapter(itemView.getContext(),productModelList,shareLink);
            subRecyclerView.setAdapter(sharingAdapter);
            subRecyclerView.setLayoutManager(gridLayoutManager);
            sharingAdapter.notifyDataSetChanged();
            subRecyclerView.setRecycledViewPool(viewPool);

        }
    }

    class ShareHeaderLayout extends RecyclerView.ViewHolder
    {
        private TextView link;
        private Button   copyBtn;

        public ShareHeaderLayout(@NonNull View itemView) {
            super(itemView);
            link = itemView.findViewById(R.id.share_detail_link);
            copyBtn = itemView.findViewById(R.id.share_detail_copy);
            viewGroup = itemView.findViewById(android.R.id.content);
        }

        private void setData(final String link)
        {

            shareLink = link;
            final String message = "سلام دوست من\nبا این لینک تو سایت جذبه ثبت نام و اپ جذبه رو نصب کن تا 15 هزار تومان شارژ رایگان برای خرید محصولات دریافت کنی\nبیش از 500 محصول شگفت انگیز تو این نرم افزار منتظر جفتمونه\n"+"http://share.jazbe.com/"+link;

            this.link.setText("http://share.jazbe.com/"+link);

            this.link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("jazbeh",message);
                    clipboard.setPrimaryClip(clip);
                }
            });

            this.copyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("jazbeh",message);
                    clipboard.setPrimaryClip(clip);
                }
            });
        }
    }

}
