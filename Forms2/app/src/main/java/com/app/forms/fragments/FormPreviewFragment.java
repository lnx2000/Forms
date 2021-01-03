package com.app.forms.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.forms.R;
import com.app.forms.activities.CreateFormActivity;
import com.app.forms.adapters.FormPreviewAdapter;
import com.app.forms.constants.Constants;
import com.app.forms.items.BaseClass;
import com.app.forms.items.ItemResponse;
import com.app.forms.items.Response;

import java.util.ArrayList;

public class FormPreviewFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<BaseClass> data;
    RecyclerView.LayoutManager manager;
    FormPreviewAdapter adapter;
    int formID;
    boolean preview;
    Response response = null;
    boolean count = false;
    TextView title;
    String _title;


    public FormPreviewFragment() {
        // Required empty public constructor
    }




    public FormPreviewFragment(ArrayList<BaseClass> data, int formID, boolean preview, String _title) {
        this.data = data;
        this.formID = formID;
        this.preview = preview;
        this._title = _title;
    }

    public FormPreviewFragment(ArrayList<BaseClass> data, int formID, boolean preview, boolean count, Response response, String _title) {
        this.data = data;
        this.formID = formID;
        this.preview = preview;
        this.count = count;
        this.response = response;
        this._title = _title;
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
        title = v.findViewById(R.id.title);
        title.setText(_title);


        if (!preview) {
            response.setResponses(prepareResponses());
        }

        setCounts();

        if (getActivity() instanceof CreateFormActivity)
            adapter = new FormPreviewAdapter(getContext(), data, preview, null, "" + formID, ((CreateFormActivity) getActivity()).getCount());
        else {
            adapter = new FormPreviewAdapter(getContext(), data, preview, response.getResponses(), "" + formID, count);
        }
        manager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemViewCacheSize(100);

        return v;
    }

    private void setCounts() {
        int _count = 1;
        for (BaseClass b : data) {
            if (b.getType() != Constants.typeTextMsg) {
                b.setCount(_count);
                _count++;
            }
        }


    }

    private ArrayList<ItemResponse> prepareResponses() {
        ArrayList<ItemResponse> responses = new ArrayList<>();

        for (BaseClass b : data) {
            if (b.getType() == Constants.typerating || b.getType() == Constants.typeTextMsg)
                responses.add(new ItemResponse(b.getType(), true));
            else responses.add(new ItemResponse(b.getType(), false));
        }
        return responses;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }


}