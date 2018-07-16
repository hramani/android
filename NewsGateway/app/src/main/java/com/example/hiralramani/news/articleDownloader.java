package com.example.hiralramani.news;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hiral ramani on 5/5/2017.
 */


public class articleDownloader extends AsyncTask<String,Void,String> {
    private static final String TAG = "nss";
    private MyService newsService;
    private final String newsURL = "https://newsapi.org/v1/articles?source=";



    public articleDownloader(MyService ma ) {
        newsService = ma;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("OnPostexecute Result: " + s);
        List<ArticleClass> List=parseJSON(s);
        newsService.setArticle(List);

    }

    @Override
    protected String doInBackground(String... params) {
        Uri.Builder bl;

        String newURL=newsURL+""+params[0];
        bl = Uri.parse(newURL).buildUpon();

        bl.appendQueryParameter("apiKey", "4545e74fb4cf451ba63610e4f635b7bb");
        String urlToUse = bl.build().toString();


        StringBuilder sb = new StringBuilder();
        try {
            URL ul = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) ul.openConnection();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();

            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }


        } catch (Exception e) {

            return null;
        }

        return sb.toString();
    }

    private List<ArticleClass> parseJSON(String s) {
        List<ArticleClass> List = new ArrayList<>();
        try {

            JSONObject mJsonObject = null, news = null;
            JSONArray arry = null;
            String author;
            String title;String date;
            String source;
            String desc;
            String uimgg;

            String url;

            mJsonObject = new JSONObject(s);

            source=mJsonObject.getString("source");

            arry = mJsonObject.getJSONArray("articles");

            for (int i = 0; i < arry.length(); i++) {

                news = arry.getJSONObject(i);
                author = news.getString("author");title = news.getString("title");
                url=news.getString("url");
                desc = news.getString("description");date=news.getString("publishedAt"); uimgg = news.getString("urlToImage");



                List.add(new ArticleClass(source,author,title,desc,uimgg,url,date));
            }
        }
        catch(Exception e)
        {

        }
        return List;
    }}
