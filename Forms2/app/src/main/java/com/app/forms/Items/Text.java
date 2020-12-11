package com.app.forms.Items;

public class Text extends BaseClass {
    int textTypeChoice;

    public Text(int type) {
        this.type = type;
    }

    public int getTextTypeChoice() {
        return textTypeChoice;
    }

    public void setTextTypeChoice(int textTypeChoice) {
        this.textTypeChoice = textTypeChoice;
    }
}
