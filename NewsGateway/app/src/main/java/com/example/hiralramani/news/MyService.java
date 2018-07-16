package com.example.hiralramani.news;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hiral ramani on 5/5/2017.
 */

public class MyService extends Service {
    private static final String TAG = "mservice";

    private ServiceReceiver sr;

    private List<ArticleClass> articleList=new ArrayList<>();

    private final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    private boolean running = true;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sr = new ServiceReceiver();
        IntentFilter filter1 = new IntentFilter(ACTION_MSG_TO_SERVICE);
        registerReceiver(sr, filter1);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (running) {
                    try {
                        while (articleList.isEmpty())
                        { Thread.sleep(300);}
                        if(!articleList.isEmpty())
                        {
                            Intent intent=new Intent();
                            intent.setAction("ACTION_NEWS_STORY");
                            intent.putParcelableArrayListExtra("ArticleList",(ArrayList<ArticleClass>) articleList);
                            sendBroadcast(intent);
                            articleList.clear();
                            Thread.interrupted();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (running) {


                    }
                }

            }
        }).start();

        return Service.START_STICKY;
    }

    public void setArticle(List<ArticleClass> List)
    {
        articleList.clear();
        articleList.addAll(List);

    }

    @Override
    public void onDestroy() {
        running = false;
        this.unregisterReceiver(sr);

        super.onDestroy();
    }
    class ServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals("ACTION_MSG_TO_SERVICE")) {

                    Bundle bdl = intent.getBundleExtra("Bundle");
                    String id = bdl.getString("Id");
                    new articleDownloader(MyService.this).execute(id);

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}
