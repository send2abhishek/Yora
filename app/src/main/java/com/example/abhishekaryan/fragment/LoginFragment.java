package com.example.abhishekaryan.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.abhishekaryan.services.Account;
import com.example.abhishekaryan.yora.R;
import com.squareup.otto.Subscribe;


public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private Button btn_login;
    private Callbacks callbacks;
    private EditText userNameText;
    private EditText passwordText;
    private ProgressBar progressBar;
    private String defaultLoginBtnText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragement_login,container,false);

        userNameText=(EditText)view.findViewById(R.id.fragment_login_username);
        passwordText=(EditText)view.findViewById(R.id.fragment_login_password);
        btn_login=(Button)view.findViewById(R.id.fragment_login_btn);
        progressBar=(ProgressBar)view.findViewById(R.id.fragment_progressbar);
        defaultLoginBtnText=btn_login.getText().toString();
        btn_login.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        if(view==btn_login){
            /* notify parent activity */

          /*  ((LoginActivity)getActivity()).finishLogin(); -> we can call this way also, but we will use generalize way
            using interface */

           /* application.getAuth().getUser().setLoggedIn(true);
            application.getAuth().getUser().setDisplayName("Abhishek Aryan"); */

           progressBar.setVisibility(View.VISIBLE);
            btn_login.setText("");
            userNameText.setEnabled(false);
            passwordText.setEnabled(false);
            btn_login.setEnabled(false);

           bus.post(new Account.LoginWithUserNameRequest(userNameText.getText().toString()
                   ,passwordText.getText().toString()));



        }

    }

    @Subscribe
    public void onLoginWithUserName(Account.LoginWithUserNameResponse response){

        response.showErrorToast(getActivity());

        if(response.didSucceed()){
            callbacks.onLoggedIn();
            return;
        }

        userNameText.setError(response.getPropertyErrors("username"));
        userNameText.setEnabled(true);
        passwordText.setError(response.getPropertyErrors("password"));
        passwordText.setEnabled(true);
        btn_login.setEnabled(true);
        btn_login.setText(defaultLoginBtnText);


    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        callbacks=(Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks=null;
    }

    public interface Callbacks{

        void onLoggedIn();

    }
}
