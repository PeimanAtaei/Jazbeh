package com.atisapp.jazbeh.Delegate.ui.sell;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atisapp.jazbeh.Delegate.DelegateAPIService;
import com.atisapp.jazbeh.R;

import java.util.List;

public class SellFragment extends Fragment {

    private static final String TAG = "SellFragment";
    private Context context;
    private View purchaseRoot;
    private DelegateAPIService delegateAPIService;
    private PurchaseAdapter purchaseAdapter;
    private RecyclerView purchaseRecyclerView;
    private ProgressDialog progressDialog;
    private LinearLayout emptyView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        purchaseRoot = inflater.inflate(R.layout.fragment_sell, container, false);
        SetupViews();
        return purchaseRoot;
    }

    private void SetupViews()
    {
        context = getContext();
        delegateAPIService = new DelegateAPIService(context);
        purchaseRecyclerView = purchaseRoot.findViewById(R.id.sell_recyclerView);
        emptyView = purchaseRoot.findViewById(R.id.sell_empty_list_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        purchaseRecyclerView.setLayoutManager(layoutManager);

        GetPurchases();

    }
    private void GetPurchases()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت لیست فروش");
            progressDialog.show();
        }

        delegateAPIService.GetPurchase(new DelegateAPIService.onGetParchases() {
            @Override
            public void onGet(List<PurchasesModel> purchasesModelList) {
                if(purchasesModelList!=null || !purchasesModelList.isEmpty())
                {
                    purchaseAdapter = new PurchaseAdapter(context,purchasesModelList);
                    purchaseRecyclerView.setAdapter(purchaseAdapter);
                    purchaseAdapter.notifyDataSetChanged();
                }

                else
                {
                    emptyView.setVisibility(View.VISIBLE);
                }

                if(progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }
}
