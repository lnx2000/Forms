package com.app.forms.Items;

import android.net.Uri;

public class BaseClass {
    String title;
    Uri imagepath;
    boolean mandatory;
    boolean image;
    int type;

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

    public Uri getImagepath() {
        return imagepath;
    }

    public void setImagepath(Uri imagepath) {
        this.imagepath = imagepath;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }
}
