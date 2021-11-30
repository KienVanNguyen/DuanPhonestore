package com.kiennv.duanphonestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kiennv.duanphonestore.User.Activity.EditUserActivity;
import com.kiennv.duanphonestore.User.Activity.ForgotPassActivity;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmailLo, edtPassLo;
    private Button btnLogin;
    private TextView tvRegister, forgotPass,tvchuacotk,phonestore;
    private CheckBox checkbox_login;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private String URL = "http://10.0.2.2/Duan/user/login.php";
    private ImageView face,gg,twiter,imgeyelo;
    float v=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmailLo = findViewById(R.id.edtEmailLo);
        edtPassLo = findViewById(R.id.edtPassLo);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        forgotPass = findViewById(R.id.forgotPass);
        checkbox_login = findViewById(R.id.checkbox_login);
        tvchuacotk = findViewById(R.id.tvchuacotk);
        phonestore = findViewById(R.id.phonestore);
        imgeyelo = findViewById(R.id.imgeyelo);

        face = findViewById(R.id.face);
        gg = findViewById(R.id.gg);
        twiter = findViewById(R.id.twiter);

        phonestore.setTranslationX(300);
        phonestore.setAlpha(v);
        phonestore.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(200).start();

        face.setTranslationY(300);
        gg.setTranslationY(300);
        twiter.setTranslationY(300);
        edtEmailLo.setTranslationY(300);
        edtPassLo.setTranslationY(300);
        checkbox_login.setTranslationY(300);
        forgotPass.setTranslationY(300);
        btnLogin.setTranslationY(300);
        tvchuacotk.setTranslationY(300);
        tvRegister.setTranslationY(300);
        imgeyelo.setTranslationY(300);

        face.setAlpha(v);
        gg.setAlpha(v);
        twiter.setAlpha(v);
        edtEmailLo.setAlpha(v);
        edtPassLo.setAlpha(v);
        checkbox_login.setAlpha(v);
        forgotPass.setAlpha(v);
        btnLogin.setAlpha(v);
        tvchuacotk.setAlpha(v);
        tvRegister.setAlpha(v);
        imgeyelo.setAlpha(v);

        face.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        gg.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        twiter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();

        edtEmailLo.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        edtPassLo.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        imgeyelo.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        checkbox_login.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        forgotPass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1200).start();
        btnLogin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1400).start();
        tvchuacotk.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1600).start();
        tvRegister.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1800).start();


        loginPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            edtEmailLo.setText(loginPreferences.getString("email", ""));
            checkbox_login.setChecked(true);
        }
        //lay du lieu user vo
        loginPreferences = getSharedPreferences("getuser", Context.MODE_PRIVATE);

        //login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailLo.getText().toString().trim();
                String password = edtPassLo.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    edtEmailLo.setError("Chưa nhập Email");
                    return;
                }
//                if (password.length() >= 6) {
//                    edtPassLo.setError("Mật khẩu phải > = 6 ký tự");
//                    return;
//                }
                if(TextUtils.isEmpty(password)){
                    edtPassLo.setError("Chưa nhập mật khẩu");
                    return;
                    // đăng nhập

                }
                if (!email.equals("") && !password.equals("")){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (checkbox_login.isChecked()) {
                                loginPrefsEditor.putBoolean( "saveLogin", true );
                                loginPrefsEditor.putString( "email", email );
                                loginPrefsEditor.commit();
                            } else {
                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                            }
                            String message = "";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("success") == 1) {
                                    User account = new User();

                                    account.setId(jsonObject.getInt("id"));
                                    account.setFullName(jsonObject.getString("name"));
                                    account.setEmail(jsonObject.getString("email"));
                                    account.setPhone(jsonObject.getString("phone"));
                                    account.setAddress(jsonObject.getString("address"));
                                    account.setImages(jsonObject.getString("images"));
                                    message = jsonObject.getString("message");

                                    SharedPreferences.Editor editor = loginPreferences.edit();
                                    editor.putInt("id", account.getId());
                                    editor.putString("name", account.getFullName());
                                    editor.putString("email", account.getEmail());
                                    editor.putString("password", account.getPassword());
                                    editor.putString("phone", account.getPhone());
                                    editor.putString("address", account.getAddress());
                                    editor.putString("images", account.getImages());
                                    editor.commit();
                                    new SweetAlertDialog(v.getContext(), SweetAlertDialog.PROGRESS_TYPE)
                                            .setTitleText("Đang đăng nhập...")
                                            .show();
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("login", account);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    message = jsonObject.getString("message");
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("email", email);
                            data.put("password", password);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }
        });

        //quên mật khẩu
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);

            }
        });

        //chuyen dang ky
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //login facebook
        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Đăng nhập Facebook hiện chưa phát triển")
                        .show();
            }
        });
        //login twiter
        twiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Đăng nhập Twiter hiện chưa phát triển")
                        .show();
            }
        });

        //login google
        gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Đăng nhập Google hiện chưa phát triển")
                        .show();
            }
        });
    }

    //hien thi mat khau
    public void ShowHidePass(View view){

        if(view.getId()==R.id.imgeyelo){

            if(edtPassLo.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                imgeyelo.setImageResource(R.drawable.hideeye);

                //Show Password
                edtPassLo.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                imgeyelo.setImageResource(R.drawable.showeye);

                //Hide Password
                edtPassLo.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}