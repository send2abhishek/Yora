package com.example.abhishekaryan.services;

import com.example.abhishekaryan.services.entites.ConatctRequest;
import com.example.abhishekaryan.services.entites.UserDetails;
import com.example.abhishekaryan.yora.infrastructure.ServiceResponse;

import java.util.List;

public final class Contacts {

    private Contacts(){

    }

    public static class GetContactRequestRequest{

        /* responsible to get contact Request request, from us or other contacts
        * display the contact request(Someone has sent) */



        public boolean FromUs;

        public GetContactRequestRequest(boolean fromUs) {
            FromUs = fromUs;
        }
    }
    public static class GetContactRequestResponse extends ServiceResponse{

         /* respose class to the GetContactRequestRequest
         *  display the total contacts
         * */

         public List<ConatctRequest> Requests;

    }
    public static class GetContactRequest{

        /* responsible for getting list contacts(total contcats including pending request Contacts */

        public boolean IncludePendingContacts;

        public GetContactRequest(boolean includePendingContacts) {
            IncludePendingContacts = includePendingContacts;
        }
    }

    public static class GetContactResponse extends ServiceResponse{

         /* respose to the class GetContactRequest */

         public List<UserDetails> contacts;

    }

    public static class sendContactRequestRequest{

        /* Api calls to send the contact request to other contact */

        public int UserId;

        public sendContactRequestRequest(int userId) {
            UserId = userId;
        }
    }

    public static class sendContactRequestResponse extends ServiceResponse{


    }

    public static class RespondToContactRequestRequest{

        /* Api calls for when you get the contact request from any users */

        public int ContactRequestId;
        public boolean Accept;

        public RespondToContactRequestRequest(int contactRequestId, boolean accept) {
            ContactRequestId = contactRequestId;
            Accept = accept;
        }
    }

    public static class RespondToContactRequestResponse extends ServiceResponse{


    }

    public static class RemoveContactRequest{

        public int contactId;

        public RemoveContactRequest(int contactId) {
            this.contactId = contactId;
        }
    }

    public  static class RemoveContactIdResponse extends ServiceResponse{

        int removeConatctId;
    }

    public static class searchUserRequest{

        public String Query;

        public searchUserRequest(String query) {
            Query = query;
        }
    }
    public static class searchUserResponse extends ServiceResponse{
        public List<UserDetails> users;
        public String Query;
    }



}
