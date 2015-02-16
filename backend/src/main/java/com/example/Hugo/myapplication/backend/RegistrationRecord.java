package com.example.Hugo.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The Objectify object model for device registrations we are persisting
 */
@Entity
public class RegistrationRecord {

    @Id
    private String phone;

    @Index
    private String regId;

    public RegistrationRecord() {
    }

    public String getRegId() {
        return regId;
    }
    public String getPhone(){
        return phone;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
}