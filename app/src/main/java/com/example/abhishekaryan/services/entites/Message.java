package com.example.abhishekaryan.services.entites;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;
import java.util.GregorianCalendar;



public class Message implements Parcelable {

    private int id;
    private  Calendar createdAt;
    private String shortMessage;
    private String longMessgae;
    private String url;
    private UserDetails otherUser;
    private boolean isFromUs;
    private boolean isRead;
    private boolean isSelected;

    public Message(int id,
                   Calendar createdAt,
                   String shortMessage,
                   String longMessgae,
                   String url,
                   UserDetails
                           otherUser,
                   boolean isFromUs,
                   boolean isRead) {
        this.id = id;
        this.createdAt = createdAt;
        this.shortMessage = shortMessage;
        this.longMessgae = longMessgae;
        this.url = url;
        this.otherUser = otherUser;
        this.isFromUs = isFromUs;
        this.isRead = isRead;
    }

    private Message(Parcel parcel) {
        id=parcel.readInt();
        createdAt=new GregorianCalendar();
        createdAt.setTimeInMillis(parcel.readLong());
        shortMessage=parcel.readString();
        longMessgae=parcel.readString();
        url=parcel.readString();
        otherUser=(UserDetails)parcel.readParcelable(UserDetails.class.getClassLoader());
        isFromUs=parcel.readByte()==1;
        isRead=parcel.readByte()==1;

    }
    @Override
    public void writeToParcel(Parcel destination, int flags) {

        destination.writeInt(id);
        destination.writeLong(createdAt.getTimeInMillis());
        destination.writeString(shortMessage);
        destination.writeString(longMessgae);
        destination.writeString(longMessgae);
        destination.writeString(url);
        destination.writeParcelable(otherUser,0);
        destination.writeByte((byte)(isFromUs?1:0));
        destination.writeByte((byte)(isRead?1:0));

    }

    public int getId() {
        return id;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public String getLongMessgae() {
        return longMessgae;
    }

    public String getUrl() {
        return url;
    }

    public UserDetails getOtherUser() {
        return otherUser;
    }

    public boolean isFromUs() {
        return isFromUs;
    }

    public boolean isRead() {
        return isRead;
    }
    public void setIsRead(boolean isRead){

        this.isRead=isRead;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
           return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
