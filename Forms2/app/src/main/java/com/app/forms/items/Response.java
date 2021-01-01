package com.app.forms.items;

import java.util.Set;

public class Response {
    int type;

    String text;
    int schecked;
    Set<Integer> mchecked;
    int rating;
    //String fileurl;
    boolean mandatory;

    public Response(int type) {
        mandatory = false;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSchecked() {
        return schecked;
    }

    public void setSchecked(int schecked) {
        this.schecked = schecked;
    }

    public Set<Integer> getMchecked() {
        return mchecked;
    }

    public void setMchecked(Set<Integer> mchecked) {
        this.mchecked = mchecked;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void addIntoSet(int i) {
        mchecked.add(i);
    }
}
