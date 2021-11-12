package com.kiennv.duanphonestore.User.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private TextInputEditText edtEmailForgot, edtPhoneForgot;
    private Button btnConfirmForgot;
    private String URL_fogot_pass = "http://10.0.2.2/Duan/user/fogotpassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        imgBackForgot = findViewById(R.id.imgBackForgot);
        edtEmailForgot = findViewById(R.id.edtEmailForgot);
        edtPhoneForgot = findViewById(R.id.edtPhoneForgot);
        btnConfirmForgot = findViewById(R.id.btnConfirmForgot);

        imgBackForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnConfirmForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = edtEmailForgot.getText().toString();
                String phone = edtPhoneForgot.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_fogot_pass, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagco.nvertstr", "["+response+"]");
                        if (response.equals("success")){
                            Toast.makeText(ForgotPassActivity.this, "Kiểm tra Email để lấy lại mật khẩu.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ForgotPassActivity.this, "Gửi mật khẩu thất bại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPassActivity.this, "Kiểm tra", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        data.put("email", mail);
                        data.put("phone", phone);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });

    }
}