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

    private static ImageView floatingImageedit,imageedit;
    private static Button btnConfirmedit;
    private static TextInputEditText edtAddressedit,edtPhoneedit,edtNameedit;
    private static TextView edtEmailedit;
    private static User user;
    private static String URL_updateUser = " http://10.0.2.2/Duan/user/updateUser.php";
    private static String encodeImageString;
    private static Bitmap bitmap;
    private static Uri filepath;

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
        edtNameedit = findViewById(R.id.edtNameedit);
        //tro lai fragment
//        Toolbar toolbar = findViewById(R.id.toolbarEdituser);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //chuyen thong tin login sang
        Intent intent = getIntent();
        user = new User();
        user = (User) intent.getSerializableExtra("edituser");
        Log.e( "onBindViewHolder: ",String.valueOf(user.getId()));
        Log.e( "onBindViewHolder: ",String.valueOf(user.getEmail()));
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
                    String name = edtNameedit.getText().toString();

                if(!name.equals("") &&phone.length()==10 && imageedit.getDrawable()!=null) {
                    StringRequest request = new StringRequest(Request.Method.POST, URL_updateUser,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String message = "";

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getInt("success") == 1) {
                                            User account = new User();
                                            account.setFullName(jsonObject.getString("name"));
                                            account.setPhone(jsonObject.getString("phone"));
                                            account.setAddress(jsonObject.getString("address"));
                                            account.setImages(jsonObject.getString("images"));
                                            message = jsonObject.getString("message");
                                            Toast.makeText(EditUserActivity.this, message, Toast.LENGTH_LONG).show();

                                        } else {
                                            message = jsonObject.getString("message");
                                            Toast.makeText(EditUserActivity.this, message, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException error) {
                                        error.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", name);
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
                        Toast.makeText( EditUserActivity.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        });
    }
    //lay du lieu nguoi dung ve
    private void showAlluserdata() {
        Picasso.get().load(user.getImages()).into(imageedit);
        edtEmailedit.setText(user.getEmail());
        edtNameedit.setText(user.getFullName());
        edtPhoneedit.setText(user.getPhone());
        edtAddressedit.setText(user.getAddress());

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