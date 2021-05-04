package com.atisapp.jazbeh.Sharing;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atisapp.jazbeh.Comment.CommentAPIService;
import com.atisapp.jazbeh.Comment.CommentAdapter;
import com.atisapp.jazbeh.Comment.CommentModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.util.List;

public class SharingAdapter extends RecyclerView.Adapter<SharingAdapter.ViewHolder> {

    private static final String TAG = "CommentAdapter";
    private List<SharingModel> commentsList;
    private SharingModel commentModel;
    private Context ctx;
    private String link;
    private LayoutInflater layoutInflater;
    private View view;

    public SharingAdapter(Context context, List<SharingModel> commentsList,String link) {
        this.ctx = context;
        this.commentsList = commentsList;
        this.link = link;

    }

    public void updateAdapterData(List<SharingModel> data, String type) {
        this.commentsList = data;
    }

    @NonNull
    @Override
    public SharingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.eachitem_sharing_apps,parent,false);
        SharingAdapter.ViewHolder viewHolder = new SharingAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SharingAdapter.ViewHolder holder, final int position) {
        commentModel = commentsList.get(position);

        holder.title.setText(commentModel.getTitle());
        holder.icon.setImageDrawable(commentModel.getIcon());

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "سلام دوست من\nبا این لینک تو سایت جذبه ثبت نام و اپ جذبه رو نصب کن تا 15 هزار تومان شارژ رایگان برای خرید محصولات دریافت کنی\nبیش از 500 محصول شگفت انگیز تو این نرم افزار منتظر جفتمونه\n"+"http://share.jazbe.com/"+link;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                sendIntent.setPackage(commentsList.get(position).getPackageName());

                //Intent shareIntent = Intent.createChooser(sendIntent, null);
                ctx.startActivity(sendIntent);
            }
        });

    }

    @Override
    public int getItemCount() {

        if (this.commentsList != null)
            return commentsList.size();
        else
            return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {


        public TextView title;
        public ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.eachItem_sharing_title);
            icon = itemView.findViewById(R.id.eachItem_sharing_image);



        }


    }


}
