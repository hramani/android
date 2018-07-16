package com.example.hiralramani.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hiral ramani on 3/19/2017.
 */



public class StockDataDownloader extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;
    private int count;
    public String StockName;
    String news;
    Stock stt;
    private final String stockData_Url = "http://finance.google.com/finance/info?client=ig&q=";
    private static final String TAG = "AsyncStockDownload2";


    public StockDataDownloader(MainActivity ma) {
        mainActivity = ma;
    }



    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading Stock_Data ...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        String StockLine = params[0];
        StockName = params[1];
        String s;

        String final_Url_stock_data = stockData_Url+StockLine;
        //System.out.println(final_Url);
        Log.d(TAG,"final URL"+final_Url_stock_data);

        Uri stockName_Uri = Uri.parse(final_Url_stock_data);
        String urlToUse = stockName_Uri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String linee;
            while ((linee = reader.readLine()) != null) {
                sb.append(linee).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());
            s = sb.toString();
            news = s.replace("//","");
            Log.d(TAG, "new String" +news);

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + news.toString());

        return news.toString();

    }
    @Override
    protected void onPostExecute(String s) {

        Stock stockDataList = parseJSON(s);

        mainActivity.financialData(stockDataList);

    }

    private Stock parseJSON(String s) {
        Log.d(TAG,"test1 count"+ s);

        Log.d(TAG,"test2 count"+ s);
        try {
            Log.d(TAG,"test3 count"+ s);

            JSONArray jObjMain = new JSONArray(s);
            Log.d(TAG,"test4 count"+ s);
            count = jObjMain.length();
            Log.d(TAG,"test5 count"+ count);

               for (int i = 0; i < count; i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                String stock_symbol = jStock.getString("t");
                Double stock_price = Double.parseDouble(jStock.getString("l"));
                Double stock_change = Double.parseDouble(jStock.getString("c"));
                Double stock_percentage = Double.parseDouble(jStock.getString("cp"));

               Log.d(TAG,"stockname"+StockName+"   stocksymbol"+stock_symbol);

                stt=new Stock(StockName, stock_symbol, stock_price, stock_change, stock_percentage);
            }

            return stt;
        }
        catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
