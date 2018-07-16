package com.example.hiralramani.multinote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class infoactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoactivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
