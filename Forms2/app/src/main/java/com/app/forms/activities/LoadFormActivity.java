package com.app.forms.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.app.forms.fragments.FormPreviewFragment;
import com.app.forms.helpers.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class LoadFormActivity extends AppCompatActivity {
    Map<String, Object> map = null;
    FirebaseFirestore firebaseFirestore;
    DocumentReference ref;
    ConstraintLayout cl;
    ProgressBar progressBar;
    FormPreviewFragment formPreviewFragment;
    int formID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_form);
        cl = findViewById(R.id.cl);
        progressBar = findViewById(R.id.progressbar);
        getSupportActionBar().hide();


        Uri data = getIntent().getData();
        List<String> params = data.getPathSegments();

        formID = Integer.parseInt(params.get(1));
        firebaseFirestore = FirebaseFirestore.getInstance();
        ref = firebaseFirestore.collection("Forms").document("" + formID);

        retrive();


    }

    private void retrive() {

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    map = documentSnapshot.getData();

                    progressBar.setVisibility(View.GONE);
                    loadForm(map);

                } else {
                    //Toast.makeText(LoadFormActivity.this, "No such form exists :(", Toast.LENGTH_SHORT).show();
                    Snackbar.make(cl, "No such form exists :(", BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(LoadFormActivity.this, "Please check your network connection :(", Toast.LENGTH_SHORT).show();
                Snackbar.make(cl, "Please check your connection :(", BaseTransientBottomBar.LENGTH_SHORT)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                retrive();
                            }
                        }).show();

            }
        });

    }

    private void loadForm(Map<String, Object> map) {
        boolean accepting = (boolean) map.get("acceptingResponses");
        if (accepting) {
            if (verifyallConditions(map)) {


            }
        } else {
            String name = (String) map.get("name");
            View v = LayoutInflater.from(this).inflate(R.layout.not_accepting, null);
            ((TextView) v.findViewById(R.id.title)).setText(name);
            ((TextView) v.findViewById(R.id.subtext)).setText("The " + name + Constants.notAccepting);
            v.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            TextView report = v.findViewById(R.id.report);
            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LoadFormActivity.this, ReportActivity.class);
                    i.putExtra("formID", ""+formID);
                    startActivity(i);
                    finish();
                }
            });


            cl.addView(v);
        }


        //String jForm = (String) map.get("Form");
    }

    private boolean verifyallConditions(Map<String, Object> map) {
        if ((boolean) map.get("loginToSubmit")) {
            if (!Utils.isUserLoggedIn()) {
                View v = LayoutInflater.from(this).inflate(R.layout.login_required, null);
                MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(this);
                alert.setView(v);
                alert.show();
                return false;
            }
        } else if ((boolean) map.get("recordEmail")) {
            if (!Utils.isUserLoggedIn()) {
                View v = LayoutInflater.from(this).inflate(R.layout.login_required, null);
                MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(this);
                alert.setView(v);
                alert.show();
                return false;
            }
        }


        return true;
    }


    public void showThankYouPage() {
        cl.removeAllViews();
        View v = LayoutInflater.from(this).inflate(R.layout.thank_you, null);
        v.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cl.addView(v);
    }
}