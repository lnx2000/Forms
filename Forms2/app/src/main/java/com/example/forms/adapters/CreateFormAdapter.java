package com.example.forms.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forms.Items.SingleMultipleCheck;
import com.example.forms.Items.TextField;
import com.example.forms.R;
import com.example.forms.constants.Constants;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;

public class CreateFormAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Object> data;

    public CreateFormAdapter(Context context, ArrayList<Object> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        int ret = -1;
        if (data.get(position) instanceof TextField)
            ret = Constants.typeTextField;
        else if (data.get(position) instanceof SingleMultipleCheck)
            ret = ((SingleMultipleCheck)data.get(position)).getType();


        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        View v;
        switch (viewType) {
            case Constants.typeTextField:
                v = LayoutInflater.from(context).inflate(R.layout.item_text_field, parent, false);
                vh = new TextFieldHolder(v);
                break;
            case Constants.typeSingleCheck:
                v = LayoutInflater.from(context).inflate(R.layout.item_single_check, parent, false);
                vh = new SingleCheckHolder(v, Constants.typeSingleCheck);
                break;
            case Constants.typeMultipleCheck:
                v = LayoutInflater.from(context).inflate(R.layout.item_single_check, parent, false);
                vh = new SingleCheckHolder(v, Constants.typeMultipleCheck);
                break;

        }


        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (data.get(position) instanceof SingleMultipleCheck) {
            ((SingleCheckHolder) holder).initRecyclerView(position);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TextFieldHolder extends RecyclerView.ViewHolder {
        Spinner spinner;
        Toolbar toolbar;
        SwitchMaterial ismandatory;
        ImageView imageview;
        TextInputEditText title;

        public TextFieldHolder(@NonNull View itemView) {
            super(itemView);
            spinner = itemView.findViewById(R.id.spinner);
            toolbar = itemView.findViewById(R.id.toolbar);
            ismandatory = itemView.findViewById(R.id.ismandatory);
            imageview = itemView.findViewById(R.id.imageview);
            title = itemView.findViewById(R.id.title);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Constants.textTypes);
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
                    case R.id.delete:
                        data.remove(position);
                        notifyItemRemoved(position);
                        break;
                    case R.id.showimage:
                        if (imageview.getVisibility() == View.VISIBLE) {
                            imageview.setVisibility(View.GONE);
                            imageview.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_image));
                            ((TextField) data.get(position)).setImage(false);
                        } else
                            imageview.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            });

            imageview.setOnClickListener(v -> {
                int position = getAdapterPosition();

                //ToDo: add "select image from gallery" intent
                Bitmap bitmap = null;
                ((TextField) data.get(position)).setBitmap(bitmap);
                imageview.setImageBitmap(bitmap);
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int position1 = getAdapterPosition();
                    ((TextField) data.get(position1)).setTextTypeChoice(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ismandatory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                ((TextField) data.get(position)).setMandatory(isChecked);
            });

            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = getAdapterPosition();
                    ((TextField) data.get(position)).setTitle(s.toString());
                    ((TextField) data.get(position)).setImage(true);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    }

    public class SingleCheckHolder extends RecyclerView.ViewHolder {
        TextInputEditText title;
        ImageView imageView, addRadio;
        SwitchMaterial isMandatory;
        RecyclerView recyclerView;
        Toolbar toolbar;
        SingleMultipleCheckAdapter adapter;
        int type;

        public SingleCheckHolder(@NonNull View itemView,  int type) {
            super(itemView);
            this.type = type;
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.imageview);
            isMandatory = itemView.findViewById(R.id.ismandatory);
            recyclerView = itemView.findViewById(R.id.radiogroup);
            addRadio = itemView.findViewById(R.id.addradio);
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
                    case R.id.delete:
                        data.remove(position);
                        notifyItemRemoved(position);
                        break;
                    case R.id.showimage:
                        if (imageView.getVisibility() == View.VISIBLE) {
                            imageView.setVisibility(View.GONE);
                            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_image));
                            ((SingleMultipleCheck) data.get(position)).setImage(false);
                        } else
                            imageView.setVisibility(View.VISIBLE);
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
                    ((SingleMultipleCheck) data.get(position)).setTitle(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            imageView.setOnClickListener(v -> {
                int position = getAdapterPosition();

                //ToDo: add "select image from gallery" intent
                Bitmap bitmap = null;
                ((SingleMultipleCheck) data.get(position)).setBitmap(bitmap);
                imageView.setImageBitmap(bitmap);
            });
            isMandatory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                ((SingleMultipleCheck) data.get(position)).setMandatory(isChecked);
            });

            addRadio.setOnClickListener(v -> {
                int position = getAdapterPosition();
                ((SingleMultipleCheck) data.get(position)).addInRadioGroup();
                adapter.notifyItemInserted(((SingleMultipleCheck) data.get(position)).radioGroupSize());
            });


        }

        public void initRecyclerView(int position) {
            adapter = new SingleMultipleCheckAdapter(context, ((SingleMultipleCheck) data.get(position)).getRadioGroup(),type);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            recyclerView.setItemViewCacheSize(100);
        }
    }


}
