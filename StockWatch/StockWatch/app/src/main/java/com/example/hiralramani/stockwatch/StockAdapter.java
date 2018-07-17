package com.example.hiralramani.stockwatch;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by hiral ramani on 3/7/2017.
 */

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder> {

    private static final String TAG = "StockAdapter";
    private List<Stock> stockDataList;
    private MainActivity mainAct;

    public StockAdapter(List<Stock> stockDataList, MainActivity ma) {
        this.stockDataList = stockDataList;
        mainAct = ma;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_row_list, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, final int position) {

        Stock stock = stockDataList.get(position);
        Double chngamnt = stock.getChangeAmount();
        Double percentage = stock.getPercentage();
        String chngamntinString = percentage.toString();
        int size = chngamntinString.length();

        String changeAmount;
        String changeAmountoo = chngamntinString.substring(1,size);

        if(chngamnt > 0)
        {

            changeAmount = "▲"+chngamntinString+"(%"+changeAmountoo.toString()+")";
            holder.symbol.setText(stock.getSymbol());
            holder.symbol.setTextColor(Color.GREEN);
            holder.price.setText(stock.getPrice().toString());
            holder.price.setTextColor(Color.GREEN);
            holder.changeAmount.setText(changeAmount);
            holder.changeAmount.setTextColor(Color.GREEN);
            holder.name.setText(stock.getName());
            holder.name.setTextColor(Color.GREEN);
        }
        else
        {
            changeAmount = "▼"+chngamntinString+"(%"+changeAmountoo.toString()+")";
            holder.symbol.setText(stock.getSymbol());
            holder.symbol.setTextColor(Color.RED);
            holder.price.setText(stock.getPrice().toString());
            holder.price.setTextColor(Color.RED);
            holder.changeAmount.setText(changeAmount);
            holder.changeAmount.setTextColor(Color.RED);
            holder.name.setText(stock.getName());
            holder.name.setTextColor(Color.RED);
        }




        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainAct.deleteSingleStock(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockDataList.size();
    }
}
