package com.example.hiralramani.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {
    private List<Stock> stockList = new ArrayList<>();  // Main content is here
    private List<Stock> stockDataList = new ArrayList<>();

    private RecyclerView recyclerView; // Layout's recyclerview
    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout

    private StockAdapter stockadapter;
    private static final String TAG = "MainActivity";
  //  public DatabaseHandler dbObject;
    ArrayList<Stock> tempObjeclist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        stockadapter = new StockAdapter(stockDataList, this);

        recyclerView.setAdapter(stockadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);


            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    doRefresh();
                }
            });
            DatabaseHandler.getInstance(this).setupDb();

    }

    protected void onResume(){

        doRefresh();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.buttonmenu:
                // Toast.makeText(this,"button menu pressed",Toast.LENGTH_SHORT).show();
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                    Toast.makeText(this, "You ARE Connected to the Internet!", Toast.LENGTH_LONG).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    final EditText stockName = new EditText(MainActivity.this);
                    stockName.setInputType(InputType.TYPE_CLASS_TEXT);
                    stockName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                    stockName.setGravity(Gravity.CENTER);


                    stockName.setInputType(InputType.TYPE_CLASS_TEXT);
                    stockName.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setView(stockName);
                    //   builder.setIcon(R.drawable.icon1);

                    AlertDialog.Builder ok = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            final String EnteredStockName = stockName.getText().toString();
                            Log.d(TAG, "stock_name ==> " + EnteredStockName);
                          //Toast.makeText(MainActivity.this, "you wrote" + EnteredStockName, Toast.LENGTH_LONG).show();
                            boolean fg = false;
                            for(int i=0;i<stockDataList.size();i++) {
                                if (stockDataList.get(i).getSymbol().equalsIgnoreCase(EnteredStockName))
                                {
                                    fg = true;
                                }
                            }
                            if(fg)
                            {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Duplicate Stock does not allow");
                                builder.setMessage("you entered duplicate stock, please enter unique stock value");

                                AlertDialog alertDialogObject = builder.create();

                                alertDialogObject.show();
                            }
                            else {
                                new StockNameDownloader(MainActivity.this).execute(EnteredStockName);
                            }


                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.dismiss();
                        }
                    });

                    builder.setMessage("Please enter a Stock name:");
                    builder.setTitle("Stock Name");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("No Network connection");
                    builder.setMessage("Stock can not be added without a network connection");

                    //Create alert dialog object via builder
                    AlertDialog alertDialogObject = builder.create();

                    //Show the dialog
                    alertDialogObject.show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doRefresh() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            tempObjeclist = DatabaseHandler.getInstance(MainActivity.this).loadStocks();
            stockDataList.clear();
            stockadapter.notifyDataSetChanged();

            Stock sto =null;
            String name_symbol;

            String part1;
            String part2;
            List<String> l = new ArrayList<>();
            for (int i=0;i<tempObjeclist.size();i++) {
                sto = tempObjeclist.get(i);
                String stcName = sto.getName();
                String stcSymbol = sto.getSymbol();
                name_symbol = stcSymbol + "=>" + stcName;
                l.add(name_symbol);


                String[] partall = name_symbol.split("=>");
                part1 = partall[0];
                part2 = partall[1];

                new StockDataDownloader(MainActivity.this).execute(part1, part2);
                //Selected item in listview
            }
            Collections.shuffle(stockList);

            stockadapter.notifyDataSetChanged();
            swiper.setRefreshing(false);
            Toast.makeText(this, "List content shuffled", Toast.LENGTH_SHORT).show();

        }
        else
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network connection");
            builder.setMessage("Stock can not be updated without a network connection");

            AlertDialog alertDialogObject = builder.create();

            alertDialogObject.show();

        }

    }





    public void onClick(View v) {  // click listener called by ViewHolder clicks

        int pos = recyclerView.getChildLayoutPosition(v);
        Stock stock = stockDataList.get(pos);
        String url = "http://www.marketwatch.com/investing/stock/";
        String stocl_Symbol = stock.getSymbol();
        String final_URL = url+stocl_Symbol;

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(final_URL));
        startActivity(i);

       // Toast.makeText(v.getContext(), "SHORT " + stock.toString(), Toast.LENGTH_SHORT).show();
    }


    //.....................delete stock from the stocklist.........................
    public void deleteSingleStock(final int position) {

        Log.d(TAG, "position ==> " + position);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke YES event
                DatabaseHandler.getInstance(MainActivity.this).deleteStock(stockDataList.get(position).getSymbol());
                stockDataList.remove(position);
                stockadapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "stock Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    //..................stock name and symbol from asyncTask....................................

    public void displyInDialogueBox(ArrayList<Stock> stlist) {
        // stockList.addAll(stockList);
       // Toast.makeText(this, "back to main activity with all stock name", Toast.LENGTH_LONG).show();
        ArrayList<Stock> stockList = stlist;
        if(stockList != null)
        {
            List<String> allvalue = new ArrayList<String>();
            String name_symbol;


            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select");
            Stock st =null;
            for (int i=0;i<stockList.size();i++) {
                st = stockList.get(i);
                String stcName = st.getName();
                String stcSymbol = st.getSymbol();
                name_symbol = stcSymbol + "=>" + stcName;
                allvalue.add(name_symbol);
            }
            final CharSequence[] stock = allvalue.toArray(new String[allvalue.size()]);

            builder.setItems(stock, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    String selectedText = stock[item].toString();
                    String[] partall = selectedText.split("=>");
                    String part1=partall[0];
                    String part2=partall[1];
                    boolean flg=false;
                    for(int i=0;i<stockDataList.size();i++)
                    {
                        if(stockDataList.get(i).getSymbol().equals(part1))
                        {
                            flg = true;
                        }
                    }
                    if(!flg)
                    {
                     //   Toast.makeText(getBaseContext(),selectedText,Toast.LENGTH_LONG).show();
                        new StockDataDownloader(MainActivity.this).execute(part1,part2);
                    }
                    else
                    {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Duplicte stock ");
                        builder.setMessage("Enter Unique Stock Name");
                        AlertDialog alertDialogObject = builder.create();
                        alertDialogObject.show();
                    }


                    //Selected item in listview
                }
            });


            //  builder.setNegativeButton("NeverMind",);
            builder.setNegativeButton("NeverMind", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    dialog.cancel();
                }
            });
            builder.show();
        }
        else
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Stock Found");
            builder.setMessage("Enter proper stock name");

            AlertDialog alertDialogObject = builder.create();

            alertDialogObject.show();
        }


    }
    //..........................Financial Data...........................................
    public void financialData(Stock stlist) {
        Log.d(TAG,"object to pass to db"+stlist.toString());
       // DatabaseHandler.getInstance(this).addStock(stlist);

        boolean b1 = DatabaseHandler.getInstance(this).addStock(stlist);

        stockDataList.add(stlist);

        if (stockDataList.size() > 0) {

            Collections.sort(stockDataList, new Comparator<Stock>() {
                @Override
                public int compare(Stock o1, Stock o2) {
                    return o1.getSymbol().compareTo(o2.getSymbol());
                }
            });
            stockadapter.notifyDataSetChanged();
           // doRefresh();
        }


        }

}


