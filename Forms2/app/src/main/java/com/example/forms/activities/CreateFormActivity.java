package com.example.forms.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forms.Items.SingleMultipleCheck;
import com.example.forms.Items.TextField;
import com.example.forms.R;
import com.example.forms.adapters.CreateFormAdapter;
import com.example.forms.constants.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class CreateFormActivity extends AppCompatActivity {

    ImageView additem, save;
    NavigationView navview;
    FloatingActionButton navviewcancel;
    MaterialCardView savedmsg;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);
        additem = findViewById(R.id.additem);
        navview = findViewById(R.id.drawer);
        navviewcancel = findViewById(R.id.nav_view_cancel);
        save = findViewById(R.id.save);
        savedmsg = findViewById(R.id.savedmsg);
        recyclerView = findViewById(R.id.recyclerview);

        save.setOnClickListener(v -> {
            savedmsg.setVisibility(View.VISIBLE);
            savedmsg.postDelayed(() -> savedmsg.setVisibility(View.GONE), 1000);
        });

        additem.setOnClickListener(v -> visibility(true));
        navviewcancel.setOnClickListener(v -> visibility(false));

        ArrayList<Object> data = new ArrayList<>();
        data.add(new TextField());


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CreateFormAdapter createFormAdapter = new CreateFormAdapter(this, data);
        recyclerView.setAdapter(createFormAdapter);
        recyclerView.setItemViewCacheSize(100);

        navview.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.textfield:
                    data.add(new TextField());
                    createFormAdapter.notifyItemInserted(data.size() - 1);
                    break;
                case R.id.radiofield:
                    data.add(new SingleMultipleCheck(Constants.typeSingleCheck));
                    createFormAdapter.notifyItemInserted(data.size() - 1);
                    break;
                case R.id.checkfield:
                    data.add(new SingleMultipleCheck(Constants.typeMultipleCheck));
                    createFormAdapter.notifyItemInserted(data.size() - 1);
            }
            visibility(false);
            return true;
        });

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


        save.setOnClickListener(v -> {
            //TODO: save arraylist(data) in shared preferences
        });


    }

    public void visibility(boolean vis) {
        if (vis) {
            navviewcancel.setVisibility(View.VISIBLE);
            navview.setVisibility(View.VISIBLE);
        } else {
            navviewcancel.setVisibility(View.GONE);
            navview.setVisibility(View.GONE);
        }
    }


}