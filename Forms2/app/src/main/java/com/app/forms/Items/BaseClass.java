package com.app.forms.Items;

public class BaseClass {
    String title;
    boolean mandatory;
    int type;

    public BaseClass() {
    }

    public BaseClass(int type) {
        title = "";
        this.type = type;
    }

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


    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

}
