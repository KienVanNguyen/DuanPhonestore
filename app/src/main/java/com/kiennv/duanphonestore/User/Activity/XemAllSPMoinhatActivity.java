package com.kiennv.duanphonestore.User.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Adapter.SanPhamAdapter;
import com.kiennv.duanphonestore.User.Model.SanPham;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class XemAllSPMoinhatActivity extends AppCompatActivity {
    private RecyclerView rcv_showallSPMoinhat;
    private SanPhamAdapter sanPhamAdapter;
    private List<SanPham> sanPhamList;
    private RequestQueue requestQueue;
    private static String URL_ShowSPMN= "http://192.168.1.7/Duan/question/readDSMN.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_all_spmoinhat);

        //tro lai fragment
        Toolbar toolbar = findViewById(R.id.toolbarSeeall);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rcv_showallSPMoinhat = findViewById(R.id.rcv_showallSPMoinhat);
        //show sp
        sanPhamList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(XemAllSPMoinhatActivity.this);
        showAllSanphammoinhat();
    }
    //show all sp
    private void showAllSanphammoinhat() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ShowSPMN, null,
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

                        rcv_showallSPMoinhat.setLayoutManager(new GridLayoutManager(XemAllSPMoinhatActivity.this, 2));
                        sanPhamAdapter = new SanPhamAdapter(XemAllSPMoinhatActivity.this, sanPhamList);
                        rcv_showallSPMoinhat.setAdapter(sanPhamAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        requestQueue.add(request);
    }
}