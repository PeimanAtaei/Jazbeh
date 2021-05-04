package com.atisapp.jazbeh.Delegate.CheckOut;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atisapp.jazbeh.R;

import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ViewHolder> {

    private static final String TAG = "PurchaseAdapter";
    private List<CheckOutModel> checkOutModelList;
    private CheckOutModel checkOutModel;
    private Context ctx;

    public CheckOutAdapter(Context context,List<CheckOutModel> checkOutModels) {
        this.ctx = context;
        this.checkOutModelList = checkOutModels;
    }

    public void updateAdapterData(List<CheckOutModel> data) {
        this.checkOutModelList = data;
    }

    @NonNull
    @Override
    public CheckOutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.eachitem_delegate_check_out,parent,false);
        CheckOutAdapter.ViewHolder viewHolder = new CheckOutAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckOutAdapter.ViewHolder holder, final int position) {
        checkOutModel = checkOutModelList.get(position);

        Log.i(TAG, "onBindViewHolder: "+checkOutModel.getInvoiceNumber()+" "+checkOutModel.getRequestDate());

        holder.balance.setText(checkOutModel.getBalance()+" تومان");
        holder.trackNumber.setText(checkOutModel.getInvoiceNumber());
        holder.payDate.setText(checkOutModel.getCheckOutDate());
        holder.requestDate.setText(checkOutModel.getRequestDate());

        if(checkOutModel.isPaid()) {
            holder.stamp.setVisibility(View.VISIBLE);
            holder.status.setText("تسویه شده");
        }
        else{
            holder.status.setText("منتظر تسویه");
            holder.payDate.setText("در حال بررسی");
            holder.balance.setText("در حال محاسبه");
        }


    }

    @Override
    public int getItemCount() {

        if (this.checkOutModelList != null)
            return checkOutModelList.size();
        else
            return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView requestDate,payDate,trackNumber,balance,status;
        public ImageView stamp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            requestDate = itemView.findViewById(R.id.delegate_check_request_date);
            payDate = itemView.findViewById(R.id.delegate_check_pay_date);
            status = itemView.findViewById(R.id.delegate_check_status);
            balance = itemView.findViewById(R.id.delegate_check_balance);
            trackNumber = itemView.findViewById(R.id.delegate_check_track_number);
            stamp = itemView.findViewById(R.id.delegate_check_stamp_img);


        }


    }
}
