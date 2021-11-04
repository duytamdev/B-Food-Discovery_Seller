package com.fpoly.pro1121.adminapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.fpoly.pro1121.adminapp.R;

public class AddProductActivity extends AppCompatActivity {

    ImageView ivProduct;
    EditText edtName,edtPrice,edtDescription;
    Spinner spinnerCategory;
    Button btnAddProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initUI();
    }

    private void initUI() {
        ivProduct = findViewById(R.id.ivAddProduct_food);
        edtName = findViewById(R.id.etAddProduct_name);
        edtPrice = findViewById(R.id.etAddProduct_description);
        spinnerCategory = findViewById(R.id.addProduct_spinner);
        btnAddProduct = findViewById(R.id.btnAddProduct);
    }
}