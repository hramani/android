package com.example.hiralramani.temperature;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    EditText tempInput;
    RadioButton ftoc;
    RadioButton ctof;
    EditText ans;
    ListView listvw;
  //  private static final String KEY_ADAPTER_STATE = "StateSavingArrayAdapter.KEY_ADAPTER_STATE";

    ArrayList<String> list = new ArrayList<String>();

    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempInput = (EditText)findViewById(R.id.editText);
        ftoc = (RadioButton)findViewById(R.id.radioButton3);
        ctof = (RadioButton)findViewById(R.id.radioButton4);
        ans =(EditText)findViewById(R.id.editText2);
        listvw = (ListView) findViewById(R.id.listviewID);

        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listvw.setAdapter(listAdapter);

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig)
//    {
//        super.onConfigurationChanged(newConfig);
////        setContentView(R.layout.activity_main);
//    }

    public void convertmethod(View v)
    {
        float inputvalue = new Float(tempInput.getText().toString());

        if(ftoc.isChecked())
        {
            inputvalue = convertFunction.ftoc(inputvalue);

            list.add(0,tempInput.getText().toString() +"(F)"+"  =>  "+ inputvalue+"(C)");
//            listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
//            listvw.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        }

        else
        {
            inputvalue = convertFunction.ctof(inputvalue);
            list.add(0,tempInput.getText().toString()+"(C)"+"  =>  "+ inputvalue+"(F)");
//            listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
//            listvw.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        }
        ans.setText(new Float(inputvalue).toString());
      //  tempInput.setText("");
      //  ans.setText("");
    }
    public void reset(View v)
    {
        tempInput.setText("");
        ans.setText("");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putStringArrayList("history1", list);
//        outState.putString("anskey",ans.getText().toString());
        // Call super last

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);
//        ans.setText(savedInstanceState.getString("anskey"));
//        list.addAll(savedInstanceState.getStringArrayList("history1"));
//        listAdapter.notifyDataSetChanged();
        //history.setText(savedInstanceState.getString("HISTORY"));
    }

}
