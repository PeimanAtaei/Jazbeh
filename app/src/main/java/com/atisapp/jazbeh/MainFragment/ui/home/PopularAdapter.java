package com.atisapp.jazbeh.MainFragment.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atisapp.jazbeh.ProductList.ProductListActivity;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    private static final String TAG = "PopularAdapter";
    private List<ProductModel> productList;
    private ProductModel productModel;
    private Context ctx;
    private ProductPrefs productPrefs;
    private int viewType;
    private String product_id;

    public PopularAdapter(Context context,List<ProductModel> productList,int viewType) {
        this.ctx = context;
        productPrefs = new ProductPrefs(ctx);
        this.productList = productList;
        this.viewType = viewType;

    }

    public void updateAdapterData(List<ProductModel> data) {
        this.productList = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        view = layoutInflater.from(parent.getContext()).inflate(R.layout.layout_dashboard_popular_eachitem,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)  {
        productModel = productList.get(position);
        holder.dashboard_list_each_item_title.setText(productModel.getTitle());
        //holder.dashboard_list_each_item_brief.setText(productModel.getBrief());
        product_id = productList.get(position).getProduct_id();

        Drawable myImage = ctx.getResources().getDrawable(ctx.getResources()
                .getIdentifier(productModel.getImage(), "drawable", ctx.getPackageName()));
        holder.dashboard_list_each_item_image.setImageDrawable(myImage);


        holder.dashboard_list_each_item_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productPrefs.set_product_group_id(productModel.getProduct_id());
                Intent intent = new Intent(ctx, ProductListActivity.class);
                ctx.startActivity(intent);
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

        private LinearLayout dashboard_list_each_item_box;
        public TextView dashboard_list_each_item_title,dashboard_list_each_item_brief;
        public ImageView dashboard_list_each_item_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dashboard_list_each_item_box = itemView.findViewById(R.id.dashboard_popular_box);
            dashboard_list_each_item_title = itemView.findViewById(R.id.dashboard_popular_first_title);
            //dashboard_list_each_item_brief = itemView.findViewById(R.id.dashboard_category_brief);
            //dashboard_list_each_item_image = itemView.findViewById(R.id.dashboard_category_image);


        }


    }


}
