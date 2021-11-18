package com.fpoly.pro1121.adminapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.adapter.DetailsOrderAdapter;
import com.fpoly.pro1121.adminapp.model.ProductOrder;

import java.util.List;

public class ShowDetailsOrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvShowDetails;
    DetailsOrderAdapter detailsOrderAdapter;
    List<ProductOrder>list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details_order);
        initUI();
        initToolbar();
    }
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_details_order);
        toolbar.setTitle("Danh Sách Sản Phẩm Trong Hoá Đơn");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}