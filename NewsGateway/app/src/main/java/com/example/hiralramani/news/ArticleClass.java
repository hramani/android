package com.example.hiralramani.news;

import android.os.Parcel;
import android.os.Parcelable;


public class ArticleClass implements Parcelable {
    String nauthor;  String ndate;String nurl;String ndescription; String nsource; String nurlToImage; String ntitle;







    public  ArticleClass (String source,String author,String title,String description,String urlToImage,String url,String date)
    {
        this.nsource=source;
        this.nauthor=author;
        this.ntitle=title;
        this.ndescription=description;
        this.nurlToImage=urlToImage;
        this.nurl=url;
        this.ndate=date;
    }

    protected ArticleClass(Parcel in) {
        nauthor = in.readString();
        ntitle = in.readString();
        ndescription = in.readString();
        nurlToImage = in.readString();
        nsource = in.readString();
        nurl=in.readString();
        ndate=in.readString();

    }

    public static final Creator<ArticleClass> CREATOR = new Creator<ArticleClass>() {
        @Override
        public ArticleClass createFromParcel(Parcel in) {
            return new ArticleClass(in);
        }

        @Override
        public ArticleClass[] newArray(int size) {
            return new ArticleClass[size];
        }
    };


    public String gets() {
        return nsource;
    }public String getaut() {
        return nauthor;
    }public String getUrl() {
        return nurl;
    }







    public String getuimg() {
        return nurlToImage;
    }

    public String getdt() {
        return ndate;
    } public String gett() {
        return ntitle;
    }
    public String getd() {
        return ndescription;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nauthor);
        dest.writeString(ntitle);
        dest.writeString(ndescription);
        dest.writeString(nurlToImage);
        dest.writeString(nsource);
        dest.writeString(nurl);
        dest.writeString(ndate);
    }
}
