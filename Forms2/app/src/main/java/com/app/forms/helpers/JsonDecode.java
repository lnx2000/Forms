package com.app.forms.helpers;

import com.app.forms.Items.BaseClass;
import com.app.forms.Items.Check;
import com.app.forms.Items.FormItem;
import com.app.forms.Items.Text;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonDecode {
    public static FormItem decode(String jsonForm) throws JSONException {
        Gson gson = new Gson();

        JSONObject obj = new JSONObject(jsonForm);
        String form = obj.getString("form");
        obj.remove("form");

        FormItem item = gson.fromJson(obj.toString(), FormItem.class);
        ArrayList<BaseClass> data = decodeArray(form);
        item.setForm(data);
        return item;
    }

    public static ArrayList<BaseClass> decodeArray(String jsonarray) throws JSONException {
        Gson gson = new Gson();
        JSONArray array = new JSONArray(jsonarray);
        ArrayList<BaseClass> data = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jobj = array.getJSONObject(i);
            if (jobj.has("textTypeChoice")) {
                Text text = gson.fromJson(jobj.toString(), Text.class);
                data.add(text);
            } else if (jobj.has("group")) {
                Check check = gson.fromJson(jobj.toString(), Check.class);
                data.add(check);

            } else {
                BaseClass baseClass = gson.fromJson(jobj.toString(), BaseClass.class);
                data.add(baseClass);
            }

        }
        return data;

    }
}
