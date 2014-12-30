package com.example.hugo.syms.clientData;

/**
 * Created by Hugo on 26/12/2014.
 */
public class Notification {
    private String icon;
    private String title;
    private String text;
    private String type;
    private long _id;
    public Notification(String icon, String title, String text){
        this.icon = icon;
        this.title = title;
        this.text = text;
    }
    public Notification(String title, String text){
        this.icon = "ic_action_new";
        this.title = title;
        this.text = text;
    }
    public Notification(long _id,String icon, String title, String text, String type){
        this._id = _id;
        this.icon = icon;
        this.title = title;
        this.text = text;
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_id() {
        return _id;
    }

    public String getType() {
        return type;
    }
}
