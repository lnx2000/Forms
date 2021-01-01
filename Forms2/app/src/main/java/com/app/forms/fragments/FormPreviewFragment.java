package com.app.forms.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.forms.R;
import com.app.forms.adapters.FormPreviewAdapter;
import com.app.forms.constants.Constants;
import com.app.forms.items.BaseClass;
import com.app.forms.items.Response;

import java.util.ArrayList;

public class FormPreviewFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<BaseClass> data;
    RecyclerView.LayoutManager manager;
    FormPreviewAdapter adapter;
    int formID;
    boolean preview;
    ArrayList<Response> responses = null;


    public FormPreviewFragment() {
        // Required empty public constructor
    }

    public FormPreviewFragment(ArrayList<BaseClass> data, int formID, boolean preview) {
        this.data = data;
        this.formID = formID;
        this.preview = preview;
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


        if (!preview) {
            responses = prepareResponses();
        } else responses = null;
        adapter = new FormPreviewAdapter(getContext(), data, preview, responses, "" + formID);

        manager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemViewCacheSize(100);

        return v;
    }

    private ArrayList<Response> prepareResponses() {
        ArrayList<Response> responses = new ArrayList<>();

        for (BaseClass b : data) {
            if (b.getType() == Constants.typeTextMsg)
                responses.add(null);
            else responses.add(new Response(b.getType()));
        }
        return responses;
    }



}