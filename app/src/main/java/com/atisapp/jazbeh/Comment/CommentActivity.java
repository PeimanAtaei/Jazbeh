package com.atisapp.jazbeh.Comment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atisapp.jazbeh.MainFragment.FeatureAdapter;
import com.atisapp.jazbeh.MainFragment.UpdateModel;
import com.atisapp.jazbeh.R;

import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";
    private Context context;
    private CommentAdapter commentAdapter;
    private CommentAPIService commentAPIService;
    private RecyclerView commentsRecyclerView;
    private String productType,productId;
    private List<CommentModel> commentList;
    private LinearLayout emptyView;
    private ProgressDialog progressDialog;
    private EditText commentEditText;
    private TextView sendCommentBtn;
    private boolean canSendComment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        SetupViews();

        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(canSendComment)
                {
                    canSendComment = false;
                    CreateComment(commentEditText.getText().toString());
                }

            }
        });
    }

    private void SetupViews()
    {
        context = CommentActivity.this;
        commentAPIService = new CommentAPIService(context);
        commentsRecyclerView = findViewById(R.id.comment_activity_recyclerView);
        emptyView = findViewById(R.id.comment_empty_list_view);
        commentEditText = findViewById(R.id.searchEditText);
        sendCommentBtn = findViewById(R.id.sendCommentButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();

        productType = intent.getStringExtra("productType");
        productId = intent.getStringExtra("productId");

        Log.i(TAG, "SetupViews: type :"+productType+" id : "+productId);

        GetComments();
    }

    private void GetComments()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت نظرات");
            progressDialog.show();
        }

        commentAPIService.GetComments(productType, productId, new CommentAPIService.onGetComments() {
            @Override
            public void onGet(boolean msg, List<CommentModel> list_comments) {
                Log.i(TAG, "onGet: "+list_comments.size());
                if(msg && list_comments!=null)
                {
                    commentList = list_comments;
                    commentAdapter = new CommentAdapter(context,list_comments);
                    commentsRecyclerView.setAdapter(commentAdapter);
                    commentAdapter.notifyDataSetChanged();
                }

                if(list_comments== null || list_comments.isEmpty())
                {
                    emptyView.setVisibility(View.VISIBLE);
                }

                if(progressDialog != null)
                    progressDialog.dismiss();
            }
        });



    }

    private void CreateComment(String commentText)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال ارسال نظر شما");
        progressDialog.show();

        commentAPIService.CreateComment(productId, productType, commentText, new CommentAPIService.onCreateComment() {
            @Override
            public void onGet(boolean msg) {
                if(progressDialog != null)
                    progressDialog.hide();

                commentEditText.setText("");

                if(msg)
                    openDialogComment();
                else
                    Toast.makeText(context,"اختلال در ارسال پیام",Toast.LENGTH_SHORT).show();

                GetComments();
                canSendComment = true;

                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });
    }

    private void openDialogComment() {
        Log.i(TAG, "onGet: dialog");
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_sent_comment_dialog, viewGroup, false);

        Button      commentDialogBtn               = dialogView.findViewById(R.id.comment_sent_btn);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        final AlertDialog updateDialog = builder.create();
        updateDialog.show();

        commentDialogBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
            }
        });
        updateDialog.setCancelable(true);
        updateDialog.setCanceledOnTouchOutside(true);



    }
}
