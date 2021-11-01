package com.kiennv.duanphonestore.User.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Adapter.GioHangAdater;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class CardFragment extends Fragment {
    private static TextView tv_diachicard, txtPriceTongtienCard, tvThongBaoGH;
    private static RecyclerView rcv_giohang;
    private static GioHangAdater giohangAdapter;
    private String URL_JSON = " http://192.168.1.7/Duan/user/user.php";
    private String address;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        txtPriceTongtienCard = v.findViewById(R.id.txtPriceTongtienCard);
        tvThongBaoGH = v.findViewById(R.id.tvThongBaoGH);

        rcv_giohang = v.findViewById(R.id.rcv_giohang);
        //recycleview
        rcv_giohang.setHasFixedSize(true);
        giohangAdapter = new GioHangAdater(getContext(), MainActivity.listcard);
        rcv_giohang.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcv_giohang.setAdapter(giohangAdapter);

        //get thong tin dia chi
        tv_diachicard = v.findViewById(R.id.tv_diachicard);
        diachiKH();
        Evenutil();
        checkData();

        return v;
    }
    // tinh tong tien gio hang
    public static void Evenutil() {
        long tongtien=0;
        for(int i=0;i<MainActivity.listcard.size();i++){
            tongtien+=MainActivity.listcard.get(i).getPriceSP();
        }
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        txtPriceTongtienCard.setText(decimalFormat.format(tongtien)+" VND");
    }
    public static void checkData() {
        if (MainActivity.listcard.size()<=0){
            //cap nhat lai neu xoa het tt
            giohangAdapter.notifyDataSetChanged();
            tvThongBaoGH.setVisibility(View.VISIBLE);
            //cho lv an
            rcv_giohang.setVisibility(View.INVISIBLE);
        }else {
            giohangAdapter.notifyDataSetChanged();
            tvThongBaoGH.setVisibility(View.INVISIBLE);
            //cho lv an
            rcv_giohang.setVisibility(View.VISIBLE);
        }
    }
    //lay thong tin dia chi khach hang
    private void diachiKH(){
        address = tv_diachicard.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_JSON, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    address = jsonObject.getString("address");

                    tv_diachicard.setText(address);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        queue.add(stringRequest);

    }
}