package com.example.abhishekaryan.services;

import android.net.Uri;

import com.example.abhishekaryan.yora.infrastructure.ServiceResponse;
import com.example.abhishekaryan.yora.infrastructure.User;

public final class Account {

    public Account(){

    }

    public static class changeAvatarRequest{

        public Uri NewAvatarUri;
        public changeAvatarRequest(Uri newAvatarUri){

            NewAvatarUri=newAvatarUri;
        }
    }

    public static class changeAvatarResponse extends ServiceResponse{

        //in response server will respose back as avatar url
        public String avatarUrl;

    }

    public static class UpdateProfileRequest{

        public String DisplayName;
        public String Email;

     public UpdateProfileRequest(String displayName, String email){


         DisplayName=displayName;
         Email=email;

     }


    }
    public static class UpdateProfileResponse extends ServiceResponse{
        //in response server will respose back as displayName and email
        public String DisplayName;
        public String Email;
    }

    public static class UpdateChangePasswordRequest{

        public String currentPassword;
        public String newPassword;
        public String confirmPassword;


        public UpdateChangePasswordRequest(String currentPassword, String newPassword, String confirmPassword) {
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
            this.confirmPassword = confirmPassword;
        }
    }

    public static class UpdateChangePasswordResponse extends ServiceResponse{

    }


    public static class UserResponse extends ServiceResponse{

        public int id;
        public String DisplayName;
        public String UserName;
        public String Email;
        public String AvatarUrl;
        public String Autotoken;
        public boolean HasPassword;

    }

    public static class LoginWithUserNameRequest{

        public String userName;
        public String password;

        public LoginWithUserNameRequest(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }
    }

    public static class LoginWithUserNameResponse extends ServiceResponse{

    }

    public static class LoginWithLocalTokenRequest{

        public String AuthToken;


        public LoginWithLocalTokenRequest(String authToken) {
            AuthToken = authToken;
        }
    }
    public static class LoginWithLocalTokenResponse extends UserResponse{

    }

    public static class LoginWithExternalTokenRequest{
        public String provider;
        public String Token;
        public String ClientId;

        public LoginWithExternalTokenRequest(String provider, String token) {
            this.provider = provider;
            Token = token;
            ClientId="Android";
        }
    }

    public static class LoginWithExternalTokenResponse extends UserResponse{

    }

    public static class RegisterRequest{
        public String userName;
        public String password;
        public String Email;
        public String ClientId;

        public RegisterRequest(String userName, String password, String email) {
            this.userName = userName;
            this.password = password;
            Email = email;
            ClientId="Android";
        }
    }

    public static class RegisterResponse extends UserResponse{

    }

    public static class RegisterExternalTokenRequest{
        public String userName;
        public String password;
        public String Email;
        public String ClientId;

        public RegisterExternalTokenRequest(String userName, String password, String email) {
            this.userName = userName;
            this.password = password;
            Email = email;
            ClientId="Android";
        }
    }

    public static class RegisterExternalTokenResponse extends UserResponse{

    }


    public static class UserDetailsUpdatedEvent{

        public User user;

        public UserDetailsUpdatedEvent(User user) {
            this.user = user;
        }
    }


    //this below class for live webserver APi

    public static class UpdateGcmRegistrationRequest{

        public String RegistrationId;

        public UpdateGcmRegistrationRequest(String registrationId) {
            RegistrationId = registrationId;
        }
    }

    public static class UpdateGcmRegistrationReponse extends ServiceResponse{

    }




}
