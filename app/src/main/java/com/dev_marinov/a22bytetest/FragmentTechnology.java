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

public class FragmentTechnology extends Fragment {
    AdapterListTechnology adapterListTechnology;
    RecyclerView rvTechnology;
    SwipeRefreshLayout swipe_container;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    String country = "ru";
    String category = "technology";

    ViewGroup viewGroupTechnology;
    LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("444", "-зашел FragmentTechnology-");

        viewGroupTechnology = container;
        this.layoutInflater = inflater;

        return initInterface();
    }

    public View initInterface() {
        View view;
        if(viewGroupTechnology != null) {
            viewGroupTechnology.removeAllViewsInLayout();
            Log.e("444", "-viewGroup.removeAllViewsInLayout() FragmentTechnology-");
        }

        // получить экран ориентации
        int orientation = getActivity().getResources().getConfiguration().orientation;
        // раздуть соответствующий макет в зависимости от ориентации экрана
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = layoutInflater.inflate(R.layout.fragment_technology, viewGroupTechnology, false);

            mySwipeOnRefreshListener(view); // метод Swipe чтобы обновлять данные вручную
            // метод для установки recyclerview, GridLayoutManager и AdapterListHome
            myRecyclerLayoutManagerAdapter(view, 1, ((MainActivity)getActivity()).lastVisibleItemTechnology);

            Log.e("444", "-FragmentTechnology ПОРТРЕТ-");
        } else {
            view = layoutInflater.inflate(R.layout.fragment_technology, viewGroupTechnology, false);

            mySwipeOnRefreshListener(view); // метод Swipe чтобы обновлять данные вручную
            // метод для установки recyclerview, GridLayoutManager и AdapterListHome
            myRecyclerLayoutManagerAdapter(view, 2, ((MainActivity)getActivity()).lastVisibleItemTechnology);

            Log.e("444", "-FragmentTechnology ЛАНШАФТ-");
        }

        if (((MainActivity)getActivity()).arrayListTechnology.size() == 0) {
            getDateNews();
        }
        else
        {
            Log.e("444", "FragmentTechnology arrayList.size()  НЕ ПУСТОЙ=");
        }

        return view; // в onCreateView() возвращаем объект View, который является корневым элементом разметки фрагмента.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("444", "-зашел FragmentTechnology onConfigurationChanged-");
        // ДО СОЗДАНИЯ НОВОГО МАКЕТА ПИШЕМ ПЕРЕМЕННЫЕ В КОТОРЫЕ СОХРАНЯЕМ ЛЮБЫЕ ДАННЫЕ ИЗ ТЕКУЩИХ VIEW

//        // создать новый макет------------------------------
//        View view = initInterface();
//        // ПОСЛЕ СОЗДАНИЯ НОВОГО МАКЕТА ПЕРЕДАЕМ СОХРАНЕННЫЕ ДАННЫЕ В СТАРЫЕ(ТЕ КОТОРЫЕ ТЕКУЩИЕ) VIEW
//
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
                        ((MainActivity)getActivity()).arrayListTechnology.clear(); // очищаем массив чтобы в список не копировалась сверху те же данные
                        getDateNews();
                    }
                }, 500); // задержка кпол секунды
            }
        });
    }

    // метод для установки recyclerview, GridLayoutManager и AdapterListHome
    public void myRecyclerLayoutManagerAdapter(View view, int column, int lastVisableItem) {

        rvTechnology = view.findViewById(R.id.rvTechnology);
        rvTechnology.setHasFixedSize(false);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL);
        rvTechnology.setLayoutManager(staggeredGridLayoutManager);

        adapterListTechnology = new AdapterListTechnology(getContext(),
                ((MainActivity)getActivity()).arrayListTechnology, rvTechnology);
        rvTechnology.setAdapter(adapterListTechnology);

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
                    Log.e("444","-try catch FragmentTechnology 1 -" + e);
                }

            }
        }, 500);

    }

    private void getDateNews() {
        Log.e("444", "-зашел FragmentTechnology getDateNews-");

        ApiUtilities.getApiInterface().getCategoryNews(country, category, 100,
                ((MainActivity)getActivity()).API).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()) {
                     Log.e("444","-responceTechnology-" + response);
                    try {
                        ((MainActivity)getActivity()).arrayListTechnology.addAll(response.body().getArticles());
                        adapterListTechnology.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        Log.e("444","-try catch FragmentTechnology respone-" + e);
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