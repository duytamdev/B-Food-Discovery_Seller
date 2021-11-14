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
import com.fpoly.pro1121.adminapp.model.Order;
import com.fpoly.pro1121.adminapp.model.ProductOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.UserOrderViewHoder>{

    public interface IClickUserOrderListener {
        void clickShowDetails(List<ProductOrder> productOrderList);
    }
    IClickUserOrderListener iClickUserOrderListener;

    List<Order> list = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserOrderAdapter(IClickUserOrderListener iClickUserOrderListener) {
        this.iClickUserOrderListener = iClickUserOrderListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Order> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UserOrderViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_order,parent,false);
        return new UserOrderViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderViewHoder holder, int position) {
        Order order = list.get(position);
        if(order == null) return;
        String date = Utils.DateToString(order.getDate());
        holder.tvDate.setText(date);
        holder.tvUnitPrice.setText(order.getUnitPrice()+" đ");
        String idUser = order.getUserID();
        db.collection("users")
                .document(idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Map<String,Object> data = document.getData();
                                String urlImage = (String) data.get("urlImage");
                                String phoneNumber = (String) data.get("phoneNumber");
                                String location = (String) data.get("location");
                                String name = (String) data.get("name");
                                // to view

                                if(!urlImage.isEmpty()){
                                    Glide.with(holder.itemView.getContext())
                                            .load(urlImage)
                                            .centerCrop()
                                            .into(holder.ivAvt);
                                }
                                holder.tvPhoneNumber.setText(phoneNumber);
                                holder.tvLocation.setText(location);
                                holder.tvName.setText(name);

                            }
                        }
                    }
                });
        holder.itemView.setOnClickListener(view->{
            iClickUserOrderListener.clickShowDetails(order.getProductOrderList());
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null) return list.size();
        return 0;
    }

    public class UserOrderViewHoder extends RecyclerView.ViewHolder {
        CircleImageView ivAvt;
        TextView tvName,tvPhoneNumber,tvLocation,tvDate,tvUnitPrice;
        public UserOrderViewHoder(@NonNull View itemView) {
            super(itemView);
            ivAvt = itemView.findViewById(R.id.iv_avt_user_order);
            tvName = itemView.findViewById(R.id.tv_name_user_order);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number_user_order);
            tvLocation = itemView.findViewById(R.id.tv_location_user_order);
            tvDate = itemView.findViewById(R.id.tv_date_user_order);
            tvUnitPrice = itemView.findViewById(R.id.tv_unitPrice_user_order);
        }
    }
}
