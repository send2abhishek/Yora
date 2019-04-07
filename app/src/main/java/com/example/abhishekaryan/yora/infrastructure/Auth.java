package com.example.abhishekaryan.yora.infrastructure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.abhishekaryan.activities.LoginActivity;

public class Auth {

    private static final String AUTH_PREFENCES="AUTH_PREFENCES";
    private static final String AUTH_PREFENCES_TOKEN="AUTH_PREFENCES_TOKEN";

    private  Context context = null;
    private User user;
    private final SharedPreferences preferences;


    private String AuthToken;

    public Auth(Context context) {
        this.context = context;
        user=new User();

        preferences=context.getSharedPreferences(AUTH_PREFENCES,Context.MODE_PRIVATE);
        AuthToken=preferences.getString(AUTH_PREFENCES_TOKEN,null);
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public boolean hasAuthToken(){

        return AuthToken !=null && !AuthToken.isEmpty();

    }
    public void setAuthToken(String authToken) {
        AuthToken = authToken;

        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(AUTH_PREFENCES_TOKEN,AuthToken);
        editor.commit();
    }
    public User getUser() {
        return user;

    }

    public void logout(){

        setAuthToken(null);
        Intent loginIntent=new Intent(context, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);

    }
}
