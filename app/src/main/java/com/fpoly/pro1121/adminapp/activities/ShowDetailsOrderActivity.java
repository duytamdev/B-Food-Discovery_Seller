package com.fpoly.pro1121.adminapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.adapter.DetailsOrderAdapter;
import com.fpoly.pro1121.adminapp.model.ProductOrder;

import java.util.List;

public class ShowDetailsOrderActivity extends AppCompatActivity {

    RecyclerView rvShowDetails;
    DetailsOrderAdapter detailsOrderAdapter;
    List<ProductOrder>list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details_order);
        initUI();
    }

    private void readData() {
        Intent intent = getIntent();
        list = intent.getParcelableArrayListExtra("list");
        detailsOrderAdapter.setData(list);
    }

    private void initUI() {
        rvShowDetails = findViewById(R.id.rv_show_details_order);
        detailsOrderAdapter = new DetailsOrderAdapter();
        detailsOrderAdapter.setData(list);
        rvShowDetails.setAdapter(detailsOrderAdapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        rvShowDetails.setLayoutManager(linearLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        readData();
    }
}