package com.app.forms.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
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

import com.app.forms.items.BaseClass;
import com.app.forms.items.Check;
import com.app.forms.items.Text;
import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.cooltechworks.views.WhatsAppEditText;
import com.cooltechworks.views.WhatsAppTextView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
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
            case Constants.typepoll:
            case Constants.typeSingleCheck:
                v = LayoutInflater.from(context).inflate(R.layout.item_check, parent, false);
                vh = new CheckHolder(v, Constants.typeSingleCheck);
                break;
            case Constants.typeMultipleCheck:
                v = LayoutInflater.from(context).inflate(R.layout.item_check, parent, false);
                vh = new CheckHolder(v, Constants.typeMultipleCheck);
                break;
            case Constants.typeTextMsg:
                v = LayoutInflater.from(context).inflate(R.layout.item_text_msg, parent, false);
                vh = new TextMsgHolder(v);
                break;
            case Constants.typerating:
            case Constants.typeupload:
                v = LayoutInflater.from(context).inflate(R.layout.item_simple_text, parent, false);
                vh = new SimpleTextHolder(v, viewType);
                break;

        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TextHolder) {
            TextHolder th = (TextHolder) holder;
            Text text = (Text) data.get(position);
            th.title.setText(text.getTitle());
            th.spinner.setSelection(text.getTextTypeChoice());
            th.mandatory.setChecked(text.isMandatory());
            /*if (text.isImage()) {
                Glide.with(context)
                        .load(text.getImagepath())
                        .into(th.imageView);
                th.imageView.setVisibility(View.VISIBLE);
            }*/

        } else if (holder instanceof CheckHolder) {
            CheckHolder ch = (CheckHolder) holder;
            Check check = (Check) data.get(position);
            ch.initRecyclerView(position);
            ch.title.setText(check.getTitle());
            ch.mandatory.setChecked(check.isMandatory());
            /*if (check.isImage()) {
                Glide.with(context)
                        .load(check.getImagepath())
                        .into(ch.imageView);
                ch.imageView.setVisibility(View.VISIBLE);
            }*/
        } else if (holder instanceof TextMsgHolder) {
            ((TextMsgHolder) holder).title.setText(Html.fromHtml(data.get(position).getTitle(), Html.FROM_HTML_MODE_LEGACY));
        } else if (holder instanceof SimpleTextHolder) {
            SimpleTextHolder th = (SimpleTextHolder) holder;
            BaseClass text = (BaseClass) data.get(position);
            th.title.setText(text.getTitle());
            th.mandatory.setChecked(text.isMandatory());

        }
    }

    @Override
    public int getItemCount() {
        return data.size()-1;
    }


    public class TextHolder extends RecyclerView.ViewHolder {
        TextInputEditText title;
        Spinner spinner;
        SwitchMaterial mandatory;
        //ImageView imageView;
        Toolbar toolbar;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
            spinner = itemView.findViewById(R.id.spinner);
            toolbar = itemView.findViewById(R.id.toolbar);
            mandatory = itemView.findViewById(R.id.ismandatory);
            //imageView = itemView.findViewById(R.id.imageview);
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
                    /*case R.id.showimage:
                        if (imageView.getVisibility() == View.GONE) {
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageDrawable(((Activity) context).getResources().getDrawable(R.drawable.ic_add_image));
                        } else {
                            imageView.setVisibility(View.GONE);
                            data.get(position).setImage(false);
                        }
                        break;*/
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
                    ((Text) data.get(position)).setTitle(s.toString().trim());
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
            /*imageView.setOnClickListener(v -> {
                int position = getAdapterPosition();

                ((CreateFormActivity) context).StartImageChooserActivity(position);
            });*/


        }
    }

    public class CheckHolder extends RecyclerView.ViewHolder {

        TextInputEditText title;
        ImageView /*imageView,*/ addOption;
        SwitchMaterial mandatory;
        RecyclerView recyclerView;
        Toolbar toolbar;
        CheckAdapter checkAdapter;
        int type;

        public CheckHolder(@NonNull View itemView, int type) {
            super(itemView);
            this.type = type;
            title = itemView.findViewById(R.id.title);
            //imageView = itemView.findViewById(R.id.imageview);
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
                    /*case R.id.showimage:
                        if (imageView.getVisibility() == View.GONE) {
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageDrawable(((Activity) context).getResources().getDrawable(R.drawable.ic_add_image));
                        } else {
                            imageView.setVisibility(View.GONE);
                            data.get(position).setImage(false);
                        }
                        break;*/
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
                    ((Check) data.get(position)).setTitle(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mandatory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                ((Check) data.get(position)).setMandatory(isChecked);
            });

            /*imageView.setOnClickListener(v -> {
                int position = getAdapterPosition();

                ((CreateFormActivity) context).StartImageChooserActivity(position);
            });*/

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

    public class TextMsgHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ShapeableImageView bold, italic, st;
        WhatsAppEditText title;
        TextWatcher textWatcher;
        WhatsAppTextView titletv;
        TabLayout tabLayout;
        ImageView delete;

        public TextMsgHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            titletv = itemView.findViewById(R.id.titletv);
            tabLayout = itemView.findViewById(R.id.tablayout);
            bold = itemView.findViewById(R.id.bold);
            italic = itemView.findViewById(R.id.italic);
            st = itemView.findViewById(R.id.st);
            delete = itemView.findViewById(R.id.delete);
            bold.setOnClickListener(this);
            italic.setOnClickListener(this);
            st.setOnClickListener(this);
            delete.setOnClickListener(this);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        title.setVisibility(View.VISIBLE);
                        titletv.setVisibility(View.GONE);
                    } else {
                        titletv.setVisibility(View.VISIBLE);
                        title.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = getAdapterPosition();
                    data.get(position).setTitle(s.toString().trim());
                    titletv.setText(s.toString().trim());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            title.addTextChangedListener(textWatcher);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            int pos;
            String s;
            switch (v.getId()) {
                case R.id.bold:
                    pos = title.getSelectionStart();
                    s = title.getText().toString();
                    s = s.substring(0, pos) + "**" + s.substring(pos);
                    title.setText(s);
                    title.setSelection(pos + 1);

                    break;
                case R.id.italic:
                    pos = title.getSelectionStart();
                    s = title.getText().toString();
                    s = s.substring(0, pos) + "__" + s.substring(pos);
                    title.setText(s);
                    title.setSelection(pos + 1);
                    break;
                case R.id.st:
                    pos = title.getSelectionStart();
                    s = title.getText().toString();
                    s = s.substring(0, pos) + "~~" + s.substring(pos);
                    title.setText(s);
                    title.setSelection(pos + 1);
                    break;
                case R.id.delete:
                    data.remove(position);
                    notifyItemRemoved(position);
                    break;
            }

        }
    }

    public class SimpleTextHolder extends RecyclerView.ViewHolder {
        TextInputEditText title;
        SwitchMaterial mandatory;
        Toolbar toolbar;

        public SimpleTextHolder(@NonNull View itemView, int viewtype) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            mandatory = itemView.findViewById(R.id.ismandatory);
            toolbar = itemView.findViewById(R.id.toolbar);
            if (viewtype == Constants.typerating) {
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_star_rate_24, 0, 0, 0);
            }

            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = getAdapterPosition();
                    data.get(position).setTitle(s.toString().trim());


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            mandatory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                ((BaseClass) data.get(position)).setMandatory(isChecked);
            });

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
                    /*case R.id.showimage:
                        if (imageView.getVisibility() == View.GONE) {
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageDrawable(((Activity) context).getResources().getDrawable(R.drawable.ic_add_image));
                        } else {
                            imageView.setVisibility(View.GONE);
                            data.get(position).setImage(false);
                        }
                        break;*/
                    case R.id.delete:
                        data.remove(position);
                        notifyItemRemoved(position);
                        break;
                }
                return true;
            });

        }
    }

}
