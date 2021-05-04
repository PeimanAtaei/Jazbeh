package com.atisapp.jazbeh.VideoPlayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atisapp.jazbeh.Comment.CommentAPIService;
import com.atisapp.jazbeh.Comment.CommentModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private static final String TAG = "CommentAdapter";
    private List<VideoCategoryModel> categoryList;
    private VideoAPIService videoAPIService;
    private VideoCategoryModel categoryModel;
    private Context ctx;

    public CategoryAdapter(Context context, List<VideoCategoryModel> categoryList) {
        this.ctx = context;
        this.categoryList = categoryList;
        videoAPIService = new VideoAPIService(context);
    }

    public void updateAdapterData(List<VideoCategoryModel> data) {
        this.categoryList = data;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.eachitem_video_category,parent,false);
        CategoryAdapter.ViewHolder viewHolder = new CategoryAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        categoryModel = categoryList.get(position);

        holder.categoryTitle.setText(categoryModel.getCategoryTitle());
        holder.categoryDescription.setText(categoryModel.getCategoryBrief());

        Glide.with(ctx)
                .load(categoryModel.getCategoryImage())
                .placeholder(R.drawable.binauralimage)
                .into(holder.categoryImage);

        holder.categoryBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx,VideoListActivity.class);
                intent.putExtra("categoryId",categoryList.get(position).getCategoryId());
                ctx.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        if (this.categoryList != null)
            return categoryList.size();
        else
            return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        private LinearLayout categoryBox;
        public TextView categoryTitle,categoryDescription;
        public ImageView categoryImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryBox = itemView.findViewById(R.id.eachItem_video_category_box);
            categoryTitle = itemView.findViewById(R.id.eachItem_video_category_title);
            categoryDescription = itemView.findViewById(R.id.eachItem_video_category_description);
            categoryImage = itemView.findViewById(R.id.eachItem_video_category_image);
        }


    }

}
