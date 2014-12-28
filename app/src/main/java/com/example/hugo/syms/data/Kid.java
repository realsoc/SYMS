package com.example.hugo.syms.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;

import java.util.ArrayList;

/**
 * Created by Hugo on 09/12/2014.
 */
public class Kid implements Parcelable{
    private long _id;
    private String picture;
    private String name;
    private String number;

    public Kid(Parcel in){
        _id = in.readLong();
        picture = in.readString();
        name = in.readString();
        number = in.readString();
    }
    public Kid(String name, String number){
        this.name = name;
        this.number = number;
    }
    public Kid(String name, String number, String picture){
        this.name = name;
        this.number = number;
        this.picture = picture;
    }
    public Kid(long _id, String name, String number, String picture){
        this.name = name;
        this.number = number;
        this.picture = picture;
        this._id = _id;
    }

    public String getName(){
        return this.name;
    }
    public String getNumber(){
        return this.number;
    }


    public String getPicture() {
        return picture;
    }

    public void setName(String newName){
        name = newName;
    }
    public void setPicture(String newPicture){
        picture = newPicture;
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

        dest.writeLong(_id);
        dest.writeString(picture);
        dest.writeString(name);
        dest.writeString(number);
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

}
