package com.atisapp.jazbeh.MainFragment.ui.Shop.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MusicFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private int Index;
    private PageViewModel pageViewModel;

    private static final String TAG = "PlaceholderFragment";
    private Context context;
    private RecyclerView shopRecyclerView;
    private CourseAPIService courseAPIService;
    private EpisodeAPIService episodeAPIService;
    private List<MultiViewModel> totalMultiViewModelList;
    private MultiViewAdapter multiViewAdapter;
    private int courseSize,episodeSize;
    private Toolbar shopToolbar;
    private View shopRoot;
    private ProgressDialog progressDialog;
    private LinearLayout emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        shopRoot = inflater.inflate(R.layout.fragment_shop_main, container, false);
        SetupViews();

        if(isOnline()) {
            GetMyCourses();
        }

        return shopRoot;
    }


    private void SetupViews()
    {
        context = getContext();
        shopRecyclerView = (RecyclerView) shopRoot.findViewById(R.id.music_fragment_recyclerView);
        episodeAPIService = new EpisodeAPIService(context);
        courseAPIService = new CourseAPIService(context);
        emptyView = shopRoot.findViewById(R.id.music_empty_list_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shopRecyclerView.setLayoutManager(layoutManager);
    }


    private void GetMyCourses()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
            progressDialog.show();
        }

        totalMultiViewModelList = new ArrayList<>();
        courseAPIService.GetMyCourses("podcast",new CourseAPIService.onGetCourses() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_courses) {

                Log.i(TAG, "onGet: "+list_courses.size());
                courseSize = list_courses.size();

                if(msg && list_courses.size()>0)
                {
                    if(totalMultiViewModelList.isEmpty()) {
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "دوره ها"));
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.COURSE_LIST_LAYOUT, list_courses,true));
                    }
                    Log.i(TAG, "onGet: course received"+totalMultiViewModelList.size());
//                    multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList,context);
//                    shopRecyclerView.setAdapter(multiViewAdapter);
//                    multiViewAdapter.notifyDataSetChanged();
                }
                GetMyEpisodes();
            }
        });


    }

    private void GetMyEpisodes()
    {
        if(courseSize>0 && totalMultiViewModelList.isEmpty())
        {
            GetMyCourses();
        }
        episodeAPIService.GetMyEpisodes("podcast",new EpisodeAPIService.onGetEpisodes() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_episodes) {

                Log.i(TAG, "onGet: "+list_episodes.size());
                if(msg && list_episodes.size()>0)
                {
                    if(courseSize > 0 && totalMultiViewModelList.size()<=2) {
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "محصولات"));
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.EPISODE_LIST_LAYOUT, list_episodes,true));
                    }
                    else if(courseSize == 0 && totalMultiViewModelList.size()==0)
                    {
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "محصولات"));
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.EPISODE_LIST_LAYOUT, list_episodes,true));
                    }
                    Log.i(TAG, "onGet: product received"+totalMultiViewModelList.size());
                }

                if(totalMultiViewModelList.size()>0) {
                    emptyView.setVisibility(View.GONE);
                    multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList, context);
                    shopRecyclerView.setAdapter(multiViewAdapter);
                    multiViewAdapter.notifyDataSetChanged();

                }
                else {
                    emptyView.setVisibility(View.VISIBLE);
                }

            }
        });
        if (progressDialog != null) progressDialog.dismiss();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        else
        {

            Toast.makeText(context,"اتصال به اینترنت خود را بررسی نمایید",Toast.LENGTH_LONG).show();

        }
        return false;
    }
}