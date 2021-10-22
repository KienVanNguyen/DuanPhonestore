package com.kiennv.duanphonestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.User;

import java.util.ArrayList;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmailLo, edtPassLo;
    private ImageView imgeyelo;
    private Button btnLogin;
    private TextView tvRegister, forgotPass,tvchuacotk,phonestore;
    private CheckBox checkbox_login;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private FirebaseAuth fAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    private ImageView face,gg,twiter;
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
        imgeyelo = findViewById(R.id.imgeyelo);
        tvchuacotk = findViewById(R.id.tvchuacotk);
        phonestore = findViewById(R.id.phonestore);

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
        imgeyelo.setTranslationY(300);
        checkbox_login.setTranslationY(300);
        forgotPass.setTranslationY(300);
        btnLogin.setTranslationY(300);
        tvchuacotk.setTranslationY(300);
        tvRegister.setTranslationY(300);

        face.setAlpha(v);
        gg.setAlpha(v);
        twiter.setAlpha(v);
        edtEmailLo.setAlpha(v);
        edtPassLo.setAlpha(v);
        imgeyelo.setAlpha(v);
        checkbox_login.setAlpha(v);
        forgotPass.setAlpha(v);
        btnLogin.setAlpha(v);
        tvchuacotk.setAlpha(v);
        tvRegister.setAlpha(v);

        face.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        gg.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        twiter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();

        edtEmailLo.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        edtPassLo.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        imgeyelo.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        checkbox_login.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        forgotPass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1200).start();
        btnLogin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1400).start();
        tvchuacotk.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1600).start();
        tvRegister.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1800).start();


        fAuth = FirebaseAuth.getInstance();
        loginPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            edtEmailLo.setText(loginPreferences.getString("email", ""));
            edtPassLo.setText(loginPreferences.getString("password", ""));
            checkbox_login.setChecked(true);
        }
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
                if (password.length() > 6) {
                    edtPassLo.setError("Mật khẩu phải > 6 ký tự");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    edtPassLo.setError("Chưa nhập mật khẩu");
                    return;

                    // đăng nhập

                } DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id 0

                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                // do something with the individual "issues"
                                User usersBean = user.getValue(User.class);

                                if (usersBean.getEmail().equals(edtEmailLo.getText().toString().trim()) && usersBean.getPassword().equals(edtPassLo.getText().toString().trim())) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    if (checkbox_login.isChecked()) {
                                        loginPrefsEditor.putBoolean( "saveLogin", true );
                                        loginPrefsEditor.putString( "email", email );
                                        loginPrefsEditor.putString( "password", password );
                                        loginPrefsEditor.commit();
                                    } else {
                                        loginPrefsEditor.clear();
                                        loginPrefsEditor.commit();
                                    }
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Kiểm tra email và mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

            }
        });

        //quên mật khẩu
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle( "Đặt lại mật khẩu?");
                passwordResetDialog.setIcon(R.drawable.forgot);
                passwordResetDialog.setMessage( "Nhập Email để lấy lại mật khẩu.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this,  "Đã gửi liên kết đặt lại mật khẩu vào email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Lỗi! Liên kết chưa được gửi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });
                passwordResetDialog.create().show();
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
                new SweetAlertDialog(v.getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Login Facebook hiện chưa phát triển")
                        .show();
            }
        });
        //login twiter
        twiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(v.getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Login Twiter hiện chưa phát triển")
                        .show();
            }
        });

        //login google
        createRequest();
        gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }
    //phan dang nhap google
    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Login với Google thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login với Google thất bại", Toast.LENGTH_SHORT).show();
                        }
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