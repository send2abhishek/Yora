package com.example.abhishekaryan.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.abhishekaryan.yora.R;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText usernameText;
    private EditText emailText;
    private EditText passwordText;
    private Button registerBtn;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        usernameText =(EditText)findViewById(R.id.activity_register_username);
        emailText =(EditText)findViewById(R.id.activity_register_email);
        passwordText=(EditText)findViewById(R.id.activity_register_password);
        registerBtn=(Button)findViewById(R.id.activity_register_register_btn);
        progressBar=findViewById(R.id.activity_register_progressBar);
        progressBar.setVisibility(View.GONE);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view==registerBtn){
            application.getAuth().getUser().setLoggedIn(true);
            setResult(RESULT_OK);
            finish();
        }

    }
}
