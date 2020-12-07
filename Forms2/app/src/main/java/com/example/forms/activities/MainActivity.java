package com.example.forms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.forms.fragments.HomeFragment;
import com.example.forms.fragments.InfoFragment;
import com.example.forms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btnAdd;
    BottomNavigationView bottomNavigation;
    FrameLayout fl;
    boolean addvis = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.btnAdd);
        bottomNavigation = findViewById(R.id.nav_view);
        bottomNavigation.setSelectedItemId(R.id.home);
        fl = findViewById(R.id.container);
        openFragment(new HomeFragment());
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.settings:
                    btnAdd.setImageResource(R.drawable.ic_baseline_home_24);
                    addvis = false;
                    fl.removeAllViews();
                    return true;
                case R.id.info:
                    btnAdd.setImageResource(R.drawable.ic_baseline_home_24);
                    addvis = false;
                    fl.removeAllViews();
                    openFragment(new InfoFragment());
                    return true;
            }
            return false;
        });
        btnAdd.setOnClickListener(v -> {
            if(addvis){
                Intent i = new Intent(MainActivity.this, CreateFormActivity.class);
                startActivity(i);
            }
            else{
                addvis = true;
                btnAdd.setImageResource(R.drawable.ic_baseline_add_24);
                bottomNavigation.setSelectedItemId(R.id.home);
                openFragment(new HomeFragment());
            }
        });

    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "current");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}