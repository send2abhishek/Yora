package com.example.abhishekaryan.activities;

import android.content.Intent;
import android.os.Bundle;

public abstract class BaseAuthenticateActivity extends BaseActivity {

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(!application.getAuth().getUser().getLoggedIn()){

            startActivity(new Intent(this,LoginActivity.class));
            finish();
            return;
        }
        onYoraCreate(savedInstanceState);
    }
protected abstract void onYoraCreate(Bundle savedInstanceState);

}
