package com.example.abhishekaryan.services.entites;


import android.os.Parcel;
import android.os.Parcelable;

public class UserDetails implements Parcelable {

    private final int id;
    private final boolean isContact;
    private final String displayName;
    private final String userName;
    private final String avatarUrl;

    public UserDetails(int id, boolean isContact, String displayName, String userName, String avatarUrl) {
        this.id = id;
        this.isContact = isContact;
        this.displayName = displayName;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
    }

    private UserDetails(Parcel parcel){

       id=parcel.readInt();
        isContact=parcel.readByte()==1;
        displayName=parcel.readString();
        userName=parcel.readString();
        avatarUrl=parcel.readString();

    }
    @Override
    public void writeToParcel(Parcel destination, int flags) {
        //this method used for save the data when user clicks home button

        destination.writeInt(id);
        destination.writeByte((byte) ( isContact ? 1: 0));
        destination.writeString(displayName);
        destination.writeString(userName);
        destination.writeString(avatarUrl);

    }

    @Override
    public int describeContents() {
        return 0;
    }
    public int getId() {
        return id;
    }

    public boolean isContact() {
        return isContact;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }





    public static final Creator<UserDetails> CREATOR=new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel source) {
            return new UserDetails(source);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };


}
