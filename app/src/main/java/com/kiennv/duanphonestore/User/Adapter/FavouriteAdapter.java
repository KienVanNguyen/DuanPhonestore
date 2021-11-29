package com.kiennv.duanphonestore.User.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.ChiTietFavourite;
import com.kiennv.duanphonestore.User.Activity.ChitietspActivity;
import com.kiennv.duanphonestore.User.Fragment.CardFragment;
import com.kiennv.duanphonestore.User.Fragment.HomeFragment;
import com.kiennv.duanphonestore.User.Model.Favourite;
import com.kiennv.duanphonestore.User.Model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    public static List<Favourite> favouriteList;
    private SharedPreferences loginPreferences;
    private String URL_deleteFavourite = "http://10.0.2.2/Duan/question/deleteFavourite.php";

    public FavouriteAdapter(Context context,List<Favourite> favouriteList){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.favouriteList=favouriteList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_favourite,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Favourite sanPham=favouriteList.get(position);
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.txttenfavourite.setText(favouriteList.get(position).getName());
        holder.txtGiafavourite.setText(decimalFormat.format(sanPham.getPrice()) + " VND");
//        holder.txtGiasp.setText(sanPhamList.get(position).getPrice() + " VND");
        Picasso.get().load(favouriteList.get(position).getImage_SP()).into(holder.imgSanphamfavourite);

        int pos =0;
        holder.imgdeletefavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                //Cài đặt các thuộc tính
                builder.setTitle("Xóa sản phẩm");
                builder.setMessage("Bạn có muốn xóa " + favouriteList.get(pos).getName() + " khỏi yêu thích.");
                // Cài đặt button Cancel- Hiển thị Toast
                builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest request = new StringRequest(Request.Method.POST, URL_deleteFavourite,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            Delete(pos);
                                            Toast.makeText(v.getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<>();
                                params.put("id", String.valueOf(favouriteList.get(pos).getId()));
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                        requestQueue.add(request);

                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView txttenfavourite,txtGiafavourite;
        ImageView imgSanphamfavourite,imgdeletefavourite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttenfavourite = itemView.findViewById(R.id.txttenfavourite);
            txtGiafavourite = itemView.findViewById(R.id.txtGiafavourite);
            imgSanphamfavourite = itemView.findViewById(R.id.imgSanphamfavourite);
            imgdeletefavourite = itemView.findViewById(R.id.imgdeletefavourite);

            //truyen du liệu
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChiTietFavourite.class);
                    intent.putExtra("motasanphamCTF", favouriteList.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
    public void Delete(int item){
        favouriteList.remove(item);
        notifyItemRemoved(item);
    }
}