package com.example.abhishekaryan.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;

import com.example.abhishekaryan.yora.infrastructure.ActionSchedular;
import com.example.abhishekaryan.yora.infrastructure.YoraApplication;
import com.squareup.otto.Bus;

public abstract class BaseDialogFragment extends DialogFragment {

    protected YoraApplication application;
    protected Bus bus;
    protected ActionSchedular schedular;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application =(YoraApplication)getActivity().getApplication();
        schedular=new ActionSchedular(application);
        bus=application.getBus();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        schedular.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        schedular.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
