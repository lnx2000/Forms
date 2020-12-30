package com.app.forms.activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.forms.R;

import java.util.List;

public class LoadFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_form);

        Uri data = getIntent().getData();
        List<String> params = data.getPathSegments();
        int formID = Integer.parseInt(params.get(1));


    }
}