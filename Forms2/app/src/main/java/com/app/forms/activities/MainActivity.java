package com.app.forms.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.forms.AlarmIntentPublishReceiver;
import com.app.forms.AlarmIntentUnPublishReceiver;
import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.app.forms.fragments.AppSettingFragment;
import com.app.forms.fragments.HomeFragment;
import com.app.forms.fragments.InfoFragment;
import com.app.forms.helpers.SPOps;
import com.app.forms.helpers.Utils;
import com.app.forms.items.FormItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<FormItem> data;


    FloatingActionButton btnAdd;
    BottomNavigationView bottomNavigation;
    FrameLayout fl;
    boolean addvis = true;
    HomeFragment homeFragment;
    AppSettingFragment appSettingFragment;
    InfoFragment infoFragment;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.btnAdd);
        bottomNavigation = findViewById(R.id.nav_view);
        bottomNavigation.setSelectedItemId(R.id.home);
        fl = findViewById(R.id.container);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images/");


        data = SPOps.loadSP(this);

        homeFragment = new HomeFragment();
        appSettingFragment = new AppSettingFragment();
        infoFragment = new InfoFragment();

        openFragment(homeFragment);


        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.settings:
                    btnAdd.setImageResource(R.drawable.ic_baseline_home_24);
                    addvis = false;
                    openFragment(appSettingFragment);
                    return true;
                case R.id.info:
                    btnAdd.setImageResource(R.drawable.ic_baseline_home_24);
                    addvis = false;
                    openFragment(infoFragment);
                    return true;
            }
            return false;
        });
        btnAdd.setOnClickListener(v -> {
            if (addvis) {
                startCreateFormActivity("Untitled Form");

            } else {
                addvis = true;
                btnAdd.setImageResource(R.drawable.ic_baseline_add_24);
                bottomNavigation.setSelectedItemId(R.id.home);
                openFragment(homeFragment);
            }
        });
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void startCreateFormActivity(String formTitle) {

        FormItem formItem = Utils.createForm(formTitle);
        if (homeFragment.sortNewFirst)
            data.add(0, formItem);
        else data.add(formItem);
        homeFragment.addData();
        SPOps.newForm(this, homeFragment.sortNewFirst);
        if (homeFragment.sortNewFirst)
            startCreateFormActivity(0, Constants.editFragment);
        else startCreateFormActivity(data.size() - 1, Constants.editFragment);

    }

    public void startCreateFormActivity(int position, int fragment) {
        Intent i = new Intent(MainActivity.this, CreateFormActivity.class);
        String jForm = Utils.jsonForm(data.get(position));
        i.putExtra("Form", jForm);
        i.putExtra("position", position);
        i.putExtra("fragment", fragment);
        startActivityForResult(i, Constants.returnFormCreateFormActivity);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.returnFormCreateFormActivity) {
            homeFragment.refreshAdapter();
        }
    }


    public boolean makeFormPublic(int position) {
        Date publishDate = null, unPublishDate = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        data.get(position).setUserID(user.getUid());
        SPOps.saveToSP(position, this);
        String pDate = data.get(position).getConfig().getPublishDate() + data.get(position).getConfig().getPublishTime();
        String uDate = data.get(position).getConfig().getUnPublishDate() + data.get(position).getConfig().getUnPublishTime();
        try {
            publishDate = Constants.parseDateTime.parse(pDate);
            unPublishDate = Constants.parseDateTime.parse(uDate);
        } catch (ParseException e) {
            return false;

        }
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent p_i = new Intent(MainActivity.this, AlarmIntentPublishReceiver.class);
        p_i.putExtra("enable", true);
        p_i.putExtra("form", data.get(position).getUID());
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, data.get(position).getUID(), p_i, 0);
        am.set(AlarmManager.RTC_WAKEUP, publishDate.getTime(), pi);
        Toast.makeText(this, "publish alarm set!", Toast.LENGTH_SHORT).show();
        SPOps.updatePrefs(data.get(position).getUID(), true, this);

        Intent u_i = new Intent(MainActivity.this, AlarmIntentUnPublishReceiver.class);
        u_i.putExtra("enable", false);
        u_i.putExtra("form", data.get(position).getUID());
        u_i.putExtra("name", data.get(position).getName());
        PendingIntent ui = PendingIntent.getBroadcast(MainActivity.this, data.get(position).getUID(), u_i, 0);
        am.set(AlarmManager.RTC_WAKEUP, unPublishDate.getTime(), ui);
        Toast.makeText(this, "unpublish alarm set!", Toast.LENGTH_SHORT).show();

        return true;

    }


    public void unpublishForm(int position) {

        int formID = data.get(position).getUID();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firebaseFirestore.collection("Forms").document("" + formID);
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(Void aVoid) {
                SPOps.updatePrefs(formID, false, MainActivity.this);
                Utils.showNotification(MainActivity.this, "Form Unpulished", "Form is no more accepting responses", 2);
                data.get(position).setEnabled(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.showNotification(MainActivity.this, "Unpublishing...", "Error occured while revoking form", 2);
            }
        });
    }


    public void removeItem(int position) {

        data.remove(position);
        homeFragment.refreshAdapter();

    }

    public void deleteForm(int position) {
        String name = data.get(position).getName();
        FormItem f = data.get(position);
        data.remove(position);
        homeFragment.refreshAdapter();
        Snackbar sb = Snackbar.make(fl, name + " has been deleted.", 5000)
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_SWIPE)
                            SPOps.removeLocalForm(f.getUID(), MainActivity.this);
                    }
                }).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.add(position, f);
                        homeFragment.refreshAdapter();
                    }
                }).setAnchorView(btnAdd).setTextColor(Color.BLACK);

        sb.getView().setBackgroundColor(Color.WHITE);

        sb.show();

    }
}