package com.example.hiralramani.multinote;

import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.text;
import static android.R.id.list;
import static android.R.id.message;
import static java.lang.System.out;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private EditText title;
    private EditText notetext;
    private Note nt;
    private static final String TAG = "MainActivity";
    private static final int B_REQ = 1;


    public RecyclerView rs;
    public RecyclerView.Adapter ap;
    public RecyclerView.LayoutManager lm;
    ProgressDialog progressDialog;


    public List<Note> ntlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rs = (RecyclerView) findViewById(R.id.recycle);
        ap = new adapter(ntlist, this);
        lm = new LinearLayoutManager(this);
        rs.setLayoutManager(lm);
        rs.setHasFixedSize(true);
        rs.setAdapter(ap);

//        progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setMessage("File loading .....");
//        progressDialog.setIndeterminate(false);
//        progressDialog.setCancelable(false);
//        progressDialog.show();


        //load data from json file
//        loadFile();

//        async loading of file
        new FileAsyncLoading().execute();

    }

    public void loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");

        try {
            InputStream is = getApplicationContext().openFileInput("Note.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

            try {

                readMessagesArray(reader);

            } finally {

                reader.close();
            }


        } catch (FileNotFoundException e) {
            // Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Note> readMessagesArray(JsonReader reader) throws Exception {

        reader.setLenient(true);
        reader.beginArray();
        while (reader.hasNext()) {
            ntlist.add(readMessage(reader));
        }
        reader.endArray();
        return ntlist;
    }

    public Note readMessage(JsonReader reader) throws IOException {
        String Title = "";
        String Note = "";
        String Time = "";

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("Title")) {
                Title = reader.nextString();
            } else if (name.equals("Note")) {
                Note = reader.nextString();
            } else if (name.equals("Time")) {
                Time = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Note(Title, Note, Time);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.infoid:
                Intent intent = new Intent(MainActivity.this, infoactivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                startActivity(intent);
                return true;

            case R.id.addid:
                Intent intent1 = new Intent(MainActivity.this, noteactivity.class);
                intent1.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                // startActivity(intent1);
                startActivityForResult(intent1, B_REQ);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
//............................onresume method..................................

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == B_REQ) {
            if (resultCode == RESULT_OK) {

                Note nt = (Note) data.getSerializableExtra("noteobject");
                Log.d(TAG, "nt.getDate() ==>" + nt.getDate());
                Log.d(TAG, " nt.getTitle() ==>" + nt.getTitle());
                Log.d(TAG, "nt.getNote() ==>" + nt.getNote());
                ntlist.add(0,nt);
              //  saveNote(ntlist);
                ap.notifyDataSetChanged();

                Log.d(TAG, "onActivityResult: User Text: " + text);

            } else if (resultCode == 5) {

                int position = data.getIntExtra("position", 0);
                ntlist.remove(position);
                Note nt = (Note) data.getSerializableExtra("noteobject");
                ntlist.add(0, nt);
             //   saveNote(ntlist);
                ap.notifyDataSetChanged();

                Toast.makeText(this, "record updated ", Toast.LENGTH_SHORT).show();

            } else {

                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNote(ntlist);

    }

    private void saveNote(List<Note> ntlistt) {

        Log.d(TAG, "saveNote: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("Note.json", Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
//            writer.setIndent("  ");
            writeMessagesArray(writer, ntlistt);
            writer.close();

            Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void writeMessagesArray(JsonWriter writer, List<Note> ntlistt) throws IOException {
        writer.beginArray();
        for (Note notelist : ntlistt) {
            writeMessage(writer, notelist);
        }
        writer.endArray();
    }

    public void writeMessage(JsonWriter writer, Note notelist) throws IOException {
        writer.beginObject();

        writer.name("Title").value(notelist.getTitle());
        writer.name("Note").value(notelist.getNote());
        writer.name("Time").value(notelist.getDate());

        writer.endObject();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }


    private class FileAsyncLoading extends AsyncTask<Void, Void, String>

    {


        @Override
        protected void onPreExecute() {

//            progressDialog = new ProgressDialog(MainActivity.this);
//            progressDialog.setMessage("File loading .....");
//            progressDialog.setIndeterminate(false);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
            Toast.makeText(getApplicationContext(), "Data is loading", Toast.LENGTH_SHORT).show();


        }

        @Override
        protected String doInBackground(Void... params) {
//
            loadFile();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//
//            if (progressDialog.isShowing()) {
//                //progressDialog.cancel();
//                progressDialog.dismiss();
//            }
        }
    }

    public void deleteSingleNote(final int position) {

        Log.d(TAG, "position ==> " + position);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event

                ntlist.remove(position);
                saveNote(ntlist);
                ap.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
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
}
