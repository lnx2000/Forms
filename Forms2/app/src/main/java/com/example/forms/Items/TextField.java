package com.example.forms.Items;

import android.graphics.Bitmap;

public class TextField {
    String title;
    boolean isImage;
    int textTypeChoice;
    boolean mandatory;
    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public int getTextTypeChoice() {
        return textTypeChoice;
    }

    public void setTextTypeChoice(int textTypeChoice) {
        this.textTypeChoice = textTypeChoice;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public TextField() {
        title = "";
        isImage = false;
        textTypeChoice = 0;
        mandatory = false;


    }
}
