package com.example.shrad.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

/**
 * Created by shrad on 4/9/2017.
 */

public class GovAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "CountryAdapter";
    private List<Official> officialList;
    private MainActivity mainAct;


    public GovAdapter(List<Official> officialList, MainActivity ma) {
        this.officialList = officialList;
        mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.officiallist, parent, false);


        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Official official = officialList.get(position);
        holder.office.setText(official.getOffice());
        holder.official_name.setText(official.getName()+"("+official.getParty()+")");
    }
    @Override
    public int getItemCount() {
        return officialList.size();
    }

}