package com.atisapp.jazbeh.MainFragment.ui.free;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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

public class FreeFragment extends Fragment {

    private static final String TAG = "FreeFragment";
    private Context context;
    private RecyclerView freeRecyclerView;
    private CourseAPIService courseAPIService;
    private EpisodeAPIService episodeAPIService;
    private List<MultiViewModel> totalMultiViewModelList;
    private MultiViewAdapter multiViewAdapter;
    private Toolbar freeToolbar;
    private View freeRoot;
    private int courseSize;
    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        freeRoot = inflater.inflate(R.layout.activity_free_products, container, false);
        SetupViews();
        return freeRoot;
    }

    private void SetupViews()
    {
        context = getContext();
        freeRecyclerView = (RecyclerView) freeRoot.findViewById(R.id.free_activity_recyclerView);
        episodeAPIService = new EpisodeAPIService(context);
        courseAPIService = new CourseAPIService(context);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        freeRecyclerView.setLayoutManager(layoutManager);

        if(isOnline()) {
            GetFreeCourses();

        }
    }


    private void GetFreeCourses()
    {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("در حال دریافت اطلاعات محصولات");
            progressDialog.show();
        }

        totalMultiViewModelList = new ArrayList<>();
        courseAPIService.GetFreeCourses(new CourseAPIService.onGetCourses() {
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
//                    freeRecyclerView.setAdapter(multiViewAdapter);
//                    multiViewAdapter.notifyDataSetChanged();
                }
                GetFreeEpisodes();

            }
        });
    }

    private void GetFreeEpisodes()
    {
        if(courseSize>0 && totalMultiViewModelList.isEmpty())
        {
            GetFreeCourses();
        }
        episodeAPIService.GetFreeEpisodes(new EpisodeAPIService.onGetEpisodes() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_episodes) {

                Log.i(TAG, "onGet: "+list_episodes.size());
                if(msg && list_episodes.size()>0)
                {
                    if(totalMultiViewModelList.size()<=2) {
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "محصولات"));
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.EPISODE_LIST_LAYOUT, list_episodes,true));
                    }
                    Log.i(TAG, "onGet: product received"+totalMultiViewModelList.size());
                }

                multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList,context);
                freeRecyclerView.setAdapter(multiViewAdapter);
                multiViewAdapter.notifyDataSetChanged();
                if (progressDialog != null)progressDialog.dismiss();

            }
        });
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
