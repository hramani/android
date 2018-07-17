package com.example.hiralramani.multinote;

import java.io.Serializable;

/**
 * Created by hiral ramani on 2/25/2017.
 */

public class Note implements Serializable{
    private String date;
    private String note;
    private String title;

    public  Note(String title,String note,String time)
    {
        this.title=title;
        this.note = note;
        this.date = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString() {
        return date + ": " + note;
    }
}
