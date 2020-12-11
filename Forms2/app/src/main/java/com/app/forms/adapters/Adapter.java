package com.app.forms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.app.forms.Items.FormItem;
import com.app.forms.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FormItem> data;
    Context context;

    public Adapter(ArrayList<FormItem> data, Context context) {
        this.data = new ArrayList<>(data);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.froms_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Toolbar toolbar = ((ViewHolder) holder).toolbar;
        toolbar.setVisibility(View.GONE);
        ((ViewHolder) holder).title.setText(data.get(position).getName());
        ((ViewHolder) holder).date.setText("Created On: " + data.get(position).getCreatedOn());

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

    public void addData(FormItem formItem) {
        data.add(formItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Toolbar toolbar;
        ConstraintLayout container;
        TextView title, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            toolbar = itemView.findViewById(R.id.toolbar);
            container = itemView.findViewById(R.id.container);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);

            container.setOnClickListener(v -> {
                if (toolbar.getVisibility() == View.VISIBLE) {
                    toolbar.setVisibility(View.GONE);
                    TransitionManager.beginDelayedTransition(container,
                            new AutoTransition());
                } else {
                    TransitionManager.beginDelayedTransition(container,
                            new AutoTransition().setDuration(100));
                    toolbar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}
