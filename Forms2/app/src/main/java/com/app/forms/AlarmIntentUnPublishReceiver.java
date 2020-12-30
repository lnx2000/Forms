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

public class AlarmIntentUnPublishReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("123", "unpulish intent received");

        boolean enable = intent.getExtras().getBoolean("enable");
        int formID = intent.getExtras().getInt("form");
        if (!enable) {
            removeForm(formID, context);
        }

    }


    private void removeForm(int formID, Context context) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firebaseFirestore.collection("Forms").document("" + formID);
        Map<String, Object> map = new HashMap<>();
        map.put("acceptingResponses", false);
        map.put("text", "This form is not accepting responses anymore");
        ref.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(Void aVoid) {
                updatePrefs(formID, context, false);
                showNotification(context, "Form Unpulished", "Form is no more accepting responses", 2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFailure(@NonNull Exception e) {
                showNotification(context, "Unpublishing...", "Error occured while revoking form", 2);
            }
        });


    }

    private void updatePrefs(int formID, Context context, boolean enabled) {
        FormItem formItem = null;
        SharedPreferences sp = context.getSharedPreferences("Forms", Context.MODE_PRIVATE);
        String jsonForm = sp.getString("" + formID, "");
        try {

            formItem = JsonDecode.decode(jsonForm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        formItem.setEnabled(enabled);
        formItem.getConfig().setAcceptingResponses(false);
        Gson gson = new Gson();
        jsonForm = gson.toJson(formItem);

        SharedPreferences.Editor edit = sp.edit();
        edit.putString("" + formID, jsonForm);
        edit.apply();

    }


}
