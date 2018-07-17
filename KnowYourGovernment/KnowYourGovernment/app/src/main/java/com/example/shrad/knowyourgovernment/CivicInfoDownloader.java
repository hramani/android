package com.example.shrad.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shrad on 3/31/2017.
 */

public class CivicInfoDownloader extends AsyncTask<String,Void,String> {
    private static final String TAG = "CiviInfoDownloader";
    private MainActivity mainActivity;
    private final String nameURL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyCwIB6-T3PMLca1ojBl2Kiv8ZF-L6_3PNA";
    private final String NODATA="No Data Provided";
    private String Loc;

    public CivicInfoDownloader(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("OnPostexecute Result: "+s);
        if(s==null)
        {
            Toast.makeText(mainActivity,"The Civic Info Service is Unavailable",Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialList(null);
        }
        if(s.isEmpty())
        {
            Toast.makeText(mainActivity,"No data is available for the specified location",Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialList(null);
        }
        else {
       List<Official> officialList = parseJSON(s);
            Map<String,List<Official>> list=new LinkedHashMap<>();

            list.put(Loc,officialList);
        mainActivity.setOfficialList(list);

    }
    }

    @Override
    protected String doInBackground(String... params) {

        Uri.Builder buildURL = Uri.parse(nameURL).buildUpon();

        buildURL.appendQueryParameter("address", params[0]);
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        }catch(Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        return sb.toString();
    }

    private List<Official> parseJSON(String s) {
        List<Official> officialList = new ArrayList<>();
        HashMap<Integer, String> reprlist = new HashMap<>();
      try {

          JSONObject repr = null, mJsonObject1 = null, off = null;
          String name = NODATA, office = NODATA,officials_name = NODATA, full_address = NODATA, line1, line2, add_city, add_state, add_zip, off_party = NODATA, off_url = NODATA, off_ph = NODATA, off_emails =NODATA, off_photourl =NODATA,  gid = NODATA, fbid = NODATA, yid = NODATA, tid = NODATA;
          try {
              mJsonObject1 = new JSONObject(s);
//Normalized Input
              JSONObject mJsonObject = mJsonObject1.getJSONObject("normalizedInput");
              String city = mJsonObject.getString("city");
              String state = mJsonObject.getString("state");
              String zip = mJsonObject.getString("zip");
              Loc = city + ", " + state + ", " + zip;

          } catch (Exception e) {
              Loc = NODATA;
          }

          JSONArray OfficeArry = null, officialArray = null;
          try {
              OfficeArry = mJsonObject1.getJSONArray("offices");
          } catch (Exception e) {
          }
          for (int i = 0; i < OfficeArry.length(); i++) {
              try {
                  off = OfficeArry.getJSONObject(i);
                  office = off.getString("name");
                  System.out.println("Office :" + office);
              } catch (Exception e) {
                  office = NODATA;
              }

              //Office Indices

              try {
                  JSONArray indicesArray = off.getJSONArray("officialIndices");
                  for (int j = 0; j < indicesArray.length(); j++) {
                      reprlist.put(indicesArray.getInt(j),office);

                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }

          //Officials

          try {
              officialArray = mJsonObject1.getJSONArray("officials");
          } catch (Exception e) {
              e.printStackTrace();
          }
          for (int i = 0; i < officialArray.length(); i++) {
              try {
                  repr = officialArray.getJSONObject(i);
                  officials_name = repr.getString("name");
              } catch (Exception e) {
                  officials_name = NODATA;
              }

try{ JSONArray webUrl = repr.getJSONArray("urls");
off_url=webUrl.getString(0);}
catch(Exception e){
                  off_url=NODATA;
              }

              try {
                  JSONArray address = repr.getJSONArray("address");
                  JSONObject addr = address.getJSONObject(0);
                  line1 = addr.getString("line1")+", ";
                 try { line2 = addr.getString("line2")+", ";}
                  catch(Exception e)
                  {line2="";}
                  add_city = addr.getString("city")+", ";
                  add_state = addr.getString("state")+", ";
                  add_zip = addr.getString("zip");
                  full_address = line1+line2 +add_city +add_state + add_zip;
              } catch (Exception e) {
                  full_address = NODATA;
              }
              try {
                  off_party = repr.getString("party");
              } catch (Exception e) {
                  off_party = "Unknown";
              }
              try {
                  JSONArray phno = repr.getJSONArray("phones");
                  off_ph = phno.getString(0);
              } catch (Exception e) {
                  off_ph = NODATA;
              }
              try {
                  JSONArray emails = repr.getJSONArray("emails");
                  off_emails = emails.getString(0);
              } catch (Exception e) {
                  off_emails = NODATA;
              }
              try {
                  off_photourl = repr.getString("photoUrl");
              } catch (Exception e) {
                  off_photourl = NODATA;
              }

          try {
              JSONArray channels = repr.getJSONArray("channels");
              for (int j = i; j <= i; j++) {
                  JSONObject ch = channels.getJSONObject(0);

                 try {
                      if (ch.getString("type").equals("GooglePlus")) {
                          gid = ch.getString("id");
                      }
                 } catch (Exception e) {
                      gid = NODATA;
                  }
                  try {
                      if (ch.getString("type").equals("Facebook")) {
                          fbid = ch.getString("id");
                      }
                  } catch (Exception e) {
                      fbid = NODATA;
                  }
                  try {
                      if (ch.getString("type").equals("Youtube")) {
                          yid = ch.getString("id");
                      }
                  } catch (Exception e) {
                      yid = NODATA;
                  }
                  try {

                      if (ch.getString("type").equals("Twitter")) {
                          tid = ch.getString("id");
                          System.out.println(tid);
                      }
                  } catch (Exception e) {
                      tid = NODATA;
                  }
              }
          }catch(Exception e)
          {
              e.printStackTrace();
          }
              if(reprlist.containsKey(i))
              {
                  office=reprlist.get(i);
              }

              officialList.add(new Official(Loc, full_address, off_ph, off_emails, off_url, office, officials_name, off_party, off_photourl, fbid, yid, gid, tid));
          }


      }   catch(Exception e) {

            return null;
        }
        Log.d(TAG, "onPostExecute: CivicInfoDownloader");
        return officialList;


    }

}
