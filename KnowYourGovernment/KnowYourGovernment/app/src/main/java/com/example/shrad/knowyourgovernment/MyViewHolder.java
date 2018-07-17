package com.example.shrad.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shrad on 3/29/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView office;
    TextView official_name;
    ImageView placeholder;

    public MyViewHolder(View itemView) {
        super(itemView);
        office=(TextView)itemView.findViewById(R.id.office1);
        official_name=(TextView)itemView.findViewById(R.id.official_name);
        placeholder =(ImageView) itemView.findViewById(R.id.separator);

    }
}
