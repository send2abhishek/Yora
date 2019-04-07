package com.example.abhishekaryan.activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.abhishekaryan.fragment.ContactFragment;
import com.example.abhishekaryan.fragment.PendingContactRequestFragment;
import com.example.abhishekaryan.views.mainNavDrawer;
import com.example.abhishekaryan.yora.R;


public class contactsActivity extends BaseAuthenticateActivity implements AdapterView.OnItemSelectedListener {
    private ObjectAnimator currentAnimation;
    private ArrayAdapter<contactSpinnerItem> adapter;

    @Override
    protected void onYoraCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_contacts);
        setNavDrawer(new mainNavDrawer(this));

        adapter=new ArrayAdapter<>(this,R.layout.list_item_toolbar_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapter.add(new contactSpinnerItem("Contacts", Color.parseColor("#00BCD4"), ContactFragment.class));
        adapter.add(new contactSpinnerItem("Pending Contacts Request",
                Color.parseColor("#607D8B"), PendingContactRequestFragment.class));
        Spinner spinner=(Spinner)findViewById(R.id.activity_contact_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        contactSpinnerItem item=adapter.getItem(position);

        if(item==null)
            return;
        if(currentAnimation !=null)
            currentAnimation.end();
        int currentColor=((ColorDrawable)toolbar.getBackground()).getColor();
        currentAnimation=ObjectAnimator
                .ofObject(toolbar,"backgroundColor", new ArgbEvaluator(),currentColor,item.getColor())
                .setDuration(250);
        currentAnimation.start();

        Fragment fragment = null;
        try{
            fragment=(Fragment)item.getFragment().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.i("ContactsActivity","Could not instatiate the fragment"+ item.getFragment().getName(),e);
            return;
        }
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out)
                .replace(R.id.activity_contact_fragment_container,fragment)
                .commit();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class contactSpinnerItem {

        private final String title;
        private final int color;
        private Class fragment;


        public contactSpinnerItem(String title, int color, Class fragment) {
            this.title = title;
            this.color = color;
            this.fragment = fragment;
        }

        public String getTitle() {
            return title;
        }

        public int getColor() {
            return color;
        }

        public Class getFragment() {
            return fragment;
        }

        public void setFragment(Class fragment) {
            this.fragment = fragment;
        }

        @Override
        public String toString() {
            return getTitle();
        }
    }


}
