package com.example.abhishekaryan.services;

import android.os.Handler;
import android.util.Log;

import com.example.abhishekaryan.yora.infrastructure.YoraApplication;
import com.squareup.otto.Bus;

import java.util.Random;

public class BaseInMemoryService {

    protected final Bus bus;
    protected  YoraApplication application;
    protected final Handler handler;
    protected final Random random;

    protected BaseInMemoryService(YoraApplication application){

        this.application=application;
        bus=application.getBus();
        handler=new Handler();
        random=new Random();
        bus.register(this);

    }

    protected void invokeDeleyed(Runnable runnable, long millisecoundMin, long millisecoundMax){

        if(millisecoundMin > millisecoundMax)
            throw new IllegalArgumentException("min must be smaller than max");

        long delay=(long)(random.nextDouble() * (millisecoundMax - millisecoundMin)) + millisecoundMin ;
        handler.postDelayed(runnable,delay);

    }

    protected void postDelayed(final Object event, long millisecoundMin, long millisecoundMax){

        invokeDeleyed(new Runnable() {
            @Override
            public void run() {
                bus.post(event);

            }
        },millisecoundMin,millisecoundMax);
    }
    protected void postDelayed(final Object event, long millisecoundMin){

        postDelayed(event,millisecoundMin,millisecoundMin);
    }
    protected void postDelayed(final Object event){

        postDelayed(event,60,1200);
    }

}
