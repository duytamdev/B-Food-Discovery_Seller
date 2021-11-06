package com.fpoly.pro1121.adminapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpoly.pro1121.adminapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail,edtPassword;
    Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initUI();
        events();
    }


    private void initUI() {
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_password_login);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void events() {
        btnLogin.setOnClickListener(view -> {
            try {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                actionLogin(email,password);

            }catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void actionLogin(String email, String password) {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("loading....");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String userID = mAuth.getCurrentUser().getUid();
                       DocumentReference docRef = db.collection("users").document(userID);
                       docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        boolean isAdmin = document.getBoolean("admin");
                                        if(isAdmin) {
                                            Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        }else{
                                            Toast.makeText(LoginActivity.this,"Tài khoản không có quyền truy cập",Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                    }

                                }else{
                                    Toast.makeText(LoginActivity.this,"Login false",Toast.LENGTH_SHORT).show();
                                }
                           }
                       });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this,"Login không thành công",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}