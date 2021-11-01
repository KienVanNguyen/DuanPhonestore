package com.kiennv.duanphonestore.User.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.kiennv.duanphonestore.User.Fragment.CardFragment;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.Card;
import com.kiennv.duanphonestore.User.Model.SanPham;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GioHangAdater extends RecyclerView.Adapter<GioHangAdater.ViewHolder> {
    private ArrayList<Card> cardList;
    private LayoutInflater layoutInflater;
    private Context context;

    public GioHangAdater(Context context,ArrayList<Card> cardList){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.cardList=cardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_card,parent,false);
        return new GioHangAdater.ViewHolder(view);
    }

    public Object getItem(int position) {
        return cardList.get(position);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        Card card= (Card) getItem(position);
        Log.e( "onBindViewHolder: ",card.getImageSPGH());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.txtNameSPGH.setText(card.getNameSPGH());
        holder.txtPriceSPGH.setText(decimalFormat.format(card.getPriceSP()) + " VND");
        holder.txtqualttySPGH.setText(String.valueOf(card.getQuantitySPGH()));
        Picasso.get().load(card.getImageSPGH()).into(holder.imgSPGH);

        int sl= Integer.parseInt(holder.txtqualttySPGH.getText().toString());
        //an hien so luon max min
        if(sl>99){
            holder.imageTang.setVisibility(View.INVISIBLE);
            holder.imgGiam.setVisibility(View.VISIBLE);
        }else if(sl==1){
            holder.imageTang.setVisibility(View.VISIBLE);
            holder.imgGiam.setVisibility(View.INVISIBLE);
        }else if(sl>=1){
            holder.imageTang.setVisibility(View.VISIBLE);
            holder.imgGiam.setVisibility(View.VISIBLE);
        }

        ViewHolder finalViewHolder=holder;
        //nut cong them so luong
        holder.imageTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slmn=Integer.parseInt(finalViewHolder.txtqualttySPGH.getText().toString())+1;
                int slhientai= MainActivity.listcard.get(position).getQuantitySPGH();
                long giahientai=MainActivity.listcard.get(position).getPriceSP();

                long gtmt=(giahientai*slmn)/slhientai;
                MainActivity.listcard.get(position).setPriceSP(gtmt);
                //co 3sp(slht) 100000(GHT) neu 4(slm)-->gtmn lay 10000(GHT) * 4(slm) %3slht
                MainActivity.listcard.get(position).setQuantitySPGH(slmn);
                DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
                finalViewHolder.txtPriceSPGH.setText(decimalFormat.format(gtmt)+" VNĐ");
                finalViewHolder.txtqualttySPGH.setText(String.valueOf(slmn));
                CardFragment.Evenutil();
                if(slmn==99){
                    finalViewHolder.imageTang.setVisibility(View.INVISIBLE);
                    finalViewHolder.imgGiam.setVisibility(View.VISIBLE);
                    finalViewHolder.txtqualttySPGH.setText(String.valueOf(slmn));
                }else if (slmn==1){
                    finalViewHolder.imageTang.setVisibility(View.VISIBLE);
                    finalViewHolder.imgGiam.setVisibility(View.INVISIBLE);
                    finalViewHolder.txtqualttySPGH.setText(String.valueOf(slmn));
                }else if(slmn>=1){
                    finalViewHolder.imageTang.setVisibility(View.VISIBLE);
                    finalViewHolder.imgGiam.setVisibility(View.VISIBLE);
                    finalViewHolder.txtqualttySPGH.setText(String.valueOf(slmn));
                }
            }
        });
        //xoa gio hang
        holder.imgxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder buider=new AlertDialog.Builder(context);
                buider.setTitle("Xóa sản phẩm?");
                buider.setIcon(R.drawable.removecart);
                buider.setMessage("Bạn có muốn xóa " + cardList.get(position).getNameSPGH() + " khỏi giỏ hàng");
                buider.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cardList.size()>0) {
                            cardList.remove(position);
                            notifyItemRemoved(which);
                            CardFragment.checkData();
                            CardFragment.Evenutil();
                        }
                    }
                });
                buider.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                       CardFragment.Evenutil();
                    }
                });
                buider.show();

            }
        });
        //nut giam so luong
        holder.imgGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slmn=Integer.parseInt(finalViewHolder.txtqualttySPGH.getText().toString())-1;
                int slhientai= MainActivity.listcard.get(position).getQuantitySPGH();
                long giahientai=MainActivity.listcard.get(position).getPriceSP();

                long gtmt=(giahientai*slmn)/slhientai;
                MainActivity.listcard.get(position).setPriceSP(gtmt);
                //co 3sp(slht) 100000(GHT) neu 4(slm)-->gtmn lay 10000(GHT) * 4(slm) %3slht
                MainActivity.listcard.get(position).setQuantitySPGH(slmn);
                DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
                finalViewHolder.txtPriceSPGH.setText(decimalFormat.format(gtmt)+" VND");
                CardFragment.Evenutil();
                if(slmn==99){
                    finalViewHolder.imageTang.setVisibility(View.INVISIBLE);
                    finalViewHolder.imgGiam.setVisibility(View.VISIBLE);
                    finalViewHolder.txtqualttySPGH.setText(String.valueOf(slmn));
                }else if (slmn==1){
                    finalViewHolder.imageTang.setVisibility(View.VISIBLE);
                    finalViewHolder.imgGiam.setVisibility(View.INVISIBLE);
                    finalViewHolder.txtqualttySPGH.setText(String.valueOf(slmn));
                }else if(slmn>=1){
                    finalViewHolder.imageTang.setVisibility(View.VISIBLE);
                    finalViewHolder.imgGiam.setVisibility(View.VISIBLE);
                    finalViewHolder.txtqualttySPGH.setText(String.valueOf(slmn));
                }
            }
        });

    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return cardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtNameSPGH,txtPriceSPGH,txtqualttySPGH;
        ImageView imgSPGH,imageTang,imgGiam,imgxoa;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameSPGH = itemView.findViewById(R.id.tv_tenSPItemcard);
            txtPriceSPGH = itemView.findViewById(R.id.tvPriceItemcard);
            txtqualttySPGH=itemView.findViewById(R.id.tvSoLuongItemcard);
            imgSPGH=itemView.findViewById(R.id.imgImageItemcard);
            imgGiam=itemView.findViewById(R.id.imgTruItemcard);
            imageTang=itemView.findViewById(R.id.imgCongItemcard);
            imgxoa=itemView.findViewById(R.id.imgDelItemcard);
        }
    }
}
