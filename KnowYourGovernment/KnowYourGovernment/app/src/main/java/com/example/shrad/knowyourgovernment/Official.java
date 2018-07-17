package com.example.shrad.knowyourgovernment;

import android.location.Address;
import android.location.Location;

/**
 * Created by shrad on 3/29/2017.
 */

public class Official {

    private String location;
    private String address;
    private String PhoneNo;
    private String EmailId;
    private String Website;
    private String Office;
    private String Name;
    private String Party;
    private String photourl;
    private String url;
    private String fbid,yid,tid,gid;


Official(String name,String office)
{
    Office=office;
    Name=name;
}

    Official( String loc, String add, String ph, String emailId, String website, String office, String name, String party, String Photourl, String fb, String youtube, String google, String twitter)
    {
        location=loc;
        address=add;
        PhoneNo=ph;
        EmailId=emailId;
        Website= website;
        Office=office;
        Name=name;
        Party=party;
        photourl=Photourl;
        fbid=fb;
        yid=youtube;
        gid=google;
        tid=twitter;
    }

public String getLocation()
{return location;
}
    public String getAddress()
    {
        return address;
    }
    public String getPhoneNo()
    {
        return PhoneNo;
    }
    public String getEmailId()
    {
        return EmailId;
    }
    public String getWebsite()
    {
        return Website;
    }
    public String getOffice()
    {
        return Office;
    }
    public String getName()
    {
        return Name;
    }
    public String getParty()
    {
        return Party;
    }

    public String getPhotourl() {
        return photourl;
    }

    public String getFbid() {
        return fbid;
    }

    public String getTid() {
        return tid;
    }

    public String getGid() {
        return gid;
    }

    public String getYid() {
        return yid;
    }

}
