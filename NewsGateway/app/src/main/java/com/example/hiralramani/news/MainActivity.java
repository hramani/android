package com.example.hiralramani.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private mypgadpt pdp;
    private final String  ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    private List<Fragment> fgs;
    private ViewPager pager;
    private DrawerLayout dlout;
    private ListView dlistt;
    private ActionBarDrawerToggle dt;
    private ArrayList<String> items = new ArrayList<>();
    HashMap<String, NewsClass> sourceList = new HashMap<>();
    private ArrayAdapter dAdapter;
    private NewsReceiver yourReceiver;
    private final String TAG = "Mainactivity";
    private final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);

        yourReceiver = new NewsReceiver();
        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(yourReceiver, filter1);

        dlout = (DrawerLayout) findViewById(R.id.drawerID);
        dlistt = (ListView) findViewById(R.id.left_drawer);
        dAdapter = new ArrayAdapter<>(this, R.layout.drawerlist, items);
        dlistt.setAdapter(dAdapter);
        dlistt.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pager.setBackground(null);

                        selectItem(position);
                    }
                }
        );


        dt = new ActionBarDrawerToggle(
                this,
                dlout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        fgs = getFragments();

        pdp = new mypgadpt(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pdp);


        new sourceDownloader(this).execute("");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        this.unregisterReceiver(this.yourReceiver);
    }

    void setSources(HashMap<String, NewsClass> List) {
        sourceList.clear();
        items.clear();
        if (List.isEmpty()) {
            items.add(0, "all");
        } else {
            sourceList = List;
            for (String key : sourceList.keySet()) {
                items.add(key);

            }
        }
        dAdapter.notifyDataSetChanged();
    }


    public boolean checkConnection(Context Context) {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Check Network Connection");
            builder.setTitle("No Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }

    }

    private void selectItem(int position) {
        Toast.makeText(this, items.get(position), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.setAction(ACTION_MSG_TO_SERVICE);
        String key = items.get(position);
        NewsClass news = sourceList.get(key);
        setTitle(news.getName());
        Bundle b = new Bundle();

        b.putString("Id", news.getId());  b.putString("category", news.gettype()); b.putString("name", news.getName());


        intent.putExtra("Bundle", b);
        sendBroadcast(intent);

        dlout.closeDrawer(dlistt);
    }

    private void reDoFragments(List<ArticleClass> op) {
        for (int i = 0; i < pdp.getCount(); i++)
            pdp.notifyChangeInPosition(i);
        fgs.clear();

        int count = (int) (Math.random() * 8 + 2);

        for (int i = 0; i < count; i++) {

            fgs.add(Fragments.newInstance(op.get(i).gets(), op.get(i).getaut(), op.get(i).gett(), op.get(i).getd(), op.get(i).getuimg(), op.get(i).getUrl(), op.get(i).getdt(), (i + 1) + " of " + count));

            pdp.notifyChangeInPosition(i);
        }

        pdp.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        dt.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        dt.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (dt.onOptionsItemSelected(item)) {

            return true;
        }

        switch (item.getItemId()) {
            case R.id.all:
                if (checkConnection(getApplicationContext())) {

                    new sourceDownloader(this).execute("all");
                }
                return true;
            
           
           
            case R.id.music:
                if (checkConnection(getApplicationContext())) {
                    new sourceDownloader(this).execute("music");
                }
                return true;
            case R.id.entertainment:
                if (checkConnection(getApplicationContext())) {
                    new sourceDownloader(MainActivity.this).execute("entertainment");
                }
                return true;
            case R.id.business:
                if (checkConnection(getApplicationContext())) {
                    new sourceDownloader(this).execute("business");
                }
                return true;
            case R.id.general:
                if (checkConnection(getApplicationContext())) {
                    new sourceDownloader(this).execute("general");
                }
                return true;
            case R.id.politics:
                if (checkConnection(getApplicationContext())) {
                    new sourceDownloader(this).execute("politics");
                }
                return true;
            case R.id.gaming:
                if (checkConnection(getApplicationContext())) {
                    new sourceDownloader(this).execute("gaming");
                }
                return true;
            case R.id.technology:
                if (checkConnection(getApplicationContext())) {
                    new sourceDownloader(this).execute("technology");
                }
                return true;
            case R.id.sport:
                if (checkConnection(getApplicationContext())) {
                    new sourceDownloader(this).execute("sport");
                }
                return true;
            case R.id.scinnature:
                if (checkConnection(getApplicationContext())) {
                    new sourceDownloader(this).execute("science-and-nature");
                }
                return true;
            

            default:
                return super.onOptionsItemSelected(item);

        }



    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        return fList;
    }

    private class mypgadpt extends FragmentPagerAdapter {
        private long bd = 0;


        public mypgadpt(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fgs.get(position);
        }

        @Override
        public int getCount() {
            return fgs.size();
        }

        @Override
        public long getItemId(int position) {

            return bd + position;
        }


        public void notifyChangeInPosition(int n) {

            bd += getCount() + n;

        }

    }

    class NewsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (action.equals("ACTION_NEWS_STORY")) {
                    List<ArticleClass> list = intent.getParcelableArrayListExtra("ArticleList");
                    reDoFragments(list);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
