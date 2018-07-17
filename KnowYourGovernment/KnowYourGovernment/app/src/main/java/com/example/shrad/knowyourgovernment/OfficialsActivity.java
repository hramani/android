package com.example.shrad.knowyourgovernment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;

import static android.text.Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE;
import static com.example.shrad.knowyourgovernment.R.string.Address;

/**
 * Created by shrad on 3/29/2017.
 */

public class OfficialsActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView location, office,official_name,addressvalu,phoneno,emailid,webaddress;
    public ImageView Image;
    private ImageButton fb,youtube,gplus,twitter;
    Bundle official;
    String NODATA="No Data Provided";
    String imageurl,party=NODATA;

protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.officialactivity);
    View view = findViewById(R.id.ScollView1);
    location = (TextView) findViewById(R.id.location);
    office = (TextView) findViewById(R.id.office);
    official_name = (TextView) findViewById(R.id.official_name);
    addressvalu = (TextView) findViewById(R.id.addressvalu);
    phoneno = (TextView) findViewById(R.id.phoneno);
    emailid = (TextView) findViewById(R.id.emailid);
    webaddress = (TextView) findViewById(R.id.webaddress);
    Image = (ImageView) findViewById(R.id.photo);
    fb = (ImageButton) findViewById(R.id.fb);
    youtube = (ImageButton) findViewById(R.id.youtube);
    gplus = (ImageButton) findViewById(R.id.gplus);
    twitter = (ImageButton) findViewById(R.id.twitter);


    official = this.getIntent().getExtras();
    party=official.getString("Party");

    location.setText(official.getString("location"));
    if (official != null) {
        if (party.equals("Republican")){
            view.setBackgroundColor(Color.parseColor("#FF0000"));

        }
        if (party.equals("Democratic")) {
            view.setBackgroundColor(Color.parseColor("#3498DB"));

        } if(party.equals(NODATA)) {
            view.setBackgroundColor(Color.parseColor("#17202A"));

        }
        if (official.getString("fbid").equals(null) || official.getString("fbid").equals("No Data Provided"))
        {
            fb.setVisibility(View.INVISIBLE);
        }
        else {
            fb.setVisibility(View.VISIBLE);
        }

        if (official.getString("tid").equals(null) || official.getString("tid").equals("No Data Provided")) {
            twitter.setVisibility(View.INVISIBLE);
        } else {
            twitter.setVisibility(View.VISIBLE);
        }
        if (official.getString("gplus").equals(null) || official.getString("gplus").equals("No Data Provided")) {
            gplus.setVisibility(View.INVISIBLE);
        }
        else {
            gplus.setVisibility(View.VISIBLE);
        }

        if (official.getString("yid").equals(null) || official.getString("yid").equals("No Data Provided")) {
            youtube.setVisibility(View.INVISIBLE);
        }
        else {
            youtube.setVisibility(View.VISIBLE);
        }
        String Location = official.getString("location");

        //   official = b;
        imageurl=official.getString("PhotoURL");
            office.setText(official.getString("office"));
        official_name.setText(official.getString("name")+"    ("+official.getString("Party")+" )");

        if(official.getString("Address").equals(NODATA))
        {  addressvalu.setText(official.getString("Address"));}

        else
        {
            addressvalu.setMovementMethod(LinkMovementMethod.getInstance());
            addressvalu.setText( Html.fromHtml(
                    "<a href="+official.getString("Address")+ ">"+official.getString("Address") +"</a> " ));
            addressvalu.setClickable(true);
        }
        if(official.getString("phone").equals(NODATA))
        {  phoneno.setText(official.getString("phone"));}
        else
        {
            phoneno.setMovementMethod(LinkMovementMethod.getInstance());
            phoneno.setText( Html.fromHtml(
                    "<a href="+official.getString("phone")+ ">"+official.getString("phone") +"</a> "));
            phoneno.setClickable(true);
        }

        if(official.getString("Email").equals(NODATA))
        {  emailid.setText(official.getString("Email"));

        }
        else
        {
            emailid.setMovementMethod(LinkMovementMethod.getInstance());
            emailid.setText( Html.fromHtml(
                    "<a href="+official.getString("Email")+ ">"+official.getString("Email") +"</a> "));
            emailid.setClickable(true);
        }
        if(official.getString("URL").equals(NODATA))
        {  webaddress.setText(official.getString("URL"));}
        else
        {
            webaddress.setMovementMethod(LinkMovementMethod.getInstance());
            webaddress.setText( Html.fromHtml(
                    "<a href="+official.getString("URL")+ ">"+official.getString("URL") +"</a> "));;
            webaddress.setClickable(true);
        }

        try {
            if (!official.getString("PhotoURL").equals(null) || !official.getString("PhotoURL").equals("No Data Provided")) {


                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        // Here we try https if the image attempt failed
                        String changedUrl = official.getString("PhotoURL").replace("http:", "https:");
                        picasso.load(changedUrl)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.ic_brokenimg)
                                .into(Image);
                    }
                }).build();
                picasso.load(official.getString("PhotoURL"))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.ic_brokenimg)
                        .into(Image);
            } else {
                Picasso.with(this).load(official.getString("PhotoURL"))
                        .placeholder(R.drawable.missing)
                        .error(R.drawable.ic_brokenimg)
                        .into(Image);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
    public void clickUrl(View v) {
        String url = webaddress.getText().toString();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void clickEmail(View v) {
        String addresses = emailid.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Email from Sample Implied Intent App");
        intent.putExtra(Intent.EXTRA_TEXT, "Email text body...");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles SENDTO (mailto) intents");
        }
    }
/*@Override
    protected void onResume()
{
    super.onResume();
    }*/
@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(OfficialsActivity.this, MainActivity.class);
       startActivity(i);
        finish();
    }
    public void facebookClicked(View v) {
            String FACEBOOK_URL = "https://www.facebook.com/" + official.getString("fbid");
            String urlToUse;
            PackageManager packageManager = getPackageManager();
            try {
                int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                if (versionCode >= 3002850) {
                    //newer versions of fb app
                    urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                } else { //older versions of fb app
                    urlToUse = "fb://page/" + official.getString("fbid");
                }
            } catch (PackageManager.NameNotFoundException e) {
                urlToUse = FACEBOOK_URL;
                //normal web url
            }
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(urlToUse));
            startActivity(facebookIntent);
        }



   //     Twitter (example onClick method to be associated with the Twitter ImageView icon):
    public void twitterClicked(View v) {

            Intent intent = null;
            String name = official.getString("tid");
            try { // get the Twitter app if possible
                getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) { // no Twitter app, revert to browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
            }
            startActivity(intent);
        }

   // GooglePlus (example onClick method to be associated with the GooglePlus ImageView icon):
    public void googlePlusClicked(View v) {

            String name = official.getString("gplus");
            if (!name.equals(null) || !name.equals("No Data Provided")) {
                Intent intent = null;
                try {

                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
                    intent.putExtra("customAppUri", name);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
                }
            }

        }

    //YouTube (example onClick method to be associated with the YouTube ImageView icon):
    public void youTubeClicked(View v) {

            String name = official.getString("yid");
            if (!name.equals(null) || !name.equals("No Data Provided")) {
                Intent intent = null;
                try {

                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse("https://www.youtube.com/" + name));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
                }
            }

        }

    public void clickCall(View v) {
        String number = phoneno.getText().toString();

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_DIAL (tel) intents");
        }
    }
    public void clickMap(View v) {
        String address = addressvalu.getText().toString();

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
        }
    }
    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
       Bundle b=new Bundle();
        b.putString("URL",imageurl);
        b.putString("Party",party);
        b.putString("location",location.getText().toString());
        b.putString("office",office.getText().toString());
        b.putString("official_name",official_name.getText().toString());
        Intent intent=new Intent(OfficialsActivity.this,PhotoActivity.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

}
