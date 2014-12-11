package com.example.hugo.syms;

/**
 * Created by Hugo on 11/12/2014.
 */
public class BusMessage {
    private Kid mKid;
    private boolean canceled = true;

    public BusMessage(Kid mKid, boolean canceled) {
        this.mKid = mKid;
        this.canceled = canceled;
    }

    public Kid getmKid() {
        return mKid;
    }

    public void setmKid(Kid mKid) {
        this.mKid = mKid;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
