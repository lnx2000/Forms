package com.app.forms.Items;

public class TextMsg extends BaseClass {


    boolean preview, center;

    public TextMsg(int type) {
        this.title = "";
        this.type = type;
        preview = false;
        center = false;
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

}
