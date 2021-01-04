package com.app.forms.items;

import java.util.HashSet;
import java.util.Set;

public class ItemResponse {
    String text;
    Integer schecked;
    Set<Integer> mchecked;
    int rating;
    //String fileurl;
    Boolean mandatory;

    public ItemResponse(boolean mandatory) {
        mchecked = null;
        this.mandatory = mandatory;
        schecked = null;
        text = null;
        rating = 1;
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
        if (mchecked == null)
            mchecked = new HashSet<>();
        mchecked.add(i);
    }

    public void removeFromSet(int i) {
        mchecked.remove(i);
        if (mchecked.size() == 0)
            mchecked = null;

    }
}
