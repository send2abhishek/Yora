package com.example.abhishekaryan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.abhishekaryan.services.Contacts;
import com.example.abhishekaryan.services.Messages;
import com.example.abhishekaryan.services.entites.ConatctRequest;
import com.example.abhishekaryan.services.entites.Message;
import com.example.abhishekaryan.views.MainActivityAdapter;
import com.example.abhishekaryan.views.mainNavDrawer;
import com.example.abhishekaryan.yora.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


public class MainActivity extends BaseAuthenticateActivity implements
        View.OnClickListener, MainActivityAdapter.MainActivityListener {

    private ArrayList<Message> messages;
    private ArrayList<ConatctRequest> requests;
    private MainActivityAdapter adapter;
    @Override
    protected void onYoraCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Inbox");
        setNavDrawer(new mainNavDrawer(this));
        adapter=new MainActivityAdapter(this,this);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.activity_main_recylerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messages=adapter.getMessages();
        requests=adapter.getConatctRequests();
        findViewById(R.id.activity_main_new_messageButton).setOnClickListener(this);
        schedular.invokeEveryMilliSecounds(new Runnable() {
            @Override
            public void run() {
                onRefresh();

            }
        },1000*60*3);
    }

    @Override
    public void onClick(View view) {

        int id=view.getId();
        if(id==R.id.activity_main_new_messageButton){

            startActivity(new Intent(this,NewMessageActivity.class));
        }

    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);
        bus.post(new Messages.SearchMessageRequest(false,true));
        bus.post(new Contacts.GetContactRequest(false));
    }

    @Subscribe
    public void onMessageLoaded(final Messages.SearchMessageResponse response){
        schedular.invokeonResume(response.getClass(), new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                if(!response.didSucceed()){
                    response.showErrorToast(MainActivity.this);
                    return;
                }

                messages.clear();
                messages.addAll(response.Messages);
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Subscribe
    public void onContactRequestLoaded(final Contacts.GetContactRequestResponse response){
        schedular.invokeonResume(response.getClass(), new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                if(!response.didSucceed()){
                    response.showErrorToast(MainActivity.this);
                    return;
                }

                requests.clear();
                requests.addAll(response.Requests);
                //adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onMessageClicked(Message message) {
        
    }

    @Override
    public void onContactRequestClicked(ConatctRequest request, int position) {

    }
}
