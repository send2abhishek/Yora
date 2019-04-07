package com.example.abhishekaryan.yora.infrastructure;


import android.app.Application;

import com.example.abhishekaryan.services.Module;
import com.squareup.otto.Bus;

public class YoraApplication extends Application {

    private Auth auth;

    private Bus bus;

    public YoraApplication(){

        bus =new Bus();
    }

    @Override
    public void onCreate(){

        super.onCreate();
        auth=new Auth(this);
        Module.register(this);

    }

    public Auth getAuth() {
        return auth;
    }

    public Bus getBus() {
        return bus;
    }
}
