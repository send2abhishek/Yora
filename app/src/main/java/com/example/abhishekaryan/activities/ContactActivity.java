package com.example.abhishekaryan.activities;

/* Activity to show contact details where user selects a particular contact in contactActivity

 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.abhishekaryan.services.Contacts;
import com.example.abhishekaryan.services.Messages;
import com.example.abhishekaryan.services.entites.Message;
import com.example.abhishekaryan.services.entites.UserDetails;
import com.example.abhishekaryan.views.MessageAdapter;
import com.example.abhishekaryan.views.NavDrawer;
import com.example.abhishekaryan.yora.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class ContactActivity extends BaseAuthenticateActivity implements MessageAdapter.onMessageClickListener {

    public static final String EXTRA_USER_DETAILS="EXTRA_USER_DETAILS";
    private static final int RESULT_USER_REMOVED =101 ;
    private static final int REQUEST_SEND_MESSAGE=1;

    private MessageAdapter adapter;
    private ArrayList<Message> messages;
    private UserDetails userDetails;
    private View progressFrame;
    @Override
    protected void onYoraCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contact);

        progressFrame=findViewById(R.id.activty_contact_progressFrame);


        //here we are reciving the object when user clicks on a prticular contact
        userDetails=getIntent().getParcelableExtra(EXTRA_USER_DETAILS);

        if(userDetails==null){

            userDetails=new UserDetails(1,true,"A contact","a_contact","http://www.gravatar.com/avatar/1.jpg");
        }

        adapter=new MessageAdapter(this,this);
        messages=adapter.getMessages();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.activity_contact_recylerView);

        if(isTablet){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setTitle(userDetails.getDisplayName());
        toolbar.setNavigationIcon(R.mipmap.ic_del_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();

            }
        });

       schedular.postEveryMilliSecounds(new Messages.SearchMessageRequest(userDetails.getId(),true,true),1000*60*3);

    }


    @Override
    public void OnmessageClicked(Message message) {
        Intent intent=new Intent(this,MessageActivity.class);
        intent.putExtra(MessageActivity.EXTRA_MESSAGES,message);
        startActivityForResult(intent,REQUEST_SEND_MESSAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_SEND_MESSAGE && resultCode==RESULT_OK){

            bus.post(new Messages.SearchMessageRequest(userDetails.getId(),true,true));
        }
    }

    @Subscribe
    public void onMessageReceived(final Messages.SearchMessageResponse response){

        schedular.invokeonResume(Messages.SearchMessageResponse.class, new Runnable() {
            @Override
            public void run() {
                progressFrame.setVisibility(View.GONE);
                if(!response.didSucceed()){
                    response.showErrorToast(ContactActivity.this);
                    return;
                }

                    int oldSize=messages.size();
                messages.clear();
                adapter.notifyItemRangeRemoved(0,oldSize);
                messages.addAll(response.Messages);
                adapter.notifyItemRangeInserted(0,messages.size());

            }
        });


    }

    private void doRemoveContact(){

        progressFrame.setVisibility(View.VISIBLE);
        bus.post(new Contacts.RemoveContactRequest(userDetails.getId()));
        progressFrame.setVisibility(View.GONE);
    }

    @Subscribe
    public void OnRemoveContact(final Contacts.RemoveContactIdResponse response){

        schedular.invokeonResume(Contacts.RemoveContactIdResponse.class, new Runnable() {
            @Override
            public void run() {

                if(!response.didSucceed()){
                    response.showErrorToast(ContactActivity.this);
                    progressFrame.setVisibility(View.VISIBLE);
                    return;
                }
                setResult(RESULT_USER_REMOVED);
                finish();

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.activity_contact,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.activity_contact_menu_newMessage){

            Intent intent=new Intent(this,NewMessageActivity.class);
            intent.putExtra(NewMessageActivity.EXTRA_CONTACT,userDetails);
            startActivity(intent);
            return true;
        }
        if(id==R.id.activity_contact_menu_remove_freiend){

            AlertDialog dialog=new AlertDialog.Builder(this)
                    .setTitle("Remove Friend")
                    .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doRemoveContact();
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .create();
            dialog.show();
            return true;
        }

        return false;
    }
}
