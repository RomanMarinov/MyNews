package com.dev_marinov.a22bytetest;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;


public class FragmentMain extends Fragment {

    public ViewPager2 viewPager2;
    TabLayout tabLayout;
    ArrayList<String> titles; // массив заголовков
    ArrayList<Fragment> fragmentList; // массив фрагментов
    ImageView imgSearch;

    ViewGroup viewGroupMain;
    LayoutInflater layoutInflater;
    ViewPager2Adapter viewPager2Adapter;

    // удалить
    TextView tvTitle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("444", "зашел во FragmentMain");

        Log.e("444","FragmentMain getSupportFragmentManager().getBackStackEntryCount()="
                + getActivity().getSupportFragmentManager().getBackStackEntryCount());

        viewGroupMain = container;
        this.layoutInflater = inflater;

        return initInterface();
    }
    public View initInterface() {
        View view;

        if (viewGroupMain != null) {
            viewGroupMain.removeAllViewsInLayout(); // отличается от removeAllView
            Log.e("444", "viewGroup.removeAllViewsInLayout() во FragmentMain");
        }

        // получить экран ориентации
        int orientation = getActivity().getResources().getConfiguration().orientation;
        // раздуть соответствующий макет в зависимости от ориентации экрана
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            view = layoutInflater.inflate(R.layout.fragment_main, viewGroupMain, false);
            Log.e("444", "fragment_main PORTRAIT=");

        } else {
            view = layoutInflater.inflate(R.layout.fragment_main_horiz, viewGroupMain, false);
            Log.e("444", "fragment_main LANDSCAPE=");
        }

        imgSearch = view.findViewById(R.id.imgSearch);

        viewPager2 = view.findViewById(R.id.viewPager2);
        tabLayout = view.findViewById(R.id.tabLayout);

        //tabLayout.removeAllTabs();// удалить все закладки

        titles = new ArrayList<String>();
        titles.add("Главная");
        titles.add("Спорт");
        titles.add("Бизнес");
        titles.add("Развлечение");
        titles.add("Здоровье");
        titles.add("Наука");
        titles.add("Технологии");

        fragmentList = new ArrayList<>();

//        fragmentList.clear();


        fragmentList.add(new FragmentHome());
        fragmentList.add(new FragmentSport());
        fragmentList.add(new FragmentBusiness());
        fragmentList.add(new FragmentEntertainment());
        fragmentList.add(new FragmentHealth());
        fragmentList.add(new FragmentScience());
        fragmentList.add(new FragmentTechnology());

        // установка viewPager2Adapter
        viewPager2Adapter = new ViewPager2Adapter(getActivity());

        viewPager2Adapter.setData(fragmentList); // помещаем массив фрагментов в адаптер






        viewPager2.setAdapter(viewPager2Adapter); // установка адаптера


//        viewPager2.setAdapter(new FragmentStateAdapter(this) {
//            @NonNull
//            @Override
//            public Fragment createFragment(int position) {
//                Fragment fragment = fragmentList.get(position);
//                return fragment;
//            }
//
//            @Override
//            public int getItemCount() {
//                return fragmentList.size();
//            }
//        });



        viewPager2.setOffscreenPageLimit(4); //??????????????????????????????????????????????

        // TabLayoutMediator для синхронизации компонента TabLayout с ViewPager2
        // установка текста заголовков вкладок, стиля вкладок
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Log.e("4445", "TabLayoutMediator onConfigureTab tab=" + tab.getPosition() + " position=" + position);
                try {
                    tab.setText(titles.get(position));
                }
                catch (Exception e)
                {
                    Log.e("444", " try catch TabLayoutMediator" + e);
                }

                // Log.e("444", "tab.getText() во FragmentMain" + tab.getText().toString());
            }
        }).attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("4445","viewPager2 onPageSelected =" + position);
                // вынужденое сохранение позиции последнего таба перед пересоздание макета при повороте
                // чтобы восстановить состояние последнего выбранного таба
                ((MainActivity)getActivity()).lastTab = position;

                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("4445","viewPager2 onPageScrollStateChanged =" + state);
                super.onPageScrollStateChanged(state);
            }
        });

        //восстановить состояние последнего выбранного таба
        tabLayout.selectTab(tabLayout.getTabAt(((MainActivity)getActivity()).lastTab));

        imgSearch.setOnClickListener(new View.OnClickListener() { // переход во фрагемент по поиску новостей
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("676","click imgSearch");
                       // ((MainActivity)getActivity()).flipCard("goFragmentSearch", null);

                        ///////////////////////////////////

                        FragmentSearch fragmentSearch = new FragmentSearch();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                                .setCustomAnimations(R.animator.card_flip_right_enter,
                                        R.animator.card_flip_right_exit,
                                        R.animator.card_flip_left_enter,
                                        R.animator.card_flip_left_exit);
                        fragmentTransaction.replace(R.id.llFragSearch, fragmentSearch, "llFragSearch");
                        fragmentTransaction.addToBackStack("llFragSearch");
                        fragmentTransaction.commit();
                        //////////////////////////////

                        imgSearch.setEnabled(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imgSearch.setEnabled(true);
                            }
                        },1000);
                    }
                });
            }
        });

// удалить
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentWebview fragmentWebview = new FragmentWebview();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.llFragWebview, fragmentWebview);
                fragmentTransaction.commit();
            }
        });


        return view; // в onCreateView() возвращаем объект View, который является корневым элементом разметки фрагмента.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("444", "-зашел FragmentMain onConfigurationChanged-");
         //ДО СОЗДАНИЯ НОВОГО МАКЕТА ПИШЕМ ПЕРЕМЕННЫЕ В КОТОРЫЕ СОХРАНЯЕМ ЛЮБЫЕ ДАННЫЕ ИЗ ТЕКУЩИХ VIEW
         //создать новый макет------------------------------
        View view = initInterface();
        // ПОСЛЕ СОЗДАНИЯ НОВОГО МАКЕТА ПЕРЕДАЕМ СОХРАНЕННЫЕ ДАННЫЕ В СТАРЫЕ(ТЕ КОТОРЫЕ ТЕКУЩИЕ) VIEW
        // отображать новую раскладку на экране
        viewGroupMain.addView(view);
        super.onConfigurationChanged(newConfig);

        viewPager2.setOffscreenPageLimit(4); //??????????????????????????????????????????????
    }

}