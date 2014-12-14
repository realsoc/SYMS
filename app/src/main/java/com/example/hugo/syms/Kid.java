package com.example.hugo.syms;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Hugo on 09/12/2014.
 */
public class Kid implements Parcelable{
    private InputStream picturePath;
    private String name;
    private String number;
    private Date birthday;

    public Kid(String name, String number, InputStream picturePath){
        birthday = new Date();
        this.name = name;
        this.number = number;
        this.picturePath  = picturePath;
    }

    public Kid(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName(){
        return this.name;
    }
    public String getNumber(){
        return this.number;
    }

    public Date getBirthday() {
        return birthday;
    }

    public InputStream getPicture() {
        return picturePath;
    }

    public void setName(String newName){
        name = newName;
    }
    public void setPicture(InputStream newPicture){
        picturePath = newPicture;
    }

    public void setBirthday(Date newBirthday){
        this.birthday = newBirthday;
    }

    public boolean equals(Kid kidToCompare){
        boolean ret = false;
        if(PhoneNumberUtils.compare(kidToCompare.getNumber(), number)){
            ret = true;
        }
        return ret;
    }
    public int getIndexInArrayList(ArrayList<Kid> toCompare){
        int ret = 0;
        boolean found = false;
        for(Kid currentKid : toCompare){
            if(this.equals(currentKid)){
                found = true;
                break;
            }
            ret++;
        }
        if(!found){
            ret = -1;
        }
        return ret;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
