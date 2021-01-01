package com.app.forms.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.forms.ActivityCallback;
import com.app.forms.items.BaseClass;
import com.app.forms.items.Check;
import com.app.forms.items.FormItem;
import com.app.forms.items.Text;
import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.app.forms.fragments.FormEditFragment;
import com.app.forms.fragments.FormPreviewFragment;
import com.app.forms.fragments.FormSettingsFragment;
import com.app.forms.helpers.JsonDecode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

public class CreateFormActivity extends AppCompatActivity {
    ImageView additem, save;
    NavigationView navview;
    FloatingActionButton navviewcancel;
    MaterialCardView savedmsg;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    FormEditFragment formEditFragment;
    FormSettingsFragment formSettingsFragment;
    FormPreviewFragment formPreviewFragment;
    ArrayList<BaseClass> data;
    FormItem form;
    MaterialCardView saveCard, additemfab;
    int position;
    int fromUID;
    ActivityCallback activityCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);
        additem = findViewById(R.id.additem);
        navview = findViewById(R.id.drawer);
        navviewcancel = findViewById(R.id.nav_view_cancel);
        save = findViewById(R.id.save);
        savedmsg = findViewById(R.id.savedmsg);
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        saveCard = findViewById(R.id.savecard);
        additemfab = findViewById(R.id.additemfab);

        getSupportActionBar().hide();

        position = getIntent().getExtras().getInt("position");
        int fragmenttype = getIntent().getExtras().getInt("fragment");
        try {
            form = JsonDecode.decode(getIntent().getExtras().getString("Form"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String formTitle = form.getName();

        fromUID = form.getUID();

        setTitle(formTitle);


        data = form.getForm();

        formEditFragment = new FormEditFragment(data);
        formSettingsFragment = new FormSettingsFragment(form.getConfig());
        formPreviewFragment = new FormPreviewFragment(form.getForm(), form.getUID(), true);


        if (fragmenttype == Constants.editFragment) {
            bottomNavigationView.setSelectedItemId(R.id.edit);
            openFragment(formEditFragment);
        } else if (fragmenttype == Constants.settingFragment) {
            bottomNavigationView.setSelectedItemId(R.id.setting);
            openFragment(formSettingsFragment);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit:
                    openFragment(formEditFragment);
                    break;
                case R.id.setting:
                    openFragment(formSettingsFragment);
                    break;
                case R.id.preview:
                    openFragment(formPreviewFragment);
                    break;
            }
            return true;
        });

        saveCard.setOnClickListener(v -> {
            saveDataToSP();
        });
        save.setOnClickListener(v -> {
            saveDataToSP();
        });

        additemfab.setOnClickListener(v -> visibility(true));
        additem.setOnClickListener(v -> visibility(true));
        navviewcancel.setOnClickListener(v -> visibility(false));

        navview.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.textmsg:
                    data.add(new BaseClass(Constants.typeTextMsg));
                    formEditFragment.notifyAdapterItemAdded();
                    break;
                case R.id.textfield:
                    data.add(new Text(Constants.typeTextField));
                    formEditFragment.notifyAdapterItemAdded();
                    break;
                case R.id.radiofield:
                    data.add(new Check(Constants.typeSingleCheck));
                    formEditFragment.notifyAdapterItemAdded();
                    break;
                case R.id.checkfield:
                    data.add(new Check(Constants.typeMultipleCheck));
                    formEditFragment.notifyAdapterItemAdded();
                    break;
                case R.id.pollfield:
                    data.add(new Check(Constants.typepoll));
                    formEditFragment.notifyAdapterItemAdded();
                    break;
                case R.id.ratingfield:
                    data.add(new BaseClass(Constants.typerating));
                    formEditFragment.notifyAdapterItemAdded();
                    break;
                case R.id.uploadfield:
                    data.add(new BaseClass(Constants.typeupload));
                    formEditFragment.notifyAdapterItemAdded();
                    break;
            }
            formEditFragment.smoothScroll(data.size());
            visibility(false);
            return true;
        });


    }


    private boolean saveDataToSP() {

        Gson gson = new Gson();
        form.setLastUpdate(Constants.dateFormatter.format(new Date()));
        String jsonForm = gson.toJson(form);

        SharedPreferences sp = getSharedPreferences("Forms", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("" + form.getUID(), jsonForm);
        editor.apply();
        MainActivity.data.set(position, form);


        showMsg(true);

        return false;
    }

    public boolean visibility(boolean vis) {
        if (vis) {
            navviewcancel.setVisibility(View.VISIBLE);
            navview.setVisibility(View.VISIBLE);
        } else {
            navviewcancel.setVisibility(View.GONE);
            navview.setVisibility(View.GONE);
        }
        return true;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    /*public void StartImageChooserActivity(int rescode) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, rescode);
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            formEditFragment.activityResult(selectedImage, requestCode);
        }
    }*/

    private void showMsg(boolean res) {
        if (res) {
            ((TextView) savedmsg.findViewById(R.id.savetv)).setText(Constants.changesSaved);
        } else {
            ((TextView) savedmsg.findViewById(R.id.savetv)).setText(Constants.changesNotSaved);
        }
        savedmsg.setVisibility(View.VISIBLE);
        savedmsg.postDelayed(() -> savedmsg.setVisibility(View.GONE), 1000);
    }

    public void getFile(ActivityCallback activityCallback) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        this.activityCallback = activityCallback;
        startActivityForResult(i, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Uri f = data.getData();
            activityCallback.uploadFile(f);

        }
    }


}