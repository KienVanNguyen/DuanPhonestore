package com.kiennv.duanphonestore.User.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.XemAllSPMoinhatActivity;
import com.kiennv.duanphonestore.User.Adapter.FavouriteAdapter;
import com.kiennv.duanphonestore.User.Adapter.SanPhamAdapter;
import com.kiennv.duanphonestore.User.Model.Favourite;
import com.kiennv.duanphonestore.User.Model.OrderDetail;
import com.kiennv.duanphonestore.User.Model.SanPham;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    private static Shimmer shimmer;
    private static ShimmerTextView shimmerTextPhone;
    private static ViewFlipper viewFlipper;
    private static ImageView img_cardhome;
    private static RecyclerView rcv_spmoinhat,rcv_spbanchay;
    private static EditText edtTimKiem;
    private static SanPhamAdapter sanPhamAdapter;
    private static FavouriteAdapter favouriteAdapter;
    private static List<SanPham> sanPhamList;
    private List<Favourite> favouriteList;
    private static RequestQueue requestQueue;
    private int idUS=0;
    private static TextView txtSeeallSPmoinhat;
    private static String URL_ShowSP= "http://10.0.2.2/Duan/question/readDSMN.php";
    private static String URL_ShowSPFavourite= "http://10.0.2.2/Duan/question/readFavourite.php";

    //an thanh navigation bar khi su dung ban phim
    @Override
    public void onResume() {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //text hieu ung
        shimmerTextPhone= v.findViewById(R.id.shimmerTextPhone);
        shimmer = new Shimmer();
        shimmer.start(shimmerTextPhone);

        //anh xa
        rcv_spmoinhat= v.findViewById(R.id.rcv_spmoinhat);
        rcv_spbanchay= v.findViewById(R.id.rcv_spbanchay);
        edtTimKiem= v.findViewById(R.id.edtTimKiem);

        //xem tat ca san pham moi nhat
        txtSeeallSPmoinhat= v.findViewById(R.id.txtSeeallSPmoinhat);
        txtSeeallSPmoinhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), XemAllSPMoinhatActivity.class);
                startActivity(intent);
            }
        });

        //show sp
        sanPhamList = new ArrayList<>();
        favouriteList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        showSanphammoinhat();
        //show sp readFavourite
        showSanphamnoibat();

        //chuyen sang gio hang
        img_cardhome = v.findViewById(R.id.img_cardhome);
        img_cardhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment login = new CardFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentManager,login);
                ft.commit();
            }
        });

        //tim kiem sp
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchsp(s.toString());
            }
        });

        //slide view
        viewFlipper = v.findViewById(R.id.viewFlipper);
        int images[] = {R.drawable.slide,R.drawable.slide1,R.drawable.slide2,R.drawable.slide3,R.drawable.slide4};
        for (int i=0; i<images.length; i++){
            showImages(images[i]);
        }

        return v;
    }

    //tim kiem
    private void searchsp(String text) {
        try {
            ArrayList<SanPham> sanPhams = new ArrayList<>();
            for (SanPham item : sanPhamList){
                //toLowerCase cho phép chuyển đổi mọi ký tự viết Hoa của chuỗi thành ký tự viết thường
                if (item.getName().toLowerCase().contains(text.toLowerCase())){
                    sanPhams.add(item);
                }else{
                    Toast.makeText(getContext(),"Không có dữ liệu",Toast.LENGTH_SHORT).show();
                }
            }
            sanPhamAdapter.searchspList(sanPhams);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //show san pham yeu thich
    private void showSanphamnoibat() {
        SharedPreferences sp = getContext().getSharedPreferences("getuser", Context.MODE_PRIVATE);
        idUS=sp.getInt("id",0);
//        Log.e( "info: ",String.valueOf(idUS=sp.getInt("id",0)));

        StringRequest request = new StringRequest(Request.Method.POST,URL_ShowSPFavourite, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                int idUS = 0;
                String name = "";
                int price = 0;
                String images = "";
                String mota = "";
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //get du lieu cho bien
                        id = jsonObject.getInt("id");
                        idUS = jsonObject.getInt("idUS");
                        price = jsonObject.getInt("price");
                        name = jsonObject.getString("name");
                        images = jsonObject.getString("image_SP");
                        mota = jsonObject.getString("mota");

                        favouriteList.add(new Favourite(id, idUS, name, price, images,mota));
                        //  commentList.clear();
//                        Log.e( "OrderDetail: ",String.valueOf(name) );

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rcv_spbanchay.setLayoutManager(new GridLayoutManager(getContext(), 2));
                favouriteAdapter = new FavouriteAdapter(getContext(), favouriteList);
                rcv_spbanchay.setAdapter(favouriteAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            //day len server
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //HashMap 1.tao key gui len server 2.cai gia tri gui len cho no
                HashMap<String, String> param = new HashMap<String, String>();
                //dua vao idsanpham tren php
                param.put("idUS", String.valueOf(idUS));
//                Log.e( "onBindViewHolder: ",String.valueOf(idUS));
                //return param de gui request
                return param;
            }
        };
        //thuc thi requestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
    
    //show tat ca sp
    private void showSanphammoinhat() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ShowSP, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SanPham sanPham = new SanPham();
                                sanPham.setId(jsonObject.getInt("id"));
                                sanPham.setName(jsonObject.getString("name"));
                                sanPham.setPrice(jsonObject.getInt("price"));
                                sanPham.setImage_SP(jsonObject.getString("image_SP"));
                                sanPham.setMotasanpham(jsonObject.getString("mota"));
                                sanPhamList.add(sanPham);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        rcv_spmoinhat.setHasFixedSize(true);
                        sanPhamAdapter = new SanPhamAdapter(getContext(), sanPhamList);
                        rcv_spmoinhat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        rcv_spmoinhat.setAdapter(sanPhamAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    //slide view
    public void showImages(int img){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(img);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
    }

}