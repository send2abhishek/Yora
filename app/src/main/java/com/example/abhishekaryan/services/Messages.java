package com.example.abhishekaryan.services;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.abhishekaryan.services.entites.Message;
import com.example.abhishekaryan.services.entites.UserDetails;
import com.example.abhishekaryan.yora.infrastructure.ServiceResponse;

import java.util.List;

public final class Messages {

    private Messages(){

    }

    public static class DeleteMessageRequest{

        public int MessageId;

        public DeleteMessageRequest(int messageId) {
            MessageId = messageId;
        }
    }

    public static class DeleteMessageResponse extends ServiceResponse{

        public int MessageId;

    }

    public static class SearchMessageRequest{

        public int FromUsConatctId;
        public boolean IncludeSentMessage;
        public boolean IncludeReceiveMessage;

        public SearchMessageRequest(int fromUsConatctId, boolean includeSentMessage, boolean includeReceiveMessage) {
            FromUsConatctId = fromUsConatctId;
            IncludeSentMessage = includeSentMessage;
            IncludeReceiveMessage = includeReceiveMessage;
        }

        public SearchMessageRequest(boolean includeSentMessage, boolean includeReceiveMessage) {

            //here server will ignore FromUsConatctId by setting value FromUsConatctId=-1;
            FromUsConatctId=-1;
            IncludeSentMessage = includeSentMessage;
            IncludeReceiveMessage = includeReceiveMessage;
        }


    }

    public static class SearchMessageResponse extends ServiceResponse {

        public List<Message> Messages;

    }

    public static class sendMessageRequest implements Parcelable{

        private UserDetails recipient;
        private Uri imagePath;
        private String message;

        private sendMessageRequest(Parcel in){

            recipient=in.readParcelable(UserDetails.class.getClassLoader());
            imagePath=in.readParcelable(Uri.class.getClassLoader());
            message=in.readString();

        }
        public sendMessageRequest(){

        }



        @Override
        public void writeToParcel(Parcel out, int flags) {

            out.writeParcelable(recipient,0);
            out.writeParcelable(imagePath,0);
            out.writeString(message);

        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static Creator<sendMessageRequest> CREATOR = new Creator<sendMessageRequest>() {
            @Override
            public sendMessageRequest createFromParcel(Parcel in) {
                return new sendMessageRequest(in);
            }

            @Override
            public sendMessageRequest[] newArray(int size) {
                return new sendMessageRequest[size];
            }
        };


        public UserDetails getRecipient() {
            return recipient;
        }

        public void setRecipient(UserDetails recipient) {
            this.recipient = recipient;
        }

        public Uri getImagePath() {
            return imagePath;
        }

        public void setImagePath(Uri imagePath) {
            this.imagePath = imagePath;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class sendMessageRespons extends ServiceResponse{

        //this is for actually copy of messages actually sever sends back copy of server sends messages

        public Message message;

    }

    public static class MarkMessageAsReadRequest{

        public int MessageId;

        public MarkMessageAsReadRequest(int messageId) {
            MessageId = messageId;
        }
    }

    public static class MarkMessageAsReadResponse extends ServiceResponse{
    }

    public static class GetMessageDetailsRequest{

        public int id;

        public GetMessageDetailsRequest(int id) {
            this.id = id;
        }
    }
    public static class GetMessageDetailsResponse extends ServiceResponse{
        public Message message;
    }








}
