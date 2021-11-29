package com.kiennv.duanphonestore.User.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.OrderSuccesActivity;
import com.kiennv.duanphonestore.User.Adapter.GioHangAdater;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.CardSTT;
import com.kiennv.duanphonestore.User.Model.Comment;
import com.kiennv.duanphonestore.User.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CardFragment extends Fragment {
    private static TextView tv_diachicard, txtPriceTongtienCard, tvThongBaoGH;
    private static RecyclerView rcv_giohang;
    private static GioHangAdater giohangAdapter;
    private static View btnThanhToan;
    private static User user;
    private static int id=0;
    private static String diachi="";
    private static String future;
    private static String URL_InsertSP= "http://10.0.2.2/Duan/question/orderStatus%20.php";
    private static String URL_InsertOrderDetail= "http://10.0.2.2/Duan/question/orderDetail.php";
    private static RadioButton radioButton;
    private static List<CardSTT> cardFragmentList;
    private static int idmahoadon=39;
    private static  CardSTT cardSTT;
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


        cardFragmentList=new ArrayList<>();

        //recycleview
        rcv_giohang.setHasFixedSize(true);
        giohangAdapter = new GioHangAdater(getContext(), MainActivity.listcard);
        rcv_giohang.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcv_giohang.setAdapter(giohangAdapter);

        getguture();
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
                if(MainActivity.listcard.size()>0){
//                Log.e( "onClick: ",String.valueOf(future));
                StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_InsertSP, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response ) {
                        try{
                            MainActivity.listcard.clear();
                            Intent intent = new Intent(v.getContext(), OrderSuccesActivity.class);
                            v.getContext().startActivity(intent);

//                            Log.e( "onResponse: ",String.valueOf(cardFragmentList.get(cardSTT.getId())));

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
                        String date=getdate();
                        String status=String.valueOf(radioButton.getText());
                        map.put("gia",String.valueOf(finalTongtien));
                        map.put("idnguoidung",String.valueOf(id));
                        map.put("date",date);
                        map.put("receivedDate",future);
                        map.put("diachiUS",diachi);
                        map.put("status",status);
                        Log.e( "getParams: ",date );
                        //    cardFragmentList.add(id,finalTongtien,date,status,future);
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                requestQueue.add(stringRequest);

                StringRequest stringRequest1 =new StringRequest(Request.Method.POST, URL_InsertOrderDetail, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(v.getContext(), response.toString(),Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Nullable
                    @org.jetbrains.annotations.Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        JSONArray jsonArray=new JSONArray();
                        for (int i=0;i<MainActivity.listcard.size();i++){
                            JSONObject jsonObject =new JSONObject();
                            try {
                                jsonObject.put("Price",MainActivity.listcard.get(i).getPriceSP());
                                jsonObject.put("name",MainActivity.listcard.get(i).getNameSPGH());
                                jsonObject.put("images",MainActivity.listcard.get(i).getImageSPGH());
                                jsonObject.put("quantity",MainActivity.listcard.get(i).getQuantitySPGH());
                                jsonObject.put("mahoadon",idmahoadon);
//                                Log.e( "getParams: ", String.valueOf(jsonObject.put("quantity", MainActivity.listcard.get(i).getQuantitySPGH())));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObject);
                        }
                        HashMap<String,String> hastmap1= new HashMap<>();
                        hastmap1.put("json",jsonArray.toString());
                        return hastmap1;
                    }
                };
                RequestQueue requestQue = Volley.newRequestQueue(v.getContext());
                requestQue.add(stringRequest1);
                }else {
                    new SweetAlertDialog(v.getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Vui lòng thêm sản phẩm để thanh toán")
                            .show();
                }
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
        SharedPreferences sp = getContext().getSharedPreferences("getuser", Context.MODE_PRIVATE);
        diachi = sp.getString("address", "");
        tv_diachicard.setText(diachi);
        id=sp.getInt("id",0);

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