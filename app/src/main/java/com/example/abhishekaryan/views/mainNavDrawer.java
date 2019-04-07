package com.example.abhishekaryan.views;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishekaryan.activities.BaseActivity;
import com.example.abhishekaryan.activities.LoginActivity;
import com.example.abhishekaryan.activities.MainActivity;
import com.example.abhishekaryan.activities.contactsActivity;
import com.example.abhishekaryan.activities.profileActivity;
import com.example.abhishekaryan.activities.sentMessageActivity;
import com.example.abhishekaryan.services.Account;
import com.example.abhishekaryan.yora.R;
import com.example.abhishekaryan.yora.infrastructure.User;
import com.squareup.otto.Subscribe;


public class mainNavDrawer extends NavDrawer {
    private final TextView displayNameText;
    private final ImageView avatatImage;
    public mainNavDrawer(final BaseActivity activity) {
        super(activity);


        addItem(new ActivityNavDrawerItem(MainActivity.class,"Inbox","27",R.mipmap.inbox,R.id.include_main_nav_drawer_top_item));
        addItem(new ActivityNavDrawerItem(sentMessageActivity.class,"Sent Message","12",R.mipmap.ic_sent_msg,R.id.include_main_nav_drawer_top_item));
        addItem(new ActivityNavDrawerItem(contactsActivity.class,"Contacts",null,R.mipmap.ic_contacts,R.id.include_main_nav_drawer_top_item));
        addItem(new ActivityNavDrawerItem(profileActivity.class,"Profile",null,R.mipmap.ic_profile,R.id.include_main_nav_drawer_top_item));
        addItem(new BasicNavDrawerItem("Logout",null,R.mipmap.ic_logout,R.id.include_main_nav_drawer_bottom_item) {

           @Override
           public void onClick(View v) {
               Toast.makeText(activity,"You have LoggedOut",Toast.LENGTH_SHORT).show();

           }

       });

        displayNameText =(TextView)navDrawerView.findViewById(R.id.include_main_nav_drawer_displayName);
            avatatImage=(ImageView)navDrawerView.findViewById(R.id.include_main_nav_drawer_avatar);

        User loggedInUser=activity.getYoraApplication().getAuth().getUser();
        displayNameText.setText(loggedInUser.getDisplayName());



    }

    @Subscribe
    public void onUserDetailsUpdated(Account.UserDetailsUpdatedEvent event){

        displayNameText.setText(event.user.getDisplayName());
    }





}


