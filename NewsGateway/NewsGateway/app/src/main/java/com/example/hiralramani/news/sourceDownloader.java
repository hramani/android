package com.example.hiralramani.news;

import android.net.Uri;
import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashMap;


/**
 * Created by hiral ramani on 5/5/2017.
 */


public class sourceDownloader extends AsyncTask<String,Void,String> {
    private static final String TAG = "newsdownld";
    private MainActivity mainActivity;
    private final String nul = "https://newsapi.org/v1/sources?language=en&country=us&apiKey=4545e74fb4cf451ba63610e4f635b7bb";
    private final String categoryUrl = "https://newsapi.org/v1/sources?language=en&country=us";

    private String newsCategory;

    public sourceDownloader(MainActivity ma ) {
        mainActivity = ma;

    }

    @Override
    protected void onPostExecute(String s) {

        HashMap<String,NewsClass> List=parseJSON(s);
        mainActivity.setSources(List);

    }

    @Override
    protected String doInBackground(String... params) {
        Uri.Builder buil;
        if(params[0].equals("all")|params[0].equals(""))
        {  buil = Uri.parse(nul).buildUpon();}
        else {
            buil = Uri.parse(categoryUrl).buildUpon();
            buil.appendQueryParameter("category", params[0]);
            buil.appendQueryParameter("apiKey", "4545e74fb4cf451ba63610e4f635b7bb");
        }
        String urlToUse = buil.build().toString();


        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader((new InputStreamReader(is)));
            String ln;
            while ((ln = rd.readLine()) != null) {
                sb.append(ln).append('\n');
            }


        } catch (Exception e) {

            return null;
        }

        return sb.toString();
    }
    String id;
    String name;
    String category;
    String url;

    private HashMap<String,NewsClass> parseJSON(String s) {
        HashMap<String,NewsClass> clist = new HashMap<>();


        try {

            JSONObject mJsonObject = null, news = null;
            JSONArray newsArry = null;

            mJsonObject = new JSONObject(s);
            newsArry = mJsonObject.getJSONArray("sources");
            for (int i = 0; i < newsArry.length(); i++) {

                news = newsArry.getJSONObject(i);
                id = news.getString("id");

                name = news.getString("name");
                url = news.getString("url");
                category = news.getString("category");
                clist.put(name,new NewsClass(id,name,url,category));
            }
        }
        catch(Exception e)
        {

        }
        return clist;
    }
}