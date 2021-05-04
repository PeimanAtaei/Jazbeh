package com.atisapp.jazbeh.Episodes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atisapp.jazbeh.Courses.CourseListAdapter;
import com.atisapp.jazbeh.Courses.CoursesActivity;
import com.atisapp.jazbeh.MusicPlayer.MusicPlayerActivity;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class EpisodeAdapter extends BaseAdapter {

    private static final String TAG = "EpisodeListAdapter";
    private List<ProductModel> episodeList;
    private Context ctx;
    private ProductPrefs productPrefs;
    private Random rnd;
    private int rndNum;
    private String mType;

    public EpisodeAdapter(Context context) {
        super();
        this.ctx = context;
        productPrefs = new ProductPrefs(ctx);
    }

    public void updateAdapterData(List<ProductModel> data, String type) {
        this.episodeList = data;
        this.mType = type;
    }


    @Override
    public int getCount() {
        if (this.episodeList == null) {
            return 0;
        } else {
            return this.episodeList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return episodeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return episodeList.get(position).getId();
    }
    public static String getFormatedNumber(String number){
        if(!number.isEmpty()) {
            double val = Double.parseDouble(number);
            return NumberFormat.getNumberInstance(Locale.US).format(val);
        }else{
            return "0";
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ProductModel episodeListModel = (ProductModel) getItem(position);
        EpisodeAdapter.ViewHolder holder;
        if(convertView == null){
            holder 				= new EpisodeAdapter.ViewHolder();
            convertView			= LayoutInflater.from(ctx).inflate(R.layout.product_list_each_item, parent, false);
            holder.product_list_each_item_box = (RelativeLayout) convertView.findViewById(R.id.product_list_each_item_box);
            holder.product_list_each_item_name = (TextView) convertView.findViewById(R.id.product_list_each_item_name_en);
            holder.product_list_each_item_group_name = (TextView) convertView.findViewById(R.id.product_list_each_item_group);
            holder.product_list_each_item_time = (TextView) convertView.findViewById(R.id.product_list_each_item_time);
            holder.product_list_each_item_package = (TextView) convertView.findViewById(R.id.product_list_each_item_package);
            holder.product_list_each_item_icon = (ImageView) convertView.findViewById(R.id.product_list_each_item_img);

            convertView.setTag(holder);

        }else{
            holder = (EpisodeAdapter.ViewHolder) convertView.getTag();
        }

        holder.product_list_each_item_name.setText(episodeListModel.getTitle());
        holder.product_list_each_item_time.setText(episodeListModel.getTime());
        holder.product_list_each_item_icon.setImageResource(R.drawable.eachitem1);
        holder.product_list_each_item_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ctx,"Episode Clicked",Toast.LENGTH_SHORT);

                Intent intent = new Intent(ctx, MusicPlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("episodeModel", episodeListModel);
                intent.putExtras(bundle); //pass bundle to your intent
                ctx.startActivity(intent);
                    /*productPrefs.set_product_package_id(ProductListModel.getProduct_id());
                    productPrefs.set_product_package_price(ProductListModel.getPrice());

                    productPrefs.set_is_package(true);
                    //productPrefs.set_products_is_package(true);
                    //Log.i(TAG, "onClick: "+ProductListModel.getProduct_id());

                    Intent intent = new Intent(ctx, ProductListActivity.class);
                    intent.putExtra("isPackage",true);

                    ctx.startActivity(intent);*/
            }
        });

        return convertView;
    }

    public void add(ProductModel productModel){
        episodeList.add(productModel);
    }

    public class ViewHolder {
        private RelativeLayout product_list_each_item_box;
        private ImageView   product_list_each_item_icon;
        private TextView    product_list_each_item_name;
        private TextView    product_list_each_item_group_name;
        private TextView    product_list_each_item_time;
        private TextView    product_list_each_item_package;

    }
}
