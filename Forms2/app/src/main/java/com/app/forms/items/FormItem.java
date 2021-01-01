package com.app.forms.items;

import java.util.ArrayList;

public class FormItem {
    private String name;
    private String createdOn;
    private String lastUpdate;
    private int UID;
    private FormConfig config;
    private ArrayList<BaseClass> form;
    private boolean enabled;
    private String userID;

    public FormItem(FormItem formItem){
        this.name = formItem.name;
        this.createdOn = formItem.createdOn;
        this.lastUpdate = formItem.lastUpdate;
        this.UID = formItem.UID;
        try {
            this.config =(FormConfig) formItem.config.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.form = new ArrayList<>(formItem.form);
        this.enabled = new Boolean(formItem.enabled);
        this.userID = formItem.userID;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public FormItem(String name, String createdOn, int UID) {
        this.name = name;
        this.createdOn = createdOn;
        this.UID = UID;
        form = new ArrayList<>();
        config = new FormConfig();

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }
}
