package com.atisapp.jazbeh.Offline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.activeandroid.query.Delete;
import com.atisapp.jazbeh.Courses.CoursesActivity;
import com.atisapp.jazbeh.MusicPlayer.MusicPlayerActivity;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Database.Tables.OfflineTable;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;

import java.io.File;
import java.util.List;
import java.util.Random;

public class OfflineListAdapter extends RecyclerView.Adapter<OfflineListAdapter.ViewHolder> {

    private static final String TAG = "OfflineListAdapter";
    private List<OfflineTable> productList;
    private OfflineTable productModel;
    private Context ctx;
    private ProductPrefs productPrefs;
    private Random rnd;
    private int rndNum;
    private String mType;
    private int viewType;
    private boolean clickable = true;
    private String product_id;
    private ViewGroup viewGroup;
    private int currentPosition;

    public OfflineListAdapter(Context context, List<OfflineTable> productList) {
        this.ctx = context;
        productPrefs = new ProductPrefs(ctx);
        this.productList = productList;

    }

    public void updateAdapterData(List<OfflineTable> data) {
        this.productList = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.offline_list_each_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)  {
        productModel = productList.get(position);
        holder.product_list_each_item_name.setText(productModel.getOffline_title());
        //holder.product_list_each_item_time.setText(productModel.getTime());
        product_id = productList.get(position).getOffline_id();
        //holder.product_list_each_item_time.setText(productModel.getOffline_time()+"");

        holder.product_list_each_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                ShowDialog();
            }
        });

        holder.product_list_each_item_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent;

                        intent = new Intent(ctx, OfflineMusicPlayerActivity.class);
                        intent.putExtra("episodeID", productList.get(position).getOffline_id());
                        Log.i(TAG, "onClick: "+position+" "+productList.get(position).getOffline_id());
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


        private RelativeLayout product_list_each_item_box;
        public TextView product_list_each_item_name,product_list_each_item_group_name,product_list_each_item_time;
        public ImageView product_list_each_item_icon;
        public Button product_list_each_item_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_list_each_item_box = itemView.findViewById(R.id.product_list_each_item_box);
            product_list_each_item_name = itemView.findViewById(R.id.product_list_each_item_name_en);
            product_list_each_item_group_name = itemView.findViewById(R.id.product_list_each_item_group);
            product_list_each_item_time = itemView.findViewById(R.id.product_list_each_item_time);
            product_list_each_item_icon = itemView.findViewById(R.id.product_list_each_item_img);
            product_list_each_item_delete = itemView.findViewById(R.id.product_list_each_item_delete);

        }


    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE: {

                    final File proFile = new File(productList.get(currentPosition).getOffline_url());
                    proFile.delete();

                    new Delete().from(OfflineTable.class).where("product_id = ?", productList.get(currentPosition).getOffline_id()).execute();

                    Toast.makeText(ctx, "محصول حذف گردید", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ctx,OfflineListActivity.class);
                    ctx.startActivity(intent);
                    ((OfflineListActivity)ctx).finish();

                    break;
                }

                case DialogInterface.BUTTON_NEGATIVE: {
                    break;
                }
            }
        }
    };

    private void ShowDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage("آیا مایل به حذف این محصول از حافظه دستگاه هستید؟").setPositiveButton("بله", dialogClickListener)
                .setNegativeButton("خیر", dialogClickListener).show();
    }




}
