package com.dev_marinov.a22bytetest;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONException;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    AdapterListHome adapterListHome;
    RecyclerView rvHome;
    SwipeRefreshLayout swipe_container;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    String country = "ru";

    // для сохранения данных при смене оринтации экрана
    ViewGroup viewGroupHome; // разметка, которая позволяет расположить один или несколько View.
    // layoutInflater класс, используемый для преобразования XML-файла макета в объекты представления динамическим способом
    LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("444", "зашел во FragmentHome");
        Log.e("444","FragmentHome getSupportFragmentManager().getBackStackEntryCount()="
                + getActivity().getSupportFragmentManager().getBackStackEntryCount());
        // установить контейнер viewGroup и обработчик инфлятора
        //this.viewGroup = container;
        this.viewGroupHome = container;
        this.layoutInflater = inflater;

        // отображать желаемую разметку и возвращать view в initInterface .
        // onCreateView() возвращает объект View, который является корневым элементом разметки фрагмента.
        return initInterface();
    }

    // https://stackoverflow.com/questions/54266160/changing-a-recyclerviews-layout-upon-orientation-change-only-works-on-the-first
    public View initInterface() { // удалить android:configChanges из манифеста для применения данной стратегии
        View view;
        // если уже есть надутый макет, удалить его.
        if (viewGroupHome != null) {
            viewGroupHome.removeAllViewsInLayout(); // отличается от removeAllView

            Log.e("444", "viewGroup.removeAllViewsInLayout() во FragmentHome");
        }
        // получить экран ориентации
        int orientation = getActivity().getResources().getConfiguration().orientation;
        // раздуть соответствующий макет в зависимости от ориентации экрана
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = layoutInflater.inflate(R.layout.fragment_home, viewGroupHome, false);

            mySwipeOnRefreshListener(view); // метод Swipe чтобы обновлять данные вручную
            // метод для установки recyclerview, GridLayoutManager и AdapterListHome
            Log.e("444", "во FragmentHome ПОРТРЕТ" + "-lastVisibleItemHome-" + ((MainActivity)getActivity()).lastVisibleItemHome);
            myRecyclerLayoutManagerAdapter(view, 1, ((MainActivity)getActivity()).lastVisibleItemHome);

            Log.e("444", "во FragmentHome ПОРТРЕТ");
        } else {
            view = layoutInflater.inflate(R.layout.fragment_home_horiz, viewGroupHome, false);
            mySwipeOnRefreshListener(view); // метод Swipe чтобы обновлять данные вручную
            // метод для установки recyclerview, StaggeredGridLayoutManager и AdapterListHome
            Log.e("444", "во FragmentHome ЛАНШАФТ" + "-lastVisibleItemHome-" + ((MainActivity)getActivity()).lastVisibleItemHome);
            myRecyclerLayoutManagerAdapter(view, 2, ((MainActivity)getActivity()).lastVisibleItemHome);

            Log.e("444", "во FragmentHome ЛАНШАФТ");
        }

        if (((MainActivity)getActivity()).arrayListHome.size() == 0) {
            Log.e("444", "arrayList.size()=" + ((MainActivity)getActivity()).arrayListHome.size());
            getDateNews();
        }
        else
        {
            Log.e("444", "FragmentHome arrayList.size()  НЕ ПУСТОЙ=");
        }

        return view; // в onCreateView() возвращаем объект View, который является корневым элементом разметки фрагмента.
    }

    // метод срабатывает когда происходит смена ориентации экрана.
    // мы сохраняем содержимое views в переменные и перезаписываем на новое содержимое
    // если onConfigurationChanged работает, то активность не будет пересоздаваться, иначе будет
    // если onConfigurationChanged работает, то в манифест есть запись configChanges="orientation|screenSize|keyboardHidden"
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("444", "-зашел FragmentHome onConfigurationChanged-");
        // ДО СОЗДАНИЯ НОВОГО МАКЕТА ПИШЕМ ПЕРЕМЕННЫЕ В КОТОРЫЕ СОХРАНЯЕМ ЛЮБЫЕ ДАННЫЕ ИЗ ТЕКУЩИХ VIEW
//        // создать новый макет------------------------------
//        View view = initInterface();
//        // ПОСЛЕ СОЗДАНИЯ НОВОГО МАКЕТА ПЕРЕДАЕМ СОХРАНЕННЫЕ ДАННЫЕ В СТАРЫЕ(ТЕ КОТОРЫЕ ТЕКУЩИЕ) VIEW
//        // отображать новую раскладку на экране
//        viewGroupHome.addView(view);
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
                        ((MainActivity)getActivity()).arrayListHome.clear(); // очищаем массив чтобы в список не копировалась сверху те же данные
                        getDateNews();
                    }
                }, 500); // задержка кпол секунды
            }
        });
    }

    // метод для установки recyclerview, GridLayoutManager и AdapterListHome
    public void myRecyclerLayoutManagerAdapter(View view, int column, int lastVisableItem) {

        rvHome = view.findViewById(R.id.rvHome);
        // setHasFixedSize(true), то подразумеваете, что размеры самого RecyclerView будет оставаться неизменными.
        // Если вы используете setHasFixedSize(false), то при каждом добавлении/удалении элементов RecyclerView
        // будет перепроверять свои размеры
        rvHome.setHasFixedSize(false);

        // staggeredGridLayoutManager - шахматный порядок
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL);
        rvHome.setLayoutManager(staggeredGridLayoutManager);

        adapterListHome = new AdapterListHome(getContext(), ((MainActivity)getActivity()).arrayListHome, rvHome);
        rvHome.setAdapter(adapterListHome);
////        adapterListHome.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() { // задержка 0.5 сек
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
                    Log.e("444", "-try catch FragmentHome 1-" + e);
                }
            }
        }, 500);

    }

    private void getDateNews() { // получение данных
        Log.e("444", "-зашел FragmentHome getDateNews-");

        ApiUtilities.getApiInterface().getNews(country, 100, ((MainActivity)getActivity()).API).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()) {
                    Log.e("444","-responceHome-" + response);
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((MainActivity)getActivity()).arrayListHome.addAll(response.body().getArticles());
                                adapterListHome.notifyDataSetChanged();
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        Log.e("444", "-try catch FragmentHome -" + e);
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