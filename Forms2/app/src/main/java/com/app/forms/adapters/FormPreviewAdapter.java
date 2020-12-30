package com.app.forms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.forms.Items.BaseClass;
import com.app.forms.Items.Check;
import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class FormPreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<BaseClass> data;

    public FormPreviewAdapter(Context context, ArrayList<BaseClass> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder vh = null;
        switch (viewType) {
            case Constants.typeTextField:
                v = LayoutInflater.from(context).inflate(R.layout.show_text, parent, false);
                vh = new showTextHolder(v);
                break;
            case Constants.typeSingleCheck:
                v = LayoutInflater.from(context).inflate(R.layout.show_single_check, parent, false);
                vh = new showSingleCheckHolder(v);
                break;
            case Constants.typeMultipleCheck:
                v = LayoutInflater.from(context).inflate(R.layout.show_multiple_check, parent, false);
                vh = new showMultipleCheckHolder(v);
                break;

        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof showTextHolder) {
            ((showTextHolder) holder).title.setText(data.get(position).getTitle());
            if (true) { //show_count
                ((showTextHolder) holder).count.setVisibility(View.VISIBLE);
                ((showTextHolder) holder).count.setText("" + (position + 1) + ".");
            } else ((showTextHolder) holder).count.setVisibility(View.GONE);
        } else if (holder instanceof showSingleCheckHolder) {
            ((showSingleCheckHolder) holder).title.setText(data.get(position).getTitle());
            ArrayList<String> group = ((Check) data.get(position)).getGroup();

            for (String s : group) {
                RadioButton rb =(RadioButton) LayoutInflater.from(context).inflate(R.layout.show_radio, null);

                rb.setText(s);
                ((showSingleCheckHolder)holder).rg.addView(rb);
            }
            if (true) { //show_count
                ((showSingleCheckHolder) holder).count.setVisibility(View.VISIBLE);
                ((showSingleCheckHolder) holder).count.setText("" + (position + 1) + ".");
            } else ((showTextHolder) holder).count.setVisibility(View.GONE);

        } else if (holder instanceof showMultipleCheckHolder) {
            ((showMultipleCheckHolder) holder).title.setText(data.get(position).getTitle());
            ArrayList<String> group = ((Check) data.get(position)).getGroup();

            for (String s : group) {
                CheckBox v = (CheckBox) LayoutInflater.from(context).inflate(R.layout.show_check, null);
                v.setText(s);
                ((showMultipleCheckHolder) holder).lv.addView(v);
            }

            if (true) { //show_count
                ((showMultipleCheckHolder) holder).count.setVisibility(View.VISIBLE);
                ((showMultipleCheckHolder) holder).count.setText("" + (position + 1) + ".");
            } else ((showTextHolder) holder).count.setVisibility(View.GONE);


        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private class showTextHolder extends RecyclerView.ViewHolder {
        TextView count, title;
        TextInputEditText answer;

        public showTextHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count);
            title = itemView.findViewById(R.id.title);
            answer = itemView.findViewById(R.id.answer);


        }
    }

    private class showSingleCheckHolder extends RecyclerView.ViewHolder {
        TextView count, title;
        RadioGroup rg;

        public showSingleCheckHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count);
            title = itemView.findViewById(R.id.title);
            rg = itemView.findViewById(R.id.radiogroup);
        }
    }

    private class showMultipleCheckHolder extends RecyclerView.ViewHolder {
        TextView count, title;
        ListView lv;

        public showMultipleCheckHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count);
            title = itemView.findViewById(R.id.title);
            lv = itemView.findViewById(R.id.listview);

        }
    }
}
