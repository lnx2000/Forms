package com.app.forms.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.app.forms.items.BaseClass;
import com.app.forms.items.FormItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static int generateUID(int n) {
        String AlphaNumericString = "0123456789";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());

            sb.append(AlphaNumericString.charAt(index));
        }

        return Integer.parseInt(sb.toString());
    }

    public static boolean isUserLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            return false;
        return true;
    }

    public static String getUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            return null;
        return user.getUid();
    }

    public static String getUserEmailID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            return null;
        return user.getEmail();

    }

    public static String jsonForm(FormItem f) {
        Gson gson = new Gson();
        return gson.toJson(f);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void showNotification(Context context, String title, String msg, int id) {

        CharSequence name = "Accepting Responses";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(name.toString(), name, importance);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, name.toString())
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(id, builder.build());

    }

    public static FormItem createForm(String formTitle) {
        FormItem formItem = new FormItem(formTitle, Constants.dFormatter.format(new Date()), Utils.generateUID(Constants.formUIDLength));
        Date d = new Date();
        formItem.getConfig().setPublishDate(Constants.dateFormatter.format(d));
        formItem.getConfig().setPublishTime("12:00 am");
        formItem.getConfig().setUnPublishTime("12:00 am");

        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, 1);
        d = c.getTime();
        formItem.getConfig().setUnPublishDate(Constants.dateFormatter.format(d));

        formItem.getForm().add(new BaseClass(Constants.typeSubmit));

        return formItem;
    }

    public static String generateSUID(int len) {

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();


    }
}
