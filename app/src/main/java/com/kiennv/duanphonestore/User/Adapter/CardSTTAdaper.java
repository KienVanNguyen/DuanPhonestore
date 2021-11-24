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
import com.kiennv.duanphonestore.User.Activity.OrderDetailActyvity;
import com.kiennv.duanphonestore.User.Model.CardSTT;
import com.kiennv.duanphonestore.User.Model.SanPham;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class CardSTTAdaper extends RecyclerView.Adapter<CardSTTAdaper.ViewHolder> {
    private List<CardSTT> cardlist;
    private LayoutInflater layoutInflater;
    private Context context;
    public CardSTTAdaper(Context context,List<CardSTT> cardlist){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.cardlist=cardlist;
    }
    @NonNull
    @NotNull
    @Override
    public CardSTTAdaper.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_tinhtrang_donhang,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CardSTTAdaper.ViewHolder holder, int position) {
        CardSTT cardSTT=cardlist.get(position);
        //Log.e( "onBindViewHolder: ",String.valueOf(cardSTT.getId()));
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.txtID.setText("Mã đơn hàng: #"+String.valueOf(cardSTT.getId()));
        holder.txtDate.setText("Ngày mua: "+cardSTT.getDate());
        holder.txtStatus.setText(cardSTT.getStatus());
        holder.txtPrice.setText("Tổng tiền: "+decimalFormat.format(cardSTT.getPrice()) + " VND");
        holder.txtreceivedDate.setText("Ngày giao: "+cardSTT.getReceivedDate());
    }

    @Override
    public int getItemCount() {
        return cardlist.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtID,txtPrice,txtDate,txtStatus,txtreceivedDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.idMaTTGH);
            txtPrice = itemView.findViewById(R.id.PriceTTGH);
            txtDate=itemView.findViewById(R.id.DateTTGH);
            txtStatus=itemView.findViewById(R.id.statusTTGH);
            txtreceivedDate=itemView.findViewById(R.id.receivedDateTTGH);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderDetailActyvity.class);
                    intent.putExtra("chitietsanpham", cardlist.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
