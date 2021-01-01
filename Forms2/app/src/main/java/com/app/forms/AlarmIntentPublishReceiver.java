package com.app.forms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.app.forms.Items.FormItem;
import com.app.forms.helpers.JsonDecode;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.app.forms.helpers.Utils.showNotification;

public class AlarmIntentPublishReceiver extends BroadcastReceiver {
    // already check if user is logged in or not

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean enable = intent.getExtras().getBoolean("enable");
        int formID = intent.getExtras().getInt("form");
        if (enable) {
            uploadForm(formID, context);
        }


    }

    private void uploadForm(int formID, Context context) {
        SharedPreferences sp = context.getSharedPreferences("Forms", Context.MODE_PRIVATE);
        String jsonForm = sp.getString("" + formID, "");

        Map<String, Object> map = getMap(jsonForm, true);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firebaseFirestore.collection("Forms").document("" + map.get("formID"));

        ref.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(Void aVoid) {
                //updatePrefs(formID, context, true);
                showNotification(context, "Form Published", "Form is now accepting responses", 1);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFailure(@NonNull Exception e) {
                showNotification(context, "Publishing...", "Error occured while invoking form", 1);
                updatePrefs(formID, false, context);
            }
        });


    }


    private Map<String, Object> getMap(String jsonForm, boolean enabled) {
        FormItem formItem = null;
        Gson gson = new Gson();
        try {
            formItem = JsonDecode.decode(jsonForm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();

        String formString = gson.toJson(formItem.getForm());
        map.put("name", formItem.getName());
        map.put("Form", formString);
        map.put("userID", formItem.getUserID());
        map.put("Enabled", enabled);
        formItem.getConfig().setAcceptingResponses(true);
        map.put("acceptingResponses", formItem.getConfig().isAcceptingResponses());
        map.put("loginToSubmit", formItem.getConfig().isLoginToSubmit());
        map.put("allowEdit", formItem.getConfig().isAllowEdit());
        map.put("recordEmail", formItem.getConfig().isRecordEmail());
        map.put("shuffle", formItem.getConfig().isShuffle());
        map.put("sendResponseCopy", formItem.getConfig().isSendResponseCopy());
        map.put("formID", formItem.getUID());

        return map;
    }


    private void updatePrefs(int formID, boolean enabled, Context context) {
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

}
