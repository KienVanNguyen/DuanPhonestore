package com.kiennv.duanphonestore;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

import java.io.InputStream;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtEmailRe, edtPassRe,edtPhoneRe, edtNameRe, edtConfirmPassRe,edtAddressRe;
    private Button btnRegister;
    private TextView tvLogin;
    private CircleImageView crice_register;
    private ImageView imgeyere,imgeyecofirmre;
    private DatabaseReference mdata;
    private Bitmap bitmap;
    private Uri filepath;


    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore;
    float v=0;
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
        imgeyecofirmre = findViewById(R.id.imgeyecofirmre);
        imgeyere = findViewById(R.id.imgeyere);
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
        imgeyecofirmre.setTranslationY(300);

        edtNameRe.setAlpha(v);
        edtEmailRe.setAlpha(v);
        edtPhoneRe.setAlpha(v);
        edtAddressRe.setAlpha(v);
        edtPassRe.setAlpha(v);
        edtConfirmPassRe.setAlpha(v);
        imgeyere.setAlpha(v);
        imgeyecofirmre.setAlpha(v);

        edtNameRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        edtEmailRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        edtPhoneRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        edtAddressRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        edtPassRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1200).start();
        imgeyere.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1400).start();
        edtConfirmPassRe.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1600).start();
        imgeyecofirmre.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1800).start();


        fStore = FirebaseFirestore.getInstance();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }
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
                final int[] count = {0};

                final String email = edtEmailRe.getText().toString().trim();
                final String password = edtPassRe.getText().toString().trim();
                final String cofirmpassword = edtConfirmPassRe.getText().toString().trim();
                final String fullName = edtNameRe.getText().toString();
                final String phone = edtPhoneRe.getText().toString();
                final String address = edtAddressRe.getText().toString();

                //kiem tra loi form
                if(crice_register.getDrawable()==null){
                    Toast.makeText(RegisterActivity.this,"Bạn chưa chọn ảnh",Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(email)) {
                    edtNameRe.setError("Chưa nhập Email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edtPassRe.setError("Chưa nhập mật khẩu");
                    return;
                }
                if (password.length() > 6) {
                    edtPassRe.setError("Mật khẩu phải > 6 ký tự");
                    edtConfirmPassRe.setError("Mật khẩu phải > 6 ký tự");
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
                if(phone.length()!=10){
                    edtPhoneRe.setError( "Số điện thoại chỉ có 10 số" );
                }
                if (!password.equals(cofirmpassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không giống nhau", Toast.LENGTH_SHORT).show();
                }

                FirebaseStorage storage=FirebaseStorage.getInstance();
                final StorageReference uploader=storage.getReference("Images"+new Random().nextInt(50));

                uploader.putFile(filepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri){


                                        FirebaseDatabase db=FirebaseDatabase.getInstance();
                                        DatabaseReference root=db.getReference("User");

                                        User obj=new User(edtNameRe.getText().toString(),edtEmailRe.getText().toString(),edtAddressRe.getText().toString(),edtPhoneRe.getText().toString(),edtPassRe.getText().toString(),uri.toString());
                                        root.child(edtNameRe.getText().toString()).setValue(obj);

                                        edtEmailRe.setText("");
                                        edtPassRe.setText("");
                                        edtNameRe.setText("");
                                        edtPhoneRe.setText("");
                                        edtAddressRe.setText("");

                                        Toast.makeText(getApplicationContext(),"Đăng ký thành công",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                });
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                            {

                            }
                        });
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

        if(view.getId()==R.id.imgeyecofirmre){

            if(edtConfirmPassRe.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                imgeyecofirmre.setImageResource(R.drawable.hideeye);

                //Show Password
                edtConfirmPassRe.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                imgeyecofirmre.setImageResource(R.drawable.showeye);

                //Hide Password
                edtConfirmPassRe.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}