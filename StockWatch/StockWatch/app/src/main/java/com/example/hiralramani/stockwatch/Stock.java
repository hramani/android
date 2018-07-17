package com.example.hiralramani.stockwatch;
/**
 * Created by hiral ramani on 3/7/2017.
 */

public class Stock {


    private String symbol;
    private String name;
    private Double price;
    private Double changeAmount;
    private Double percentage;




    public Stock(String name, String symbol) {
        this.symbol =symbol;

        this.name =name;

    }

    public Stock(String name, String symbol,Double price,Double changeAmount,Double percentage) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.changeAmount = changeAmount;
        this.percentage = percentage;

    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setChangeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Stock() {

    }

    public String getSymbol() {
        return symbol;
    }

    public Double getPrice() {
        return price;
    }

    public Double getChangeAmount() {
        return changeAmount;
    }

    public String getName() {
        return name;
    }

    public Double getPercentage() {
        return percentage;
    }



    @Override
    public String toString() {
        return name + " (" + symbol+ "), " + price +""+ changeAmount +""+name;
    }
}