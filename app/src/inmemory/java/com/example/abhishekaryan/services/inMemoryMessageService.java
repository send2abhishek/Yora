package com.example.abhishekaryan.services;

import android.view.View;
import android.view.ViewGroup;

import com.example.abhishekaryan.services.entites.Message;
import com.example.abhishekaryan.services.entites.UserDetails;
import com.example.abhishekaryan.yora.infrastructure.YoraApplication;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class inMemoryMessageService extends BaseInMemoryService {

    protected inMemoryMessageService(YoraApplication application) {
        super(application);
    }

    @Subscribe
    public void deleteMessage(Messages.DeleteMessageRequest request){

        Messages.DeleteMessageResponse response=new Messages.DeleteMessageResponse();
        response.MessageId=request.MessageId;
        postDelayed(response);

    }

    @Subscribe
    public void searchMessage(Messages.SearchMessageRequest request){

        Messages.SearchMessageResponse response=new Messages.SearchMessageResponse();
        response.Messages=new ArrayList<>();

        UserDetails[] users=new UserDetails[10];
        for (int i=0; i<users.length;i++){
            String stringId=Integer.toString(i);
            users[i]=new UserDetails(

                    i,
                    true,
                    "User" + stringId,
                    "User" + stringId,
                    "http://www.gravatar.com/avatar" + stringId + "?d=identicon"
            );
        }

        Random random=new Random();
        Calendar date=Calendar.getInstance();
        date.add(Calendar.DATE,-100);

        for (int i=0;i<100;i++){
            boolean isFromUs;
            if(request.IncludeReceiveMessage && request.IncludeSentMessage){
                isFromUs=random.nextBoolean();
            }
            else{
                isFromUs=!request.IncludeReceiveMessage;
            }
            date.set(Calendar.MINUTE,random.nextInt(60 * 24));
            String numberString=Integer.toString(i);
            response.Messages.add(new Message(
                    i,
                    (Calendar)date.clone(),
                    "short Message" + numberString,
                    "Long Message " + numberString,
                    "",
                    users[random.nextInt(users.length)],
                    isFromUs,
                    i>4
            ));
        }
        postDelayed(response,2000);
    }

    @Subscribe

    public void sendMessage(Messages.sendMessageRequest request){

        Messages.sendMessageRespons respons=new Messages.sendMessageRespons();
        if(request.getMessage().equals("Error")){
            respons.setOperationError("Something Bad Happen");
        }
        else if (request.getMessage().equals("error-message")){
            respons.setPropertyErrors("message","Invalid-length");
        }
        postDelayed(respons,1500,3000);


    }

    @Subscribe
    public void markMessageRead(Messages.MarkMessageAsReadRequest request){

        postDelayed(new Messages.MarkMessageAsReadResponse());
    }
    @Subscribe
    public void getMessageDetails(Messages.GetMessageDetailsRequest request){

       Messages.GetMessageDetailsResponse response=new Messages.GetMessageDetailsResponse();
        response.message=new Message(
                1,
                Calendar.getInstance(),
                "Short Message",
                "Long Message",
                null,
                new UserDetails(1,true,"Display Name","UserName",""),
                false,
                false);

        postDelayed(response);
    }
}
