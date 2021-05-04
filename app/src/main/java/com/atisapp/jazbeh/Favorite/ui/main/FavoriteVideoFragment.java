package com.atisapp.jazbeh.Favorite.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atisapp.jazbeh.Courses.CourseAPIService;
import com.atisapp.jazbeh.Episodes.EpisodeAPIService;
import com.atisapp.jazbeh.Favorite.FavoriteAPIService;
import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.VideoPlayer.VideoMultiViewAdapter;
import com.atisapp.jazbeh.VideoPlayer.VideoMultiViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteVideoFragment extends Fragment {

    private static final String TAG = "FavoriteVideoFragment";
    private Context context;
    private EpisodeAPIService episodeAPIService;
    private CourseAPIService courseAPIService;
    private List<VideoMultiViewModel> totalMultiViewModelList;
    private VideoMultiViewAdapter videoMultiViewAdapter;
    private FavoriteAPIService favoriteAPIService;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayout emptyView;
    private String categoryId;
    private int courseSize;
    private View videoRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        videoRoot = inflater.inflate(R.layout.fragment_favorite_video, container, false);
        SetupViews();

        return videoRoot;
    }

    private void SetupViews()
    {
        context = getContext();
        recyclerView = (RecyclerView) videoRoot.findViewById(R.id.video_favorite_fragment_recyclerView);
        favoriteAPIService = new FavoriteAPIService(context);
        emptyView = videoRoot.findViewById(R.id.video_favorite_empty_list_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if(isOnline()) {
            GetFavoriteCourses();
        }
    }

    private void GetFavoriteCourses()
    {
        totalMultiViewModelList = new ArrayList<>();
        favoriteAPIService.GetFavoriteCourses("video",new FavoriteAPIService.onGetCourses() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_courses) {
                Log.i(TAG, "onGet course size: "+list_courses.size());
                courseSize = list_courses.size();
                if(msg && list_courses.size()>0)
                {
                    if(totalMultiViewModelList.isEmpty()) {
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_HEADER_LAYOUT, "دوره ها"));
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_LIST_LAYOUT, list_courses,true));
                    }
                    Log.i(TAG, "onGet: course received"+totalMultiViewModelList.size());
                    Log.i(TAG, "onGet: course received"+totalMultiViewModelList.size());
//                    multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList,context);
//                    shopRecyclerView.setAdapter(multiViewAdapter);
//                    multiViewAdapter.notifyDataSetChanged();
                }
                GetFavoriteEpisodes();
            }
        });
    }


    private void GetFavoriteEpisodes()
    {

        if(courseSize>0 && totalMultiViewModelList.isEmpty())
        {
            GetFavoriteCourses();
        }

        favoriteAPIService.GetFavoriteEpisodes("video",new FavoriteAPIService.onGetEpisodes() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_episodes) {
                Log.i(TAG, "onGet: "+list_episodes.size());
                if(msg && list_episodes.size()>0)
                {
                    if(courseSize > 0 && totalMultiViewModelList.size()<=2) {
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_HEADER_LAYOUT, "تک ویدئو ها"));
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_EPISODE_LIST_LAYOUT, list_episodes,true));
                    }else if(courseSize == 0 && totalMultiViewModelList.size()==0)
                    {
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_HEADER_LAYOUT, "تک ویدئو ها"));
                        totalMultiViewModelList.add(new VideoMultiViewModel(VideoMultiViewModel.VIDEO_EPISODE_LIST_LAYOUT, list_episodes,true));
                    }
                    Log.i(TAG, "onGet: product received"+totalMultiViewModelList.size());
                }


                if(totalMultiViewModelList.size()>0) {
                    emptyView.setVisibility(View.GONE);
                    videoMultiViewAdapter = new VideoMultiViewAdapter(totalMultiViewModelList,context);
                    recyclerView.setAdapter(videoMultiViewAdapter);
                    videoMultiViewAdapter.notifyDataSetChanged();
                    if (progressDialog != null) progressDialog.hide();
                }
                else {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        });
        if(progressDialog != null)
            progressDialog.dismiss();
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
