package com.kiennv.duanphonestore.User.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.Login;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kiennv.duanphonestore.R;

import com.kiennv.duanphonestore.User.Model.ServerRequest;
import com.kiennv.duanphonestore.User.Model.ServerResponse;
import com.kiennv.duanphonestore.User.Model.User;
import com.kiennv.duanphonestore.User.MyRetrofit.RequestInterface;
import com.kiennv.duanphonestore.User.variables.Constants;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPassActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_reset;
    private TextInputEditText et_email,et_code,et_password;
    private TextView txtTimeForgot;
    private boolean isResetInitiated = false;
    private String email;
    private CountDownTimer countDownTimer;

    public ForgotPassActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);


        btn_reset = findViewById(R.id.btnConfirmForgot);
        txtTimeForgot = (TextView)findViewById(R.id.txtTimeForgot);
        et_code = findViewById(R.id.edtCodeForgot);
        et_email = findViewById(R.id.edtEmailForgot);
        et_password = findViewById(R.id.edtPassForgot);
        et_password.setVisibility(View.GONE);
        et_code.setVisibility(View.GONE);
        txtTimeForgot.setVisibility(View.GONE);
        btn_reset.setOnClickListener(this);
//        progress = (ProgressBar)findViewById(R.id.progress);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirmForgot:
                if (!isResetInitiated) {
                    email = et_email.getText().toString();
                    if (!email.isEmpty()) {
//                        progress.setVisibility(View.VISIBLE);
                        initiateResetPasswordProcess(email);
                    } else {
                        Toast.makeText(getApplication(), "Fields are empty !", Toast.LENGTH_LONG).show();
                    }
                } else {
                    String code = et_code.getText().toString();
                    String password = et_password.getText().toString();
                    if (!code.isEmpty() && !password.isEmpty()) {
                        finishResetPasswordProcess(email, code, password);
                    } else {
                        Toast.makeText(getApplication(), "Fields are empty !", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void initiateResetPasswordProcess(String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface =
                retrofit.create(RequestInterface.class);
        User user = new User();
        user.setEmail(email);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.RESET_PASSWORD_INITIATE);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Toast.makeText(getApplication(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    et_email.setVisibility(View.GONE);
                    et_code.setVisibility(View.VISIBLE);
                    et_password.setVisibility(View.VISIBLE);
                    txtTimeForgot.setVisibility(View.VISIBLE);
                    btn_reset.setText("Xác nhận");
                    isResetInitiated = true;
                    Toast.makeText(getApplication(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                    startCountdownTimer();
                } else {
                    Toast.makeText(getApplication(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
//                progress.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
//                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed");
                Toast.makeText(getApplication(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCountdownTimer(){
        countDownTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtTimeForgot.setText("Time remaining : " + millisUntilFinished /
                        1000);
            }
            public void onFinish() {
                Toast.makeText(getApplication(), "Time Out ! Request again to reset password.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ForgotPassActivity.this, com.kiennv.duanphonestore.LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void finishResetPasswordProcess(String email, String code, String
            password) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RequestInterface requestInterface =
                retrofit.create(RequestInterface.class);
        User user = new User();
        user.setEmail(email);
        user.setCode(code);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.RESET_PASSWORD_FINISH);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call,
                                   Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Toast.makeText(getApplication(), resp.getMessage(), Toast.LENGTH_LONG).show();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    Toast.makeText(getApplication(), resp.getMessage(), Toast.LENGTH_LONG).show();
                    countDownTimer.cancel();
                    isResetInitiated = false;
                    Intent intent = new Intent(ForgotPassActivity.this, com.kiennv.duanphonestore.LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplication(), resp.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
//                progress.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG,"failed");
                Toast.makeText(getApplication(), t.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}