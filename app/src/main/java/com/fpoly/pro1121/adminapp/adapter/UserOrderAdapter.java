package com.fpoly.pro1121.adminapp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        void clickChangeState(Order order);
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
        holder.tvDate.setText(Utils.dateToString(order.getDate()));
        holder.tvTime.setText(Utils.timeToString(order.getDate()));
        holder.tvUnitPrice.setText(Utils.getFormatNumber(order.getUnitPrice()));
        holder.tvStateOrder.setText(order.getState());
        holder.tvChangeState.setOnClickListener(view -> iClickUserOrderListener.clickChangeState(order));
        try {
            if(order.getState().equalsIgnoreCase("hoàn thành")){
                holder.tvStateOrder.setTextColor(holder.itemView.getResources().getColor(R.color.green));
                holder.layoutDate.setBackgroundColor(holder.itemView.getResources().getColor(R.color.green));
            }
            else if(order.getState().equalsIgnoreCase("đang chuẩn bị")){
                holder.tvStateOrder.setTextColor(holder.itemView.getResources().getColor(R.color.yellow));
                holder.layoutDate.setBackgroundColor(holder.itemView.getResources().getColor(R.color.yellow));
            }
            else{
                holder.tvStateOrder.setTextColor(holder.itemView.getResources().getColor(R.color.red));
                holder.layoutDate.setBackgroundColor(holder.itemView.getResources().getColor(R.color.red));

            }
        }catch(Exception e){
            e.printStackTrace();
        }


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
                                String phoneNumber = (String) data.get("phoneNumber");
                                String location = (String) data.get("location");
                                String name = (String) data.get("name");
                                // to view
                                holder.tvPhoneNumber.setText(phoneNumber);
                                holder.tvLocation.setText(location);
                                holder.tvName.setText(name);

                            }
                        }
                    }
                });
        holder.layoutContent.setOnClickListener(view->{
            iClickUserOrderListener.clickShowDetails(order.getProductOrderList());
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null) return list.size();
        return 0;
    }

    public class UserOrderViewHoder extends RecyclerView.ViewHolder {
        TextView tvName,tvPhoneNumber,tvLocation,tvDate,tvUnitPrice,tvTime,tvStateOrder,tvChangeState;
        LinearLayout layoutDate;
        RelativeLayout layoutContent;
        public UserOrderViewHoder(@NonNull View itemView) {
            super(itemView);
            layoutContent = itemView.findViewById(R.id.layout_content_order);
            layoutDate = itemView.findViewById(R.id.layout_date);
            tvTime = itemView.findViewById(R.id.tv_time_user_order);
            tvName = itemView.findViewById(R.id.tv_name_user_order);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number_user_order);
            tvLocation = itemView.findViewById(R.id.tv_location_user_order);
            tvDate = itemView.findViewById(R.id.tv_date_user_order);
            tvUnitPrice = itemView.findViewById(R.id.tv_unitPrice_user_order);
            tvStateOrder = itemView.findViewById(R.id.tv_state_order);
            tvChangeState = itemView.findViewById(R.id.tv_change_state_order);
        }
    }
}
