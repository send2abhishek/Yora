package com.example.abhishekaryan.activities;


import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.abhishekaryan.views.NavDrawer;
import com.example.abhishekaryan.yora.R;
import com.example.abhishekaryan.yora.infrastructure.ActionSchedular;
import com.example.abhishekaryan.yora.infrastructure.YoraApplication;
import com.squareup.otto.Bus;

public abstract class BaseActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Boolean isRegisteredwithBus=false;
    protected YoraApplication application;
    protected  Toolbar toolbar;
    protected NavDrawer navDrawer;
    protected boolean isTablet;
    protected Bus bus;
    protected ActionSchedular schedular;
    protected SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application =(YoraApplication) getApplication();
        bus=application.getBus();
        schedular=new ActionSchedular(application);
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        isTablet=(metrics.widthPixels/metrics.density) >= 600;
        bus.register(this);
        isRegisteredwithBus=true;
    }

    public ActionSchedular getSchedular() {
        return schedular;
    }

    @Override
    protected void onPause() {
        super.onPause();
        schedular.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        schedular.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isRegisteredwithBus){
            bus.unregister(this);
            isRegisteredwithBus=false;
        }

        if(navDrawer !=null)
        navDrawer.destroy();
    }

   @Override
   public void finish(){
       super.finish();
       if(isRegisteredwithBus){
           bus.unregister(this);
           isRegisteredwithBus=false;
       }

   }
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        toolbar=(Toolbar)findViewById(R.id.include_main_activity_toolbar);
        if(toolbar != null)
            setSupportActionBar(toolbar);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        if(swipeRefreshLayout!=null){

            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setColorSchemeColors(

                    Color.parseColor("#FF00DDFE"),
                    Color.parseColor("#FF99CC00"),
                    Color.parseColor("#FFFFBB33"),
                    Color.parseColor("#FFFF4444"));

        }
    }

    public void fadeOut(final FadeOutListener listener){

        View rootview=findViewById(android.R.id.content);
        rootview.animate().alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                listener.onFadeOutEnd();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).setDuration(300).start();



    }
    protected  void setNavDrawer(NavDrawer drawer){
        this.navDrawer=drawer;
        this.navDrawer.create();

        overridePendingTransition(0,0);
        View rootview=findViewById(android.R.id.content);
        rootview.setAlpha(0);
        rootview.animate().alpha(1).setDuration(450).start();

    }

    public Toolbar getToolbar(){
        return  toolbar;
    }

    public YoraApplication getYoraApplication(){

        return application;
    }

    @Override
    public void onRefresh() {

    }

    public interface FadeOutListener{

        void onFadeOutEnd();

    }

}
