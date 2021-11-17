package com.kiennv.duanphonestore.User.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.ChitietspActivity;
import com.kiennv.duanphonestore.User.Model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    private List<SanPham> sanPhamList;
    private LayoutInflater layoutInflater;
    private Context context;

   public SanPhamAdapter(Context context,List<SanPham> sanPhamList1){
       this.context = context;
       this.layoutInflater = LayoutInflater.from(context);
       this.sanPhamList=sanPhamList1;
   }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_sanpham,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPham=sanPhamList.get(position);
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.txttenSP.setText(sanPhamList.get(position).getName());
        holder.txtGiasp.setText(decimalFormat.format(sanPham.getPrice()) + " VND");
//        holder.txtGiasp.setText(sanPhamList.get(position).getPrice() + " VND");
        Picasso.get().load(sanPhamList.get(position).getImage_SP()).into(holder.imgSanpham);


//        holder.btnlikesp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (true){
//                    holder.btnlikesp.setImageResource(R.drawable.heart);
//                }else{
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }
    public void searchspList(ArrayList<SanPham> sanPhamArrayList){
       sanPhamList = sanPhamArrayList;
       notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txttenSP,txtGiasp;
        ImageView imgSanpham,btnlikesp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttenSP = itemView.findViewById(R.id.txttenSP);
            txtGiasp = itemView.findViewById(R.id.txtGiasp);
            imgSanpham = itemView.findViewById(R.id.imgSanpham);
            btnlikesp = itemView.findViewById(R.id.btnlikesp);

            //truyen du liệu
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChitietspActivity.class);
                    intent.putExtra("motasanpham", sanPhamList.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
