package com.fpoly.pro1121.adminapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fpoly.pro1121.adminapp.R;

public class MainActivity extends AppCompatActivity {

    ImageView ivCategoryManager,ivProductManager,ivOrderManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        events();
    }


    private void initUI() {
        ivCategoryManager = findViewById(R.id.iv_category_manager);
        ivProductManager = findViewById(R.id.iv_product_manager);
        ivOrderManager = findViewById(R.id.iv_order_manager);
    }
    private void events() {
        ivCategoryManager.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,CategoryManagerActivity.class));
        });
        ivProductManager.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,ProductManagerActivity.class));

        });
        ivOrderManager.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,OrderManagerActivity.class));

        });
    }

}