package com.kiennv.duanphonestore.User.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.ChitietspActivity;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.Comment;
import com.kiennv.duanphonestore.User.Model.Favourite;
import com.kiennv.duanphonestore.User.Model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    private List<SanPham> sanPhamList;
    private LayoutInflater layoutInflater;
    private Context context;
    private static String URL_getFavourite = "http://10.0.2.2/Duan/question/Favourite.php";
    private int idUS=0;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SanPham sanPham=sanPhamList.get(position);
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.txttenSP.setText(sanPhamList.get(position).getName());
        holder.txtGiasp.setText(decimalFormat.format(sanPham.getPrice()) + " VND");
//        holder.txtGiasp.setText(sanPhamList.get(position).getPrice() + " VND");
        Picasso.get().load(sanPhamList.get(position).getImage_SP()).into(holder.imgSanpham);


        holder.btnlikesp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp= v.getContext().getSharedPreferences("getuser", Context.MODE_PRIVATE);
                idUS=sp.getInt("id",0);
                StringRequest stringRequest1=new StringRequest(Request.Method.POST, URL_getFavourite, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            holder.btnlikesp.setImageResource(R.drawable.heart);
//                          Log.i("tagco.nvertstr", "["+response+"]");
                            Toast.makeText(v.getContext(), "Đã thêm sản phẩm nổi bật.",Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map=new HashMap<>();
                        map.put("id", String.valueOf(sanPhamList.get(position).getId()));
                        map.put("idUS",String.valueOf(idUS));
                        map.put("name",sanPhamList.get(position).getName());
                        map.put("price", String.valueOf(sanPhamList.get(position).getPrice()));
                        map.put("image_SP",sanPhamList.get(position).getImage_SP());
                        map.put("mota",sanPhamList.get(position).getMotasanpham());

//                        Log.e( "getParams: ", String.valueOf(map.put("name", sanPhamList.get(position).getName())));
//                        Log.e( "getParams: ", String.valueOf(map.put("price", String.valueOf(sanPhamList.get(position).getPrice()))));
//                        Log.e( "getParams: ", String.valueOf(map.put("id", String.valueOf(sanPhamList.get(position).getId()))));
//                        favouriteList.add(new Favourite( name, images, edtcomment, idUS,id));
                        return map;

                    }
                };
                RequestQueue requestQueue1 = Volley.newRequestQueue(v.getContext());
                requestQueue1.add(stringRequest1);
            }
        });

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
