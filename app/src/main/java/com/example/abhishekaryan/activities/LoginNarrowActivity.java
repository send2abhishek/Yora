package com.example.abhishekaryan.activities;


import android.os.Bundle;

import com.example.abhishekaryan.fragment.LoginFragment;
import com.example.abhishekaryan.yora.R;

public class LoginNarrowActivity extends BaseActivity implements LoginFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_narrow);
    }

    @Override
    public void onLoggedIn() {
        setResult(RESULT_OK);
        finish();
    }
}
