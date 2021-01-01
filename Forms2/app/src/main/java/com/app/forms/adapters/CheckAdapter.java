package com.app.forms.adapters;

import android.content.Context;
import android.os.strictmode.FileUriExposedViolation;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CheckAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<String> data;
    int type;

    public CheckAdapter(Context context, ArrayList<String> data, int type) {
        this.context = context;
        this.data = data;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (type == Constants.typeSingleCheck) {
            View v = LayoutInflater.from(context).inflate(R.layout.radio_item, parent, false);
            RadioItemViewHolder vh = new RadioItemViewHolder(v);
            return vh;
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.check_box_item, parent, false);
            CheckItemViewHolder vh = new CheckItemViewHolder(v);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (type == Constants.typeSingleCheck) {
            if (data.get(position).contains("Option"))
                ((RadioItemViewHolder) holder).text.setHint(data.get(position));
            else ((RadioItemViewHolder) holder).text.setText(data.get(position));
        } else {
            if (data.get(position).contains("Option"))
                ((CheckItemViewHolder) holder).text.setHint(data.get(position));
            else ((CheckItemViewHolder) holder).text.setText(data.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RadioItemViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText text;
        ImageView remove;

        public RadioItemViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            remove = itemView.findViewById(R.id.remove);

            text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = getAdapterPosition();
                    data.set(position, s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            remove.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (data.size() > 2) {
                    data.remove(position);
                    notifyItemRemoved(position);
                }
            });

        }
    }

    public class CheckItemViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText text;
        ImageView remove;

        public CheckItemViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            remove = itemView.findViewById(R.id.remove);

            text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = getAdapterPosition();
                    data.set(position, s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            remove.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (data.size() > 2) {
                    data.remove(position);
                    notifyItemRemoved(position);
                }
            });


        }
    }
}
