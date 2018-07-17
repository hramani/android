package com.example.hiralramani.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


/**
 * Created by hiral ramani on 3/7/2017.
 */

public class StockViewHolder extends RecyclerView.ViewHolder {

    public TextView symbol;
    public TextView price;
    public TextView changeAmount;
    public TextView name;

    public StockViewHolder(View view) {

        super(view);

        symbol = (TextView) view.findViewById(R.id.symbolID);
        price = (TextView) view.findViewById(R.id.priceID);
        changeAmount = (TextView) view.findViewById(R.id.changeAmountID);
        name = (TextView) view.findViewById(R.id.stockNameID);
    }
}
