package com.fpoly.pro1121.adminapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.adapter.UserOrderAdapter;
import com.fpoly.pro1121.adminapp.model.Order;
import com.fpoly.pro1121.adminapp.model.ProductOrder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderManagerActivity extends AppCompatActivity {

    RecyclerView rvUserOrder;
    UserOrderAdapter userOrderAdapter;
    List<Order> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);
        initUI();
        readDataRealtime();
    }

    private void readDataRealtime() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        db
                .collection("orders")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("-->", "Listen failed.", error);
                            return;
                        }
                        if (value != null) {
                            try {
                                List<Order> clones= new ArrayList<>();
                                for (DocumentSnapshot document : value.getDocuments()) {
                                    Map<String, Object> data = document.getData();
                                    assert data != null;
                                    String id = (String) data.get("id");
                                    String idUser = (String) data.get("userID");
                                    int unitPriceOrder = ((Long) Objects.requireNonNull(data.get("unitPrice"))).intValue();
                                    // get list productOrder
                                    List<ProductOrder> productOrderList= new ArrayList<>();
                                    List<Map<String,Object>> productOrders = (List<Map<String, Object>>) data.get("productOrderList");
                                    assert productOrders != null;
                                    for(Map<String,Object> dataOfProductOrders : productOrders){
                                        int idProductOrder = ((Long) dataOfProductOrders.get("id")).intValue();
                                        String idProduct = (String) dataOfProductOrders.get("idProduct");
                                        int priceProduct = ((Long) dataOfProductOrders.get("priceProduct")).intValue();
                                        int quantity = ((Long) dataOfProductOrders.get("quantity")).intValue();
                                        int unitPrice = ((Long) dataOfProductOrders.get("unitPrice")).intValue();
                                        ProductOrder productOrder = new ProductOrder(idProductOrder,idUser,idProduct,priceProduct,quantity,unitPrice);
                                        productOrderList.add(productOrder);
                                    }
                                    Order order = new Order(id,idUser,productOrderList,unitPriceOrder);
                                    clones.add(order);
                                }
                               list = new ArrayList<>();
                                list.addAll(clones);
                                userOrderAdapter.setData(list);
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void initUI() {
        rvUserOrder = findViewById(R.id.rv_user_order);
        userOrderAdapter = new UserOrderAdapter(new UserOrderAdapter.IClickUserOrderListener() {
            @Override
            public void clickShowDetails(List<ProductOrder> productOrderList) {
                Intent intent = new Intent(OrderManagerActivity.this,ShowDetailsOrderActivity.class);
                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) productOrderList);
                startActivity(intent);
            }
        });
        userOrderAdapter.setData(list);
        rvUserOrder.setAdapter(userOrderAdapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        rvUserOrder.setLayoutManager(linearLayout);
    }
}