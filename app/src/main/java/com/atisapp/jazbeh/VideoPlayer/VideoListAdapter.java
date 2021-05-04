package com.atisapp.jazbeh.VideoPlayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atisapp.jazbeh.Courses.CoursesActivity;
import com.atisapp.jazbeh.MusicPlayer.MusicPlayerActivity;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private static final String TAG = "VideoListAdapter";
    private List<ProductModel> productList;
    private ProductModel productModel;
    private Context ctx;
    private ProductPrefs productPrefs;
    private String mType;
    private int viewType;
    private boolean clickable = true;
    private String product_id;

    public VideoListAdapter(Context context, List<ProductModel> productList, int viewType, boolean clickable) {
        this.ctx = context;
        productPrefs = new ProductPrefs(ctx);
        this.productList = productList;
        this.viewType = viewType;
        this.clickable = clickable;

    }

    public void updateAdapterData(List<ProductModel> data, String type) {
        this.productList = data;
        this.mType = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.eachitem_video,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)  {
        productModel = productList.get(position);

        Log.i(TAG, "onBindViewHolder: "+productModel.getTitle()+" "+productModel.getImage());

        holder.eachItem_video_title.setText(productModel.getTitle());
        //holder.eachItem_video_description.setText(productModel.getBrief());
        Glide.with(ctx)
                .load(productModel.getImage())
                .placeholder(R.drawable.playimage)
                .into(holder.eachItem_video_image);

        //holder.product_list_each_item_time.setText(productModel.getTime());
        product_id = productList.get(position).getProduct_id();
        holder.video_list_each_item_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clickable)
                {
                    Intent intent = null;
                    if(viewType == 1) {
                        intent = new Intent(ctx, VideoCoursesActivity.class);
                        intent.putExtra("courseID",productList.get(position).getProduct_id());
                    }else
                    {
                        intent = new Intent(ctx, VideoPlayerActivity.class);
                        intent.putExtra("episodeID", productList.get(position).getProduct_id());
                    }
                    //Bundle bundle = new Bundle();
                    //bundle.putSerializable("courseModel", ProductListModel);
                    //intent.putExtras(bundle); //pass bundle to your intent
                    ctx.startActivity(intent);
                }else {

                    Log.i(TAG, "onClick: can not click"+ctx);
                    Toast.makeText(ctx,"ویدئو ها بعد از دریافت دوره قابل مشاهده می باشند",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {

        if (this.productList != null)
            return productList.size();
        else
            return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        private LinearLayout video_list_each_item_box;
        public TextView eachItem_video_title,eachItem_video_description;
        public ImageView eachItem_video_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            video_list_each_item_box = itemView.findViewById(R.id.video_list_each_item_box);
            eachItem_video_image = itemView.findViewById(R.id.eachItem_video_image);
            eachItem_video_title = itemView.findViewById(R.id.eachItem_video_title);
            eachItem_video_description = itemView.findViewById(R.id.eachItem_video_description);


        }


    }


}
