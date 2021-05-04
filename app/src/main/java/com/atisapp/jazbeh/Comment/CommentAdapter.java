package com.atisapp.jazbeh.Comment;

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

import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Courses.CourseListAdapter;
import com.atisapp.jazbeh.Courses.CoursesActivity;
import com.atisapp.jazbeh.MusicPlayer.MusicPlayerActivity;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.util.List;
import java.util.Random;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private static final String TAG = "CommentAdapter";
    private List<CommentModel> commentsList;
    private CommentAPIService commentAPIService;
    private CommentModel commentModel;
    private Context ctx;
    private ProductPrefs productPrefs;
    private String mType;
    private String product_id;
    private int likeCounter;
    private boolean hasLiked;

    public CommentAdapter(Context context,List<CommentModel> commentsList) {
        this.ctx = context;
        productPrefs = new ProductPrefs(ctx);
        this.commentsList = commentsList;
        commentAPIService = new CommentAPIService(context);
    }

    public void updateAdapterData(List<CommentModel> data, String type) {
        this.commentsList = data;
        this.mType = type;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.comment_eachitem,parent,false);
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        commentModel = commentsList.get(position);
        if(commentModel.getUserName()!= null)
            holder.userName.setText(commentModel.getUserName());
        holder.commentsText.setText(commentModel.getCommentText());
        holder.likeCount.setText(commentModel.getLikeCount()+"");
        holder.time.setText(commentModel.getCreateDate());
        if(commentModel.isHasLiked())
            holder.likeImage.setImageResource(R.drawable.ic_like);
        if(commentModel.getResponseText() == null)
            holder.responseBox.setVisibility(View.GONE);
        else
            holder.responseText.setText(commentModel.getResponseText());
        holder.likeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: "+commentsList.get(position).isHasLiked());
                if(commentsList.get(position).isHasLiked())
                {
                    holder.likeImage.setImageResource(R.drawable.ic_favorite_border);
                    likeCounter = commentsList.get(position).getLikeCount();
                    likeCounter--;
                    holder.likeCount.setText((likeCounter)+"");
                    commentsList.get(position).setLikeCount(likeCounter);
                    commentsList.get(position).setHasLiked(false);

                   commentAPIService.DeleteLikeComment(commentsList.get(position).getCommentId(), new CommentAPIService.onDislikeComments() {
                       @Override
                       public void onGet(boolean msg) {
                           Log.i(TAG, "onGet: like deleted"+commentsList.get(position).getCommentId());
                       }
                   });

                }else {
                    holder.likeImage.setImageResource(R.drawable.ic_like);
                    likeCounter = commentsList.get(position).getLikeCount();
                    likeCounter++;
                    holder.likeCount.setText((likeCounter)+"");
                    commentsList.get(position).setLikeCount(likeCounter);
                    commentsList.get(position).setHasLiked(true);

                    commentAPIService.LikeComment(commentsList.get(position).getCommentId(), new CommentAPIService.onLikeComments() {
                        @Override
                        public void onGet(boolean msg) {
                            Log.i(TAG, "onGet: comment like"+commentsList.get(position).getCommentId());
                        }
                    });
                }
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

        private LinearLayout commentBox;
        private LinearLayout responseBox;
        private LinearLayout likeBox;
        public TextView userName,commentsText,responseText,likeCount,time;
        public ImageView likeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            commentBox = itemView.findViewById(R.id.commentBox);
            responseBox = itemView.findViewById(R.id.comment_admin_box);
            likeBox = itemView.findViewById(R.id.comment_card_like_box);
            userName = itemView.findViewById(R.id.comment_user_name);
            commentsText = itemView.findViewById(R.id.comment_user_text);
            responseText = itemView.findViewById(R.id.comment_admin_text);
            likeCount = itemView.findViewById(R.id.comment_card_like);
            likeImage = itemView.findViewById(R.id.comment_card_like_image);
            time = itemView.findViewById(R.id.comment_card_time);


        }


    }

}
