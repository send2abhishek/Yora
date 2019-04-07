package com.example.abhishekaryan.services;


import com.example.abhishekaryan.services.entites.ConatctRequest;
import com.example.abhishekaryan.services.entites.UserDetails;
import com.example.abhishekaryan.yora.infrastructure.YoraApplication;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class InMemoryContactService extends BaseInMemoryService {
    public InMemoryContactService(YoraApplication application) {
        super(application);
    }


    @Subscribe
    public void getContactRequests(Contacts.GetContactRequestRequest request){

         /* responsible to get contact Request request, from us or other contacts
        * display the contact request(Someone has sent) */

        Contacts.GetContactRequestResponse response=new Contacts.GetContactRequestResponse();

        response.Requests=new ArrayList<>();

        for (int i=0;i<3;i++) {
            response.Requests.add(new ConatctRequest(request.FromUs,createFakeUser(i,false),new GregorianCalendar()));
        }
        postDelayed(response);
    }

    @Subscribe
    public void getContacts(Contacts.GetContactRequest request){

        Contacts.GetContactResponse response=new Contacts.GetContactResponse();
        response.contacts=new ArrayList<>();
        for (int i=0;i<10;i++){

            response.contacts.add(createFakeUser(i,true));
        }
        postDelayed(response);
    }

    @Subscribe
    public void sendContactRequest(Contacts.sendContactRequestRequest request){



        if(request.UserId==2){
            Contacts.sendContactRequestResponse response=new  Contacts.sendContactRequestResponse();
            response.setOperationError("Something bad happen");
            postDelayed(response);

        }
        else{
            postDelayed(new  Contacts.sendContactRequestResponse());
        }
    }

    @Subscribe
    public void respondToContactRequest(Contacts.RespondToContactRequestRequest request){

        postDelayed(new Contacts.RespondToContactRequestResponse());
    }


    @Subscribe
    public void onRemoveContactRequest(Contacts.RemoveContactRequest request){

        Contacts.RemoveContactIdResponse response=new Contacts.RemoveContactIdResponse();
        response.removeConatctId=request.contactId;
        postDelayed(response);

    }



    private UserDetails createFakeUser(int id, boolean isContact){

        String idString=Integer.toString(id);

        return new UserDetails(

                id,
                isContact,
                "contact" +idString,
                "contact" +idString,
                "https://gravatar.com/avatar/c168f265a4d6f26b5a7a00212a2fe47d?s=400&d=robohash&r=x"

        );
    }

    @Subscribe
    public void serchUsers(Contacts.searchUserRequest request){

        Contacts.searchUserResponse response=new  Contacts.searchUserResponse();
        response.users=new ArrayList<>();
        response.Query=request.Query;
        for(int i=0;i<request.Query.length();i++){

            response.users.add(createFakeUser(i,false));

        }
        postDelayed(response,2000,3000);
    }


}
