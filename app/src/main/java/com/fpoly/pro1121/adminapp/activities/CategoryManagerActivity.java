package com.fpoly.pro1121.adminapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class CategoryManagerActivity extends AppCompatActivity {

    RecyclerView rvCategory;
    FloatingActionButton fabAddCategory;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);
        initUI();
        events();
    }


    private void initUI() {
        rvCategory = findViewById(R.id.rv_category);
        fabAddCategory = findViewById(R.id.fab_add_category);
    }

    private void events() {
        fabAddCategory.setOnClickListener(view -> openDialogAddUpdatedCategory(true));
    }

    private void openDialogAddUpdatedCategory(boolean isAdd, Category ...categoryCurrent) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_add_category);
        EditText edtName = dialog.findViewById(R.id.edt_name_category);
        Button btnAdd = dialog.findViewById(R.id.btn_add_category);
        if(isAdd){
            btnAdd.setText("Add");
            btnAdd.setOnClickListener(view -> {
               try {
                   String name = edtName.getText().toString();
                   // get chuỗi random làm id category
                   UUID uuid = UUID.randomUUID();
                   Category category = new Category(uuid.toString(),name);
                   addCategory(category);
                   dialog.dismiss();
               }catch(Exception e){
                   e.printStackTrace();
               }
            });
        }
        else{
            btnAdd.setText("Update");
        }
        dialog.show();
    }

    private void addCategory(Category category) {
        db.collection("categories")
                .add(category)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CategoryManagerActivity.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CategoryManagerActivity.this,"Thêm không thành công",Toast.LENGTH_SHORT).show();
                    }
                });
    }


}