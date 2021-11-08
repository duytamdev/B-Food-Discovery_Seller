package com.fpoly.pro1121.adminapp.adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.adminapp.R;
import com.fpoly.pro1121.adminapp.Utils;
import com.fpoly.pro1121.adminapp.model.ProductOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsOrderAdapter extends RecyclerView.Adapter<DetailsOrderAdapter.DetailsOrderViewHoder>{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<ProductOrder> list = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ProductOrder> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DetailsOrderViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_order,parent,false);
        return new DetailsOrderViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsOrderViewHoder holder, int position) {
        ProductOrder productOrder = list.get(position);
        if(productOrder==null) return;
        db.collection("products")
                .document(productOrder.getIdProduct())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Map<String,Object> data = document.getData();
                                String urlImage = (String) data.get("urlImage");
                                if(!urlImage.isEmpty()){
                                    Glide.with(holder.itemView.getContext())
                                            .load(urlImage)
                                            .into(holder.ivAvt);
                                }
                                holder.tvName.setText((String) data.get("name"));
                            }
                        }
                    }
                });
        holder.tvQuantity.setText("số lượng: "+productOrder.getQuantity());
        holder.tvPrice.setText(Utils.getFormatNumber(productOrder.getUnitPrice()));
    }

    @Override
    public int getItemCount() {
        if(list!=null) return list.size();
        return 0;
    }

    public class DetailsOrderViewHoder extends RecyclerView.ViewHolder {
        CircleImageView ivAvt;
        TextView tvName,tvPrice,tvQuantity;
        public DetailsOrderViewHoder(@NonNull View itemView) {
            super(itemView);
            ivAvt = itemView.findViewById(R.id.iv_detail_order);
            tvName = itemView.findViewById(R.id.tv_name_detail_order);
            tvPrice = itemView.findViewById(R.id.tv_price_detail_order);
            tvQuantity = itemView.findViewById(R.id.tv_quantity_detail_order);
        }
    }
}
