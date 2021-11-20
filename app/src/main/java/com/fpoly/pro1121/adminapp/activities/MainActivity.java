package com.fpoly.pro1121.adminapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click phím back lần nữa để thoát", Toast.LENGTH_SHORT).show();
        // nếu quá 2 giây ko thao tác thì chuyen trang thai false
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

}