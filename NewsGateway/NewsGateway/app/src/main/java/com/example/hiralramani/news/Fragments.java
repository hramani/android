package com.example.hiralramani.news;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by hiral ramani on 5/5/2017.
 */


public class Fragments extends Fragment implements View.OnClickListener {
    private TextView title;

    private String url;
    private ImageView imgg;
    private TextView d;
    public static final Fragments newInstance(String nsrc,String nauthor,String ntitle,String ndesc,String numg,String nurl,String ndate,String nk)
    {

        Fragments fml = new Fragments();
        Bundle bddl = new Bundle(1);
        bddl.putString("date",ndate);
        bddl.putString("Source",nsrc);
        bddl.putString("pmll",nk);
        bddl.putString("title",ntitle);
        bddl.putString("desc",ndesc);
        bddl.putString("urltoimg",numg);bddl.putString("Author",nauthor);
        bddl.putString("url",nurl);
        fml.setArguments(bddl);
        return fml;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { String description = getArguments().getString("desc");
        String Publisheddate=getArguments().getString("date"); String pgno=getArguments().getString("pmll");
        String Source = getArguments().getString("Source"); String Title = getArguments().getString("title");
        String Author = getArguments().getString("Author");

        String pdate = "";
        final String urltoimg = getArguments().getString("urltoimg");


        SimpleDateFormat sdf = null;


        url=getArguments().getString("url");



        View v = inflater.inflate(R.layout.myfragment, container, false);
        d=(TextView)v.findViewById(R.id.decID);

        title = (TextView)v.findViewById(R.id.titID);

        TextView date=(TextView)v.findViewById(R.id.dateID);
        TextView kl=(TextView)v.findViewById(R.id.pgnumberID);

        TextView author=(TextView)v.findViewById(R.id.authID);
        imgg=(ImageView)v.findViewById(R.id.aimgID);


        title.setText(Title);
        d.setText(description);

        date.setText("May 7 2017  04:45");
        kl.setText(pgno);

        try {
            if (!urltoimg.equals(null)) {


                Picasso picasso = new Picasso.Builder(v.getContext()).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {

                        String changedUrl = urltoimg.replace("http:", "https:");
                        picasso.load(changedUrl)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.nothing)
                                .into(imgg);
                    }
                }).build();
                picasso.load(urltoimg)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.nothing)
                        .into(imgg);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Author.equals(""))
        {
            author.setText(Source);
        }
        else{ author.setText(Author+", "+Source);}


        return v;
    }


    @Override
    public void onClick(View v) {

    }
}