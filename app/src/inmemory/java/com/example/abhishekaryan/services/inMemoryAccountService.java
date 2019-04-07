package com.example.abhishekaryan.services;


import android.util.Log;

import com.example.abhishekaryan.yora.infrastructure.Auth;
import com.example.abhishekaryan.yora.infrastructure.User;
import com.example.abhishekaryan.yora.infrastructure.YoraApplication;
import com.squareup.otto.Subscribe;

public class inMemoryAccountService extends BaseInMemoryService {



    public inMemoryAccountService(YoraApplication application) {
        super(application);
    }

    @Subscribe
    public void UpdateProfile(final Account.UpdateProfileRequest request){




        final Account.UpdateProfileResponse response=new Account.UpdateProfileResponse();

        if (request.DisplayName.equals("Abhishek Aryan"))
            response.setPropertyErrors("displayName","You may not be named abhishek");

       invokeDeleyed(new Runnable() {
           @Override
           public void run() {
               User user=application.getAuth().getUser();
               user.setDisplayName(request.DisplayName);
               user.setEmail(request.Email);
               bus.post(response);
               bus.post(new Account.UserDetailsUpdatedEvent(user));
           }
       },2000,3000);


    }

    @Subscribe
    public void changePassword(Account.UpdateChangePasswordRequest request){

     Account.UpdateChangePasswordResponse response=new Account.UpdateChangePasswordResponse();
        if(!request.newPassword.equals(request.confirmPassword)){
            response.setPropertyErrors("newPassword","new password and confrim password mismatch");

        }

        postDelayed(response);
    }

    private void loginUser(Account.UserResponse response){


        Auth auth=application.getAuth();
        User user=auth.getUser();
        user.setDisplayName("Abhishek Aryan");
        user.setUserName("Abhishek");
        user.setEmail("xyz@gmail.com");
        user.setAvatarUrl("https://www.gravatar.com/avatar/1?d=identicon");
        user.setLoggedIn(true);
        user.setId(123);
        bus.post(new Account.UserDetailsUpdatedEvent(user));
        auth.setAuthToken("fakeToken");
        user.getDisplayName();
        user.getUserName();
        user.getEmail();
        user.getAvatarUrl();
        user.getLoggedIn();
        user.getId();
    }


    @Subscribe
    public void loginWithUserName(final Account.LoginWithUserNameRequest request){

        invokeDeleyed(new Runnable() {
            @Override
            public void run() {

                Account.LoginWithUserNameResponse response=new Account.LoginWithUserNameResponse();

                if (request.userName.equals("Abhishek"))
                    response.setPropertyErrors("username","invalid username or password");
                loginUser(new Account.UserResponse());
                bus.post(response);

            }
        },1000,2000);
    }


    @Subscribe
    public void loginWithExternalToken(final Account.LoginWithExternalTokenRequest request){

        invokeDeleyed(new Runnable() {
            @Override
            public void run() {

                Account.LoginWithExternalTokenResponse response=new  Account.LoginWithExternalTokenResponse();
                loginUser(response);
                bus.post(response);

            }
        },1000,2000);
    }

    @Subscribe
    public void updateGcmRegistration(Account.UpdateGcmRegistrationRequest request){

        postDelayed(new Account.UpdateGcmRegistrationReponse());
    }

    @Subscribe
    public void Register(Account.RegisterRequest request){

        invokeDeleyed(new Runnable() {
            @Override
            public void run() {

                Account.RegisterResponse response=new Account.RegisterResponse();
                loginUser(response);
                bus.post(response);

            }
        },1000,2000);
    }

    @Subscribe
    public void ExternalRegister(Account.RegisterExternalTokenRequest request){

        invokeDeleyed(new Runnable() {
            @Override
            public void run() {
                Account.RegisterExternalTokenResponse response=new Account.RegisterExternalTokenResponse();
                loginUser(response);
                bus.post(response);

            }
        },1000,2000);
    }

    @Subscribe
    public void LoginWithLOcalToken(Account.LoginWithLocalTokenRequest request){

        invokeDeleyed(new Runnable() {
            @Override
            public void run() {
                Account.LoginWithLocalTokenResponse response=new Account.LoginWithLocalTokenResponse();
                loginUser(response);
                bus.post(response);

            }
        },1000,2000);
    }




}
