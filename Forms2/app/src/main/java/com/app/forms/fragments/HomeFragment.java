package com.app.forms.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.forms.Items.FormItem;
import com.app.forms.R;
import com.app.forms.adapters.Adapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ImageView search;
    TextInputEditText et;
    ArrayList<FormItem> data;
    Adapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new ArrayList<>();
        adapter = new Adapter(data, getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = v.findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        et = v.findViewById(R.id.edit_query);
        search = v.findViewById(R.id.search);


        search.setOnClickListener(v1 -> {
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
        });
        et.setOnFocusChangeListener((v12, hasFocus) -> {
            if (!hasFocus) {
                et.setFocusable(false);
                et.setText("MY FORMS");
            } else {
                et.setText("");
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filter = et.getText().toString().toLowerCase().trim();
                ArrayList<FormItem> newdata = new ArrayList<>();
                if (filter == null || filter.length() == 0)
                    newdata.addAll(data);
                else {
                    for (FormItem f : data) {
                        if (f.getName().toLowerCase().trim().contains(filter))
                            newdata.add(f);
                    }
                }
                adapter.applyFilter(newdata);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    public void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void addData(FormItem f) {
        data.add(0, f);
        adapter.addData(f);
    }
}