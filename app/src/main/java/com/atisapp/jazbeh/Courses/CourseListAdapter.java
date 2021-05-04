package com.atisapp.jazbeh.Courses;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atisapp.jazbeh.MusicPlayer.MusicPlayerActivity;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.util.List;
import java.util.Random;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {

    private static final String TAG = "CourseListAdapter";
    private List<ProductModel> productList;
    private ProductModel productModel;
    private Context ctx;
    private ProductPrefs productPrefs;
    private Random rnd;
    private int rndNum;
    private String mType;
    private int viewType;
    private boolean clickable = true;
    private String product_id;

    public CourseListAdapter(Context context,List<ProductModel> productList,int viewType,boolean clickable) {
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
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.product_list_each_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)  {
        productModel = productList.get(position);
        holder.product_list_each_item_name.setText(productModel.getTitle());
        //holder.product_list_each_item_time.setText(productModel.getTime());
        product_id = productList.get(position).getProduct_id();

        if(viewType ==1) {
            holder.product_list_each_item_icon.setImageResource(R.drawable.eachitem2);
            holder.product_list_each_item_time.setText("محصول دوره ای");

        }
        else {
            holder.product_list_each_item_icon.setImageResource(R.drawable.eachitem1);
            holder.product_list_each_item_time.setText("تک اپیزود");

        }

        holder.product_list_each_item_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clickable)
                {
                    Intent intent;
                    if(viewType == 1) {
                        intent = new Intent(ctx, CoursesActivity.class);
                        intent.putExtra("courseID",productList.get(position).getProduct_id());
                        Log.i(TAG, "onClick: "+position);
                    }else
                    {
                        intent = new Intent(ctx, MusicPlayerActivity.class);
                        intent.putExtra("episodeID", productList.get(position).getProduct_id());
                        Log.i(TAG, "onClick: "+position+" "+productList.get(position).getProduct_id());
                    }
                    //Bundle bundle = new Bundle();
                    //bundle.putSerializable("courseModel", ProductListModel);
                    //intent.putExtras(bundle); //pass bundle to your intent
                    ctx.startActivity(intent);
                }else {

                    Log.i(TAG, "onClick: can not click"+ctx);
                    Toast.makeText(ctx,"جزئیات محصول بعد از خرید دوره قابل مشاهده می باشد",Toast.LENGTH_LONG).show();
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

        private RelativeLayout product_list_each_item_box;
        public TextView product_list_each_item_name,product_list_each_item_group_name,product_list_each_item_time;
        public ImageView product_list_each_item_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_list_each_item_box = itemView.findViewById(R.id.product_list_each_item_box);
            product_list_each_item_name = itemView.findViewById(R.id.product_list_each_item_name_en);
            product_list_each_item_group_name = itemView.findViewById(R.id.product_list_each_item_group);
            product_list_each_item_time = itemView.findViewById(R.id.product_list_each_item_time);
            product_list_each_item_icon = itemView.findViewById(R.id.product_list_each_item_img);


        }


    }


}
