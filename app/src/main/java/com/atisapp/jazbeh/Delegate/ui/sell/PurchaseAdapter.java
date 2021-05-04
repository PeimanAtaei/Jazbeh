package com.atisapp.jazbeh.Delegate.ui.sell;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.atisapp.jazbeh.Delegate.DelegateAPIService;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.ProductPrefs;
import java.util.List;

public class PurchaseAdapter  extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {

    private static final String TAG = "PurchaseAdapter";
    private List<PurchasesModel> purchaseList;
    private PurchasesModel purchasesModel;
    private Context ctx;

    public PurchaseAdapter(Context context,List<PurchasesModel> purchaseList) {
        this.ctx = context;
        this.purchaseList = purchaseList;
    }

    public void updateAdapterData(List<PurchasesModel> data) {
        this.purchaseList = data;
    }

    @NonNull
    @Override
    public PurchaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.eachitem_delegate_purchase,parent,false);
        PurchaseAdapter.ViewHolder viewHolder = new PurchaseAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PurchaseAdapter.ViewHolder holder, final int position) {
        purchasesModel = purchaseList.get(position);

        holder.productName.setText(purchasesModel.getName());
        holder.balance.setText(purchasesModel.getBalance()+" تومان");
        holder.type.setText(purchasesModel.getType());
        holder.trackNumber.setText(purchasesModel.getTrackNumber());
        holder.price.setText(purchasesModel.getPrice()+" تومان");
        holder.time.setText(purchasesModel.getDate());

        if(purchasesModel.isUsed())
            holder.productUsed.setVisibility(View.GONE);

        if(purchasesModel.isPaid())
            holder.paid.setText("پرداخت شده");
        else
            holder.paid.setText("پرداخت نشده");

    }

    @Override
    public int getItemCount() {

        if (this.purchaseList != null)
            return purchaseList.size();
        else
            return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView productName,productUsed,trackNumber,type,time,price,balance,paid;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.delegate_purchase_date);
            type = itemView.findViewById(R.id.delegate_purchase_product_type);
            price = itemView.findViewById(R.id.delegate_purchase_price);
            balance = itemView.findViewById(R.id.delegate_purchase_balance);
            trackNumber = itemView.findViewById(R.id.delegate_purchase_track_number);
            productUsed = itemView.findViewById(R.id.delegate_purchase_used);
            productName = itemView.findViewById(R.id.delegate_purchase_product_name);
            paid = itemView.findViewById(R.id.delegate_purchase_paid);


        }


    }
}
