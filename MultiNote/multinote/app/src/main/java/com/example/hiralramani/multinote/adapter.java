package com.example.hiralramani.multinote;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.R.attr.id;
import static android.R.attr.key;
import static android.R.attr.value;
import static android.R.id.list;

/**
 * Created by hiral ramani on 2/25/2017.
 */

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {

    private MainActivity mainAct;
    View itemView;


    public List<Note> ls;

    public adapter(List<Note> ls, MainActivity ma) {
        this.ls = ls;
        this.mainAct = ma;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
//        itemView.setOnClickListener(mainAct);
//        itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
////                mainAct.deleteSingleNote();
//                return true;
//            }
//        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Note ntt = ls.get(position);
        Log.d("adapter", "ntt.getTitle() ==>" + ntt.getTitle());
        holder.title.setText(ntt.getTitle());
        holder.date.setText(ntt.getDate());
        String note_string = ntt.getNote();
        if (note_string.length() > 80) {
            note_string = note_string.substring(0, 80);
            note_string = note_string + "...";
        }
        holder.notecontent.setText(note_string);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainAct.deleteSingleNote(position);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView date;
        public TextView notecontent;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleid);
            date = (TextView) view.findViewById(R.id.dateid);
            notecontent = (TextView) view.findViewById(R.id.notecontentid);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(mainAct, noteactivity.class);
                    i.putExtra("title", ls.get(getAdapterPosition()).getTitle());
                    i.putExtra("position", getAdapterPosition());
                    i.putExtra("note", ls.get(getAdapterPosition()).getNote());
                    i.putExtra("date", ls.get(getAdapterPosition()).getDate());
                    mainAct.startActivityForResult(i, 1);


                }
            });

        }

    }
}
