package com.fpoly.pro1121.adminapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
        actionAddProduct();
    }

    private void actionAddProduct() {
        fabAddProduct.setOnClickListener(view ->{
            startActivity(new Intent(ProductManagerActivity.this,AddProductActivity.class));
        });
    }


    private void initUI() {
        rvProduct = findViewById(R.id.rv_product);
        fabAddProduct = findViewById(R.id.fab_add_product);
    }
}