package com.example.abhishekaryan.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.abhishekaryan.services.Contacts;
import com.example.abhishekaryan.services.entites.UserDetails;
import com.example.abhishekaryan.views.UserDetailAdapter;
import com.example.abhishekaryan.yora.R;
import com.squareup.otto.Subscribe;

public class AddContactActivity extends BaseAuthenticateActivity implements AdapterView.OnItemClickListener {

    public static final String RESULT_CONTACT="RESULT_CONTACT";
    private UserDetailAdapter adapter;
    private Handler handler;
    private SearchView searchView;
    private View progressFrame;
    private UserDetails Selectedusers;
    private String lastQuery;
    private Runnable searchRunnable =new Runnable() {
        @Override
        public void run() {

            lastQuery=searchView.getQuery().toString();
            progressFrame.setVisibility(View.VISIBLE);
            bus.post(new Contacts.searchUserRequest(lastQuery));

        }
    };

    @Override
    protected void onYoraCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_contact);
        progressFrame=findViewById(R.id.activty_add_contact_progressFrame);
        progressFrame.setVisibility(View.GONE);
        adapter=new UserDetailAdapter(this);
        ListView listView=(ListView)findViewById(R.id.activity_add_contact_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        handler=new Handler();
        searchView=new SearchView(this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(searchView);
        searchView.setIconified(false);
        searchView.setQueryHint("Search User Here...");
        searchView.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.MATCH_PARENT
                ));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(query.length() < 3)
                return true;
                handler.removeCallbacks(searchRunnable);
                handler.postDelayed(searchRunnable,750);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                setResult(RESULT_CANCELED);
                finish();
                return true;
            }
        });
    }
    @Subscribe
    public void onUserSearched(Contacts.searchUserResponse response){
        progressFrame.setVisibility(View.GONE);
        if(!response.didSucceed()){
            response.showErrorToast(this);
            return;
        }
        if(!response.Query.equals(lastQuery)){
            //if sever respose slowly and we have made another request to search
            return;
        }
        adapter.clear();
        adapter.addAll(response.users);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog dialog=new AlertDialog.Builder(this)
                .setPositiveButton("Send Contact Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendContactRequest(adapter.getItem(position));
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();
        dialog.show();


    }

    public void sendContactRequest(UserDetails user){
        Selectedusers=user;
        progressFrame.setVisibility(View.VISIBLE);
        bus.post(new Contacts.sendContactRequestRequest(user.getId()));

    }
    @Subscribe
    public void onContactRequestSend(Contacts.sendContactRequestResponse response){

        if(!response.didSucceed()){
            response.showErrorToast(this);
            progressFrame.setVisibility(View.GONE);
            return;
        }
        Intent intent=new Intent();
        intent.putExtra(RESULT_CONTACT,Selectedusers);
        setResult(RESULT_OK,intent);
        finish();

    }

}
