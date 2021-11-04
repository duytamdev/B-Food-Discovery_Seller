package com.fpoly.pro1121.adminapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.model.Category;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomArrayAdapter extends ArrayAdapter<Category> {
    Context context;
    public CustomArrayAdapter(@NonNull Context context, int resource, List<Category> categories) {
        super(context, resource,categories);
        this.context = context;

    }
    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_category,parent,false);
        }

        TextView tvName = currentItemView.findViewById(R.id.tv_name_category_spinner_item);
        CircleImageView img = currentItemView.findViewById(R.id.iv_category_spinner_item);

        Category category = this.getItem(position);
        tvName.setText(category.getName());
        Glide.with(context)
                .load(category.getUrlImage())
                .centerCrop()
                .into(img);
        return currentItemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_category,parent,false);
        }
        TextView tvName = currentItemView.findViewById(R.id.tv_name_category_spinner_item);
        CircleImageView img = currentItemView.findViewById(R.id.iv_category_spinner_item);

        Category category = this.getItem(position);
        tvName.setText(category.getName());
        Glide.with(context)
                .load(category.getUrlImage())
                .centerCrop()
                .into(img);

        return currentItemView;
    }
}
