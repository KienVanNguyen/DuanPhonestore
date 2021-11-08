package com.kiennv.duanphonestore.User.Fragment;

import android.content.Intent;
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
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.XemAllSPMoinhatActivity;
import com.kiennv.duanphonestore.User.Adapter.SanPhamAdapter;
import com.kiennv.duanphonestore.User.Model.SanPham;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static Shimmer shimmer;
    private static ShimmerTextView shimmerTextView;
    private static ViewFlipper viewFlipper;
    private static ImageView img_cardhome;
    private static RecyclerView rcv_spmoinhat,rcv_spbanchay;
    private static EditText edtTimKiem;
    private static SanPhamAdapter sanPhamAdapter;
    private static List<SanPham> sanPhamList;
    private static RequestQueue requestQueue;
    private static TextView txtSeeallSPmoinhat;
    private static String URL_ShowSP= "http://10.0.2.2/Duan/question/readDSMN.php";

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
        shimmerTextView= v.findViewById(R.id.shimmer);
        shimmer = new Shimmer();
        shimmer.start(shimmerTextView);

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
        requestQueue = Volley.newRequestQueue(getContext());
        showSanphammoinhat();
//        showSanphambanchay();

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
                }
            }
            sanPhamAdapter.searchspList(sanPhams);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //show sp
    private void showSanphambanchay() {
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

                        rcv_spbanchay.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        sanPhamAdapter = new SanPhamAdapter(getContext(), sanPhamList);
                        rcv_spbanchay.setAdapter(sanPhamAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        requestQueue.add(request);
    }
    //show sp
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
                Log.d("tag", "onErrorResponse: " + error.getMessage());
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