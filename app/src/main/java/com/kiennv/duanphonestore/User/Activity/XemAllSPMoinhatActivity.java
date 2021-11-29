package com.kiennv.duanphonestore.User.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Adapter.SanPhamAdapter;
import com.kiennv.duanphonestore.User.Model.SanPham;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XemAllSPMoinhatActivity extends AppCompatActivity {
    private static RecyclerView rcv_showallSPMoinhat;
    private static SanPhamAdapter sanPhamAdapter;
    private static List<SanPham> sanPhamList;
    private static CardView cardviewImgXiaomi, cardviewImgApple, cardviewImgSamsung, cardviewImgOppo, cardviewImgHuawei, cardviewImgVivo;
    private static RequestQueue requestQueue;

    private static String URL_ShowSPMN = "http://10.0.2.2/Duan/question/readDSMN.php";
    private static String URL_ShowApple = "http://10.0.2.2/Duan/question/readApple.php";
    private static String URL_ShowXiaomi = "http://10.0.2.2/Duan/question/readXiaomi.php";
    private static String URL_ShowSamsung = "http://10.0.2.2/Duan/question/readSamsung.php";
    private static String URL_ShowOppo = "http://10.0.2.2/Duan/question/readOppo.php";
    private static String URL_ShowHuwei = "http://10.0.2.2/Duan/question/readHuwei.php";
    private static String URL_ShowVivo = "http://10.0.2.2/Duan/question/readVivo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_all_spmoinhat);

        rcv_showallSPMoinhat = findViewById(R.id.rcv_showallSPMoinhat);
        //show sp
        sanPhamList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(XemAllSPMoinhatActivity.this);
        showAllSanphammoinhat();

        cardviewImgXiaomi = findViewById(R.id.cardviewImgXiaomi);
        cardviewImgApple = findViewById(R.id.cardviewImgApple);
        cardviewImgSamsung = findViewById(R.id.cardviewImgSamsung);
        cardviewImgOppo = findViewById(R.id.cardviewImgOppo);
        cardviewImgHuawei = findViewById(R.id.cardviewImgHuawei);
        cardviewImgVivo = findViewById(R.id.cardviewImgVivo);

        //sự kiện click vào card => filter ra loại sp
        cardviewImgXiaomi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPhamList.clear();
                showXiaomiNew();
            }
        });
        cardviewImgApple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPhamList.clear();
                showAppleNew();
            }
        });
        cardviewImgSamsung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPhamList.clear();
                showSamsungNew();
            }
        });
        cardviewImgOppo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPhamList.clear();
                showOppoNew();
            }
        });
        cardviewImgHuawei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPhamList.clear();
                showHuweiNew();
            }
        });
        cardviewImgVivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPhamList.clear();
                showVivoNew();
            }
        });
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
                Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
    // Show sản phẩm có lọa là Xiaomi
    private void showXiaomiNew() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ShowXiaomi, null,
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

    // Show sản phẩm có lọa là Apple
    private void showAppleNew() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ShowApple, null,
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
    // Show sản phẩm có lọa là Samsung
    private void showSamsungNew() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ShowSamsung, null,
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
    // Show sản phẩm có lọa là Oppo
    private void showOppoNew() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ShowOppo, null,
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
    // Show sản phẩm có lọa là Huwei
    private void showHuweiNew() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ShowHuwei, null,
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
    // Show sản phẩm có lọa là Vivo
    private void showVivoNew() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ShowVivo, null,
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