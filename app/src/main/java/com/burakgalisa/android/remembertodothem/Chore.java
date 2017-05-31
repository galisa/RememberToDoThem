package com.burakgalisa.android.remembertodothem;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Burak on 21.5.2017.
 */

public class Chore {


    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mDone;


    public Chore(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getUUID(){
        return mId;
    }


    public String getTitle() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public boolean isDone() {
        return mDone;
    }
}
