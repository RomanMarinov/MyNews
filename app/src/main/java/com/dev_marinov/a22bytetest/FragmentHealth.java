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

public class FragmentHealth extends Fragment {

    AdapterListHealth adapterListHealth;

    RecyclerView rvHealth;
    //AdapterListHealthFake adapterListHealthFake;
    SwipeRefreshLayout swipe_container;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    String country = "ru";
    String category = "health";

    ViewGroup viewGroupHealth;
    LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e("444", "зашел во FragmentHealth");

        Log.e("444","FragmentHealth getSupportFragmentManager().getBackStackEntryCount()="
                + getActivity().getSupportFragmentManager().getBackStackEntryCount());
        viewGroupHealth = container;
        this.layoutInflater = inflater;

        return initInterface();
    }

    public View initInterface() {
        View view;
        if(viewGroupHealth != null) {
            viewGroupHealth.removeAllViewsInLayout();
            Log.e("444", "viewGroup.removeAllViewsInLayout() FragmentHealth");
        }

        // получить экран ориентации
        int orientation = getActivity().getResources().getConfiguration().orientation;
        // раздуть соответствующий макет в зависимости от ориентации экрана
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = layoutInflater.inflate(R.layout.fragment_health, viewGroupHealth, false);

            mySwipeOnRefreshListener(view); // метод Swipe чтобы обновлять данные вручную
            // метод для установки recyclerview, GridLayoutManager и AdapterListHome
            myRecyclerLayoutManagerAdapter(view, 1, ((MainActivity)getActivity()).lastVisibleItemHealth);

            Log.e("444", "FragmentHealth ПОРТРЕТ");
        } else {
            view = layoutInflater.inflate(R.layout.fragment_health, viewGroupHealth, false);

            mySwipeOnRefreshListener(view); // метод Swipe чтобы обновлять данные вручную
            // метод для установки recyclerview, GridLayoutManager и AdapterListHome
            myRecyclerLayoutManagerAdapter(view, 2, ((MainActivity)getActivity()).lastVisibleItemHealth);

            Log.e("444", "FragmentHealth ЛАНШАФТ");
        }

        if (((MainActivity)getActivity()).arrayListHealth.size() == 0) {
            getDateNews();
        }
        else
        {
            Log.e("444", "FragmentHealth arrayList.size()  НЕ ПУСТОЙ=");
        }

        return view; // в onCreateView() возвращаем объект View, который является корневым элементом разметки фрагмента.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("444", "-зашел FragmentHealth onConfigurationChanged-");
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
                        ((MainActivity)getActivity()).arrayListHealth.clear(); // очищаем массив чтобы в список не копировалась сверху те же данные
                        getDateNews();
                    }
                }, 500); // задержка кпол секунды
            }
        });
    }

    // метод для установки recyclerview, GridLayoutManager и AdapterListHome
    public void myRecyclerLayoutManagerAdapter(View view, int column, int lastVisableItem) {

        rvHealth = view.findViewById(R.id.rvHealth);
        rvHealth.setHasFixedSize(false);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL);
        rvHealth.setLayoutManager(staggeredGridLayoutManager);

        Log.e("444"," arrayListHealth.size -" + ((MainActivity)getActivity()).arrayListHealth.size());
        adapterListHealth = new AdapterListHealth(getContext(),
                ((MainActivity)getActivity()).arrayListHealth, rvHealth);
        rvHealth.setAdapter(adapterListHealth);


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
                    Log.e("444","-try catch FragmentHealth 1 -" + e);
                }


            }
        }, 1000);

    }

//    private void getDateNews() {
//        Log.e("444", "-зашел FragmentHealth getDateNews-");
//
//        for (int i = 0; i < 2; i++) {
//            ((MainActivity)getActivity()).arrayListHealth.add(i + " здоровье");
//        }
//        adapterListHealthFake.notifyDataSetChanged();
//
//    }


    private void getDateNews() {
        Log.e("444", "-зашел FragmentHealth getDateNews-");

        ApiUtilities.getApiInterface().getCategoryNews(country, category, 100,
                ((MainActivity)getActivity()).API).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()) {
                    //Log.e("444","-responceHealth-" + response);
                    try {
                        ((MainActivity)getActivity()).arrayListHealth.addAll(response.body().getArticles());
                        adapterListHealth.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        Log.e("444","-try catch FragmentHealth -" + e);
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("444","-responceHealthonFailure-" + t);
            }
        });

    }

}