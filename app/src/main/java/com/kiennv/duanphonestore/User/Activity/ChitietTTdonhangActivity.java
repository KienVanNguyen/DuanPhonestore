package com.kiennv.duanphonestore.User.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.kiennv.duanphonestore.R;

public class ChitietTTdonhangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_ttdonhang);

        //tro lai fragment
        Toolbar toolbar = findViewById(R.id.toolbarTTDH);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}