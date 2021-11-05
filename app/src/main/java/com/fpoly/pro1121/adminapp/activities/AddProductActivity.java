package com.fpoly.pro1121.adminapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.adapter.CustomArrayAdapter;
import com.fpoly.pro1121.adminapp.model.Category;
import com.fpoly.pro1121.adminapp.model.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    ImageView ivProduct;
    EditText edtName,edtPrice,edtDescription;
    Spinner spinnerCategory;
    Button btnAddProduct;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Category> categories;
    String categoryIDSelected;
    CustomArrayAdapter customArrayAdapter;
    String urlImageProductSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initUI();
        readCategoriesRealtime();
        actionAddProduct();
    }


    private void actionAddProduct() {
        Intent intent = getIntent();
        ivProduct.setOnClickListener(view->{
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            activityResultLauncher.launch(photoPickerIntent);
        });
        if(intent.getAction().equals("add")){
            btnAddProduct.setText("Add Product");

            btnAddProduct.setOnClickListener(view ->{
                try {
                    UUID uuid = UUID.randomUUID();
                    String id = uuid.toString();
                    String name = edtName.getText().toString();
                    int price = Integer.valueOf(edtPrice.getText().toString());
                    String descriptor = edtDescription.getText().toString();
                    String categoryID = categoryIDSelected;
                    String urlImage = urlImageProductSelected;
                    Product product = new Product(id,urlImage,name,price,descriptor,categoryID);
                    addProductToServer(product);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            });
        }else if(intent.getAction().equals("update")){
            Product product = intent.getParcelableExtra("product");
            Glide.with(this)
                    .load(product.getUrlImage())
                    .centerCrop()
                    .into(ivProduct);
            urlImageProductSelected = product.getUrlImage();
            edtName.setText(product.getName());
            edtPrice.setText(product.getPrice()+"");
            edtDescription.setText(product.getDescription());

            btnAddProduct.setText("Update Product");
            btnAddProduct.setOnClickListener(view->{
                try {
                    String name = edtName.getText().toString();
                    int price = Integer.valueOf(edtPrice.getText().toString());
                    String descriptor = edtDescription.getText().toString();
                    String categoryID = categoryIDSelected;
                    String urlImage = urlImageProductSelected;
                    product.updateProduct(urlImage,name,price,descriptor,categoryID);
                    updateProduct(product);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            });

        }


    }

    private void updateProduct(Product product) {
        db.collection("products")
                .document(product.getId())
                .update(
                        "name",product.getName(),
                        "urlImage",product.getUrlImage(),
                        "price",product.getPrice(),
                        "description",product.getDescription(),
                        "categoryID",product.getCategoryID())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddProductActivity.this,"Cập nhập thành công",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addProductToServer(Product product) {
        db.collection("products")
                .document(product.getId())
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddProductActivity.this,"Thêm Thành Công",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("--->", "onFailure: "+e.getMessage() );
                    }
                });
    }


    private void readCategoriesRealtime() {
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
                                // get categories to spinner
                                customArrayAdapter = new CustomArrayAdapter(AddProductActivity.this, R.layout.item_spinner_category, categories);
                                spinnerCategory.setAdapter(customArrayAdapter);
                                spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        categoryIDSelected = customArrayAdapter.getItem(i).getId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){
                        Intent data = result.getData();
                        try {
                            Uri uriImage = data.getData();
                            ivProduct.setImageURI(uriImage); // dom
                            StorageReference ref  = FirebaseStorage.getInstance().getReference().child("imagesProduct").child(UUID.randomUUID().toString());
                            UploadTask uploadTask = ref.putFile(uriImage);
                            ProgressDialog progressDialog = new ProgressDialog(AddProductActivity.this);

                            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    progressDialog.setMessage("loading....");
                                    progressDialog.show();
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        urlImageProductSelected = task.getResult().toString();
                                        progressDialog.dismiss();
                                    }
                                }
                            });

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private void initUI() {
        ivProduct = findViewById(R.id.ivAddProduct_food);
        edtName = findViewById(R.id.etAddProduct_name);
        edtPrice = findViewById(R.id.etAddProduct_price);
        edtDescription = findViewById(R.id.etAddProduct_description);
        spinnerCategory = findViewById(R.id.addProduct_spinner);
        btnAddProduct = findViewById(R.id.btnAddProduct);
    }
}