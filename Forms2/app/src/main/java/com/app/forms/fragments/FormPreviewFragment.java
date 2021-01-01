package com.app.forms.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.forms.Items.BaseClass;
import com.app.forms.R;
import com.app.forms.adapters.FormPreviewAdapter;

import java.util.ArrayList;

public class FormPreviewFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<BaseClass> data;
    RecyclerView.LayoutManager manager;
    FormPreviewAdapter adapter;
    int formID;


    public FormPreviewFragment() {
        // Required empty public constructor
    }

    public FormPreviewFragment(ArrayList<BaseClass> data, int formID) {
        this.data = data;
        this.formID = formID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_form_preview, container, false);
        recyclerView = v.findViewById(R.id.recyclerview);


        adapter = new FormPreviewAdapter(getContext(), data, true, null, "" + formID);

        manager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemViewCacheSize(100);

        return v;
    }

}