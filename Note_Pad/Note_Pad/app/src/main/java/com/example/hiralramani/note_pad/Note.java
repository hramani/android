package com.example.hiralramani.note_pad;

/**
 * Created by hiral ramani on 2/12/2017.
 */


public class Note {

    private String date;
    private String note;

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
