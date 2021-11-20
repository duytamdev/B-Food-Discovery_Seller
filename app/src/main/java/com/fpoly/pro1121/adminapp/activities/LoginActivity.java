package com.fpoly.pro1121.adminapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.Utils;

import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout tilEmail,tilPassword;
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
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_password_login);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void events() {
        Utils.addTextChangedListener(edtEmail,tilEmail,true);
        Utils.addTextChangedListenerPass(edtPassword,tilPassword);
        btnLogin.setOnClickListener(view -> {
            try {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if(email.isEmpty()|| password.isEmpty()||tilEmail.getError()!=null|| tilPassword.getError()!=null){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login failed")
                            .setMessage("Vui lòng điền đầy đủ thông tin!")
                            .setNegativeButton("Thử lại",null)
                            .show();
                    return;
                }
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
                .addOnSuccessListener(authResult -> {
                    // kiểm tra quyền của user
                    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                   DocumentReference docRef = db.collection("users").document(userID);
                   docRef.get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if(document.exists()){
                                boolean isAdmin = document.getBoolean("admin");
                                if(isAdmin) {
                                    Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();
                                }else{
                                    new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("Login failed")
                                            .setMessage("Tài Khoản Không Có Quyền Đăng Nhập!")
                                            .setNegativeButton("Thử lại",null)
                                            .show();
                                }
                            }

                        }else{
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Login failed")
                                    .setMessage("Tài khoản hoặc mật khẩu không chính xác!")
                                    .setNegativeButton("Thử lại",null)
                                    .show();
                        }
                       progressDialog.dismiss();
                   });
                })
                .addOnFailureListener(e -> {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login failed")
                            .setMessage("Tài khoản hoặc mật khẩu không chính xác!")
                            .setNegativeButton("Thử lại",null)
                            .show();
                    progressDialog.dismiss();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            if(email!=null){
                edtEmail.setText(email);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click phím back lần nữa để thoát", Toast.LENGTH_SHORT).show();
        // nếu quá 2 giây ko thao tác thì chuyen trang thai false
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}