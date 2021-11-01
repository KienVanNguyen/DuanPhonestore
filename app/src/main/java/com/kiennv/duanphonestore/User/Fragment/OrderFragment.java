package com.kiennv.duanphonestore.User.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.ChatadminActivity;
import com.kiennv.duanphonestore.User.Activity.XemAllSPMoinhatActivity;


public class OrderFragment extends Fragment {
    private Button btnLienheOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        //chuyen sang man hinh nhan tin admin
        btnLienheOrder= v.findViewById(R.id.btnLienheOrder);
        btnLienheOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatadminActivity.class);
                startActivity(intent);
            }
        });
        return  v;
    }
}