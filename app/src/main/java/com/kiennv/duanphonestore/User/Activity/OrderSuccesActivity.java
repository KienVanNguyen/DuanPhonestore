package com.kiennv.duanphonestore.User.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Fragment.OrderFragment;

public class OrderSuccesActivity extends AppCompatActivity {
    private static Button btnTieptucmuahang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_succes);

        btnTieptucmuahang = findViewById(R.id.btnTieptucmuahang);
        btnTieptucmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccesActivity.this, XemAllSPMoinhatActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}