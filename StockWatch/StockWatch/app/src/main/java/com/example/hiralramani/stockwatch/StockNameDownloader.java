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
 * Created by hiral ramani on 3/18/2017.
 */

public class StockNameDownloader extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;
    private int count;

    private final String stockName_Url = "http://stocksearchapi.com/api/?api_key=507aa3b491a69a99978e35492d322f32ee4c6d78&search_text=";
    private static final String TAG = "AsyncStockDownload";

    public StockNameDownloader(MainActivity ma) {
        mainActivity = ma;
    }



    @Override
        protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading Stock_Name Data...", Toast.LENGTH_SHORT).show();
        }

    @Override
    protected String doInBackground(String... params) {
        String Stockname_Initials = params[0];
        String final_Url = stockName_Url+Stockname_Initials;
        //System.out.println(final_Url);
        Log.d(TAG,"final URL"+final_Url);

        Uri stockName_Uri = Uri.parse(final_Url);
        String urlToUse = stockName_Uri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());

        return sb.toString();

    }
    @Override
    protected void onPostExecute(String s) {

        ArrayList<Stock> stockList = parseJSON(s);
      //  Toast.makeText(mainActivity, "OnPost Execute Method...empty", Toast.LENGTH_SHORT).show();


     /*   if(stockList == null)
        {
            Toast.makeText(mainActivity, "OnPost Execute Method... empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(mainActivity, "OnPost Execute Method..not .empty", Toast.LENGTH_SHORT).show();
        } */
        mainActivity.displyInDialogueBox(stockList);

    }

    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();


            for (int i = 0; i < count; i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                String stock_name = jStock.getString("company_name");
                String stock_symbol = jStock.getString("company_symbol");
                Log.d(TAG,"stockname"+stock_name+"   stocksymbol"+stock_symbol);

                stockList.add(
                        new Stock(stock_name, stock_symbol));
            }

            return stockList;
        }
        catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    }
