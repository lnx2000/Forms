package com.app.forms.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.forms.Items.FormConfig;
import com.app.forms.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FormSettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private final SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    SwitchMaterial publish, unpublish, acceptingresponses, logintosubmit,
            allowmultiple, allowedit, recordemail, shuffle, sendResponse;
    ImageView chosepublishdate, chosepublishtime, choseunpublishdate, choseunpublishtime;
    TextView publishdate, publishtime, unpublishdate, unpublishtime;
    FormConfig configs;

    public FormSettingsFragment() {
        // Required empty public constructor
    }

    public FormSettingsFragment(FormConfig configs) {
        this.configs = configs;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_form_settings, container, false);
        publish = v.findViewById(R.id.publish);
        unpublish = v.findViewById(R.id.unpublish);
        chosepublishdate = v.findViewById(R.id.chosepublishdate);
        chosepublishtime = v.findViewById(R.id.chosepublishtime);
        choseunpublishdate = v.findViewById(R.id.choseunpublishdate);
        choseunpublishtime = v.findViewById(R.id.choseunpublishtime);
        publishdate = v.findViewById(R.id.publishdate);
        publishtime = v.findViewById(R.id.publishtime);
        unpublishdate = v.findViewById(R.id.unpublistdate);
        unpublishtime = v.findViewById(R.id.unpublishtime);

        acceptingresponses = v.findViewById(R.id.acceptingresponses);
        logintosubmit = v.findViewById(R.id.logintosubmit);
        allowmultiple = v.findViewById(R.id.allowmultiple);
        allowedit = v.findViewById(R.id.allowedit);
        recordemail = v.findViewById(R.id.recordemail);
        shuffle = v.findViewById(R.id.shuffle);
        sendResponse = v.findViewById(R.id.sendResponse);


        chosepublishdate.setOnClickListener(this);
        chosepublishtime.setOnClickListener(this);
        choseunpublishdate.setOnClickListener(this);
        choseunpublishtime.setOnClickListener(this);

        publish.setOnCheckedChangeListener(this);
        unpublish.setOnCheckedChangeListener(this);
        acceptingresponses.setOnCheckedChangeListener(this);
        logintosubmit.setOnCheckedChangeListener(this);
        allowmultiple.setOnCheckedChangeListener(this);
        allowedit.setOnCheckedChangeListener(this);
        recordemail.setOnCheckedChangeListener(this);
        shuffle.setOnCheckedChangeListener(this);
        sendResponse.setOnCheckedChangeListener(this);


        updateUI();
        return v;
    }

    private void updateUI() {
        acceptingresponses.setChecked(configs.isAcceptingResponses());
        logintosubmit.setChecked(configs.isLoginToSubmit());
        allowmultiple.setChecked(configs.isAllowMultipleResponses());
        allowedit.setChecked(configs.isAllowEdit());
        recordemail.setChecked(configs.isRecordEmail());
        shuffle.setChecked(configs.isShuffle());
        sendResponse.setChecked(configs.isSendResponseCopy());

    }


    public MaterialDatePicker buildDateDialog(TextView v) {

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        CalendarConstraints.Builder calendarConstraintBuilder = new CalendarConstraints.Builder();
        calendarConstraintBuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDateBuilder.setCalendarConstraints(calendarConstraintBuilder.build());
        materialDateBuilder.setSelection(today);

        MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> {
                    v.setText(materialDatePicker.getHeaderText());
                    if (v.getId() == R.id.publishdate) {
                        configs.setPublishDate(materialDatePicker.getHeaderText());
                    } else configs.setUnPublishDate(materialDatePicker.getHeaderText());

                });


        return materialDatePicker;
    }

    public MaterialTimePicker buildTimeDialog(TextView v) {

        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .build();

        materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
            int newHour = materialTimePicker.getHour();
            int newMinute = materialTimePicker.getMinute();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, newHour);
            cal.set(Calendar.MINUTE, newMinute);
            cal.setLenient(false);

            String format = formatter.format(cal.getTime());
            v.setText(format);
            if (v.getId() == R.id.publishtime) {
                configs.setPublishTime(format);
            } else configs.setUnPublishTime(format);

        });

        return materialTimePicker;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chosepublishdate:
                MaterialDatePicker publishDatePicker = buildDateDialog(publishdate);
                publishDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                break;
            case R.id.chosepublishtime:
                MaterialTimePicker publishTimePicker = buildTimeDialog(publishtime);
                publishTimePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
                break;
            case R.id.choseunpublishdate:
                MaterialDatePicker unpublishDatePicker = buildDateDialog(unpublishdate);
                unpublishDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                break;
            case R.id.choseunpublishtime:
                MaterialTimePicker unpublishTimePicker = buildTimeDialog(unpublishtime);
                unpublishTimePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.publish:
                configs.setPublish(isChecked);
                if (isChecked) {
                    publishtime.setVisibility(View.VISIBLE);
                    publishdate.setVisibility(View.VISIBLE);
                    chosepublishdate.setVisibility(View.VISIBLE);
                    chosepublishtime.setVisibility(View.VISIBLE);
                    publishdate.setText("Choose Date");
                    publishtime.setText("Choose Time");
                } else {
                    publishtime.setVisibility(View.GONE);
                    publishdate.setVisibility(View.GONE);
                    chosepublishdate.setVisibility(View.GONE);
                    chosepublishtime.setVisibility(View.GONE);
                }
                break;
            case R.id.unpublish:
                configs.setUnPublish(isChecked);
                if (isChecked) {
                    unpublishtime.setVisibility(View.VISIBLE);
                    unpublishdate.setVisibility(View.VISIBLE);
                    choseunpublishdate.setVisibility(View.VISIBLE);
                    choseunpublishtime.setVisibility(View.VISIBLE);
                    unpublishdate.setText("Choose Date");
                    unpublishtime.setText("Choose Time");
                } else {
                    unpublishtime.setVisibility(View.GONE);
                    unpublishdate.setVisibility(View.GONE);
                    choseunpublishdate.setVisibility(View.GONE);
                    choseunpublishtime.setVisibility(View.GONE);
                }
                break;
            case R.id.acceptingresponses:
                configs.setAcceptingResponses(isChecked);
                break;
            case R.id.logintosubmit:
                configs.setLoginToSubmit(isChecked);
                break;
            case R.id.allowmultiple:
                configs.setAllowMultipleResponses(isChecked);
                break;
            case R.id.allowedit:
                configs.setAllowEdit(isChecked);
                break;
            case R.id.recordemail:
                configs.setRecordEmail(isChecked);
                break;
            case R.id.shuffle:
                configs.setShuffle(isChecked);
                break;
            case R.id.sendResponse:
                configs.setSendResponseCopy(isChecked);
                break;

        }
    }

    public FormConfig getConfigs() {
        return configs;
    }
}