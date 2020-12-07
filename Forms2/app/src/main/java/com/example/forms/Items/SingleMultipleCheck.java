package com.example.forms.Items;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class SingleMultipleCheck {
    String title;
    boolean isImage;
    boolean mandatory;
    Bitmap bitmap;
    ArrayList<String> radioGroup;
    int type;

    public SingleMultipleCheck(int type) {
        title = "";
        isImage = false;
        bitmap = null;
        radioGroup = new ArrayList<>(Arrays.asList("Option 1", "Option 2"));
        this.type = type;
    }

    public void addInRadioGroup() {
        Log.e("123", "" + radioGroup.size());
        String name = "Option " + (radioGroup.size() + 1);
        radioGroup.add(name);
    }

    public int radioGroupSize() {
        return radioGroup.size();
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

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ArrayList<String> getRadioGroup() {
        return radioGroup;
    }

    public int getType() {
        return type;
    }
}
