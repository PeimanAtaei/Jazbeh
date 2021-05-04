package com.atisapp.jazbeh.MainFragment.ui.Shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atisapp.jazbeh.MusicPlayer.MusicPlayerActivity;
import com.atisapp.jazbeh.ProductList.ProductListActivity;
import com.atisapp.jazbeh.ProductList.ProductListModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.util.List;
import java.util.Random;

public class ShopListAdapter extends BaseAdapter {

    private static final String TAG = "ShopListAdapter";
    private List<ProductListModel> productList;
    private Context ctx;
    private ProductPrefs productPrefs;
    private Random rnd;
    private int rndNum;

    public ShopListAdapter(Context context) {
        super();
        this.ctx = context;
        productPrefs = new ProductPrefs(ctx);
    }


    public void updateAdapterData(List<ProductListModel> data) {
        this.productList = data;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ProductListModel ProductListModel = (ProductListModel) getItem(position);
        ViewHolder holder;
        if(convertView == null){
            holder 				= new ViewHolder();
            convertView			= LayoutInflater.from(ctx).inflate(R.layout.shop_list_each_item, parent, false);
            holder.product_list_each_item_box = (RelativeLayout) convertView.findViewById(R.id.shop_list_each_item_box);
            holder.product_list_each_item_name = (TextView) convertView.findViewById(R.id.shop_list_each_item_name);
            holder.product_list_each_item_group_name = (TextView) convertView.findViewById(R.id.shop_list_each_item_group);
            holder.product_list_each_item_time = (TextView) convertView.findViewById(R.id.shop_list_each_item_time);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.product_list_each_item_name.setText(ProductListModel.getProduct_name());
        holder.product_list_each_item_time.setText(ProductListModel.getProduct_time());

//        if(productPrefs.get_product_type() == 1)
//        {
//            holder.product_list_each_item_icon.setImageResource(R.drawable.ic_music);
//        }
//        else if(productPrefs.get_product_type() == 2)
//        {
//            holder.product_list_each_item_icon.setImageResource(R.drawable.ic_ballad);
//        }
        //holder.product_list_each_item_group_name.setText(ProductListModel.getGroup_id()+"");

        if (ProductListModel.getIs_package()) {
            holder.product_list_each_item_box.setVisibility(View.VISIBLE);

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
            holder.product_list_each_item_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    productPrefs.set_product_name(ProductListModel.getProduct_name());
                    productPrefs.set_product_alarm(ProductListModel.getProduct_alarm());
                    productPrefs.set_product_explain(ProductListModel.getProduct_explain());
                    productPrefs.set_product_id(ProductListModel.getProduct_id());
                    productPrefs.set_product_price(ProductListModel.getPrice());
                    productPrefs.set_product_time(ProductListModel.getProduct_time());
                    productPrefs.set_product_usage(ProductListModel.getProduct_usage());
                    productPrefs.set_product_bought(ProductListModel.isBought());

                    //Log.i(TAG, "onClick: "+productPrefs.get_product_bought());

                    Intent intent = new Intent(ctx, MusicPlayerActivity.class);
                    ctx.startActivity(intent);

                }
            });
        }

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

    private static class ViewHolder {

        private RelativeLayout product_list_each_item_box;
        private TextView    product_list_each_item_name;
        private TextView    product_list_each_item_group_name;
        private TextView    product_list_each_item_time;

    }

}
