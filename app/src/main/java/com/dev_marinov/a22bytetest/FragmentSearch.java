package com.dev_marinov.a22bytetest;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.TransitionManager;

import android.os.Handler;
import android.print.PrintAttributes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSearch extends Fragment {

   // GET https://newsapi.org/v2/everything?q=bitcoin&apiKey=f725144c0220437d87363920fe7b20ba

   View frag;
   TextInputLayout textInputLayout;
   TextInputEditText textInputEditText;
   ImageView imgFragmentSearch;
   RecyclerView rvSearch;
   AdapterListSearch adapterListSearch;
   ArrayList<ModelClass> arrayListArticles;

   ConstraintLayout constraintLayout;
   LinearLayout llTitle;

   StaggeredGridLayoutManager staggeredGridLayoutManager;

   ViewGroup viewGroupSearch;
   LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("4444","-open FragmentSearch-");
        Log.e("4444","-open FragmentSearch getSupportFragmentManager().getBackStackEntryCount()-" + getActivity().getSupportFragmentManager().getBackStackEntryCount());



        this.viewGroupSearch = container;
        this.layoutInflater = inflater;

        return initInterface();
    }

    public View initInterface()
    {
        View view; // view глобальный чтобы в
        if(viewGroupSearch != null) {
            viewGroupSearch.removeAllViewsInLayout();
            Log.e("4444", "viewGroup.removeAllViewsInLayout() во FragmentSearch");
        }

        // получить экран ориентации
        int orientation = getActivity().getResources().getConfiguration().orientation;
        // раздуть соответствующий макет в зависимости от ориентации экрана
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = layoutInflater.inflate(R.layout.fragment_search, viewGroupSearch, false);

            // метод для установки recyclerview, GridLayoutManager и AdapterListHome
            myRecyclerLayoutManagerAdapter(view, 1, ((MainActivity)getActivity()).lastVisibleItemSearch);
            Log.e("4444", "верх во FragmentSearch ПОРТРЕТ");
        } else {
            view = layoutInflater.inflate(R.layout.fragment_search, viewGroupSearch, false);

            // метод для установки recyclerview, GridLayoutManager и AdapterListHome
            myRecyclerLayoutManagerAdapter(view, 2, ((MainActivity)getActivity()).lastVisibleItemSearch);
            Log.e("4444", "верх во FragmentSearch ЛАНШАФТ");
        }

        imgFragmentSearch = view.findViewById(R.id.imgFragmentSearch);
        textInputEditText = view.findViewById(R.id.textInputEditText);
        constraintLayout = view.findViewById(R.id.clFragSearch);
        llTitle = view.findViewById(R.id.llTitle);

        if(((MainActivity)getActivity()).arrayListSearch.size() != 0)
        {
            layoutChange();
        }

        imgFragmentSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutChange();
                String stringSearch = textInputEditText.getText().toString();
                String q = "";
                for (int i = 0; i < stringSearch.length(); i++) {
                    if (stringSearch.charAt(i) == ' ' && stringSearch.charAt(i + 1) == ' ') {

                    } else {
                        if (stringSearch.charAt(i) == ' ') {
                            q = q + (String.valueOf(stringSearch.charAt(i)).toString().replace(' ', '+'));
                        } else {
                            q = q + stringSearch.charAt(i);
                        }
                    }
                    Log.e("333","-q-" + q);
                }
                getDataSearchNews(q); // текст, который введен в edittext
            }
        });

        return view;
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("4444", "-зашел FragmentSearch onConfigurationChanged-");
        // ДО СОЗДАНИЯ НОВОГО МАКЕТА ПИШЕМ ПЕРЕМЕННЫЕ В КОТОРЫЕ СОХРАНЯЕМ ЛЮБЫЕ ДАННЫЕ ИЗ ТЕКУЩИХ VIEW
        String searchText = textInputEditText.getText().toString();
//        // создать новый макет------------------------------
        View view = initInterface();
        // ПОСЛЕ СОЗДАНИЯ НОВОГО МАКЕТА ПЕРЕДАЕМ СОХРАНЕННЫЕ ДАННЫЕ В СТАРЫЕ(ТЕ КОТОРЫЕ ТЕКУЩИЕ) VIEW
        textInputEditText.setText(searchText);
        // отображать новую раскладку на экране
        viewGroupSearch.addView(view);
        super.onConfigurationChanged(newConfig);
        Log.e("4444","-open onConfigurationChanged FragmentSearch getSupportFragmentManager().getBackStackEntryCount()-" + getActivity().getSupportFragmentManager().getBackStackEntryCount());



    }

    // метод для установки recyclerview, GridLayoutManager и AdapterListHome
    public void myRecyclerLayoutManagerAdapter(View view, int column, int lastVisableItem) {

        rvSearch = view.findViewById(R.id.rvSearch);


        rvSearch.setHasFixedSize(false);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(staggeredGridLayoutManager);

        adapterListSearch = new AdapterListSearch(getContext(), ((MainActivity)getActivity()).arrayListSearch, rvSearch);
        rvSearch.setAdapter(adapterListSearch);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("4444", "Handler во FragmentSearch lastVisableItem=" + lastVisableItem);
                            staggeredGridLayoutManager.scrollToPositionWithOffset(lastVisableItem, 0);
                        }
                    });
                }
                catch (Exception e)
                {
                    Log.e("444","-try catch FragmentSearch 1 -" + e);
                }
            }
        }, 500);

    }

    public void getDataSearchNews(String q)
    {
        ApiUtilities.getApiInterface().getSearchNews(q, ((MainActivity)getActivity()).API).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                Log.e("log","-onResponse-response-" + response);
                if(response.isSuccessful())
                {
                    try {
                        ((MainActivity)getActivity()).arrayListSearch.addAll(response.body().getArticles());
                        adapterListSearch.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        Log.e("log","-try catch frag search-" + e);
                    }
                }
            }
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("log","-onFailure-" + t);
            }
        });
    }

    public void layoutChange()
    {
        ConstraintSet constraintSet = new ConstraintSet();
        // copy constraints settings from current ConstraintLayout to set
        constraintSet.clone(constraintLayout);
        // change constraints settings
        changeConstraints(constraintSet);
        // enable animation
        TransitionManager.beginDelayedTransition(constraintLayout);
        // apply constraints settings from set to current ConstraintLayout
        constraintSet.applyTo(constraintLayout);
    }

    private void changeConstraints(ConstraintSet set) {
        set.clear(R.id.textInputLayout, ConstraintSet.BOTTOM);
        set.clear(R.id.imgFragmentSearch, ConstraintSet.BOTTOM);

        set.connect(R.id.textInputLayout, ConstraintSet.TOP, R.id.llTitle, ConstraintSet.BOTTOM);
        set.connect(R.id.imgFragmentSearch, ConstraintSet.TOP, R.id.llTitle, ConstraintSet.BOTTOM, 10);

    }

}