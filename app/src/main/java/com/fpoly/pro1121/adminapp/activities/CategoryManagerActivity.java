package com.fpoly.pro1121.adminapp.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.Utils;
import com.fpoly.pro1121.adminapp.adapter.CategoryAdapter;
import com.fpoly.pro1121.adminapp.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CategoryManagerActivity extends AppCompatActivity {

    RecyclerView rvCategory;
    FloatingActionButton fabAddCategory;
    CategoryAdapter categoryAdapter;
    List<Category> categories;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String urlImage = "https://firebasestorage.googleapis.com/v0/b/project-1-android-teamwork.appspot.com/o/imgCategory%2Fhamburger.png?alt=media&token=acbdd123-075f-470b-8ac2-6c169bb2d7f4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);
        initUI();
        initRecyclerView();
        readDataRealtime();
        events();
    }

    private void readDataRealtime() {
        db.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value!=null){
                            try {
                                List<Category> clones = new ArrayList<>();
                                List<DocumentSnapshot> snapshotsList = value.getDocuments();
                                for(DocumentSnapshot snapshot:  snapshotsList){
                                    Map<String, Object> data = snapshot.getData();
                                    assert data != null;
                                    String id = Objects.requireNonNull(data.get("id")).toString();
                                    String name = Objects.requireNonNull(data.get("name")).toString();
                                    String urlImage = Objects.requireNonNull(data.get("urlImage")).toString();
                                    Category category = new Category(id,name,urlImage);
                                    clones.add(category);
                                }
                                categories = new ArrayList<>();
                                categories.addAll(clones);
                                categoryAdapter.setData(categories);
                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void initRecyclerView() {
        categoryAdapter = new CategoryAdapter(new CategoryAdapter.IClickCategory() {
            // khi click show 1 alerdialog xác nhận xoá ( truyền 1 categoryID)
            @Override
            public void clickDelete(String categoryID) {
                // click delete category
                new AlertDialog.Builder(CategoryManagerActivity.this)
                        .setTitle("Xác Nhận")
                        .setMessage("Bạn có thật sự muốn xoá ?")
                        .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // xoá category ở đây
                                // nhận 1 categoryID rồi xoá trên server
                                deleteCategory(categoryID);

                            }
                        })
                        .setNegativeButton("Huỷ",null)
                        .show();

            }

            @Override
            public void clickUpdate(Category category) {
                openDialogAddUpdatedCategory(false,category);
            }
        });
        categoryAdapter.setData(categories);
        rvCategory.setAdapter(categoryAdapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rvCategory.setLayoutManager(linearLayout);
    }

    private void deleteCategory(String categoryID) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("category").child(categoryID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CategoryManagerActivity.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
            }
        });
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
        TextInputLayout tilNameCategory = dialog.findViewById(R.id.til_name_category);
        EditText edtName = dialog.findViewById(R.id.edt_name_category);
        Button btnAdd = dialog.findViewById(R.id.btn_add_category);
        assert edtName != null;
        Utils.addTextChangedListener(edtName,tilNameCategory,false);
        if(isAdd){
            btnAdd.setText("Add");
            btnAdd.setOnClickListener(view -> {
               try {
                   String name = edtName.getText().toString().trim();
                   //check validation
                   if(name.isEmpty()||tilNameCategory.getError()!=null){
                       return;
                   }
                   // get chuỗi random làm id category
                   UUID uuid = UUID.randomUUID();
                   String idCategory = uuid.toString();
                   // tạo 1 đối tượng rồi add vào firebase
                   Category category = new Category(idCategory,name,urlImage);
                   // hàm add
                   addCategory(category);
                   dialog.dismiss();
               }catch(Exception e){
                   e.printStackTrace();
               }
            });
        }
        else{
            try {
                btnAdd.setText("Update");
                edtName.setText(categoryCurrent[0].getName());
                btnAdd.setOnClickListener(view -> {
                    String name = edtName.getText().toString();
                    categoryCurrent[0].setName(name);
                    if(name.isEmpty()||tilNameCategory.getError()!=null){
                        return;
                    }
                    updateCategory(categoryCurrent[0]);
                    dialog.dismiss();
                });

            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        dialog.show();
    }

    private void updateCategory(Category category) {
        db.collection("categories").document(category.getId()).update("name",category.getName())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CategoryManagerActivity.this,"Cập nhập thành công",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // nhận 1 category
    private void addCategory(Category category) {
        // viết code ở đây
        // lưu ý: tạo 1 document trùng với categoryID
    }


}