package com.fpoly.pro1121.adminapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

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
        ivCategoryManager.setOnClickListener(view -> startMyActivity(CategoryManagerActivity.class));
        ivProductManager.setOnClickListener(view -> startMyActivity(ProductManagerActivity.class));
        ivOrderManager.setOnClickListener(view -> startMyActivity(OrderManagerActivity.class));
    }
    private void startMyActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this,cls);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
    }
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public void onBackPressed() {
        // doubleBackToTrue = true: thoát ứng dụng
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        // click lần 1: doubleBackToExit = true , show thông báo
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this,"Click phím back lần nữa để thoát", Toast.LENGTH_SHORT).show();
    }

}