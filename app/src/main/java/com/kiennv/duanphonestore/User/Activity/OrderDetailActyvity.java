package com.kiennv.duanphonestore.User.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Adapter.CommentAdapter;
import com.kiennv.duanphonestore.User.Adapter.OrderDetailAdapter;
import com.kiennv.duanphonestore.User.Model.CardSTT;
import com.kiennv.duanphonestore.User.Model.Comment;
import com.kiennv.duanphonestore.User.Model.OrderDetail;
import com.kiennv.duanphonestore.User.Model.SanPham;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActyvity extends AppCompatActivity {
    private RecyclerView rcvOrderDetai;
    private OrderDetailAdapter orderDetailAdapter;
    private List<OrderDetail> orderDetailList;
    private static String URL_getOrderDetail = "http://10.0.2.2/Duan/question/getOrderDetail.php";
    private int mahoadon =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        rcvOrderDetai = findViewById(R.id.rcv_chiteietsanpham);
        orderDetailList=new ArrayList<>();
        orderDetailAdapter = new OrderDetailAdapter(getApplicationContext(), orderDetailList);

        showgetOrderDetail();

        CardSTT cardSTT= (CardSTT) getIntent().getSerializableExtra("chitietsanpham");
        mahoadon=cardSTT.getId();
      //  Log.e( "onCreate: ",String.valueOf(mahoadon));



    }

    private void showgetOrderDetail() {

        StringRequest request = new StringRequest(Request.Method.POST,URL_getOrderDetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                int Price = 0;
                String name = "";
                String images = "";
                int quantity = 0;
                int mahoadon=0;

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //get du lieu cho bien
                        id = jsonObject.getInt("id");
                        Price = jsonObject.getInt("Price");
                        name = jsonObject.getString("name");
                        images = jsonObject.getString("images");
                        quantity = jsonObject.getInt("quantity");
                        mahoadon = jsonObject.getInt("mahoadon");

                        orderDetailList.add(new OrderDetail(id, Price, name, images, quantity,mahoadon));
                        //  commentList.clear();
//                        Log.e( "OrderDetail: ",String.valueOf(name) );

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rcvOrderDetai.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                rcvOrderDetai.setAdapter(orderDetailAdapter);
                //orderDetailAdapter.notifyDataSetChanged();

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
                param.put("idMaHoaDon", String.valueOf(mahoadon));
//                Log.e( "onBindViewHolder: ",String.valueOf(mahoadon));
                //return param de gui request
                return param;
            }
        };
        //thuc thi requestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}