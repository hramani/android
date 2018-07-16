package com.example.hiralramani.multinote;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hiral ramani on 2/25/2017.
 */


public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView date;
    public TextView notecontent;

    public MyViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.titleid);
        date = (TextView) view.findViewById(R.id.dateid);
        notecontent = (TextView) view.findViewById(R.id.notecontentid);


    }

}