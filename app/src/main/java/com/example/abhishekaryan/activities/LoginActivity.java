package com.example.abhishekaryan.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.abhishekaryan.fragment.LoginFragment;
import com.example.abhishekaryan.yora.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener,LoginFragment.Callbacks {

    private static final int REGISTER_REQUEST_CODE = 2 ;
    private static final int REQUEST_EXTRA_EXTERNAL_LOGIN = 3 ;
    private Button login_btn;
    private Button register_btn;
    private Button facebook_login_btn;
    private Button google_login_btn;
    private final int login_request_code=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_btn=(Button)findViewById(R.id.yora_login);
        register_btn=(Button)findViewById(R.id.yora_Registration);
        facebook_login_btn=(Button)findViewById(R.id.facebook_login);
        google_login_btn=(Button)findViewById(R.id.google_login);
        if(login_btn!=null){
            login_btn.setOnClickListener(this);
        }
        register_btn.setOnClickListener(this);
        facebook_login_btn.setOnClickListener(this);
        google_login_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(v==login_btn) {

            startActivityForResult(new Intent(this, LoginNarrowActivity.class), login_request_code);
        }
        else if (v==register_btn){
            startActivityForResult(new Intent(this,RegisterActivity.class),REGISTER_REQUEST_CODE);
        }

        else if (v==google_login_btn){
            doExternalLogin("Google");
        }
        else if (v==facebook_login_btn){
            doExternalLogin("facebook");
        }

      }

    private void doExternalLogin(String externalServices) {

        Intent intent=new Intent(this,ExternalLoginActivity.class);
        intent.putExtra(ExternalLoginActivity.EXTRA_EXTERNAL_SERVICE,externalServices);
        startActivityForResult(intent,REQUEST_EXTRA_EXTERNAL_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=RESULT_OK)
            return;
        if(requestCode==login_request_code || requestCode==REGISTER_REQUEST_CODE || requestCode==REQUEST_EXTRA_EXTERNAL_LOGIN)
            finishLogin();
    }
    private void finishLogin(){

        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    public void onLoggedIn() {
        finishLogin();
    }
}

