package com.kiennv.duanphonestore.User.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Adapter.CardSTTAdaper;
import com.kiennv.duanphonestore.User.Adapter.CommentAdapter;
import com.kiennv.duanphonestore.User.Fragment.HomeFragment;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.Card;
import com.kiennv.duanphonestore.User.Model.CardSTT;
import com.kiennv.duanphonestore.User.Model.Comment;
import com.kiennv.duanphonestore.User.Model.SanPham;
import com.kiennv.duanphonestore.User.Model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChitietspActivity extends AppCompatActivity {
    private ImageView imgProduct,imgTruChitietsp,imgCongChitietsp,imageSend;
    private Button btnaddcardProduct;
    private TextView txtNameProduct, txtGiaProduct,txtChitietProduct,txtSoluongChitietsp;
    private String mota="";
    private String ten="";
    private int gia=0,id=0;
    private String hinhanh="";
    private static User user;
    private int number=1;
    private TextInputEditText txtcomment;
    private String tenuser="";
    private String name="";
    private int idUS=0;
    private String images="";
    private RecyclerView rcvCommment;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private int couter=0;
    private int posion;
    private static String URL_Comment = "http://10.0.2.2/Duan/question/comment.php";
    private static String URL_getComment = "http://10.0.2.2/Duan/question/getComment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsp);
        imgProduct = findViewById(R.id.imgProduct);
        btnaddcardProduct = findViewById(R.id.btnaddcardProduct);
        txtNameProduct = findViewById(R.id.txtNameProduct);
        txtGiaProduct = findViewById(R.id.txtGiaProduct);
        txtChitietProduct = findViewById(R.id.txtChitietProduct);
        imgTruChitietsp = findViewById(R.id.imgTruChitietsp);
        imgCongChitietsp = findViewById(R.id.imgCongChitietsp);
        txtSoluongChitietsp = findViewById(R.id.txtSoluongChitietsp);
        txtcomment=findViewById(R.id.txtComment);
        rcvCommment=findViewById(R.id.RCV_Comment);
        commentList=new ArrayList<>();
        imageSend=findViewById(R.id.imgSend);
        showgetCommment();

        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmt = txtcomment.getText().toString();
                //lay du lieu user
                if(TextUtils.isEmpty(cmt)){
                    txtcomment.setError("Chưa phản hồi.");
                    return;
                }
                SharedPreferences sp=getApplicationContext().getSharedPreferences("getuser", Context.MODE_PRIVATE);
                String edtcomment= txtcomment.getText().toString();
                images=String.valueOf(sp.getString("images",""));
                name=String.valueOf(sp.getString("name",""));
                idUS=sp.getInt("id",0);

                StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_Comment, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(v.getContext(), "Đánh giá thành công",Toast.LENGTH_LONG).show();
                        // int id, String nameUS, String imageUS, String status, int idUS, int idSP

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map=new HashMap<>();
                        map.put("imageUS",images);
                        map.put("nameUS",name);
                        map.put("idUS",String.valueOf(idUS));
                        map.put("status",edtcomment);
                        map.put("idSP",String.valueOf(id));
//                        Log.e( "getusser: ", String.valueOf(idSP));
                        //int id, String nameUS, String imageUS, String status, int idSP

                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                requestQueue.add(stringRequest);
            }
        });

        //lay thong tin san pham
        GetInfomation();
        //getusser();
        try {
            //them san pham vao gio hang
            Eventaddgiohang();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //lay thong tin san pham
    private void GetInfomation() {
        //nhan du lieu 1 object
        SanPham sanPhamMN= (SanPham) getIntent().getSerializableExtra("motasanpham");
        ten=sanPhamMN.getName();
        gia=sanPhamMN.getPrice();
        hinhanh=sanPhamMN.getImage_SP();
        mota=sanPhamMN.getMotasanpham();
        txtChitietProduct.setText(mota);
        id=sanPhamMN.getId();
        txtNameProduct.setText(ten);
        //DecimalFormat định dạng cho số thập phân
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        txtGiaProduct.setText(decimalFormat.format(gia)+" VNĐ");
        Picasso.get().load(hinhanh).into(imgProduct);
        commentAdapter = new CommentAdapter(getApplicationContext(), commentList);
        EventImages();
        //cong them so luong
        imgCongChitietsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number=number+1;
                txtSoluongChitietsp.setText(String.valueOf(number));
                EventImages();
            }
        });
        //tru so luong
        imgTruChitietsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number=number-1;
                txtSoluongChitietsp.setText(String.valueOf(number));
                EventImages();
            }
        });
    }
    //an hien so luong san pham max min
    public void EventImages(){
        int soluong= Integer.parseInt(String.valueOf(number));
        if(soluong>1){
            imgTruChitietsp.setVisibility(View.VISIBLE);
            imgCongChitietsp.setVisibility(View.VISIBLE);
        }else if (soluong==1){
            imgTruChitietsp.setVisibility(View.INVISIBLE);
            imgCongChitietsp.setVisibility(View.VISIBLE);
        }else if (soluong>99){
            imgTruChitietsp.setVisibility(View.VISIBLE);
            imgCongChitietsp.setVisibility(View.INVISIBLE);
        }
    }
    //them san pham vao gio hang
    public void Eventaddgiohang(){
        btnaddcardProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.listcard.size()>0){
                    //khi mang co gia tri nguoi dung moi them moi thi cap nhat lại sl
                    int soluongmoi= Integer.parseInt(String.valueOf(number));
                    boolean exit=false;
                    //duyet qua tung vi tri mang
                    for (int i = 0; i< MainActivity.listcard.size(); i++){
                        //id mang them gio them moi bang nhau thi cap nhat lai du lieu
                        if(MainActivity.listcard.get(i).getIdGH()==id){
                            //cap nhap so luong san pham
                            MainActivity.listcard.get(i).setQuantitySPGH(MainActivity.listcard.get(i).getQuantitySPGH()+soluongmoi);//+soluongmoi
                            if(MainActivity.listcard.get(i).getQuantitySPGH()>=10){
                                MainActivity.listcard.get(i).setQuantitySPGH(10);
                            }
                            //cap nhat lai gia tri
                            MainActivity.listcard.get(i).setPriceSP(gia*MainActivity.listcard.get(i).getQuantitySPGH());
                            exit=true;
                        }
                    }
                    //khong thay trung id
                    if(exit==false){
                        //chua co du lieu thi add them du lieu
                        int soluong= Integer.parseInt(String.valueOf(number));
                        long giamoi=soluong*gia;
                        MainActivity.listcard.add(new Card(id,ten,giamoi,hinhanh,soluong));
                    }
                }else {
//                    //chua co du lieu thi add them du lieu
                    int soluong= Integer.parseInt(String.valueOf(number));
                    long giamoi=soluong*gia;
                    MainActivity.listcard.add(new Card(id,ten,giamoi,hinhanh,soluong));
                }
                new SweetAlertDialog(ChitietspActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Thêm sản phẩm vào giỏ hàng thành công")
                        .show();
            }
        });
    }
    private void showgetCommment() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_getComment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String nameUS = "";
                String imageUS = "";
                String status = "";
                int idSP = 0;
                int idUS=0;

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //get du lieu cho bien
                        id = jsonObject.getInt("id");
                        nameUS = jsonObject.getString("nameUS");
                        imageUS = jsonObject.getString("imageUS");
                        status = jsonObject.getString("status");
                        idUS = jsonObject.getInt("idUS");
                        idSP = jsonObject.getInt("idSP");
//                        Log.e("onBindViewHolder1111: ",imageUS);
                        // ArrayList<ModelChat> list=new ArrayList<>(lisymodelChats);
//                        list.add(modelChat);
//                        lisymodelChats.clear();
//                        lisymodelChats.addAll(list);
//                        chatdapter.notifyDataSetChanged();
//                        lv.setAdapter(chatdapter);
                        //get ve mang

                        commentList.add(new Comment(id, nameUS, imageUS, status, idUS,idSP));
                        //  commentList.clear();




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rcvCommment.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                rcvCommment.setAdapter(commentAdapter);
                commentAdapter.notifyDataSetChanged();

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
                param.put("idsanpham", String.valueOf(id));
//                Log.e( "onBindViewHolder: ",String.valueOf(id));
                //return param de gui request
                return param;
            }
        };
        //thuc thi requestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}