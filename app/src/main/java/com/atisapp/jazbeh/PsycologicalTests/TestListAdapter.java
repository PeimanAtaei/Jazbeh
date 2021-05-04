package com.atisapp.jazbeh.PsycologicalTests;

import android.content.Context;
import android.content.Intent;
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

import com.atisapp.jazbeh.PsycologicalTests.model.TestModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.VideoPlayer.VideoAPIService;
import com.atisapp.jazbeh.VideoPlayer.VideoCategoryModel;
import com.atisapp.jazbeh.VideoPlayer.VideoListActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ViewHolder> {

    private static final String TAG = "TestListAdapter";
    private List<TestModel> testList;
    private TestAPIService testAPIService;
    private TestModel testModel;
    private Context ctx;

    public TestListAdapter(Context context, List<TestModel> testList) {
        this.ctx = context;
        this.testList = testList;
        testAPIService = new TestAPIService(context);
    }

    public void updateAdapterData(List<TestModel> data) {
        this.testList = data;
    }

    @NonNull
    @Override
    public TestListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.test_list_each_item,parent,false);
        TestListAdapter.ViewHolder viewHolder = new TestListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        testModel = testList.get(position);

        holder.testTitle.setText(testModel.getName());
        holder.questionCount.setText("تعداد سوالات : "+testModel.getQuestionCount());

        Glide.with(ctx)
                .load(testModel.getImage())
                .placeholder(R.drawable.test)
                .into(holder.testImage);

        holder.itemBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx,TestActivity.class);
                intent.putExtra("testId",testList.get(position).getTestId());
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        if (this.testList != null)
            return testList.size();
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        private RelativeLayout itemBox;
        public TextView testTitle,questionCount;
        public ImageView testImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemBox = itemView.findViewById(R.id.test_list_each_item_box);
            testTitle = itemView.findViewById(R.id.test_list_each_item_name_en);
            questionCount = itemView.findViewById(R.id.test_list_each_item_group);
            testImage = itemView.findViewById(R.id.test_list_each_item_img);
        }
    }
}
