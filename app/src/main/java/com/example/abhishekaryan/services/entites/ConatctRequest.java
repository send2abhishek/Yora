package com.example.abhishekaryan.services.entites;

import java.util.Calendar;

public class ConatctRequest {


    private boolean isFromUs;
    private UserDetails user;
    private Calendar createdAt;

    public ConatctRequest( boolean isFromUs, UserDetails user, Calendar createdAt) {
        this.isFromUs = isFromUs;
        this.user = user;
        this.createdAt = createdAt;
    }



    public boolean isFromUs() {
        return isFromUs;
    }

    public UserDetails getUser() {
        return user;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }
}
