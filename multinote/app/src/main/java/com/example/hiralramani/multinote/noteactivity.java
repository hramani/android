package com.example.hiralramani.multinote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class noteactivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText title;
    private EditText notetext;
    private Note nt;
    String time, titlee, content;
    boolean isEditMode = false, isFileEdited = false;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteactivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        title = (EditText) findViewById(R.id.ntitleid);
        notetext = (EditText) findViewById(R.id.nnotetextid);

        notetext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                isFileEdited = true;
//                if (charSequence.length() > 25) {
//                    Toast.makeText(noteactivity.this, "25 reached", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        SimpleDateFormat parseFormat1 = new SimpleDateFormat("E MMM dd hh:mm:ss a");
        Date datee = new Date();
        time = parseFormat1.format(datee);


//        nt = new Note(titlee,content,time);

        if (getIntent() != null && getIntent().hasExtra("title")) {

            String note_title = getIntent().getStringExtra("title");
            String note = getIntent().getStringExtra("note");
            String date = getIntent().getStringExtra("date");
            position = getIntent().getIntExtra("position", 0);

            isEditMode = true;

            title.setText(note_title);
            notetext.setText(note);

        }
        //.....................................
        notetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .showSoftInput(notetext, InputMethodManager.SHOW_FORCED);
                notetext.setMovementMethod(new ScrollingMovementMethod());
                notetext.setTextIsSelectable(true);
            }
        });
        //...................................
        // note.setMovementMethod(new ScrollingMovementMethod());
        //note.setTextIsSelectable(true);
        //note.clearFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.saveid:
                //  int i=0;
                saveCurrentNote();
                return true;

            case android.R.id.home:
                if (isFileEdited) {
                    showSaveDialog();
                } else {
                    super.onBackPressed();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveCurrentNote() {
        if (title.getText().toString().trim().length() > 0) {
            titlee = title.getText().toString();
            content = notetext.getText().toString();
            nt = new Note(titlee, content, time);
//                nt.setDate(time);
//                nt.setNote(content);
//                nt.setTitle(titlee);
            Intent data = new Intent();
            data.putExtra("noteobject", nt);
            data.putExtra("position", position);
            if (isEditMode) {

                setResult(5, data);

            } else {

                setResult(RESULT_OK, data);
            }

            finish();

        } else {

            Toast.makeText(noteactivity.this, "Title required", Toast.LENGTH_SHORT).show();
            finish();
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(noteactivity.this);
//                    builder.setTitle("Error");
//                    builder.setMessage("Title required. NOte not saved");
//
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                            dialogInterface.dismiss();
//                            finish();
//                        }
//                    });
//
//
//                    builder.show();

        }
    }

    @Override
    public void onBackPressed() {
        if (isFileEdited) {
            showSaveDialog();
        } else {
            super.onBackPressed();
        }
    }

    public void showSaveDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(noteactivity.this);
        // builder.setTitle("Error");
        builder.setMessage("Do you want to save note?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                saveCurrentNote();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                finish();
            }
        });


        builder.show();
    }

    //..............................back button...........................


}
