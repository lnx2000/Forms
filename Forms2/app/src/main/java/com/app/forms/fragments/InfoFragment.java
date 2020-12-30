package com.app.forms.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.app.forms.R;
import com.app.forms.constants.Constants;

public class InfoFragment extends Fragment {
    ImageView linkedin, github;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        linkedin = v.findViewById(R.id.linkedin);
        github = v.findViewById(R.id.github);

        github.setOnClickListener(v1 -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.githubUrl));
            startActivity(browserIntent);
        });
        linkedin.setOnClickListener(v2 -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.linkedinUrl));
            startActivity(browserIntent);
        });
        return v;
    }
}