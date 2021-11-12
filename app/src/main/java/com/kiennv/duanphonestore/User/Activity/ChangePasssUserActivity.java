package com.kiennv.duanphonestore.User.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiennv.duanphonestore.LoginActivity;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.RegisterActivity;
import com.kiennv.duanphonestore.User.Fragment.UserFragment;

import java.util.HashMap;
import java.util.Map;

public class ChangePasssUserActivity extends AppCompatActivity {

    private static int id=0;
    private static TextInputEditText edtPasswordnewchange,edtPasswordnewnhaplai;
    private static Button btnConfirmchangle;
    private static String URL_updatePass = "http://10.0.2.2/Duan/user/updatePassword.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passs_user);


        edtPasswordnewchange = findViewById(R.id.edtPasswordnewchange);
        edtPasswordnewnhaplai = findViewById(R.id.edtPasswordnewnhaplai);

        //lay id nguoi dung ve de update
        SharedPreferences sp = getApplicationContext().getSharedPreferences("getuser", Context.MODE_PRIVATE);
        id = sp.getInt("id", 0);
//        Log.e( "onBindViewHolder: ",sp.getString("email",""));
//        Log.e( "onBindViewHolder: ", String.valueOf(sp.getInt("id",0)));
//        Log.e( "onBindViewHolder: ",sp.getString("name",""));

        //thay doi mat khau
        btnConfirmchangle = findViewById(R.id.btnConfirmchangle);
        btnConfirmchangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passMoi = edtPasswordnewchange.getText().toString();
                String passMoinhaplai = edtPasswordnewnhaplai.getText().toString();

//
                if (TextUtils.isEmpty(passMoi) && TextUtils.isEmpty(passMoinhaplai)) {
                    edtPasswordnewchange.setError("Chưa nhập mật khẩu");
                    edtPasswordnewnhaplai.setError("Chưa nhập mật khẩu");
                    return;
                }
                if (!passMoi.equals(passMoinhaplai)) {
                    edtPasswordnewchange.setError("Mật khẩu không giống nhau");
                }
                if (passMoi.length() < 6 || passMoinhaplai.length() < 6) {
                    edtPasswordnewchange.setError("Mật khẩu phải < 6 ký tự");
                    edtPasswordnewnhaplai.setError("Mật khẩu phải < 6 ký tự");
                    return;
                } else {
                    StringRequest request = new StringRequest(Request.Method.POST, URL_updatePass,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Intent intent = new Intent(ChangePasssUserActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(ChangePasssUserActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<>();
                             params.put("id", String.valueOf(id));
                             params.put("password", passMoi);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(request);
                }

            }
        });

    }

}