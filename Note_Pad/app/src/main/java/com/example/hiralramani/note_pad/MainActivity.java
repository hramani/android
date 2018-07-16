package com.example.hiralramani.note_pad;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView date;
    private EditText note;
    private Note nt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        date = (TextView) findViewById(R.id.dateID);
        note = (EditText) findViewById(R.id.noteid);
        //.....................................
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .showSoftInput(note, InputMethodManager.SHOW_FORCED);
                note.setMovementMethod(new ScrollingMovementMethod());
                note.setTextIsSelectable(true);
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
    protected void onResume() {
        super.onStart();

        nt = loadFile();  // Load the JSON containing the product data - if it exists


        if (nt != null) {

                date.setText(nt.getDate());
                note.setText(nt.getNote());

        }
            else
            {
                SimpleDateFormat parseFormat1 = new SimpleDateFormat("E MMM dd,yyyy hh:mm:ss a");
                Date datee =new Date();
                String s1 = parseFormat1.format(datee);

                date.setText("Last Update:  "+s1);
            }// / null means no file was loaded

        }



    private Note loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        nt = new Note();
        try {
            InputStream is = getApplicationContext().openFileInput("Note.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("date")) {

                    nt.setDate(reader.nextString());

                } else if (name.equals("note")) {
                    nt.setNote(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
            nt = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nt;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(nt == null) {
            nt= new Note();
        }
        SimpleDateFormat parseFormat = new SimpleDateFormat("E MMM dd,yyyy hh:mm:ss a");
        Date date =new Date();
        String s = parseFormat.format(date);

      //  String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        nt.setDate("Last Update : "+s);
        nt.setNote(note.getText().toString());

        saveNote();
    }

    private void saveNote() {

        Log.d(TAG, "saveNote: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("Note.json", Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("date").value(nt.getDate());
            writer.name("note").value(nt.getNote());
            writer.endObject();
            writer.close();


            /// You do not need to do the below - it's just
            /// a way to see the JSON that is created.
            ///
            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("date").value(nt.getDate());
            writer.name("note").value(nt.getNote());
            writer.endObject();
            writer.close();
            Log.d(TAG, "saveNote: JSON:\n" + sw.toString());
            ///
            ///

            Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}

