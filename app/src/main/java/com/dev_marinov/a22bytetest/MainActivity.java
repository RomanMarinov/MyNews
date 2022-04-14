package com.dev_marinov.a22bytetest;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.ArcMotion;
import androidx.transition.ChangeBounds;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import java.util.ArrayList;
import java.util.List;

import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity {

    String API = "f725144c0220437d87363920fe7b20ba"; // help https://newsapi.org/docs

    // сохранение последнего видимого item из списка (получаю из адаптеров для каждого фрагмента в своем табе)
    public int lastVisibleItemHome;
    public int lastVisibleItemSport;
    public int lastVisibleItemBusiness;
    public int lastVisibleItemEntertainment;
    public int lastVisibleItemHealth;
    public int lastVisibleItemScience;
    public int lastVisibleItemTechnology;
    public int lastVisibleItemSearch;

    // массивы для сохранения данных для каждого таба
    public ArrayList<ModelClass> arrayListHome;
    public ArrayList<ModelClass> arrayListSport;
    public ArrayList<ModelClass> arrayListBusiness;
    public ArrayList<ModelClass> arrayListEntertainment;
    public ArrayList<ModelClass> arrayListHealth;
    public ArrayList<ModelClass> arrayListScience;
    public ArrayList<ModelClass> arrayListTechnology;
    public ArrayList<ModelClass> arrayListSearch;

    ViewGroup viewGroup;
    Handler handler; // класс потока
    Bundle bundle; // для сохранения savedInstanceState
    Button btYes, btNo; // кнопки диалогового окна при выходе из приложения
    boolean flag = false; // для backstack fragmentWebview b fragmentMain и fragmentSearch
    int lastTab = 0; // сохранение последнего таба, чтобы вернуть его при пересоздании макета
    LinearLayout llFragMain, llFragSearch; // выташил для управления
    List<String> previous = new ArrayList<String>(); // для реализации правильного backstack в браузере

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(Looper.getMainLooper()); // handler для главного потока
        Log.e("444","зашел MainActivity");

        bundle = savedInstanceState; // bundle делаю общедоступной, чтобы исп.для перехода во FragmentMain

        Log.e("444","MainActivity getSupportFragmentManager().getBackStackEntryCount()="
                + getSupportFragmentManager().getBackStackEntryCount());

        llFragMain = findViewById(R.id.llFragMain);
        llFragSearch = findViewById(R.id.llFragSearch);

        setWindow(); // дизайн фона

        // массивы для сохранения данных для каждого таба
        arrayListHome = new ArrayList<>();
        arrayListSport = new ArrayList<>();
        arrayListBusiness = new ArrayList<>();
        arrayListEntertainment = new ArrayList<>();
        arrayListHealth = new ArrayList<>();
        arrayListScience = new ArrayList<>();
        arrayListTechnology = new ArrayList<>();
        arrayListSearch = new ArrayList<>();

        // берем белый frameLayout, который растянут во весь экран и который находиться в activity_main
        viewGroup = findViewById(R.id.fl_viewGroup);

        //показать сцену 1, она содержит белый frameLayout и progress_bar_scene
        showScene1(false);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void showScene1(boolean bool) {    // сцена 1 - старт от progressBar
        Log.e("333","зашел showScene1");
        // scene_animation_1 - это белый FrameLayout только с progress_bar_scene
        ViewGroup root = (ViewGroup) getLayoutInflater().inflate(R.layout.scene_animation_1, null);
        ProgressBar progressBar = root.findViewById(R.id.progress_bar_scene);
        progressBar.setVisibility(View.VISIBLE); // показываем progress_bar_scene

        Runnable runnable = new Runnable() { // скрываем progress_bar_scene через 2сек
            @Override
            public void run() {
                showScene2();
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        handler.postDelayed(runnable, 2000);

        Scene scene = new Scene(viewGroup, root);
        TransitionManager.go(scene, null); // делаем транзакцию в следующую scene_animation_2
    }

    private void showScene2() {    // сцена 2 - это фон у FragmentMain
        Log.e("333","зашел showScene2");
        ViewGroup root = (ViewGroup) getLayoutInflater().inflate(R.layout.scene_animation_2, null);

        Scene scene = new Scene(viewGroup, root);
        Transition transition = getScene2Transition();
        TransitionManager.go(scene, transition);

        // как только scene_animation_2 закончила показ, через 0.5 сек мы переходим в FragmentMain
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // при первой загрузке запускаем FragmentMain если bundle пустой
                if(bundle == null)
                {
                    Log.e("333","зашел поток=" + Thread.currentThread().getName());

                    FragmentMain fragmentMain = new FragmentMain();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.llFragMain, fragmentMain, "llFragMain");
                    //fragmentTransaction.addToBackStack("llFragMain");
                    fragmentTransaction.commit();
                }
            }
        };
        handler.postDelayed(runnable, 500);
    }

    private Transition getScene2Transition() {    // сцена 2 - это фон у FragmentMain
        Log.e("333","зашел getScene2Transition");
        TransitionSet set = new TransitionSet();

        //ChangeBounds переход, меняет позицию
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                Log.e("333","зашел getScene2Transition changeTransform.addListener");
                // скрыть progress_bar в конце анимации
                viewGroup.findViewById(R.id.progress_bar_scene).setVisibility(View.INVISIBLE);

            }
        });
        changeBounds.addTarget(R.id.progress_bar_scene);
        changeBounds.setDuration(300);
        // путь дуги
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMaximumAngle(45);
        arcMotion.setMinimumHorizontalAngle(90);
        arcMotion.setMinimumVerticalAngle(0);

        changeBounds.setPathMotion(arcMotion);
        set.addTransition(changeBounds);

        //начинается анимация кругового раскрытия bg
        CircularRevealTransition circularRevealTransition = new CircularRevealTransition();
        circularRevealTransition.addTarget(R.id.cl_scene);
        circularRevealTransition.setStartDelay(200);
        circularRevealTransition.setDuration(600);
        set.addTransition(circularRevealTransition);

        return set;
    }

    // метод анимации и перехода во фрагменты с параметрами (в какой фрагмент нужно перейти
    // и ссылка для FragmentWebview если произошел клик в адаптерах по новости)
    public void flipCard(String string, String url)
    {
        if(string.equals("goFragmentSearch"))  // переход во FragmentSearch
        {
            FragmentSearch fragmentSearch = new FragmentSearch();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // тут была анимация, теперь она во фрагменте FagmentMain(так рекомендовали)
            fragmentTransaction.replace(R.id.llFragSearch, fragmentSearch, "llFragSearch");
            fragmentTransaction.addToBackStack("llFragSearch");
            fragmentTransaction.commit();
        }

        if(string.equals("goWebviewActivity")) // переход во FragmentWebview
        {
            FragmentWebview fragmentWebview = new FragmentWebview();
            fragmentWebview.setParam(url);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_right_enter,
                            R.animator.card_flip_right_exit,
                            R.animator.card_flip_left_enter,
                            R.animator.card_flip_left_exit)
            // помещаем в контейнер R.id.clActMain фрагмент new FragmentSearch()
            // т.е. из контейнера удалится его текущий фрагмент (если он там есть) и добавится новый фрагмент
                    .replace(R.id.llFragWebview, fragmentWebview, "llFragWebview")
                    .addToBackStack("llFragWebview")
                    .commit();
        }
    }

    // метод только для myAlertDialog();
    @Override
    public void onBackPressed()
    {
        if(flag) // если flag true значит работаем с back history webview
        {
            Log.e("444", "onBackPressed webview");
            int size = previous.size();
            if (size > 0){
                FragmentWebview fragmentWebview = (FragmentWebview) getSupportFragmentManager().findFragmentById(R.id.llFragWebview);
                if(fragmentWebview != null)
                {
                    fragmentWebview.webView.loadUrl(previous.get(size - 1));
                }
                previous.remove(size - 1);
            } else {
                Log.e("444", "super.onBackPressed() webview");
                flag = false;
                super.onBackPressed();
                    Log.e("444","ПОСЛЕ super.onBackPressed() getSupportFragmentManager().getBackStackEntryCount()="
                            + getSupportFragmentManager().getBackStackEntryCount());
            }
        }
        else if(!flag) //  если flag false значит работаем с backstack fragment
        {
            Log.e("444","ДО счетчик getSupportFragmentManager().getBackStackEntryCount()="
                    + getSupportFragmentManager().getBackStackEntryCount());

            // как только будет ноль (последний экран) выполниться else
            if(getSupportFragmentManager().getBackStackEntryCount() > 0) {

                Log.e("444","ПОСЛЕ счетчик getSupportFragmentManager().getBackStackEntryCount()="
                        + getSupportFragmentManager().getBackStackEntryCount());

                // часть кода для того чтобы я просто мог только нажать кн назад и удалить view
                if(getSupportFragmentManager().getBackStackEntryCount() == 1)
                {
                  // не айс что установил, т.к. при вращении не сохранеятся позиция последней отскроленой новости
                llFragSearch.removeAllViews();
                llFragSearch.removeAllViewsInLayout();
                }
                super.onBackPressed();
            }
            else {
                getSupportFragmentManager().popBackStack(); // удаление фрагментов из транзакции
                myAlertDialog(); // метод реализации диалога с пользователем закрыть приложение или нет
            }
        }

    }

    // метод реализации диалога с пользователем закрыть приложение или нет
    public void myAlertDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.windows_alertdialog);
        dialog.setCancelable(false);
        dialog.show();

        btYes = dialog.findViewById(R.id.btYes);
        btNo = dialog.findViewById(R.id.btNo);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });
    }

    public void setWindow()
    {
        Window window = getWindow();
        // установка градиента анимации на toolbar
        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Drawable background = getResources().getDrawable(R.drawable.gradient_3);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS Флаг, указывающий, что это Окно отвечает за отрисовку фона для системных полос.
        // Если установлено, системные панели отображаются с прозрачным фоном, а соответствующие области в этом окне заполняются цветами,
        // указанными в Window#getStatusBarColor()и Window#getNavigationBarColor().
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        window.setNavigationBarColor(getResources().getColor(android.R.color.black));
        window.setBackgroundDrawable(background);
    }
}