package com.atisapp.jazbeh.Favorite.ui.main;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atisapp.jazbeh.Favorite.FavoriteAPIService;
import com.atisapp.jazbeh.Favorite.FavoriteActivity;
import com.atisapp.jazbeh.ProductList.MultiViewAdapter;
import com.atisapp.jazbeh.ProductList.MultiViewModel;
import com.atisapp.jazbeh.ProductList.ProductModel;
import com.atisapp.jazbeh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavoriteMusicFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "FavoriteMusicFragment";
    private Context context;
    private View favoriteRoot;
    private RecyclerView favoriteRecyclerView;
    private List<MultiViewModel> totalMultiViewModelList;
    private MultiViewAdapter multiViewAdapter;
    private FavoriteAPIService favoriteAPIService;
    private int courseSize= 0;
    private Toolbar favoriteToolbar;
    private LinearLayout emptyView;
    private PageViewModel pageViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        favoriteRoot = inflater.inflate(R.layout.fragment_favorite_main, container, false);
        SetupViews();
        return favoriteRoot;
    }

    private void SetupViews()
    {
        context = getContext();
        favoriteRecyclerView = (RecyclerView) favoriteRoot.findViewById(R.id.music_fragment_recyclerView);
        favoriteAPIService = new FavoriteAPIService(context);
        emptyView = favoriteRoot.findViewById(R.id.music_empty_list_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        favoriteRecyclerView.setLayoutManager(layoutManager);

        if(isOnline()) {
            GetFavoriteCourses();
            //GetFavoriteEpisodes();
        }
    }

    private void GetFavoriteCourses()
    {
        totalMultiViewModelList = new ArrayList<>();
        favoriteAPIService.GetFavoriteCourses("podcast",new FavoriteAPIService.onGetCourses() {
            @Override
            public void onGet(boolean msg, List<ProductModel> list_courses) {
                Log.i(TAG, "onGet course size: "+list_courses.size());
                courseSize = list_courses.size();
                if(msg && list_courses.size()>0)
                {
                    if(totalMultiViewModelList.isEmpty()) {
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.HEADER_LAYOUT, "دوره ها"));
                        totalMultiViewModelList.add(new MultiViewModel(MultiViewModel.COURSE_LIST_LAYOUT, list_courses,true));
                    }
                    Log.i(TAG, "onGet: course received"+totalMultiViewModelList.size());
                    multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList,context);
                    favoriteRecyclerView.setAdapter(multiViewAdapter);
                    multiViewAdapter.notifyDataSetChanged();
                }
                if(list_courses.size()>0 && totalMultiViewModelList.isEmpty())
                {
                    GetFavoriteCourses();
                }

                GetFavoriteEpisodes();
            }
        });
    }


    private void GetFavoriteEpisodes()
    {
        favoriteAPIService.GetFavoriteEpisodes("podcast",new FavoriteAPIService.onGetEpisodes() {
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
                    else
                        Log.i(TAG, "onGet: product received"+totalMultiViewModelList.size());
                }

                Log.i(TAG, "onGet: "+totalMultiViewModelList.size());
                if (totalMultiViewModelList.size()>0) {
                    emptyView.setVisibility(View.GONE);
                    multiViewAdapter = new MultiViewAdapter(totalMultiViewModelList, context);
                    favoriteRecyclerView.setAdapter(multiViewAdapter);
                    multiViewAdapter.notifyDataSetChanged();
                }
                else {
                    emptyView.setVisibility(View.VISIBLE);
                }
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