package com.app.forms.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.forms.R;

public class AfterDo extends AppCompatActivity {
    TextView report, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thank_you);
        report = findViewById(R.id.report);
        msg = findViewById(R.id.msg);
        msg.setText("Report has been submitted successfully!");
        report.setVisibility(View.GONE);

    }
}