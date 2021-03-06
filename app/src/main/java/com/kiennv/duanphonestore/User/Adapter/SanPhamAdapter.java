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
import com.kiennv.duanphonestore.User.Fragment.HomeFragment;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    private List<SanPham> sanPhamList;
    private LayoutInflater layoutInflater;
    private Context context;
    private static String URL_getFavourite = "http://10.0.2.2/Duan/question/Favourite.php";
    private int idUS=0;

   // private int id=0;
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
                boolean exit=false;
                if(HomeFragment.favouriteList.size()>0){
                for (int i = 0; i < HomeFragment.favouriteList.size(); i++) {
//                    Log.e("onClick: ", String.valueOf(HomeFragment.favouriteList.get(i).getIdSP()));
                    if (sanPhamList.get(position).getId() == HomeFragment.favouriteList.get(i).getIdSP()) {
                        new SweetAlertDialog(v.getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("S???n ph???m ???? ???????c th??m v??o m???c n???i b???t")
                                .show();
                        exit = true;
                    }
                    }

                if(exit==false) {
                SharedPreferences sp = v.getContext().getSharedPreferences("getuser", Context.MODE_PRIVATE);
                idUS = sp.getInt("id", 0);
                int idSP = (sanPhamList.get(position).getId());
                String name = sanPhamList.get(position).getName();
                int price = sanPhamList.get(position).getPrice();
                String images = sanPhamList.get(position).getImage_SP();
                String mota = sanPhamList.get(position).getMotasanpham();
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_getFavourite, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        holder.btnlikesp.setImageResource(R.drawable.heart);
//                        Log.i("tagco.nvertstr", "["+response+"]");
                        HomeFragment.rcv_spbanchay.setAdapter(HomeFragment.favouriteAdapter);
//                        Toast.makeText(v.getContext(), response.toString(),Toast.LENGTH_LONG).show();
                        Toast.makeText(v.getContext(), "???? th??m s???n ph???m n???i b???t.", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(v.getContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("idSP", String.valueOf(idSP));
                        map.put("idUS", String.valueOf(idUS));
                        map.put("name", name);
                        map.put("price", String.valueOf(price));
                        map.put("image_SP", images);
                        map.put("mota", mota);
                        //  Log.e( "getParams: ",String.valueOf(idSP) );
                        FavouriteAdapter.favouriteList.add(new Favourite(idSP, idUS, name, price, images, mota));
                        return map;
                    }
                };
                RequestQueue requestQueue1 = Volley.newRequestQueue(v.getContext());
                requestQueue1.add(stringRequest1);

                    }
                }else {
                    SharedPreferences sp = v.getContext().getSharedPreferences("getuser", Context.MODE_PRIVATE);
                    idUS = sp.getInt("id", 0);
                    int idSP = (sanPhamList.get(position).getId());
                    String name = sanPhamList.get(position).getName();
                    int price = sanPhamList.get(position).getPrice();
                    String images = sanPhamList.get(position).getImage_SP();
                    String mota = sanPhamList.get(position).getMotasanpham();
                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL_getFavourite, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            holder.btnlikesp.setImageResource(R.drawable.heart);
//                        Log.i("tagco.nvertstr", "["+response+"]");
                            HomeFragment.rcv_spbanchay.setAdapter(HomeFragment.favouriteAdapter);
//                        Toast.makeText(v.getContext(), response.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(v.getContext(), "???? th??m s???n ph???m n???i b???t.", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(v.getContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("idSP", String.valueOf(idSP));
                            map.put("idUS", String.valueOf(idUS));
                            map.put("name", name);
                            map.put("price", String.valueOf(price));
                            map.put("image_SP", images);
                            map.put("mota", mota);
                            //  Log.e( "getParams: ",String.valueOf(idSP) );
                            FavouriteAdapter.favouriteList.add(new Favourite(idSP, idUS, name, price, images, mota));
                            return map;
                        }
                    };
                    RequestQueue requestQueue1 = Volley.newRequestQueue(v.getContext());
                    requestQueue1.add(stringRequest1);
                }


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

            //truyen du li???u
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
