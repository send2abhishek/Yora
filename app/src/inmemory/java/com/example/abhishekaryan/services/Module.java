package com.example.abhishekaryan.services;


import android.util.Log;

import com.example.abhishekaryan.yora.infrastructure.YoraApplication;

public class Module {

    public static void register(YoraApplication application){

        new inMemoryAccountService(application);
        new InMemoryContactService(application);
        new inMemoryMessageService(application);
    }


}
