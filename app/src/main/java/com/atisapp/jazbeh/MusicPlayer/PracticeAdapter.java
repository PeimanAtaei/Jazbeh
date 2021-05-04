package com.atisapp.jazbeh.MusicPlayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atisapp.jazbeh.Courses.CourseListAdapter;
import com.atisapp.jazbeh.Courses.CoursesActivity;
import com.atisapp.jazbeh.ProductList.PracticeModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.util.List;
import java.util.Random;

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder> {

    private static final String TAG = "PracticeAdapter";
    private List<PracticeModel> practiceModelList;
    private PracticeModel practiceModel;
    private Context ctx;
    private ProductPrefs productPrefs;
    private Random rnd;
    private int rndNum;
    private String mType;
    private int viewType;
    private boolean clickable = true;
    private String product_id,practice_url;

    public PracticeAdapter(Context context,List<PracticeModel> practiceModelList,int viewType) {
        Log.i(TAG, "PracticeAdapter: start");
        this.ctx = context;
        productPrefs = new ProductPrefs(ctx);
        this.practiceModelList = practiceModelList;
        this.viewType = viewType;
    }

    public void updateAdapterData(List<PracticeModel> data, String type) {
        this.practiceModelList = data;
        this.mType = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            Log.i(TAG, "onCreateViewHolder: image layout");
            View view = layoutInflater.from(parent.getContext()).inflate(R.layout.practice_image_each_item,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)  {
        practiceModel = practiceModelList.get(position);

        if(viewType == 4)
        {
            Log.i(TAG, "onBindViewHolder: set image title");
            holder.practice_image_each_item_title.setText(" تصویر شماره " +(position+1));
            holder.practice_image_each_item_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent;
                    intent = new Intent(ctx, PracticeImageActivity.class);
                    intent.putExtra("Url",practiceModelList.get(position).getPracticeURL());
                    ctx.startActivity(intent);
                    Log.i(TAG, "onClick image : "+position);
                    //Bundle bundle = new Bundle();
                    //bundle.putSerializable("courseModel", ProductListModel);
                    //intent.putExtras(bundle); //pass bundle to your intent
                }
            });
        }else if(viewType == 5)
        {
            holder.practice_image_each_item_title.setText(" متن شماره " + (position+1));
            holder.practice_image_each_item_type.setText("فایل متنی");
            holder.product_list_each_item_icon.setImageResource(R.drawable.document);
            holder.practice_image_each_item_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent;
                    intent = new Intent(ctx, PracticePDFActivity.class);
                    intent.putExtra("Url",practiceModelList.get(position).getPracticeURL());
                    ctx.startActivity(intent);
                    Log.i(TAG, "onClick image : "+position);
                    //Bundle bundle = new Bundle();
                    //bundle.putSerializable("courseModel", ProductListModel);
                    //intent.putExtras(bundle); //pass bundle to your intent
                }
            });
        }
        else {
            holder.practice_image_each_item_title.setText(" صوت شماره " + position);
        }
        practice_url = practiceModelList.get(position).getPracticeURL();





    }
    @Override
    public int getItemCount() {

        if (this.practiceModelList != null)
            return practiceModelList.size();
        else
            return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        private RelativeLayout practice_image_each_item_box;
        public TextView practice_image_each_item_title,practice_image_each_item_type;
        public ImageView product_list_each_item_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            practice_image_each_item_box = itemView.findViewById(R.id.practice_image_each_item_box);


            practice_image_each_item_title = itemView.findViewById(R.id.practice_image_each_item_title);
            practice_image_each_item_type = itemView.findViewById(R.id.practice_image_each_item_type);
            product_list_each_item_icon = itemView.findViewById(R.id.practice_image_each_item_img);
        }


    }


}
