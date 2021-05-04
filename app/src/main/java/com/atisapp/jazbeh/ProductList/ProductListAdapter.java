package com.atisapp.jazbeh.ProductList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atisapp.jazbeh.MusicPlayer.MusicPlayerActivity;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ProductListAdapter extends BaseAdapter {

    private static final String TAG = "ProductListAdapter";
    private List<ProductListModel> productList;
    private Context ctx;
    private ProductPrefs productPrefs;
    private Random rnd;
    private int rndNum;
    private String mType;

    public ProductListAdapter(Context context) {
        super();
        this.ctx = context;
        productPrefs = new ProductPrefs(ctx);
    }


    public void updateAdapterData(List<ProductListModel> data, String type) {
        this.productList = data;
        this.mType = type;
    }

    @Override
    public int getCount() {
        if (this.productList == null) {
            return 0;
        } else {
            return this.productList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
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
        final ProductListModel ProductListModel = (ProductListModel) getItem(position);
        ViewHolder holder;
        if(convertView == null){
            holder 				= new ViewHolder();
            convertView			= LayoutInflater.from(ctx).inflate(R.layout.product_list_each_item, parent, false);
            holder.product_list_each_item_box = (RelativeLayout) convertView.findViewById(R.id.product_list_each_item_box);
            holder.product_list_each_item_name = (TextView) convertView.findViewById(R.id.product_list_each_item_name_en);
            holder.product_list_each_item_group_name = (TextView) convertView.findViewById(R.id.product_list_each_item_group);
            holder.product_list_each_item_time = (TextView) convertView.findViewById(R.id.product_list_each_item_time);
            holder.product_list_each_item_package = (TextView) convertView.findViewById(R.id.product_list_each_item_package);
            holder.product_list_each_item_icon = (ImageView) convertView.findViewById(R.id.product_list_each_item_img);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.product_list_each_item_name.setText(ProductListModel.getProduct_name());

        if(ProductListModel.getIs_package())
        {

            holder.product_list_each_item_package.setVisibility(View.VISIBLE);

            holder.product_list_each_item_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    productPrefs.set_product_package_id(ProductListModel.getProduct_id());
                    productPrefs.set_product_package_price(ProductListModel.getPrice());

                    productPrefs.set_is_package(true);
                    //productPrefs.set_products_is_package(true);
                    //Log.i(TAG, "onClick: "+ProductListModel.getProduct_id());

                    Intent intent = new Intent(ctx, ProductListActivity.class);
                    intent.putExtra("isPackage",true);

                    ctx.startActivity(intent);
                }
            });
        } else {
            holder.product_list_each_item_time.setText(ProductListModel.getProduct_time());


            holder.product_list_each_item_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    productPrefs.set_is_package(false);

                    productPrefs.set_product_msg(ProductListModel.getProduct_msg());
                    productPrefs.set_product_name(ProductListModel.getProduct_name());
                    productPrefs.set_product_alarm(ProductListModel.getProduct_alarm());
                    productPrefs.set_product_explain(ProductListModel.getProduct_explain());
                    productPrefs.set_product_id(ProductListModel.getProduct_id());
                    productPrefs.set_product_price(ProductListModel.getPrice());
                    productPrefs.set_product_time(ProductListModel.getProduct_time());
                    productPrefs.set_product_usage(ProductListModel.getProduct_usage());
                    productPrefs.set_practice(ProductListModel.getPractice());
                    productPrefs.set_product_bought(ProductListModel.isBought());
                    
                    //Log.i(TAG, "onClick: "+productPrefs.get_product_url());

                    Intent intent = new Intent(ctx, MusicPlayerActivity.class);
                    intent.putExtra("isPackage",false);
                    ctx.startActivity(intent);
                }
            });
        }
//        else if(productPrefs.get_product_type() == 2)
//        {
//            holder.product_list_each_item_icon.setImageResource(R.drawable.ic_ballad);
//        }
        //holder.product_list_each_item_group_name.setText(ProductListModel.getGroup_id()+"");



        return convertView;
    }

    /**
     * remove data item from messageAdapter
     *
     **/
    public void remove(int position){
        productList.remove(position);
    }

    /**
     * add data item to messageAdapter
     *
     **/
    public void add(ProductListModel familyListModel){
        productList.add(familyListModel);
    }

    public static class ViewHolder {

        private RelativeLayout product_list_each_item_box;
        private ImageView   product_list_each_item_icon;
        private TextView    product_list_each_item_name;
        private TextView    product_list_each_item_group_name;
        private TextView    product_list_each_item_time;
        private TextView    product_list_each_item_package;

    }

}
