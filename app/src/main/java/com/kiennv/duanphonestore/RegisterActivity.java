package com.kiennv.duanphonestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtEmailRe, edtPassRe,edtPhoneRe, edtNameRe, edtConfirmPassRe,edtAddressRe;
    private Button btnRegister;
    private TextView tvLogin;
    private CircleImageView crice_register;
    private ImageView imgeyere,imgeyereconfirm;
    private Bitmap bitmap;
    private Uri filepath;
    private float v=0;
    private String encodeImageString;
    private String URL = "http://10.0.2.2/Duan/user/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtNameRe = findViewById(R.id.edtNameRe);
        edtEmailRe = findViewById(R.id.edtEmailRe);
        edtPassRe = findViewById(R.id.edtPassRe);
        edtPhoneRe = findViewById(R.id.edtPhoneRe);
        edtAddressRe = findViewById(R.id.edtAddressRe);
        edtConfirmPassRe = findViewById(R.id.edtConfirmPassRe);

        imgeyere = findViewById(R.id.imgeyere);
        imgeyereconfirm = findViewById(R.id.imgeyereconfirm);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        crice_register = findViewById(R.id.crice_register);

        edtNameRe.setTranslationY(300);
        edtEmailRe.setTranslationY(300);
        edtPhoneRe.setTranslationY(300);
        edtPassRe.setTranslationY(300);
        edtAddressRe.setTranslationY(300);
        edtConfirmPassRe.setTranslationY(300);
        imgeyere.setTranslationY(300);
        imgeyereconfirm.setTranslationY(300);


        edtNameRe.setAlpha(v);
        edtEmailRe.setAlpha(v);
        edtPhoneRe.setAlpha(v);
        edtAddressRe.setAlpha(v);
        edtPassRe.setAlpha(v);
        edtConfirmPassRe.setAlpha(v);
        imgeyere.setAlpha(v);
        imgeyereconfirm.setAlpha(v);


        edtNameRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        edtEmailRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        edtPhoneRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        edtAddressRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        edtPassRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1200).start();
        imgeyere.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1200).start();
        edtConfirmPassRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1400).start();
        imgeyereconfirm.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1400).start();

        //hinh anh
        crice_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(RegisterActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        //dang ky
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = edtEmailRe.getText().toString().trim();
                final String password = edtPassRe.getText().toString().trim();
                final String cofirmpassword = edtConfirmPassRe.getText().toString().trim();
                final String fullName = edtNameRe.getText().toString();
                final String phone = edtPhoneRe.getText().toString();
                final String address = edtAddressRe.getText().toString();

                //kiem tra loi form
                if (crice_register.getDrawable() == null) {
                    Toast.makeText(RegisterActivity.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password)) {
                    edtPassRe.setError("Chưa nhập mật khẩu");
                    return;
                }
                if (password.length() < 6) {
                    edtPassRe.setError("Mật khẩu phải > = 6 ký tự");
                    return;
                }
                if (TextUtils.isEmpty(fullName)) {
                    edtNameRe.setError("Chưa nhập tên");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    edtPhoneRe.setError("Chưa nhập số điện thoại");
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    edtAddressRe.setError("Chưa nhập địa chỉ");
                    return;
                }
                if (phone.length() != 10) {
                    edtPhoneRe.setError("Số điện thoại chỉ có 10 số");
                }
                if (!password.equals(cofirmpassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không giống nhau", Toast.LENGTH_SHORT).show();
                }
                else if (!fullName.equals("") && isValidEmail(email) && !phone.equals("") && !address.equals("") && !password.equals("") ){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String message = "";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("success") == 1) {
                                    User account = new User();
                                    account.setFullName(jsonObject.getString("name"));
                                    account.setEmail(jsonObject.getString("email"));
                                    account.setPhone(jsonObject.getString("phone"));
                                    account.setAddress(jsonObject.getString("address"));
                                    account.setImages(jsonObject.getString("images"));
                                    message = jsonObject.getString("message");
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                                    //Start LoginActivity
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    message = jsonObject.getString("message");
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException error) {
                                error.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Vui lòng chọn đủ để đăng ký", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams()  {
                            Map<String, String> data = new HashMap<>();
                            data.put("name", fullName);
                            data.put("email", email);
                            data.put("phone", phone);
                            data.put("address", address);
                            data.put("password", password);
                            data.put("upload", encodeImageString);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }
        });
        //chuyen sang login
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //bat loi dinh dang email
    private boolean isValidEmail(String target) {
        if (target.matches("[a-zA-Z0-9._-]+@[a-z]+.[com]+"))
            return true;
        else {
            edtEmailRe.setError("Email sai định dạng! (abc@gmail.com)");
        }
        return false;
    }

    private void encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        encodeImageString=android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1  && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            try{
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                crice_register.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //hien thi mat khau
    public void ShowHidePassRe(View view){

        if(view.getId()==R.id.imgeyere){

            if(edtPassRe.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                imgeyere.setImageResource(R.drawable.hideeye);

                //Show Password
                edtPassRe.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                imgeyere.setImageResource(R.drawable.showeye);

                //Hide Password
                edtPassRe.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
    //hien thi mat khau
    public void ShowHidePassReConfirm(View view){

        if(view.getId()==R.id.imgeyereconfirm){

            if(edtConfirmPassRe.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                imgeyereconfirm.setImageResource(R.drawable.hideeye);

                //Show Password
                edtConfirmPassRe.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                imgeyereconfirm.setImageResource(R.drawable.showeye);

                //Hide Password
                edtConfirmPassRe.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}