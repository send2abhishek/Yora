package com.example.abhishekaryan.views;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekaryan.activities.BaseActivity;
import com.example.abhishekaryan.yora.R;

import java.util.ArrayList;

public class NavDrawer {

    protected BaseActivity activity;
    private ArrayList<NavDrawerItem> items;
    private NavDrawerItem selectedItem;

    protected DrawerLayout drawerLayout;
    protected ViewGroup navDrawerView;

    public NavDrawer(BaseActivity activity){

        this.activity=activity;
        items =new ArrayList<>();
        drawerLayout=(DrawerLayout)activity.findViewById(R.id.drawer_layout);
        navDrawerView=(ViewGroup)activity.findViewById(R.id.nav_drawer);

        if(drawerLayout == null || navDrawerView == null)
            throw new RuntimeException("to use this class, you must have views with the ids of drawerLayout and nav drawer");

        Toolbar toolbar=activity.getToolbar();
        toolbar.setNavigationIcon(R.mipmap.nav_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOpen(!isOpen());
            }
        });
       activity.getYoraApplication().getBus().register(this);

    }

    public void destroy(){

        activity.getYoraApplication().getBus().unregister(this);
    }

    public void addItem(NavDrawerItem item){

        items.add(item);
        item.navDrawer=this;

    }

    public boolean isOpen(){
       return drawerLayout.isDrawerOpen(Gravity.START);
    }

    public void setOpen(boolean isOpen){

        if(isOpen)
            drawerLayout.openDrawer(Gravity.START);
        else
            drawerLayout.closeDrawer(Gravity.START);
    }

    public void setSelectedItem(NavDrawerItem item){

        if(selectedItem !=null)
            selectedItem.setSelected(false);

        selectedItem=item;
        selectedItem.setSelected(true);


    }

    public void create(){
        LayoutInflater inflater=activity.getLayoutInflater();
        for(NavDrawerItem item: items){
            item.inflate(inflater,navDrawerView);
        }

    }

    public static abstract class NavDrawerItem {

        protected NavDrawer navDrawer;

        public abstract void inflate(LayoutInflater inflater, ViewGroup container);
        public abstract void setSelected(boolean isSelected);
    }

    public static class BasicNavDrawerItem extends NavDrawerItem implements View.OnClickListener {

        private String text;
        private String badge;
        private int iconDrawable;
        private int containerId;
        private ImageView icon;
        private TextView textView;
        private TextView badgeTextView;
        private View view;
        private int defaultTextColor;


        public BasicNavDrawerItem(String text,String badge,int iconDrawable,int containerId){

            this.text=text;
            this.badge=badge;
            this.iconDrawable=iconDrawable;
            this.containerId=containerId;

        }
        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawerView) {

            ViewGroup Container=(ViewGroup)navDrawerView.findViewById(containerId);
            if(Container==null)
                throw new RuntimeException("Nav drawer item "+ text + "Could not be attched to viewGroup view not found");


            view=inflater.inflate(R.layout.list_item_nav_drawer,Container,false);
            Container.addView(view);

            icon=(ImageView)view.findViewById(R.id.list_item_nav_drawer_icon);
            textView=(TextView)view.findViewById(R.id.list_item_nav_drawer_TextView);
            badgeTextView=(TextView)view.findViewById(R.id.list_item_nav_drawer_badge);
            defaultTextColor=textView.getCurrentTextColor();
            icon.setImageResource(iconDrawable);
            textView.setText(text);

            if(badge!=null)
            badgeTextView.setText(badge);
            else
                badgeTextView.setVisibility(view.GONE);
            view.setOnClickListener(this);
        }

        public void setText(String text) {
            this.text = text;
        }
        public void setBadge(String badge) {
            this.badge = badge;
        }
        public void setIconDrawable(int iconDrawable) {
          //  this.iconDrawable = iconDrawable;
        }


        @Override
        public void onClick(View v) {

            navDrawer.setSelectedItem(this);

        }



        @Override
        public void setSelected(boolean isSelected) {

        }
    }

    public static class ActivityNavDrawerItem extends BasicNavDrawerItem {

        private final Class targetActivity;


        public ActivityNavDrawerItem(Class targetActivity, String text, String badge, int iconDrawable, int containerId) {
            super(text, badge, iconDrawable, containerId);
            this.targetActivity=targetActivity;
        }

        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawerView) {
            super.inflate(inflater, navDrawerView);
            if(this.navDrawer.activity.getClass()==targetActivity)
                this.navDrawer.setSelectedItem(this);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            navDrawer.setOpen(false);

            if(navDrawer.activity.getClass()==targetActivity)
                return;

           navDrawer.activity.fadeOut(new BaseActivity.FadeOutListener() {
               @Override
               public void onFadeOutEnd() {
                   navDrawer.activity.startActivity(new Intent(navDrawer.activity,targetActivity));
                   navDrawer.activity.finish();
               }
           });


        }
    }
}
