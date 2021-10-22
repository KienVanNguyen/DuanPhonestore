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

import java.text.DecimalFormat;

public class CardFragment extends Fragment {
    private static TextView tv_diachicard, txtPriceTongtienCard, tvThongBaoGH;
    private static RecyclerView rcv_giohang;
    private static GioHangAdater giohangAdapter;
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
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        User userBan = user.getValue(User.class);
                        tv_diachicard.setText(userBan.getAddress());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}