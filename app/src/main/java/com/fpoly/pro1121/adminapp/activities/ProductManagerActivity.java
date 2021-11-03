package com.fpoly.pro1121.adminapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.fpoly.pro1121.adminapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductManagerActivity extends AppCompatActivity {

    RecyclerView rvProduct;
    FloatingActionButton  fabAddProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);
        initUI();
    }

    private void initUI() {
        rvProduct = findViewById(R.id.rv_product);
        fabAddProduct = findViewById(R.id.fab_add_product);
    }
}