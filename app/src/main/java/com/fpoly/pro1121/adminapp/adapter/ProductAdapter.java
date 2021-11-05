package com.fpoly.pro1121.adminapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

   public interface IClickPressedListener{
       void clickDelete(String productID);
       void clickUpdate(Product product);
   }
   IClickPressedListener iClickPressedListener;

    public ProductAdapter(IClickPressedListener iClickPressedListener) {
        this.iClickPressedListener = iClickPressedListener;
    }

    List<Product> products = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        if(product==null) return;
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice()+" vnd");
        Glide.with(holder.itemView.getContext())
                .load(product.getUrlImage())
                .centerCrop()
                .into(holder.ivImage);
        holder.ivDelete.setOnClickListener(view-> iClickPressedListener.clickDelete(product.getId()));
        holder.ivUpdate.setOnClickListener(view-> iClickPressedListener.clickUpdate(product));

    }

    @Override
    public int getItemCount() {
        if(products !=null) return products.size();
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivImage;
        TextView tvName,tvPrice;
        ImageView ivDelete,ivUpdate;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_item_product);
            tvName = itemView.findViewById(R.id.tv_item_product);
            tvPrice = itemView.findViewById(R.id.tv_price_product);
            ivDelete = itemView.findViewById(R.id.iv_DeleteButton_product);
            ivUpdate = itemView.findViewById(R.id.iv_EditButton_product);
        }
    }
}
