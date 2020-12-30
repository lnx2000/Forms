package com.app.forms.Items;

import android.net.Uri;
import android.util.Log;

public class BaseClass {
    String title;
    //String imagepath;
    //String databaseImageUrl;
    boolean mandatory;
    //boolean image;
    int type;


    /*public String getDatabaseImageUrl() {
        return databaseImageUrl;
    }

    public void setDatabaseImageUrl(String databaseImageUrl) {
        this.databaseImageUrl = databaseImageUrl;
    }*/

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*public Uri getImagepath() {
        Log.e("123", imagepath);
        return Uri.parse(imagepath);
    }*/

   /* public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public void setImagepath(Uri imagepath) {
        this.imagepath = imagepath.toString();
    }*/

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

   /* public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }*/
}
