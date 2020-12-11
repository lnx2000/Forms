package com.app.forms.fragments;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.forms.Items.BaseClass;
import com.app.forms.Items.Check;
import com.app.forms.Items.Text;
import com.app.forms.R;
import com.app.forms.adapters.CFAdapter;
import com.app.forms.constants.Constants;

import java.util.ArrayList;
import java.util.Collections;

public class FormEditFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<BaseClass> data;
    CFAdapter createFormAdapter;

    public FormEditFragment() {
        // Required empty public constructor
    }

    public FormEditFragment(ArrayList<BaseClass> data) {
        this.data = data;
        Log.e("123", "constructor");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_form_edit, container, false);
        recyclerView = v.findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        createFormAdapter = new CFAdapter(data, getContext());
        recyclerView.setAdapter(createFormAdapter);
        recyclerView.setItemViewCacheSize(100);


        ItemTouchHelper th = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(data, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                viewHolder.itemView.findViewById(R.id.main_card).setElevation(R.dimen.zero_dp);
                target.itemView.findViewById(R.id.main_card).setElevation(R.dimen.zero_dp);
                // and notify the adapter that its dataset has changed
                createFormAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewHolder.itemView.findViewById(R.id.main_card).setElevation(R.dimen.ten_dp);

            }
        });

        th.attachToRecyclerView(recyclerView);


        return v;
    }

    public void notifyAdapterItemAdded() {
        createFormAdapter.notifyItemInserted(data.size() - 1);
    }

    public void activityResult(Uri bitmap, int rescode) {

        int type = createFormAdapter.getItemViewType(rescode);
        switch (type) {
            case Constants.typeTextField:
                ((Text) data.get(rescode)).setImagepath(bitmap);
                ((Text) data.get(rescode)).setImage(true);
                break;
            case Constants.typeSingleCheck:
            case Constants.typeMultipleCheck:
                ((Check) data.get(rescode)).setImagepath(bitmap);
                ((Check) data.get(rescode)).setImage(true);
                break;
        }
        createFormAdapter.notifyItemChanged(rescode);
    }

}