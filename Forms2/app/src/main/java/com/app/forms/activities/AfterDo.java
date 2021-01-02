package com.app.forms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.forms.R;
import com.app.forms.constants.Constants;

public class AfterDo extends AppCompatActivity {
    TextView report, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thank_you);
        report = findViewById(R.id.report);
        msg = findViewById(R.id.msg);
        int formID = getIntent().getExtras().getInt("formID");
        int resCosde = getIntent().getExtras().getInt("resCode");
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AfterDo.this, ReportActivity.class);
                i.putExtra("formID", "" + formID);
                startActivity(i);
                finish();

            }
        });

        if (resCosde == Constants.reportSubmitted) {
            msg.setText("Report has been submitted successfully!");
            report.setVisibility(View.GONE);
        } else if (resCosde == Constants.alreadyfilled) {
            msg.setText("You've already responded");
            report.setVisibility(View.VISIBLE);
        } else {
            msg.setText("Your responses has been recorded!");
            report.setVisibility(View.VISIBLE);
        }

    }
}