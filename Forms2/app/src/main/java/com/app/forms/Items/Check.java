package com.app.forms.Items;

import java.util.ArrayList;

public class Check extends BaseClass {
    ArrayList<String> group;

    public Check(int type) {
        this.type = type;
        group = new ArrayList<>();
        addOption();
        addOption();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<String> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<String> radioGroup) {
        this.group = radioGroup;
    }

    public int addOption() {
        String option = "Option ";
        option += (group.size() + 1);
        group.add(option);
        return group.size();
    }
}
