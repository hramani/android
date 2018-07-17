package com.example.shrad.knowyourgovernment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by shrad on 3/29/2017.
 */

public class AboutActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
//        View view=()

    }
    public void onBackPressed() {
        Intent i = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(i);
       finish();
    }
}
