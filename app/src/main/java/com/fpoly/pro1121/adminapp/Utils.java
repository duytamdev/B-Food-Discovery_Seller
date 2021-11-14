package com.fpoly.pro1121.adminapp;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static String DateToString(Date date){
        return simpleDateFormat.format(date).toString();
    }
    public static Date StringToDate(String sDate){
        try {
            return  simpleDateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getFormatNumber(int number) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        return (currencyVN.format(number));
    }
    public static void addTextChangedListener(EditText e, final TextInputLayout t, boolean isEmail) {
        e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b&& e.getText().toString().isEmpty()){
                    t.setEnabled(true);
                    t.setError("Không được để trống");
                }
            }
        });
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()<=0) {
                    t.setEnabled(true);
                    t.setError("Không được để trống");
                }
                else if(isEmail && !Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                    t.setEnabled(true);
                    t.setError("Email không hợp lệ");
                }
                else{
                    t.setError(null);
                    t.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
