package com.atisapp.jazbeh.MainFragment.ui.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.atisapp.jazbeh.Courses.CourseListAdapter;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;


import java.util.List;

import static com.atisapp.jazbeh.MainFragment.ui.home.MultiDashboardModel.CATEGORY_LAYOUT;
import static com.atisapp.jazbeh.MainFragment.ui.home.MultiDashboardModel.HEADER_LAYOUT;
import static com.atisapp.jazbeh.MainFragment.ui.home.MultiDashboardModel.POPULAR_LAYOUT;
import static com.atisapp.jazbeh.MainFragment.ui.home.MultiDashboardModel.SLIDER_LAYOUT;

public class MultiDashboardAdapter extends RecyclerView.Adapter {

    private static final String TAG = "MultiDashboardAdapter";
    private List<MultiDashboardModel> multiViewModelList;
    private int lastPosition = -1;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private CourseListAdapter productAdapter;
    private DashboardAdapter dashboardAdapter;
    private PopularAdapter popularAdapter;

    public MultiDashboardAdapter(List<MultiDashboardModel> multiViewModelList) {
        this.multiViewModelList = multiViewModelList;
    }

    @Override
    public int getItemViewType(int position)
    {
        switch (multiViewModelList.get(position).getViewType())
        {
            case 0:{
                return SLIDER_LAYOUT;
            }
            case 1:{
                return POPULAR_LAYOUT;
            }
            case 2:{
                return HEADER_LAYOUT;
            }
            case 3:{
                return CATEGORY_LAYOUT;
            }
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType)
        {
            case SLIDER_LAYOUT: {
                Log.i(TAG, "onCreateViewHolder: SLIDER_LAYOUT");
                View userInfoLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_dashboard_slider, parent, false);
                return new SliderLayout(userInfoLayout);
            }
            case POPULAR_LAYOUT:
            {
                Log.i(TAG, "onCreateViewHolder: POPULAR_LAYOUT");
                View adLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.multiview_subrecycler_view_layout, parent, false);
                return new PopularListLayout(adLayout);
            }

            case HEADER_LAYOUT:
            {
                View adLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.multiview_header_layout, parent, false);
                return new HeaderLayout(adLayout);
            }

            case CATEGORY_LAYOUT:
            {
                View headerLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.multiview_subrecycler_view_layout, parent, false);
                return new CategoryListLayout(headerLayout);
            }



            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (multiViewModelList.get(position).getViewType())
        {
            case SLIDER_LAYOUT: {

                String title = multiViewModelList.get(position).getHeaderTitle();
                //((SliderLayout)holder).setData(title);
                break;

            }
            case POPULAR_LAYOUT:
            {
                List<ProductModel> productModelList = multiViewModelList.get(position).getPopularModelList();
                ((PopularListLayout)holder).setData(productModelList);
                break;
            }

            case HEADER_LAYOUT:
            {
                String title = multiViewModelList.get(position).getHeaderTitle();
                ((HeaderLayout)holder).setData(title);
                break;
            }

            case CATEGORY_LAYOUT:
            {

                List<ProductModel> productModelList = multiViewModelList.get(position).getPopularModelList();
                ((CategoryListLayout)holder).setData(productModelList);
                break;
            }
        }

        if (lastPosition < position)
        {
            Animation animation = AnimationUtils.loadAnimation
                    (holder.itemView.getContext(),android.R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return multiViewModelList.size();
    }



    class SliderLayout extends RecyclerView.ViewHolder
    {

        public SliderLayout(@NonNull View itemView) {
            super(itemView);
        }

        private void setData(List<ProductModel> productModelList)
        {
        }
    }

    class PopularListLayout extends RecyclerView.ViewHolder
    {
        private RecyclerView subRecyclerView;

        public PopularListLayout(@NonNull View itemView) {
            super(itemView);
            subRecyclerView = itemView.findViewById(R.id.dashboard_popular_recyclerView);
            Log.i(TAG, "ProductListLayout: "+itemView.getContext());

        }

        private void setData(List<ProductModel> productModelList)
        {
            Log.i(TAG, "setData: "+productModelList.size());

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    itemView.getContext(),LinearLayoutManager.HORIZONTAL,false
            );
            layoutManager.setInitialPrefetchItemCount(productModelList.size());
            popularAdapter = new PopularAdapter(itemView.getContext(),productModelList,1);
            subRecyclerView.setLayoutManager(layoutManager);
            subRecyclerView.setAdapter(popularAdapter);
            subRecyclerView.setRecycledViewPool(viewPool);

        }
    }

    class HeaderLayout extends RecyclerView.ViewHolder
    {

        private TextView title;
        private TextView  body;

        public HeaderLayout(@NonNull View itemView) {
            super(itemView);

            Log.i(TAG, "UserInfoLayout: UserInfoLayout");
            title = itemView.findViewById(R.id.multiView_header_text);

        }

        private void setData(String title)
        {
            this.title.setText(title);
        }
    }

    class CategoryListLayout extends RecyclerView.ViewHolder
    {
        private RecyclerView subRecyclerView;

        public CategoryListLayout(@NonNull View itemView) {
            super(itemView);
            subRecyclerView = itemView.findViewById(R.id.product_list_subRecyclerView);
            Log.i(TAG, "ProductListLayout: "+itemView.getContext());

        }

        private void setData(List<ProductModel> productModelList)
        {
            Log.i(TAG, "setData: "+productModelList.size());

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    itemView.getContext(),LinearLayoutManager.VERTICAL,false
            );
            layoutManager.setInitialPrefetchItemCount(productModelList.size());
            dashboardAdapter = new DashboardAdapter(itemView.getContext(),productModelList,3);
            subRecyclerView.setLayoutManager(layoutManager);
            subRecyclerView.setAdapter(popularAdapter);
            subRecyclerView.setRecycledViewPool(viewPool);

        }
    }

}
