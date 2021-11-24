package com.kiennv.duanphonestore.User.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.facebook.login.Login;
import com.google.android.material.textfield.TextInputEditText;
import com.kiennv.duanphonestore.LoginActivity;
import com.kiennv.duanphonestore.R;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassActivity extends AppCompatActivity {
    private ImageView imgBackForgot;
    private TextInputEditText edtEmailForgot, edtCodeForgot, edtPassForgot;
    private Button btnConfirmForgot;
    private TextView txtTimeForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        imgBackForgot = findViewById(R.id.imgBackForgot);
        edtEmailForgot = findViewById(R.id.edtEmailForgot);
        edtCodeForgot = findViewById(R.id.edtCodeForgot);
        edtPassForgot = findViewById(R.id.edtPassForgot);
        txtTimeForgot = findViewById(R.id.txtTimeForgot);
        btnConfirmForgot = findViewById(R.id.btnConfirmForgot);

        imgBackForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}