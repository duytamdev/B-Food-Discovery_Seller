package com.fpoly.pro1121.adminapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.adapter.ProductAdapter;
import com.fpoly.pro1121.adminapp.model.Category;
import com.fpoly.pro1121.adminapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductManagerActivity extends AppCompatActivity {

    RecyclerView rvProduct;
    FloatingActionButton  fabAddProduct;
    ProductAdapter productAdapter;
    List<Product> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);
        initUI();
        initRecyclerView();
        actionAddProduct();
        readDataRealtime();
    }

    private void readDataRealtime() {
        db.collection("products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null){
                            try {
                                List<Product> clones = new ArrayList<>();
                                List<DocumentSnapshot> snapshotsList = value.getDocuments();
                                for(DocumentSnapshot snapshot:  snapshotsList){
                                    Map<String, Object> data = snapshot.getData();
                                    assert data != null;
                                    String id = (String) data.get("id");
                                    String name = (String) data.get("name");
                                    int price =( (Long) data.get("price")).intValue();
                                    String categoryID = (String) data.get("IdCategory");
                                    String urlImage = (String) data.get("urlImage");
                                    String description = (String) data.get("description");
                                    Product product = new Product(id,urlImage,name,price,description,categoryID);
                                    clones.add(product);
                                }
                                list = new ArrayList<>();
                                list.addAll(clones);
                                productAdapter.setData(list);

                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }

    private void initRecyclerView() {
        productAdapter = new ProductAdapter(new ProductAdapter.IClickPressedListener() {
            @Override
            public void clickDelete(String productID) {
            new AlertDialog.Builder(ProductManagerActivity.this)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có thật sự muốn xoá không ?")
                    .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteProduct(productID);
                        }
                    })
                    .setNegativeButton("Huỷ",null)
                    .show();
            }

            @Override
            public void clickUpdate(Product product) {
                Intent intent = new Intent("update",null,ProductManagerActivity.this,AddProductActivity.class);
                intent.putExtra("product",product);
                startActivity(intent);

            }
        });
        productAdapter.setData(list);
        LinearLayoutManager linearLayout =new LinearLayoutManager(this);
        rvProduct.setLayoutManager(linearLayout);
        rvProduct.setAdapter(productAdapter);
    }

    private void deleteProduct(String productID) {
        db.collection("products").document(productID).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProductManagerActivity.this, "Xoá Thành Công", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.e("--->", "onComplete: error" );
                        }
                    }
                });
    }

    private void actionAddProduct() {
        fabAddProduct.setOnClickListener(view ->{

            startActivity(new Intent("add",null,ProductManagerActivity.this,AddProductActivity.class));
        });
    }


    private void initUI() {
        rvProduct = findViewById(R.id.rv_product);
        fabAddProduct = findViewById(R.id.fab_add_product);
    }
}