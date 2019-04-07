package com.example.abhishekaryan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.abhishekaryan.services.Messages;
import com.example.abhishekaryan.services.entites.Message;
import com.example.abhishekaryan.views.MessageAdapter;
import com.example.abhishekaryan.views.mainNavDrawer;
import com.example.abhishekaryan.yora.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class sentMessageActivity extends BaseAuthenticateActivity implements MessageAdapter.onMessageClickListener {

    private static final int REQUEST_VIEW_MESSAGE=1;
    private MessageAdapter adapter;
    private ArrayList<Message> messages;
    private View progressFrame;
    public int messageSize;
    @Override
    public void onYoraCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_sent_messsage);
        setNavDrawer(new mainNavDrawer(this));

        getSupportActionBar().setTitle("Sent Message");



        adapter=new MessageAdapter(this,this);
        messages=adapter.getMessages();
        messageSize=messages.size();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.activity_sent_message_recylerView);
        recyclerView.setAdapter(adapter);
        if(isTablet) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        else{

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            progressFrame=findViewById(R.id.activity_sent_progressbar);

        schedular.postEveryMilliSecounds
                (new Messages.SearchMessageRequest(true,false),1000*60*30);



    }

    @Override
    public void OnmessageClicked(Message message) {

        Intent intent=new Intent(this,MessageActivity.class);
        intent.putExtra(MessageActivity.EXTRA_MESSAGES,message);
        startActivityForResult(intent,REQUEST_VIEW_MESSAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode !=REQUEST_VIEW_MESSAGE || resultCode !=MessageActivity.REQUEST_IMAGE_DELETE){
            return;
        }


        int messageId=data.getIntExtra(MessageActivity.RESULT_EXTRA_MESSAGE_ID,-1);
        if(messageId==-1){
            return;
        }


       for(int i=0;i<messageSize; i++){

           Message message=messages.get(i);
           if(message.getId()!=messageId){

               continue;
           }
           messages.remove(i);
           adapter.notifyItemRemoved(i);
           break;
       }
    }

    @Subscribe
    public void onMessageLoaded(Messages.SearchMessageResponse response){

        response.showErrorToast(this);

        int oldMessageSize=messages.size();
        messages.clear();
        adapter.notifyItemRangeRemoved(0,oldMessageSize);
        messages.addAll(response.Messages);
        adapter.notifyItemRangeInserted(0,messages.size());
        progressFrame.setVisibility(View.GONE);
    }
}
