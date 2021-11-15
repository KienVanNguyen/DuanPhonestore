package com.kiennv.duanphonestore.User.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.ChatadminActivity;
import com.kiennv.duanphonestore.User.Adapter.CardSTTAdaper;
import com.kiennv.duanphonestore.User.Model.CardSTT;
import com.kiennv.duanphonestore.User.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderFragment extends Fragment {
    private static Button btnLienheOrder;
    private static RecyclerView lvdonhang;
    private static User user;
    private static CardSTTAdaper cardSTTAdaper;
    private static List<CardSTT> cardSTTList;
    private static int id=0;
    private static String URL_ShowSP= "http://10.0.2.2/Duan/question/getOrderDetail.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        //chuyen sang man hinh nhan tin admin
        btnLienheOrder= v.findViewById(R.id.btnLienheOrder);
        lvdonhang= v.findViewById(R.id.lv_orderdetail);
        cardSTTList=new ArrayList<>();

        btnLienheOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatadminActivity.class);
                startActivity(intent);
            }
        });
        //chuyen thong tin login sang
//        Intent intent = getActivity().getIntent();
//        user = new User();
//        user = (User) intent.getSerializableExtra("login");
        try {
            //lay du lieu user
            diachiKH();
        }catch (Exception e){
            e.printStackTrace();
        }
        showSanphammoinhat();
        return  v;

    }
    private void showSanphammoinhat() {
            StringRequest request = new StringRequest(Request.Method.POST, URL_ShowSP, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    int idTTGH=0;
                    int giaTTGH=0;
                    int IdUser=0;
                    String Date="";
                    String status="";
                    String receivedDate="";
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for(int i =0;i<jsonArray.length();i++) {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                //get du lieu cho bien
                                idTTGH = jsonObject.getInt("id");
                                giaTTGH = jsonObject.getInt("Price");
                                IdUser = jsonObject.getInt("IdUser");
                                Date = jsonObject.getString("Date");
                                status = jsonObject.getString("status");
                                receivedDate = jsonObject.getString("receivedDate");
                                //get ve mang
                                cardSTTList.add(new CardSTT(idTTGH, IdUser, giaTTGH, Date,status,receivedDate));
//                                Log.e( "onBindViewHolder1111: ",String.valueOf(status));
//                            sanphamMNAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    cardSTTAdaper =new CardSTTAdaper(getContext(),cardSTTList);
                    lvdonhang.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    lvdonhang.setAdapter(cardSTTAdaper);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                //day len server
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //HashMap 1.tao key gui len server 2.cai gia tri gui len cho no
                    HashMap<String,String> param=new HashMap<String, String>();
                    //dua vao idsanpham tren php
                    param.put("id",String.valueOf(id));
                    //Log.e( "onBindViewHolder: ",String.valueOf(id));
                    //return param de gui request
                    return param;
                }
            };
        //thuc thi requestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
    private void diachiKH(){
        SharedPreferences sp = getContext().getSharedPreferences("getuser", Context.MODE_PRIVATE);
        id = sp.getInt("id", 0);
    }
    }