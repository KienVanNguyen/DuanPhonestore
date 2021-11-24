package com.kiennv.duanphonestore.User.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.ChitietspActivity;
import com.kiennv.duanphonestore.User.Model.OrderDetail;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private List<OrderDetail> orderDetailList;
    private LayoutInflater layoutInflater;
    private Context context;

    public OrderDetailAdapter(Context context, List<OrderDetail> orderDetailList){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.orderDetailList=orderDetailList;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.iteam_orderdetail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        OrderDetail orderDetail=orderDetailList.get(position);
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.txtName.setText( orderDetail.getNameOrderDetail());
        holder.txtgia.setText("Tổng: "+decimalFormat.format(orderDetail.getPriceOrderDetail()) + " VND");
//        holder.txtgia.setText(String.valueOf(orderDetail.getPriceOrderDetail())+" VND");
        holder.txtsoluong.setText("Số lượng: "+String.valueOf(orderDetail.getQuantityOrderDetail()));
        Picasso.get().load(orderDetail.getImage_SPOrderDetail()).into(holder.imageViewSP);
//        Log.e( "OrderDetailAdapter: ",String.valueOf(orderDetail.getNameOrderDetail()) );
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewSP;
        TextView txtName,txtgia,txtsoluong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_tenSPIteamOrderDtail);
            imageViewSP=itemView.findViewById(R.id.imgImageIteamOrderDtail);
            txtgia=itemView.findViewById(R.id.tvPriceIteamOrderDtail);
            txtsoluong=itemView.findViewById(R.id.tvSoLuongIteamOrderDtail);

        }
    }

}
