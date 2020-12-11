package com.app.forms.Items;

import java.util.ArrayList;

public class FormItem {
    private String name;
    private String createdOn;
    private String lastUpdate;
    private String UID;
    private FormConfig config;
    private ArrayList<BaseClass> form;

    public FormItem(String name, String createdOn, String UID) {
        this.name = name;
        this.createdOn = createdOn;
        this.UID = UID;
        form = new ArrayList<>();
        config = new FormConfig();

    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public FormConfig getConfig() {
        return config;
    }

    public void setConfig(FormConfig config) {
        this.config = config;
    }

    public ArrayList<BaseClass> getForm() {
        return form;
    }

    public void setForm(ArrayList<BaseClass> form) {
        this.form = form;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
