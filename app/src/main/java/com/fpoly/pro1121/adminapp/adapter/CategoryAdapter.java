package com.fpoly.pro1121.adminapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    List<Category> list = new ArrayList<>();

    public interface IClickCategory{
        void clickDelete(String categoryID);
        void clickUpdate(Category category);
    }
    IClickCategory iClickCategory;

    public CategoryAdapter(IClickCategory iClickCategory) {
        this.iClickCategory = iClickCategory;
    }

    public void setData(List<Category> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one_category_item,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = list.get(position);
        if(category==null) return;
        holder.tvName.setText(category.getName());
        Glide.with(holder.itemView.getContext())
                .load(category.getUrlImage())
                .centerCrop()
                .into(holder.img);

        holder.ivDelete.setOnClickListener( view -> iClickCategory.clickDelete(category.getId()));
        holder.ivUpdate.setOnClickListener( view -> iClickCategory.clickUpdate(category));
    }

    @Override
    public int getItemCount() {
        if(list!=null) return list.size();
        return 0;
    }

    public  class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView img,ivDelete,ivUpdate;
        TextView tvName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_Category_Name);
            img = itemView.findViewById(R.id.ivIcon);
            ivDelete = itemView.findViewById(R.id.iv_DeleteButton);
            ivUpdate = itemView.findViewById(R.id.iv_EditButton);
        }
    }
}
