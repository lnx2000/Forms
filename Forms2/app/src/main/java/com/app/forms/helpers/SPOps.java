package com.app.forms.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.forms.activities.MainActivity;
import com.app.forms.constants.Constants;
import com.app.forms.items.FormItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class SPOps {
    public static ArrayList<FormItem> loadSP(Context context) {
        Gson gson = new Gson();
        ArrayList<FormItem> forms = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences("Forms", MODE_PRIVATE);
        String jsonUids = sp.getString("UIDs", "");
        if (jsonUids.equals(""))
            return forms;
        ArrayList<Integer> uids = gson.fromJson(jsonUids, new TypeToken<ArrayList<Integer>>() {
        }.getType());
        for (int uid : uids) {
            String jsonform = sp.getString("" + uid, "");
            FormItem f = null;
            try {
                f = JsonDecode.decode(jsonform);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            forms.add(f);
        }
        return forms;
    }

    public static void newForm(Context context, boolean sortNewFirst) {
        if (!sortNewFirst)
            Collections.reverse(MainActivity.data);

        ArrayList<Integer> uids = new ArrayList<>();
        for (FormItem item : MainActivity.data) {
            uids.add(item.getUID());
        }

        Gson gson = new Gson();
        String jsonUids = gson.toJson(uids);
        SharedPreferences sp = context.getSharedPreferences("Forms", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UIDs", jsonUids);
        editor.putString("" + uids.get(0), gson.toJson(MainActivity.data.get(0)));
        editor.apply();

        if (!sortNewFirst)
            Collections.reverse(MainActivity.data);
    }

    public static boolean saveToSP(int position, Context context) {

        FormItem form = MainActivity.data.get(position);
        Gson gson = new Gson();
        form.setLastUpdate(Constants.dFormatter.format(new Date()));
        String jsonForm = gson.toJson(form);

        SharedPreferences sp = context.getSharedPreferences("Forms", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("" + form.getUID(), jsonForm);
        editor.apply();
        MainActivity.data.set(position, form);

        return false;
    }


    public static void updatePrefs(int formID, boolean enabled, Context context) {
        FormItem formItem = null;
        SharedPreferences sp = context.getSharedPreferences("Forms", Context.MODE_PRIVATE);
        String jsonForm = sp.getString("" + formID, "");
        try {

            formItem = JsonDecode.decode(jsonForm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        formItem.setEnabled(enabled);
        Gson gson = new Gson();
        jsonForm = gson.toJson(formItem);

        SharedPreferences.Editor edit = sp.edit();
        edit.putString("" + formID, jsonForm);
        edit.apply();

    }

    public static void renameForm(int formID, String newName, Context context) {
        FormItem formItem = null;
        SharedPreferences sp = context.getSharedPreferences("Forms", Context.MODE_PRIVATE);
        String jsonForm = sp.getString("" + formID, "");
        try {

            formItem = JsonDecode.decode(jsonForm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        formItem.setName(newName);
        Gson gson = new Gson();
        jsonForm = gson.toJson(formItem);

        SharedPreferences.Editor edit = sp.edit();
        edit.putString("" + formID, jsonForm);
        edit.apply();

    }

    public static void removeLocalForm(int formID, Context context) {

        SharedPreferences sp = context.getSharedPreferences("Forms", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.remove("" + formID);

        Gson gson = new Gson();
        String jsonUids = sp.getString("UIDs", "");
        ArrayList<Integer> uids = gson.fromJson(jsonUids, new TypeToken<ArrayList<Integer>>() {
        }.getType());

        int index = uids.indexOf(formID);
        uids.remove(index);
        jsonUids = gson.toJson(uids);
        editor.putString("UIDs", jsonUids);


        editor.apply();
    }

    public static String getForm(int formID, Context context) {
        SharedPreferences sp = context.getSharedPreferences("Forms", MODE_PRIVATE);
        String jsonForm = sp.getString("" + formID, null);

        return jsonForm;

    }


}
