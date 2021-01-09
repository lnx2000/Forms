package com.app.forms.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.forms.R;
import com.app.forms.helpers.JsonDecode;
import com.app.forms.helpers.Utils;
import com.app.forms.items.BaseClass;
import com.app.forms.items.Check;
import com.app.forms.items.FormItem;
import com.app.forms.items.ItemResponse;
import com.app.forms.items.Response;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.app.forms.constants.Constants.typeMultipleCheck;
import static com.app.forms.constants.Constants.typeSingleCheck;
import static com.app.forms.constants.Constants.typeSubmit;
import static com.app.forms.constants.Constants.typeTextField;
import static com.app.forms.constants.Constants.typeTextMsg;
import static com.app.forms.constants.Constants.typepoll;
import static com.app.forms.constants.Constants.typerating;
import static com.app.forms.constants.Constants.typeupload;

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
    ArrayList<Response> responses = null;
    ArrayList<String> emails = null;
    ArrayList<Object> counts = null;
    LinearProgressIndicator progressIndicator;
    TextView title;
    MaterialCardView titilecard;
    ProgressBar progressBar;
    ShapeableImageView download;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_responses);

        getSupportActionBar().hide();

        ll = findViewById(R.id.linearLayout);
        formID = getIntent().getExtras().getInt("fromID");
        progressIndicator = findViewById(R.id.progressbar);
        title = findViewById(R.id.title);
        titilecard = findViewById(R.id.titlecard);
        progressBar = findViewById(R.id.cprogress);
        download = findViewById(R.id.download);
        constraintLayout = findViewById(R.id.parent_layout);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = Bitmap.createBitmap(ll.getWidth(), ll.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);

                canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
                ll.draw(canvas);

                Date date = new Date();

                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_", Locale.getDefault());
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, format.format(date) + map.get("name"), "nothing");

                Snackbar sb = Snackbar.make(ll, "Responses Saved :)", BaseTransientBottomBar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(Color.WHITE);
                sb.setTextColor(Color.BLACK);
                sb.show();


            }
        });


        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Forms")
                .document("" + formID)
                .collection("Responses");
        documentReference = firebaseFirestore.collection("Forms")
                .document("" + formID);


        if (Utils.isUserLoggedIn())
            loadForm();
        else login();


    }

    private void getPie(int pos) {

        ArrayList<Integer> color = getColors();
        View v = LayoutInflater.from(this).inflate(R.layout.piechart, null);
        PieChart pieChart = v.findViewById(R.id.pie);
        ArrayList<PieEntry> entry = new ArrayList<>();

        ArrayList<Integer> rad = (ArrayList<Integer>) counts.get(pos);
        ArrayList<String> str = ((Check) formItems.get(pos)).getGroup();
        int sum = 0;
        for (int j = 0; j < rad.size(); j++) {
            entry.add(new PieEntry(rad.get(j), str.get(j)));
            sum += rad.get(j);
        }
        PieDataSet pieDataSet = new PieDataSet(entry, "");

        pieDataSet.setColors(color);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5);


        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + Math.round(value) + " %";
            }
        });

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Responses: " + sum);
        pieChart.setUsePercentValues(true);
        pieChart.animateXY(1000, 1000);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setTextColor(Color.BLACK);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.invalidate();

        ((TextView) v.findViewById(R.id.res_tv)).setText(formItems.get(pos).getTitle());
        ll.addView(v);
    }

    private void getBar(int pos) {
        ArrayList<Integer> color = getColors();
        View v = LayoutInflater.from(this).inflate(R.layout.barchart, null);
        BarChart barChart = v.findViewById(R.id.bar);
        ArrayList<BarEntry> entry = new ArrayList<>();

        ArrayList<Integer> rad = (ArrayList<Integer>) counts.get(pos);
        final ArrayList<String> str = ((Check) formItems.get(pos)).getGroup();


        ArrayList<LegendEntry> ale = new ArrayList<>();

        for (int j = 0; j < rad.size(); j++) {
            entry.add(new BarEntry(j * (0.8f), rad.get(j)));
            LegendEntry le = new LegendEntry();
            le.label = str.get(j);
            le.formColor = color.get(j);
            ale.add(le);
        }
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
        bardata.setValueTextColor(Color.BLACK);
        barChart.setFitBars(true);
        barChart.animateXY(1000, 1000);

        Legend legend = barChart.getLegend();

        legend.setCustom(ale);
        legend.setWordWrapEnabled(true);
        legend.setTextColor(Color.BLACK);

        barChart.invalidate();

        ((TextView) v.findViewById(R.id.res_tv)).setText(formItems.get(pos).getTitle());
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
                        Type collectionType = new TypeToken<Collection<ItemResponse>>() {
                        }.getType();
                        ArrayList<ItemResponse> itemResponses = gson.fromJson(doc.getString("response"), collectionType);
                        Response re = new Response();
                        re.setResponses(itemResponses);
                        String mail = null;
                        if (doc.contains("email"))
                            mail = doc.getString("email");
                        emails.add(mail);
                        responses.add(re);
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
        progressBar.setVisibility(View.GONE);
        titilecard.setVisibility(View.VISIBLE);
        title.setText("Total responses: " + responses.size());
        progressIndicator.setVisibility(View.VISIBLE);

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
                case typeTextMsg:
                case typeupload:
                case typeSubmit:
                    counts.add(null);
                    break;
                case typeTextField:
                    counts.add(new ArrayList<String>());
                    break;
                case typeSingleCheck:
                case typeMultipleCheck:
                case typepoll:
                    counts.add(new ArrayList<Integer>(Collections.nCopies(((Check) b).getGroup().size(), 0)));
                    break;
                case typerating:
                    counts.add(new ArrayList<Integer>(Collections.nCopies(5, 0)));
                    break;
            }

        }

        for (int z = 0; z < responses.size(); z++) {

            progressIndicator.setProgress((z / responses.size()) * 100);

            ArrayList<ItemResponse> r = responses.get(z).getResponses();

            for (int i = 0; i < formItems.size(); i++) {
                int type = formItems.get(i).getType();
                switch (type) {
                    case typeTextMsg:
                    case typeupload:
                        continue;
                    case typepoll:
                    case typeSingleCheck:
                        int checked = r.get(i).getSchecked();
                        if (checked != -1) {
                            int val = 1 + ((ArrayList<Integer>) counts.get(i)).get(checked);
                            ((ArrayList<Integer>) counts.get(i)).set(checked, val);
                        }
                        break;
                    case typeMultipleCheck:
                        Set<Integer> checks = r.get(i).getMchecked();
                        if (checks != null) {
                            for (int _checked : checks) {
                                int val = 1 + ((ArrayList<Integer>) counts.get(i)).get(_checked);
                                ((ArrayList<Integer>) counts.get(i)).set(_checked, val);

                            }

                        }
                        break;
                    case typerating:
                        int rating = r.get(i).getRating();
                        --rating;
                        int val = ((ArrayList<Integer>) counts.get(i)).get(rating) + 1;
                        ((ArrayList<Integer>) counts.get(i)).set(rating, val);
                        break;
                    case typeTextField:
                        String txt = r.get(i).getText();
                        if (txt != null) {
                            ((ArrayList<String>) counts.get(i)).add(txt);
                        }
                        break;

                }


            }

        }

        progressIndicator.setVisibility(View.GONE);
        prepareViews();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void prepareViews() {
        for (int z = 0; z < formItems.size(); z++) {
            switch (formItems.get(z).getType()) {
                case typeTextMsg:
                case typeupload:
                    continue;
                case typeTextField: {
                    View v = LayoutInflater.from(this).inflate(R.layout.listview, null);
                    ListView lv = v.findViewById(R.id.list);
                    lv.setOnTouchListener(new ListView.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                    // Disallow ScrollView to intercept touch events.
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    break;

                                case MotionEvent.ACTION_UP:
                                    // Allow ScrollView to intercept touch events.
                                    v.getParent().requestDisallowInterceptTouchEvent(false);
                                    break;
                            }

                            // Handle ListView touch events.
                            v.onTouchEvent(event);
                            return true;
                        }
                    });
                    TextView tv = v.findViewById(R.id.res_tv);
                    tv.setText(formItems.get(z).getTitle());
                    ArrayList<String> txts = (ArrayList<String>) this.counts.get(z);
                    String[] _txts = Utils.listToArray(txts);
                    ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.string_list_item, _txts);
                    lv.setAdapter(adapter);
                    ll.addView(v);
                    break;
                }
                case typeSingleCheck:
                    getPie(z);
                    break;
                case typeMultipleCheck:
                    getBar(z);
                    break;
                case typerating:
                    getRatingVis(z);
                    break;
                case typepoll:
                    getPollVis(z);
                    break;
                default:
                    break;

            }

        }

    }

    private void getPollVis(int z) {
        View vg = LayoutInflater.from(this).inflate(R.layout.poll, null);
        ((TextView) vg.findViewById(R.id.res_tv)).setText(formItems.get(z).getTitle());

        LinearLayout _ll = vg.findViewById(R.id.ll);
        ArrayList<String> _p = ((Check) formItems.get(z)).getGroup();
        ArrayList<Integer> _v = (ArrayList<Integer>) counts.get(z);
        float total_votes = 0;
        for (int __v : _v)
            total_votes += __v;

        int size = _p.size();

        View v;
        for (int i = 0; i < size; i++) {
            v = LayoutInflater.from(this).inflate(R.layout.poll_item, null);

            TextView tv = ((TextView) v.findViewById(R.id.text));
            tv.setText(_p.get(i));
            int votes = _v.get(i);

            float flw = ((votes / total_votes) * 100);
            ((TextView) v.findViewById(R.id.text2)).setText(Math.round(flw) + "%");

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            p.weight = flw;
            v.findViewById(R.id.fl).setLayoutParams(p);
            _ll.addView(v);

        }

        ll.addView(vg);
    }

    private void getRatingVis(int pos) {
        View v = LayoutInflater.from(this).inflate(R.layout.rating, null);
        TextView title = v.findViewById(R.id.res_tv);
        TextView mt = v.findViewById(R.id.mt);
        TextView count = v.findViewById(R.id.count);
        RatingBar rb = v.findViewById(R.id.stars);
        LinearProgressIndicator p1 = v.findViewById(R.id.p1);
        LinearProgressIndicator p2 = v.findViewById(R.id.p2);
        LinearProgressIndicator p3 = v.findViewById(R.id.p3);
        LinearProgressIndicator p4 = v.findViewById(R.id.p4);
        LinearProgressIndicator p5 = v.findViewById(R.id.p5);

        ArrayList<Integer> rates = (ArrayList<Integer>) counts.get(pos);

        float _t = 0;
        float _u = 0;
        for (int i = 0; i < 5; i++) {
            _t += ((i + 1) * rates.get(i));
            _u += rates.get(i);
        }
        count.setText("" + (int) _u);
        mt.setText(String.format("%.1f", _t / _u));
        rb.setRating(_t / _u);

        p1.setProgress(getOutOf100(rates.get(0) * 1, _t));
        p2.setProgress(getOutOf100(rates.get(1) * 2, _t));
        p3.setProgress(getOutOf100(rates.get(2) * 3, _t));
        p4.setProgress(getOutOf100(rates.get(3) * 4, _t));
        p5.setProgress(getOutOf100(rates.get(4) * 5, _t));

        title.setText(formItems.get(pos).getTitle());
        ll.addView(v);

    }

    private int getOutOf100(int val, float total) {
        return (int) (val / total * 100);
    }

    private ArrayList<BaseClass> loadForm() {
        progressBar.setVisibility(View.VISIBLE);
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