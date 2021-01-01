package com.app.forms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.forms.R;
import com.app.forms.helpers.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    MaterialButton cancel, report;
    EditText des;
    RadioGroup radioGroup;
    String abuseType;
    String formID;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        formID = getIntent().getExtras().getString("formID");
        cancel = findViewById(R.id.cancel);
        report = findViewById(R.id.report);
        des = findViewById(R.id.des);
        ll = findViewById(R.id.ll);
        radioGroup = findViewById(R.id.rg);
        cancel.setOnClickListener(this);
        report.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report:
                //submit report;
                if (abuseType == null) {
                    ((MaterialRadioButton) radioGroup.getChildAt(5)).setError("Mandatory");
                } else if (des.getText().toString().length() == 0) {
                    des.setError("Mandatory");
                } else {
                    submit(abuseType, des.getText().toString());
                }

                break;
            case R.id.cancel:
                finish();
        }
    }

    private void submit(String type, String des) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firebaseFirestore.collection("Reports").document();
        Map<String, Object> map = new HashMap<>();
        String userID = Utils.getUserID();


        map.put("type", type);
        map.put("description", des);
        map.put("userID", userID);
        map.put("formID", formID);
        map.put("email", Utils.getUserEmailID());

        ref.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                startNewActivity();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(ll, "Error while reporting :(", Snackbar.LENGTH_SHORT).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit(type, des);
                    }
                }).setAction("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
            }
        });


    }

    private void startNewActivity() {
        Intent i = new Intent(ReportActivity.this, AfterDo.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.nudity:
                abuseType = "Nudity";
                break;
            case R.id.violence:
                abuseType = "Promotes hatred, violence or illegal.offensive activities";
                break;
            case R.id.spam:
                abuseType = "Spam, malware  or phishing";
                break;
            case R.id._private:
                abuseType = "Private and confidential information";
                break;
            case R.id.copy:
                abuseType = "Copyright infringement";
                break;
            case R.id.other:
                abuseType = "Other";
                break;
        }

    }
}