package com.app.forms.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app.forms.R;
import com.app.forms.helpers.JsonDecode;
import com.app.forms.helpers.SPOps;
import com.app.forms.items.FormItem;
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

import org.json.JSONException;

import java.util.ArrayList;

public class ShowResponses extends AppCompatActivity {
    int formID;
    FormItem formItem;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_responses);

        ll = findViewById(R.id.linearLayout);
        formID = getIntent().getExtras().getInt("fromID");
        String jsonForm = SPOps.getForm(formID, this);
        try {
            formItem = JsonDecode.decode(jsonForm);
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

}