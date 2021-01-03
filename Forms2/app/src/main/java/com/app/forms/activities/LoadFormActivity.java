package com.app.forms.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.app.forms.ActivityCallback;
import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.app.forms.fragments.FormPreviewFragment;
import com.app.forms.helpers.JsonDecode;
import com.app.forms.helpers.Utils;
import com.app.forms.items.BaseClass;
import com.app.forms.items.ItemResponse;
import com.app.forms.items.Response;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadFormActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    Map<String, Object> map = null;
    FirebaseFirestore firebaseFirestore;
    DocumentReference ref;
    ConstraintLayout cl;
    ProgressBar progressBar;
    FormPreviewFragment formPreviewFragment;
    int formID;
    ArrayList<BaseClass> _form;
    Response response;
    boolean already_filled = false;
    boolean loginRequired = true;
    String documentName = null;
    ActivityCallback activityCallback;

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

        //retrive();
        formretrive();

    }

    private void retrive() {
        progressBar.setVisibility(View.VISIBLE);
        documentName = Utils.getUserID();
        DocumentReference ref = firebaseFirestore.collection("Forms").
                document("" + formID).
                collection("Responses")
                .document(Utils.getUserID());

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    already_filled = true;
                }
                loadForm(map);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!Utils.isUserLoggedIn()) {
                    Snackbar.make(cl, "You need to login first", BaseTransientBottomBar.LENGTH_INDEFINITE)
                            .setAction("LOGIN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    login();
                                }
                            }).show();
                    progressBar.setVisibility(View.GONE);

                } else {
                    //Toast.makeText(LoadFormActivity.this, "Please check your network connection :(", Toast.LENGTH_SHORT).show();
                    Snackbar.make(cl, "Please check your connection :(", BaseTransientBottomBar.LENGTH_SHORT)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    retrive();
                                }
                            }).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

    }

    private void formretrive() {
        progressBar.setVisibility(View.VISIBLE);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    map = documentSnapshot.getData();
                    progressBar.setVisibility(View.GONE);

                    if (((boolean) map.get("loginToSubmit") || !(boolean) map.get("allowEdit")) && !Utils.isUserLoggedIn()) {
                        Snackbar.make(cl, "You need to login first", BaseTransientBottomBar.LENGTH_INDEFINITE)
                                .setAction("LOGIN", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        login();
                                    }
                                }).show();
                        progressBar.setVisibility(View.GONE);
                    } else if (((boolean) map.get("loginToSubmit") || !(boolean) map.get("allowEdit"))) {
                        //user is already logged in
                        retrive();
                    } else {
                        loadForm(map);
                    }
                } else {
                    //Toast.makeText(LoadFormActivity.this, "No such form exists :(", Toast.LENGTH_SHORT).show();
                    Snackbar.make(cl, "No such form exists :(", BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!Utils.isUserLoggedIn()) {
                    Snackbar.make(cl, "You need to login first", BaseTransientBottomBar.LENGTH_INDEFINITE)
                            .setAction("LOGIN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    login();
                                }
                            }).show();
                    progressBar.setVisibility(View.GONE);

                } else {
                    //Toast.makeText(LoadFormActivity.this, "Please check your network connection :(", Toast.LENGTH_SHORT).show();
                    Snackbar.make(cl, "Please check your connection :(", BaseTransientBottomBar.LENGTH_SHORT)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    formretrive();
                                }
                            }).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void login() {
        GoogleSignInClient googleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
            }
        }
        if (requestCode == 1000) {
            Uri f = data.getData();
            activityCallback.uploadFile(f);

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ;
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        if (((boolean) map.get("loginToSubmit") || !(boolean) map.get("allowEdit"))) {
                            //user is already logged in
                            retrive();
                        }
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void loadForm(Map<String, Object> map) {
        boolean accepting = (boolean) map.get("acceptingResponses");
        if (accepting) {
            if (verifyallConditions(map)) {

                formPreview(map);

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
                    i.putExtra("formID", "" + formID);
                    startActivity(i);
                    finish();
                }
            });


            cl.addView(v);
        }


        //String jForm = (String) map.get("Form");
    }

    private void formPreview(Map<String, Object> map) {

        response = new Response();
        response.setEmail(Utils.getUserEmailID());
        String form = (String) map.get("Form");
        try {
            _form = JsonDecode.decodeArray(form);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ((boolean) map.get("shuffle")) {
            Collections.shuffle(_form.subList(0, _form.size() - 1));
        }

        boolean show_count = (boolean) map.get("showCount");
        FormPreviewFragment formPreviewFragment = new FormPreviewFragment(_form, ((Long) map.get("formID")).intValue(), false, show_count, response, (String) map.get("name"));
        openFragment(formPreviewFragment);

    }

    private void openFragment(FormPreviewFragment fragment) {
        progressBar.setVisibility(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cl, fragment);
        transaction.commit();
    }

    private boolean verifyallConditions(Map<String, Object> map) {
        if (!(boolean) map.get("allowEdit")) {
            if (already_filled) {
                //start intent
                Intent i = new Intent(LoadFormActivity.this, AfterDo.class);
                i.putExtra("resCode", Constants.alreadyfilled);
                i.putExtra("formID", formID);
                startActivity(i);
                finish();
                return false;
            }
        }
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

    public void submit() {
        if (checkAllMandatory()) {

            uploadResponse();

        } else {
            Snackbar.make(cl, "Please fill all mandatory fields", BaseTransientBottomBar.LENGTH_SHORT).show();
        }


    }

    private void uploadResponse() {
        Gson gson = new Gson();
        if (documentName == null)
            documentName = Utils.generateSUID(28);
        DocumentReference ref = firebaseFirestore.collection("Forms").
                document("" + formID).
                collection("Responses")
                .document(documentName);
        Map<String, String> map = new HashMap<>();
        map.put("email", response.getEmail());
        map.put("response", gson.toJson(response.getResponses()));


        ref.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(LoadFormActivity.this, AfterDo.class);
                i.putExtra("resCode", Constants.responseSubmitted);
                i.putExtra("formID", formID);
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(cl, "Error while submitting response", BaseTransientBottomBar.LENGTH_SHORT).show();

            }
        });


    }

    private boolean checkAllMandatory() {
        ArrayList<ItemResponse> itemResponses = response.getResponses();
        for (int i = 0; i < _form.size(); i++) {
            Log.e("123", "" + _form.get(i).isMandatory() + " " + itemResponses.get(i).isMandatory());
            if (_form.get(i).isMandatory() && !itemResponses.get(i).isMandatory()) {
                Gson gson = new Gson();
                Log.e("123", gson.toJson(itemResponses.get(i)));
                Log.e("123", gson.toJson(_form.get(i)));
                return false;
            }
        }
        return true;
    }

    public void getFile(ActivityCallback activityCallback) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        this.activityCallback = activityCallback;
        startActivityForResult(i, 1000);

    }

}