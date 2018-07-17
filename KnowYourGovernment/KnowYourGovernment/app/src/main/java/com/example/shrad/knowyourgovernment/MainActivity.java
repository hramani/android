package com.example.shrad.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener, View.OnClickListener,View.OnLongClickListener{
    private static final String TAG = "MainActivity";
    private Locator locator;
   private List<Official> officialList = new ArrayList<>();  // Main content is here
    public RecyclerView recyclerView; // Layout's recyclerview
    private GovAdapter gAdapter; // Data to recyclerview adapter
    private TextView location;
    private EditText loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locator = new Locator(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gAdapter = new GovAdapter(officialList, this);
        recyclerView.setAdapter(gAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        location = (TextView) findViewById(R.id.location);


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
            builder.setIcon(R.drawable.ic_action_warning);

            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about_button:
                Toast.makeText(this,"You pressed about",Toast.LENGTH_SHORT).show();
              startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;
            case R.id.location:
                if(checkConnection(getApplicationContext())) {
                    LayoutInflater inflater = LayoutInflater.from(this);
                    final View view1 = inflater.inflate(R.layout.locationfinder, null);
                    loc = (EditText) view1.findViewById(R.id.enterLoc);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Enter a City, State or a Zip Code:");
                    builder.setView(view1);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            System.out.println("Location :" + loc.getText().toString());
                            doLocationName(view1, loc.getText().toString());
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            // User cancelled the dialog
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public void setData(double lat, double lon) {
     String location = doAddress(lat, lon);
       if(checkConnection(getApplicationContext())) {
           new CivicInfoDownloader(this).execute(location);
       }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }

//Geocoder
    private String doAddress(double latitude, double longitude) {

        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);
        List<Address> addresses = null;
        String zip=null;
        if(checkConnection(getApplicationContext())) {
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

               try {
                   Log.d(TAG, "doAddress: Getting address now");
                   addresses = geocoder.getFromLocation(latitude, longitude, 1);
                   StringBuilder sb = new StringBuilder();
                   for (Address ad : addresses) {
                       Log.d(TAG, "doLocation: " + ad);
                       ((TextView) findViewById(R.id.location)).setText(ad.getAddressLine(1).toString());
                       sb.append(ad.getPostalCode());

                   }
                   return sb.toString();
               } catch (IOException e) {
                   Log.d(TAG, "doAddress: " + e.getMessage());

               }
           }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }

    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }
    private void displayAddresses(List<Address> addresses) {
        StringBuilder sb = new StringBuilder();
            if (addresses.size() == 0) {
                Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show();
            }

            for (Address address : addresses) {
                sb.append(address.getLatitude());
                sb.append("," + address.getLongitude());

            }
            setData(Double.parseDouble(sb.toString().split(",")[0]), Double.parseDouble(sb.toString().split(",")[1]));
        }
    public void doLocationName(View v, String loc) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = null;
            addresses = geocoder.getFromLocationName(loc,1);
            displayAddresses(addresses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }
    public void setOfficialList(Map<String,List<Official>> arrayList)
    {
     if(arrayList.equals(null))
    {
    location.setText("No Data For Location");
    officialList.clear();
    }
    else{

    Map.Entry<String, List<Official>> entry=arrayList.entrySet().iterator().next();
    String key= entry.getKey();
    List<Official> value=entry.getValue();
    System.out.println(key);
   location.setText(key);
    officialList.clear();
    officialList.addAll(value);

}
        gAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(View view, int position) {
        Official c = officialList.get(position);
        Intent intent = new Intent(MainActivity.this,OfficialsActivity.class);
        Bundle b = new Bundle();
        b.putString("location",c.getLocation());
       b.putString("office",c.getOffice());
        b.putString("name",c.getName());
        b.putString("Address",c.getAddress());
        b.putString("Email",c.getEmailId());
        b.putString("URL",c.getWebsite());
        b.putString("PhotoURL",c.getPhotourl());
        b.putString("phone",c.getPhoneNo());
        b.putString("Party",c.getParty());
        System.out.println(c.getParty());
        b.putString("fbid",c.getFbid());
        b.putString("yid",c.getYid());
        b.putString("tid",c.getTid());
        b.putString("gplus",c.getGid());
      intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
onItemClick(view,position);
    }

    @Override
    public void onClick(View v) {
        onItemClick(v,recyclerView.getChildLayoutPosition(v));
    }

    @Override
    public boolean onLongClick(View v) {
        onItemClick(v,recyclerView.getChildLayoutPosition(v));

        return false;
    }
}
