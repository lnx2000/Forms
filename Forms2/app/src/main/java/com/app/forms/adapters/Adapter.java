package com.app.forms.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.app.forms.items.FormItem;
import com.app.forms.R;
import com.app.forms.activities.MainActivity;
import com.app.forms.constants.Constants;
import com.app.forms.helpers.SPOps;
import com.app.forms.helpers.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FormItem> data;
    Context context;

    public Adapter(ArrayList<FormItem> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.forms_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Toolbar toolbar = ((ViewHolder) holder).toolbar;
        toolbar.setVisibility(View.GONE);
        ((ViewHolder) holder).enable.setVisibility(View.GONE);
        ((ViewHolder) holder).title.setText(data.get(position).getName());
        ((ViewHolder) holder).date.setText("Created On: " + data.get(position).getCreatedOn());
        ((ViewHolder) holder).enable.setChecked(data.get(position).isEnabled());
        Date publishDate = null, unPublishDate = null;
        String pDate = data.get(position).getConfig().getPublishDate() + data.get(position).getConfig().getPublishTime();
        String uDate = data.get(position).getConfig().getUnPublishDate() + data.get(position).getConfig().getUnPublishTime();
        try {
            publishDate = Constants.parseDateTime.parse(pDate);
            unPublishDate = Constants.parseDateTime.parse(uDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currdate = new Date();
        if (currdate.after(publishDate) && currdate.before(unPublishDate)) {
            ((ViewHolder) holder).v.setVisibility(View.VISIBLE);
        } else ((ViewHolder) holder).v.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void applyFilter(ArrayList<FormItem> filterResults) {
        data.clear();
        data.addAll(filterResults);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Toolbar toolbar;
        ConstraintLayout container;
        TextView title, date;
        SwitchMaterial enable;
        View v;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            toolbar = itemView.findViewById(R.id.toolbar);
            container = itemView.findViewById(R.id.container);
            title = itemView.findViewById(R.id.formtitle);
            date = itemView.findViewById(R.id.date);
            enable = itemView.findViewById(R.id.enable);
            v = itemView.findViewById(R.id.acceptingresview);

            container.setOnClickListener(v -> {
                if (toolbar.getVisibility() == View.VISIBLE) {
                    toolbar.setVisibility(View.GONE);
                    enable.setVisibility(View.GONE);
                    TransitionManager.beginDelayedTransition(container,
                            new AutoTransition());
                } else {
                    TransitionManager.beginDelayedTransition(container,
                            new AutoTransition().setDuration(100));
                    toolbar.setVisibility(View.VISIBLE);
                    enable.setVisibility(View.VISIBLE);
                }
            });

            toolbar.setOnMenuItemClickListener(item -> {
                int position = getAdapterPosition();
                switch (item.getItemId()) {
                    case R.id.edit:
                        ((MainActivity) context).startCreateFormActivity(position, Constants.editFragment);
                        break;
                    case R.id.responses:
                        break;
                    case R.id.setting:
                        ((MainActivity) context).startCreateFormActivity(position, Constants.settingFragment);
                        break;
                    case R.id.share:
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_SEND);
                        i.putExtra(Intent.EXTRA_TEXT, Constants.formBaseUrl + data.get(position).getUID());
                        i.setType("text/plain");
                        context.startActivity(i);
                        break;
                    case R.id.delete:
                        SPOps.removeLocalForm(data.get(position).getUID(), position, context);

                        break;
                }
                return true;
            });

            enable.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();

                if (isChecked && buttonView.isPressed()) {
                    View view = LayoutInflater.from(context)
                            .inflate(R.layout.warning, null);
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context)
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                if (Utils.isUserLoggedIn()) {
                                    if (!((MainActivity) context).makeFormPublic(position)) {
                                        Toast.makeText(context, "Form Cant be published"/*\nPlease check whether date are correctly mentioned*/, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    enable.setChecked(false);
                                    View v = LayoutInflater.from(context).inflate(R.layout.login_required, null);
                                    MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(context);
                                    alert.setView(v);
                                    alert.show();
                                }
                            })
                            .setNegativeButton("CANCEL", (dialog, whichButton) -> {
                                dialog.cancel();
                                enable.setChecked(false);
                            });
                    materialAlertDialogBuilder.setView(view);
                    materialAlertDialogBuilder.show();


                } else if (!isChecked && buttonView.isPressed()) {
                    ((MainActivity) context).unpublishForm(position);
                }

            });
        }
    }

}
