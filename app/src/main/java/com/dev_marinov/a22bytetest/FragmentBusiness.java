package com.dev_marinov.a22bytetest;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentBusiness extends Fragment {

    AdapterListBusiness adapterListBusiness;
    RecyclerView rvBusiness;
    SwipeRefreshLayout swipe_container;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    String country = "ru";
    String category = "business";

    ViewGroup viewGroupBusiness;
    LayoutInflater layoutInflater;

    //  АНАЛОГИЧНОЕ ОПИСАНИЕ КОДА В FragmentHome
    ////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("444", "зашел во FragmentBusiness");

        viewGroupBusiness = container;
        this.layoutInflater = inflater;

        return initInterface();
    }

    public View initInterface() {
        View view;
        if(viewGroupBusiness != null) {
           viewGroupBusiness.removeAllViewsInLayout();
            Log.e("444", "viewGroup.removeAllViewsInLayout() во FragmentBusiness");
        }

        // получить экран ориентации
        int orientation = getActivity().getResources().getConfiguration().orientation;
        // раздуть соответствующий макет в зависимости от ориентации экрана
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = layoutInflater.inflate(R.layout.fragment_business, viewGroupBusiness, false);

            mySwipeOnRefreshListener(view); // метод Swipe чтобы обновлять данные вручную
            // метод для установки recyclerview, GridLayoutManager и AdapterListHome
            myRecyclerLayoutManagerAdapter(view, 1, ((MainActivity)getActivity()).lastVisibleItemBusiness);

            Log.e("444", "FragmentBusiness сменил ориентацию на ПОРТРЕТ");
        } else {
            view = layoutInflater.inflate(R.layout.fragment_business, viewGroupBusiness, false);

            mySwipeOnRefreshListener(view); // метод Swipe чтобы обновлять данные вручную
            // метод для установки recyclerview, GridLayoutManager и AdapterListHome
            myRecyclerLayoutManagerAdapter(view, 2, ((MainActivity)getActivity()).lastVisibleItemBusiness);

            Log.e("444", "FragmentBusiness сменил ориентацию на ЛАНШАФТ");
        }

        if (((MainActivity)getActivity()).arrayListBusiness.size() == 0) {
            getDateNews();
        }
        else
        {
            Log.e("444", " FragmentBusiness arrayList.size() НЕ ПУСТОЙ=");
        }

        return view; // в onCreateView() возвращаем объект View, который является корневым элементом разметки фрагмента.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("444", "-зашел FragmentBusiness onConfigurationChanged-");
        // ДО СОЗДАНИЯ НОВОГО МАКЕТА ПИШЕМ ПЕРЕМЕННЫЕ В КОТОРЫЕ СОХРАНЯЕМ ЛЮБЫЕ ДАННЫЕ ИЗ ТЕКУЩИХ VIEW
//        // создать новый макет------------------------------
//        View view = initInterface();
//        // ПОСЛЕ СОЗДАНИЯ НОВОГО МАКЕТА ПЕРЕДАЕМ СОХРАНЕННЫЕ ДАННЫЕ В СТАРЫЕ(ТЕ КОТОРЫЕ ТЕКУЩИЕ) VIEW
//        // отображать новую раскладку на экране
//        viewGroup.addView(view);
        super.onConfigurationChanged(newConfig);
    }

    // передаем в параметт view initInterface() чтобы определить swipe_container в макете
    public void mySwipeOnRefreshListener(View view) {
        // обновление данных при свайпе
        swipe_container = view.findViewById(R.id.swipe_container);
        swipe_container.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // момент оттягивания свайпа с задержкой
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("444", "-зашел mySwipeOnRefreshListener-");
                        swipe_container.setRefreshing(false);    // Отменяем анимацию обновления false true
                        ((MainActivity)getActivity()).arrayListBusiness.clear(); // очищаем массив чтобы в список не копировалась сверху те же данные
                        getDateNews();
                    }
                }, 500); // задержка кпол секунды
            }
        });
    }

    // метод для установки recyclerview, GridLayoutManager и AdapterListHome
    public void myRecyclerLayoutManagerAdapter(View view, int column, int lastVisableItem) {

        rvBusiness = view.findViewById(R.id.rvBusiness);
        rvBusiness.setHasFixedSize(false);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL);
        rvBusiness.setLayoutManager(staggeredGridLayoutManager);

        adapterListBusiness = new AdapterListBusiness(getContext(), ((MainActivity)getActivity()).arrayListBusiness, rvBusiness);
        rvBusiness.setAdapter(adapterListBusiness);
        //adapterListTest.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            staggeredGridLayoutManager.scrollToPositionWithOffset(lastVisableItem, 0);
                        }
                    });
                }
                catch (Exception e)
                {
                    Log.e("444", "-FragmentBusiness try catch 1-" + e);
                }
            }
        }, 500);

    }

    private void getDateNews() {
        Log.e("444", "-зашел getDateNews FragmentBusiness-");

        ApiUtilities.getApiInterface().getCategoryNews(country, category, 100,
                ((MainActivity)getActivity()).API).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()) {
                    //Log.e("444","-responceHome-" + response);
                    try {
                        ((MainActivity)getActivity()).arrayListBusiness.addAll(response.body().getArticles());
                        adapterListBusiness.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        Log.e("444","-try catch FragmentBusiness -" + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("444","-responceHomeonFailure-" + t);
            }
        });

    }

}