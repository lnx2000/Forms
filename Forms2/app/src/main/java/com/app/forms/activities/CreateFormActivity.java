package com.app.forms.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.forms.Items.BaseClass;
import com.app.forms.Items.Check;
import com.app.forms.Items.FormConfig;
import com.app.forms.Items.FormItem;
import com.app.forms.Items.Text;
import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.app.forms.fragments.FormEditFragment;
import com.app.forms.fragments.FormSettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CreateFormActivity extends AppCompatActivity {

    ImageView additem, save;
    NavigationView navview;
    FloatingActionButton navviewcancel;
    MaterialCardView savedmsg;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    FormEditFragment formEditFragment;
    FormSettingsFragment formSettingsFragment;
    ArrayList<BaseClass> data;
    FormItem form;

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


        String jsonStringform = getIntent().getExtras().getString("form");
        form = getFromFromJson(jsonStringform);


        String formTitle = form.getName();
        String fromUID = form.getUID();

        setTitle(formTitle);


        data = form.getForm();

        formEditFragment = new FormEditFragment(data);
        formSettingsFragment = new FormSettingsFragment(form.getConfig());


        openFragment(formEditFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            switch (item.getItemId()) {
                case R.id.edit:
                    openFragment(formEditFragment);
                    break;
                case R.id.setting:
                    openFragment(formSettingsFragment);
                    break;
                case R.id.preview:
                    break;
            }
            return true;
        });


        save.setOnClickListener(v -> {
            saveDataToFirebase();
            //TODO: save arraylist(data) in shared preferences if changes can't be saved to firebase
        });

        additem.setOnClickListener(v -> visibility(true));
        navviewcancel.setOnClickListener(v -> visibility(false));

        navview.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
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
                    data.add(new Text(Constants.typerating));
                    formEditFragment.notifyAdapterItemAdded();
                    break;
                case R.id.uploadfield:
                    data.add(new Text(Constants.typeupload));
                    formEditFragment.notifyAdapterItemAdded();
                    break;
            }
            visibility(false);
            return true;
        });

    }

    private FormItem getFromFromJson(String jsonStringform) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStringform, FormItem.class);
    }

    private boolean saveDataToFirebase() {
        Gson gson = new Gson();
        String jsonForm = gson.toJson(form);
        Log.e("123", jsonForm);
        firebaseSavedInstanceCallback(false);

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
        transaction.replace(R.id.frameLayout, fragment, "current");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void StartImageChooserActivity(int rescode) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, rescode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            formEditFragment.activityResult(selectedImage, requestCode);
        }
    }

    private void firebaseSavedInstanceCallback(boolean res) {
        if (res) {
            ((TextView) savedmsg.findViewById(R.id.savetv)).setText(Constants.changesSaved);
        } else {
            ((TextView) savedmsg.findViewById(R.id.savetv)).setText(Constants.changesNotSaved);
        }
        savedmsg.setVisibility(View.VISIBLE);
        savedmsg.postDelayed(() -> savedmsg.setVisibility(View.GONE), 1000);


    }

    private String getStringConfig(FormConfig formConfig) {
        Gson gson = new Gson();
        return gson.toJson(formConfig);
    }
}