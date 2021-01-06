package com.app.forms.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.app.forms.helpers.JsonDecode;
import com.app.forms.helpers.Utils;
import com.app.forms.items.BaseClass;
import com.app.forms.items.FormItem;
import com.app.forms.items.ItemResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShowResponses extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    int formID;
    FormItem formItem;
    LinearLayout ll;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    Map<String, Object> map = null;
    ArrayList<BaseClass> formItems = null;
    ArrayList<ItemResponse> responses = null;
    ArrayList<String> emails = null;
    ArrayList<Object> counts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_responses);

        ll = findViewById(R.id.linearLayout);
        formID = getIntent().getExtras().getInt("fromID");


        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Forms")
                .document("" + formID)
                .collection("Responses");
        documentReference = firebaseFirestore.collection("Forms")
                .document("" + formID);

        /*
        String jsonForm = SPOps.getForm(formID, this);
        try {
            formItem = JsonDecode.decode(jsonForm);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        if (Utils.isUserLoggedIn())
            loadForm();
        else login();


    }

    private void get_piechart(int pos) {

        ArrayList<Integer> color = getColors();
        View v = LayoutInflater.from(this).inflate(R.layout.piechart, null);
        PieChart pieChart = v.findViewById(R.id.pie);
        ArrayList<PieEntry> entry = new ArrayList<>();

        /*ArrayList<Integer> rad = (ArrayList<Integer>) data.get(pos);
        ArrayList<String> str = ((RadioClass) basep.get(pos)).getRadios();*/

        /*for (int j = 0; j < rad.size(); j++)
            entry.add(new PieEntry(rad.get(j), str.get(j)));*/

        PieDataSet pieDataSet = new PieDataSet(entry, "");

        pieDataSet.setColors(color);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("");
        pieChart.setUsePercentValues(true);
        pieChart.animateXY(1000, 1000);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setTextColor(Color.BLACK);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.invalidate();

        /*((TextView) v.findViewById(R.id.res_tv)).setText(((RadioClass) basep.get(pos)).getTitle());*/
        ll.addView(v);
    }

    private void get_bar(int pos) {
        ArrayList<Integer> color = getColors();
        View v = LayoutInflater.from(this).inflate(R.layout.barchart, null);
        BarChart barChart = v.findViewById(R.id.bar);
        ArrayList<BarEntry> entry = new ArrayList<>();

        /*int type = types.get(pos);
        ArrayList<Integer> rad = (ArrayList<Integer>) data.get(pos);
        final ArrayList<String> str = ((CheckClass) basep.get(pos)).getChecks();*/


        ArrayList<LegendEntry> ale = new ArrayList<>();

        /*for (int j = 0; j < rad.size(); j++) {
            entry.add(new BarEntry(j * (0.8f), rad.get(j)));
            LegendEntry le = new LegendEntry();
            le.label = str.get(j);
            le.formColor = color.get(j);
            ale.add(le);
        }*/
        BarDataSet barDataSet = new BarDataSet(entry, "");
        barDataSet.setColors(color);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setDrawValues(true);
        barDataSet.setValueFormatter(new DefaultValueFormatter(0));
        BarData bardata = new BarData(barDataSet);
        bardata.setHighlightEnabled(false);

        bardata.setBarWidth(0.6f);
        barChart.setData(bardata);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(100);

        XAxis xa = barChart.getXAxis();
        xa.setDrawGridLines(false);
        xa.setDrawAxisLine(true);
        xa.setPosition(XAxis.XAxisPosition.BOTTOM);
        xa.setGranularity(0.8f);
        xa.setTextSize(12);

        xa.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.setScaleEnabled(false);
        bardata.setValueTextSize(10);
        bardata.setValueTextColor(Color.WHITE);
        barChart.setFitBars(true);
        barChart.animateXY(1000, 1000);

        Legend legend = barChart.getLegend();

        legend.setCustom(ale);
        legend.setWordWrapEnabled(true);
        legend.setTextColor(Color.WHITE);

        barChart.invalidate();

        /*((TextView)v.findViewById(R.id.res_tv)).setText(((CheckClass) basep.get(pos)).getTitle());*/
        ll.addView(v);

    }

    private ArrayList<Integer> getColors() {
        ArrayList<Integer> color = new ArrayList<>();
        for (int c : ColorTemplate.PASTEL_COLORS)
            color.add(c);
        color.add(ColorTemplate.rgb("#4e5560"));
        color.add(ColorTemplate.rgb("#866a67"));
        color.add(ColorTemplate.rgb("#9a9385"));
        color.add(ColorTemplate.rgb("#c5bfa7"));
        color.add(ColorTemplate.rgb("#e6dbc8"));

        return color;
    }

    private void loadResponses() {
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Gson gson = new Gson();
                    emails = new ArrayList<>();
                    responses = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ItemResponse response = gson.fromJson(doc.getString("response"), ItemResponse.class);
                        String mail = null;
                        if (doc.contains("email"))
                            mail = doc.getString("email");
                        emails.add(mail);
                        responses.add(response);
                    }

                    prepareForm();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(ll, "Error while loading responses. Please try again later", BaseTransientBottomBar.LENGTH_SHORT);
            }
        });


    }

    private void prepareForm() {
        try {
            formItems = JsonDecode.decodeArray((String) map.get("Form"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        prepareCountLists();
    }

    private void prepareCountLists() {
        counts = new ArrayList<>();
        for (BaseClass b : formItems) {
            switch (b.getType()) {
                case Constants.typeTextMsg:
                    counts.add(null);
                    break;
                case Constants.typeTextField:
                    ArrayList<String> texts = new ArrayList<>();
                    for (ItemResponse r : responses) {
                        texts.add(r.getText());
                    }

                    counts.add(texts);
                    break;
                case Constants.typeSingleCheck:
                    Map<Integer, Integer> sCounts = new HashMap<>();
                    for (ItemResponse r : responses) {
                        int idx = r.getSchecked();
                        if (sCounts.containsKey(idx)) {
                            sCounts.put(idx, sCounts.get(idx) + 1);
                        } else sCounts.put(idx, 1);
                    }
                    counts.add(sCounts);
                case Constants.typeMultipleCheck:

                    Map<Integer, Integer> mCounts = new HashMap<>();
                    for (ItemResponse r : responses) {
                        int idx = r.getSchecked();
                        Set<Integer> checks = r.getMchecked();
                        for (int midx : checks) {
                            if (mCounts.containsKey(midx)) {
                                mCounts.put(midx, mCounts.get(midx) + 1);
                            } else mCounts.put(midx, 1);
                        }
                    }
                    counts.add(mCounts);
                    break;
                case Constants.typepoll:
                    Map<Integer, Integer> pCounts = new HashMap<>();
                    for (ItemResponse r : responses) {
                        int idx = r.getSchecked();
                        if (pCounts.containsKey(idx)) {
                            pCounts.put(idx, pCounts.get(idx) + 1);
                        } else pCounts.put(idx, 1);
                    }
                    counts.add(pCounts);
                    break;
                case Constants.typerating:
                    ArrayList<Integer> rates = new ArrayList<>();


                    ///TODO implements responses counts
                    // TODO it will give null exceptions as some fields are not mandatory
                    //TODO  handle null fiels properly


            }


        }


    }

    private ArrayList<BaseClass> loadForm() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                map = documentSnapshot.getData();
                loadResponses();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(ll, "Error while loading form. Please try again later", BaseTransientBottomBar.LENGTH_SHORT);
            }
        });

        return null;
    }

    private void parseForm() {


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
    }

    private void firebaseAuthWithGoogle(String idToken) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        /*if (((boolean) LoadFormActivity.this.map.get("loginToSubmit") || !(boolean) LoadFormActivity.this.map.get("allowEdit"))) {
                            //user is already logged in
                            retrive();
                        }*/
                        loadForm();
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }

                });
    }
}