package com.kiennv.duanphonestore.User.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kiennv.duanphonestore.LoginActivity;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.RegisterActivity;
import com.kiennv.duanphonestore.User.Fragment.CardFragment;
import com.kiennv.duanphonestore.User.Fragment.HomeFragment;
import com.kiennv.duanphonestore.User.Fragment.UserFragment;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    private ImageView imgbackedit,floatingImageedit,imageedit;
    private Button btnConfirmedit;
    private TextInputEditText edtAddressedit,edtPhoneedit;
    private TextView edtEmailedit;
    private static final int GALLER_ACTION_PICK_CODE = 100;
    private String phone,address,email, images;
    private String URL_JSON = " http://192.168.1.7/Duan/user/user.php";
    private String URL_updateUser = " http://192.168.1.7/Duan/user/updateUser.php";
    private String encodeImageString;
    private Bitmap bitmap;
    private Uri filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        floatingImageedit = findViewById(R.id.floatingImageedit);
        imageedit = findViewById(R.id.imageedit);
        btnConfirmedit = findViewById(R.id.btnConfirmedit);
        edtPhoneedit = findViewById(R.id.edtPhoneedit);
        edtAddressedit = findViewById(R.id.edtAddressedit);
        edtEmailedit = findViewById(R.id.edtEmailedit);

        //tro lai fragment
        Toolbar toolbar = findViewById(R.id.toolbarEdituser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //hien thi nguoi dung
        showAlluserdata();

        //chinh sua hinh anh
        floatingImageedit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(EditUserActivity.this)
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

        //chinh sua tai khoan
        btnConfirmedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String phone = edtPhoneedit.getText().toString();
                    String address = edtAddressedit.getText().toString();

                if(phone.length()==10 && imageedit.getDrawable()!=null) {
                    StringRequest request = new StringRequest(Request.Method.POST, URL_updateUser,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Intent intent = new Intent(EditUserActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(EditUserActivity.this, "Thiết lập tài khoản thành công", Toast.LENGTH_SHORT).show();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<>();
                            params.put("phone", phone);
                            params.put("address", address);
                            params.put("upload", encodeImageString);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(request);

                }else if(phone.length()!=10 || imageedit.getDrawable()==null){
                    if(phone.length()!=10){
                        edtPhoneedit.setError( "Số điện thoại chỉ có 10 số, hãy kiểm tra lại." );
                    }
                    if(imageedit.getDrawable()==null){
                        Toast.makeText( EditUserActivity.this, "Chua upload hinh anh", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        });
    }
    //lay du lieu nguoi dung ve
    private void showAlluserdata() {
         email = edtEmailedit.getText().toString();
         phone = edtPhoneedit.getText().toString();
         address = edtAddressedit.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_JSON, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    email = jsonObject.getString("email");
                    phone = jsonObject.getString("phone");
                    address = jsonObject.getString("address");
                    images = jsonObject.getString("images");
                    Picasso.get().load(images).into(imageedit);

                    edtEmailedit.setText(email);
                    edtPhoneedit.setText(phone);
                    edtAddressedit.setText(address);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        queue.add(stringRequest);

    }
    //hinh anh
    private void encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
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
                imageedit.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}