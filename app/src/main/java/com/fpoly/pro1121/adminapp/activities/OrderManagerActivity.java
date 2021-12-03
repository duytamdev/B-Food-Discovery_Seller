package com.fpoly.pro1121.adminapp.activities;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.Utils;
import com.fpoly.pro1121.adminapp.adapter.UserOrderAdapter;
import com.fpoly.pro1121.adminapp.model.Order;
import com.fpoly.pro1121.adminapp.model.ProductOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderManagerActivity extends AppCompatActivity {

    TextView tvUnitPrice;
    Toolbar toolbar;
    RecyclerView rvUserOrder;
    UserOrderAdapter userOrderAdapter;
    List<Order> list;
    int unitPrice = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);
        initUI();
        initToolbar();
        readDataRealtime();
    }
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_order_manager);
        toolbar.setTitle("Danh Sách Hoá Đơn");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }
    private void readDataRealtime() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        db
                .collection("orders")
                // sort từ hiện tại đến quá khứ
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("-->", "Listen failed.", error);
                            return;
                        }
                        if (value != null) {
                            try {
                                unitPrice=0;
                                List<Order> clones= new ArrayList<>();
                                for (DocumentSnapshot document : value.getDocuments()) {
                                    Map<String, Object> data = document.getData();
                                    assert data != null;
                                    String id = (String) data.get("id");
                                    String idUser = (String) data.get("userID");
                                    int unitPriceOrder = ((Long) Objects.requireNonNull(data.get("unitPrice"))).intValue();
                                    String state = (String) data.get("state");
                                    // chỉ tính tổng những hoá đơn đã hoàn thành
                                    if(state.equalsIgnoreCase("hoàn thành")){
                                        unitPrice+= unitPriceOrder;
                                    }
                                    Timestamp stamp = (Timestamp) data.get("date");
                                    assert stamp != null;
                                    Date date = stamp.toDate();
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
                                    Order order = new Order(id,idUser,productOrderList,unitPriceOrder,date,state);
                                    clones.add(order);
                                }
                               list = new ArrayList<>();
                                list.addAll(clones);
                                userOrderAdapter.setData(list);
                                tvUnitPrice.setText(Utils.getFormatNumber(unitPrice));
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void initUI() {
        tvUnitPrice = findViewById(R.id.tv_unit_price_orders);
        rvUserOrder = findViewById(R.id.rv_user_order);
        userOrderAdapter = new UserOrderAdapter(new UserOrderAdapter.IClickUserOrderListener() {
            @Override
            public void clickShowDetails(List<ProductOrder> productOrderList) {
                Intent intent = new Intent(OrderManagerActivity.this,ShowDetailsOrderActivity.class);
                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) productOrderList);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }

            @Override
            public void clickChangeState(Order order) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(OrderManagerActivity.this);
                bottomSheetDialog.setContentView(R.layout.dialog_change_state_order);
                RadioGroup rdoGroupState = bottomSheetDialog.findViewById(R.id.rdo_group_state);
                RadioButton rdoCancel = bottomSheetDialog.findViewById(R.id.rdo_state_cancel);
                RadioButton rdoProcessing = bottomSheetDialog.findViewById(R.id.rdo_state_processing);
                RadioButton rdoCompleted = bottomSheetDialog.findViewById(R.id.rdo_state_completed);
                Button btnUpdateState = bottomSheetDialog.findViewById(R.id.btn_update_state_order);
                // get state order
                if(order.getState().equalsIgnoreCase("đã huỷ")){
                    assert rdoCancel != null;
                    rdoCancel.setChecked(true);
                }
                else if(order.getState().equalsIgnoreCase("đang chuẩn bị")){
                    assert rdoProcessing != null;
                    rdoProcessing.setChecked(true);
                }else{
                    assert rdoCompleted != null;
                    rdoCompleted.setChecked(true);
                }
                assert btnUpdateState != null;
                btnUpdateState.setOnClickListener(view -> {
                    assert rdoGroupState != null;
                    int selectedId = rdoGroupState.getCheckedRadioButtonId();
                    RadioButton rdoSelected = bottomSheetDialog.findViewById(selectedId);
                    String state = rdoSelected.getText().toString();
                    changeStateOrder(order.getId(),state);
                    bottomSheetDialog.dismiss();
                });
                bottomSheetDialog.show();

            }
        });
        userOrderAdapter.setData(list);
        rvUserOrder.setAdapter(userOrderAdapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        rvUserOrder.setLayoutManager(linearLayout);
    }

    private void changeStateOrder(String orderID,String newState) {
        db.collection("orders").document(orderID).update("state",newState).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(OrderManagerActivity.this,"Cập nhật thành công", LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OrderManagerActivity.this,"Cập nhật không thành công", LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}