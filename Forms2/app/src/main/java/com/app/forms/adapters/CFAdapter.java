package com.app.forms.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.forms.Items.BaseClass;
import com.app.forms.Items.Check;
import com.app.forms.Items.Text;
import com.app.forms.R;
import com.app.forms.activities.CreateFormActivity;
import com.app.forms.constants.Constants;
import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;

public class CFAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<BaseClass> data;
    Context context;

    public CFAdapter(ArrayList<BaseClass> data, Context context) {
        this.data = data;
        this.context = context;
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
                v = LayoutInflater.from(context).inflate(R.layout.item_text, parent, false);
                vh = new TextHolder(v);
                break;
            case Constants.typeSingleCheck:
                v = LayoutInflater.from(context).inflate(R.layout.item_check, parent, false);
                vh = new CheckHolder(v, Constants.typeSingleCheck);
                break;
            case Constants.typeMultipleCheck:
                v = LayoutInflater.from(context).inflate(R.layout.item_check, parent, false);
                vh = new CheckHolder(v, Constants.typeMultipleCheck);
                break;

        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder == null) return;
        if (holder instanceof TextHolder) {
            TextHolder th = (TextHolder) holder;
            Text text = (Text) data.get(position);
            th.title.setText(text.getTitle());
            th.spinner.setSelection(text.getTextTypeChoice());
            th.mandatory.setChecked(text.isMandatory());
            if (text.isImage()) {
                Glide.with(context)
                        .load(text.getImagepath())
                        .into(th.imageView);
                th.imageView.setVisibility(View.VISIBLE);
            }

        } else if (holder instanceof CheckHolder) {
            CheckHolder ch = (CheckHolder) holder;
            Check check = (Check) data.get(position);
            ch.initRecyclerView(position);
            ch.title.setText(check.getTitle());
            ch.mandatory.setChecked(check.isMandatory());
            if (check.isImage()) {
                Glide.with(context)
                        .load(check.getImagepath())
                        .into(ch.imageView);
                ch.imageView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class TextHolder extends RecyclerView.ViewHolder {
        TextInputEditText title;
        Spinner spinner;
        SwitchMaterial mandatory;
        ImageView imageView;
        Toolbar toolbar;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
            spinner = itemView.findViewById(R.id.spinner);
            toolbar = itemView.findViewById(R.id.toolbar);
            mandatory = itemView.findViewById(R.id.ismandatory);
            imageView = itemView.findViewById(R.id.imageview);
            title = itemView.findViewById(R.id.title);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item,
                    Constants.textTypes);
            spinner.setAdapter(spinnerArrayAdapter);

            toolbar.setOnMenuItemClickListener(item -> {
                int position = getAdapterPosition();
                switch (item.getItemId()) {
                    case R.id.moveup:
                        if (position > 0) {
                            Collections.swap(data, position, position - 1);
                            notifyItemMoved(position, position - 1);
                        }
                        break;
                    case R.id.movedown:
                        if (position < data.size() - 1) {
                            Collections.swap(data, position, position + 1);
                            notifyItemMoved(position, position + 1);
                        }
                        break;
                    case R.id.showimage:
                        if (imageView.getVisibility() == View.GONE) {
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageDrawable(((Activity) context).getResources().getDrawable(R.drawable.ic_add_image));
                        } else imageView.setVisibility(View.GONE);
                        break;
                    case R.id.delete:
                        data.remove(position);
                        notifyItemRemoved(position);
                        break;
                }
                return true;
            });
            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = getAdapterPosition();
                    ((Text) data.get(position)).setTitle(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mandatory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                ((Text) data.get(position)).setMandatory(isChecked);
            });
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int position1 = getAdapterPosition();
                    ((Text) data.get(position1)).setTextTypeChoice(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            imageView.setOnClickListener(v -> {
                int position = getAdapterPosition();

                //ToDo: add "select image from gallery" intent
                ((CreateFormActivity) context).StartImageChooserActivity(position);
            });


        }
    }

    public class CheckHolder extends RecyclerView.ViewHolder {

        TextInputEditText title;
        ImageView imageView, addOption;
        SwitchMaterial mandatory;
        RecyclerView recyclerView;
        Toolbar toolbar;
        CheckAdapter checkAdapter;
        int type;

        public CheckHolder(@NonNull View itemView, int type) {
            super(itemView);
            this.type = type;
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.imageview);
            mandatory = itemView.findViewById(R.id.ismandatory);
            recyclerView = itemView.findViewById(R.id.radiogroup);
            addOption = itemView.findViewById(R.id.addradio);
            toolbar = itemView.findViewById(R.id.toolbar);


            toolbar.setOnMenuItemClickListener(item -> {
                int position = getAdapterPosition();
                switch (item.getItemId()) {
                    case R.id.moveup:
                        if (position > 0) {
                            Collections.swap(data, position, position - 1);
                            notifyItemMoved(position, position - 1);
                        }
                        break;
                    case R.id.movedown:
                        if (position < data.size() - 1) {
                            Collections.swap(data, position, position + 1);
                            notifyItemMoved(position, position + 1);
                        }
                        break;
                    case R.id.showimage:
                        if (imageView.getVisibility() == View.GONE) {
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageDrawable(((Activity) context).getResources().getDrawable(R.drawable.ic_add_image));
                        } else imageView.setVisibility(View.GONE);
                        break;
                    case R.id.delete:
                        data.remove(position);
                        notifyItemRemoved(position);
                        break;
                }
                return true;
            });
            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = getAdapterPosition();
                    ((Check) data.get(position)).setTitle(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mandatory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                ((Check) data.get(position)).setMandatory(isChecked);
            });

            imageView.setOnClickListener(v -> {
                int position = getAdapterPosition();

                //ToDo: add "select image from gallery" intent
                ((CreateFormActivity) context).StartImageChooserActivity(position);
            });

            addOption.setOnClickListener(v -> {
                int position = getAdapterPosition();
                int size = ((Check) data.get(position)).addOption();
                checkAdapter.notifyItemInserted(size);
            });
        }

        public void initRecyclerView(int position) {
            checkAdapter = new CheckAdapter(context, ((Check) data.get(position)).getGroup(), type);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(checkAdapter);
            recyclerView.setItemViewCacheSize(100);
        }
    }

}
