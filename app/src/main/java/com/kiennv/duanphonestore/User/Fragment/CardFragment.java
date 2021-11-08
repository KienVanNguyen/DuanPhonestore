package com.kiennv.duanphonestore.User.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.OrderSuccesActivity;
import com.kiennv.duanphonestore.User.Adapter.GioHangAdater;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.User;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CardFragment extends Fragment {
    private static TextView tv_diachicard, txtPriceTongtienCard, tvThongBaoGH;
    private static RecyclerView rcv_giohang;
    private static GioHangAdater giohangAdapter;
    private static View btnThanhToan;
    private static User user;
    private static int id=0;
    private static String future;
    private static String URL_InsertSP= "http://10.0.2.2/Duan/question/orderdetails.php";
    private static RadioButton radioButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        txtPriceTongtienCard = v.findViewById(R.id.txtPriceTongtienCard);
        tvThongBaoGH = v.findViewById(R.id.tvThongBaoGH);
        btnThanhToan=v.findViewById(R.id.btnThanhtoanCard);
        rcv_giohang = v.findViewById(R.id.rcv_giohang);
        radioButton=v.findViewById(R.id.checkboxTTGH);
        //recycleview
        rcv_giohang.setHasFixedSize(true);
        giohangAdapter = new GioHangAdater(getContext(), MainActivity.listcard);
        rcv_giohang.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcv_giohang.setAdapter(giohangAdapter);

        getguture();
        //chuyen thong tin login sang
        Intent intent = getActivity().getIntent();
        user = new User();
        user = (User) intent.getSerializableExtra("login");
        //get thong tin dia chi
        tv_diachicard = v.findViewById(R.id.tv_diachicard);

        try {
            diachiKH();
            Evenutil();
            checkData();
        }catch (Exception e){
            e.printStackTrace();
        }

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
        long finalTongtien = tongtien;


        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e( "onClick: ",String.valueOf(future));
               StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_InsertSP, new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {

                       try{

                           MainActivity.listcard.clear();
                           Intent intent = new Intent(v.getContext(), OrderSuccesActivity.class);
                           v.getContext().startActivity(intent);
//                       new SweetAlertDialog(v.getContext(), SweetAlertDialog.SUCCESS_TYPE)
//                               .setTitleText("Thanh toan giỏ hàng thành công")
//
//                               .show();
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
                       map.put("gia",String.valueOf(finalTongtien));
                       map.put("idnguoidung",String.valueOf(id));
                       map.put("date",getdate());
                       map.put("receivedDate",future);
                       map.put("status",String.valueOf(radioButton.getText()));
                       return map;
                   }
               };
                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                requestQueue.add(stringRequest);
            }
        });
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
        tv_diachicard.setText(user.getAddress());
        id=user.getId();
    }
    public static String getdate(){
         return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
    public static void getguture(){
        Calendar c=Calendar.getInstance();
        Date today=c.getTime();

        c.add(Calendar.DATE,5);
        SimpleDateFormat sdf1=new SimpleDateFormat("YYYY-MM-dd");

        future=sdf1.format(c.getTime());
       SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
       String date=sdf.format(new Date());
//        Log.e( "getguture: ",String.valueOf(future) );
    }
}