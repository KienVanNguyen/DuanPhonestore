package com.kiennv.duanphonestore.User.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Fragment.HomeFragment;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.Card;
import com.kiennv.duanphonestore.User.Model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChitietspActivity extends AppCompatActivity {
    private static ImageView imgProduct,imgTruChitietsp,imgCongChitietsp;
    private static Button btnaddcardProduct;
    private static TextView txtNameProduct, txtGiaProduct,txtChitietProduct,txtSoluongChitietsp;
    private static String mota="";
    private static String ten="";
    private static int gia=0,id=0;
    private static String hinhanh="";
    private static int number=1;

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


        //tro lai fragment
//        Toolbar toolbar = findViewById(R.id.toolbarCTSP);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //lay thong tin san pham
        GetInfomation();

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
}