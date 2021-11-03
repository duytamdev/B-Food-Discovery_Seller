package com.fpoly.pro1121.adminapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.fpoly.pro1121.adminapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryManagerActivity extends AppCompatActivity {

    RecyclerView rvCategory;
    FloatingActionButton fabAddCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);
        initUI();
    }

    private void initUI() {
        rvCategory = findViewById(R.id.rv_category);
        fabAddCategory = findViewById(R.id.fab_add_category);
    }
}