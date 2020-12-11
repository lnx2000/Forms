package com.app.forms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.forms.Items.FormItem;
import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.app.forms.fragments.AppSettingFragment;
import com.app.forms.fragments.HomeFragment;
import com.app.forms.fragments.InfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    FloatingActionButton btnAdd;
    BottomNavigationView bottomNavigation;
    FrameLayout fl;
    boolean addvis = true;
    HomeFragment homeFragment;
    AppSettingFragment appSettingFragment;
    InfoFragment infoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.btnAdd);
        bottomNavigation = findViewById(R.id.nav_view);
        bottomNavigation.setSelectedItemId(R.id.home);
        fl = findViewById(R.id.container);

        homeFragment = new HomeFragment();
        appSettingFragment = new AppSettingFragment();
        infoFragment = new InfoFragment();
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.settings:
                    btnAdd.setImageResource(R.drawable.ic_baseline_home_24);
                    addvis = false;
                    fl.removeAllViews();
                    openFragment(appSettingFragment);
                    return true;
                case R.id.info:
                    btnAdd.setImageResource(R.drawable.ic_baseline_home_24);
                    addvis = false;
                    fl.removeAllViews();
                    openFragment(infoFragment);
                    return true;
            }
            return false;
        });
        btnAdd.setOnClickListener(v -> {
            if (addvis) {
                View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_edit_text, null);
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this)
                        .setTitle("Form Title")
                        .setPositiveButton("OK", (dialog, whichButton) -> {
                            String formTitle = ((TextInputEditText) view.findViewById(R.id.edittext)).getText().toString();
                            if (formTitle.equals("")) {
                                ((TextInputEditText) view.findViewById(R.id.edittext)).setError("Field can't be empty");
                            } else {
                                startCreateFormActivity(formTitle);
                            }
                        })
                        .setNegativeButton("CANCEL", (dialog, whichButton) -> dialog.cancel());
                materialAlertDialogBuilder.setView(view);

                materialAlertDialogBuilder.show();

            } else {
                addvis = true;
                btnAdd.setImageResource(R.drawable.ic_baseline_add_24);
                bottomNavigation.setSelectedItemId(R.id.home);
                openFragment(homeFragment);
            }
        });
        openFragment(homeFragment);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "current");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void startCreateFormActivity(String formTitle) {

        FormItem formItem = new FormItem(formTitle, dateFormat.format(new Date()), generateFormUID(Constants.formUIDLength));
        homeFragment.addData(formItem);
        Intent i = new Intent(MainActivity.this, CreateFormActivity.class);
        String jsonForm = getJsonStringForm(formItem);
        i.putExtra("form", jsonForm);
        startActivityForResult(i, Constants.formAdded);

    }

    private String getJsonStringForm(FormItem formItem) {
        Gson gson = new Gson();
        return gson.toJson(formItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            if (requestCode == Constants.formAdded)
                homeFragment.refreshAdapter();
        }
    }

    private String generateFormUID(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        AlphaNumericString += AlphaNumericString.toLowerCase();
        AlphaNumericString += "0123456789";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());

            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();


    }
}