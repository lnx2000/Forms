package com.app.forms.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.forms.R;
import com.app.forms.activities.MainActivity;
import com.app.forms.adapters.Adapter;
import com.app.forms.items.FormItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {
    public boolean sortNewFirst = true;
    RecyclerView recyclerView;
    ImageView search, sort;
    TextInputEditText et;
    ArrayList<FormItem> data;
    Adapter adapter;
    ConstraintLayout bucket;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = ((MainActivity) getContext()).data;
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
        bucket = v.findViewById(R.id.bucket);
        sort = v.findViewById(R.id.sort);


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
                if (data != null) {
                    ArrayList<FormItem> newData = new ArrayList<>();
                    for (FormItem f : data) {
                        if (f.getName().toLowerCase().contains(s.toString().toLowerCase()))
                            newData.add(f);
                    }
                    adapter.setData(newData);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    et.clearFocus();
                    adapter.setData(data);
                }

                return false;
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortNewFirst) {
                    Snackbar sb = Snackbar.make(bucket, "Oldest first", 1000)
                            .setAnchorView(getActivity().findViewById(R.id.btnAdd))
                            .setTextColor(Color.BLACK);
                    Snackbar.SnackbarLayout sl = (Snackbar.SnackbarLayout) sb.getView();
                    sl.setBackgroundColor(Color.WHITE);
                    sortNewFirst = false;
                    Collections.reverse(data);
                    adapter.notifyDataSetChanged();
                    sb.show();
                } else {
                    Snackbar sb = Snackbar.make(bucket, "Newest first", 1000)
                            .setAnchorView(getActivity().findViewById(R.id.btnAdd))
                            .setTextColor(Color.BLACK);
                    Snackbar.SnackbarLayout sl = (Snackbar.SnackbarLayout) sb.getView();
                    sl.setBackgroundColor(Color.WHITE);
                    sortNewFirst = true;
                    Collections.reverse(data);
                    adapter.notifyDataSetChanged();
                    sb.show();

                }
            }
        });


        return v;
    }

    public void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }


    public void addData() {

        if (sortNewFirst) adapter.notifyItemInserted(0);
        else adapter.notifyItemInserted(data.size());
    }
}