package com.example.shrad.knowyourgovernment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by shrad on 3/29/2017.
 */

public class PhotoActivity extends AppCompatActivity{
private ImageView imageView;
    private TextView location, Office,Official_name;
    Bundle b;
    String party;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoactivity);
        View view=findViewById(R.id.FullView);
        location=(TextView)findViewById(R.id.location);
        Office=(TextView)findViewById(R.id.office);
        Official_name=(TextView)findViewById(R.id.official_name);
        imageView=(ImageView)findViewById(R.id.fullscreen);
         b=this.getIntent().getExtras();
        party=b.getString("Party");
        if (party.equals("Republican")){
            view.setBackgroundColor(Color.parseColor("#FF0000"));

        }
        if (party.equals("Democratic")) {
            view.setBackgroundColor(Color.parseColor("#3498DB"));

        } if(party.equals("No Data Provided")) {
            view.setBackgroundColor(Color.parseColor("#17202A"));

        }
        location.setText(b.getString("location"));
        Office.setText(b.getString("office"));
        Official_name.setText(b.getString("official_name"));
        try {
            if (!b.getString("URL").equals(null) || !b.getString("PhotoURL").equals("No Data Provided")) {


                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        // Here we try https if the image attempt failed
                        String changedUrl = b.getString("URL").replace("http:", "https:");
                        picasso.load(changedUrl)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.ic_brokenimg)
                                .into(imageView);
                    }
                }).build();
                picasso.load(b.getString("URL"))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.ic_brokenimg)
                        .into(imageView);
            } else {
                Picasso.with(this).load(b.getString("URL"))
                        .placeholder(R.drawable.missing)
                        .error(R.drawable.ic_brokenimg)
                        .into(imageView);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
@Override
public void onBackPressed() {
    super.onBackPressed();
        try {
            Intent i = new Intent(PhotoActivity.this, OfficialsActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
