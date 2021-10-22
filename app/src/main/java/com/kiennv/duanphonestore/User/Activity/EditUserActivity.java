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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.karumi.dexter.listener.single.PermissionListener;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Fragment.CardFragment;
import com.kiennv.duanphonestore.User.Fragment.HomeFragment;
import com.kiennv.duanphonestore.User.Fragment.UserFragment;
import com.kiennv.duanphonestore.User.Model.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    private ImageView imgbackedit,floatingImageedit,imageedit;
    private Button btnConfirmedit;
    private TextInputEditText edtAddressedit,edtPhoneedit;
    private TextView edtEmailedit;
    private DatabaseReference reference;
    private static final int GALLER_ACTION_PICK_CODE = 100;


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
        reference = FirebaseDatabase.getInstance().getReference("User");
        //chinh sua hinh anh
        floatingImageedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTimePermission();
            }
        });
        //hien thi nguoi dung
        showAlluserdata();

        //chinh sua tai khoan
        btnConfirmedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String mail = edtEmailedit.getText().toString();
                    String phone = edtPhoneedit.getText().toString();
                    String address = edtAddressedit.getText().toString();

                if(phone.length()==10 && imageedit.getDrawable()!=null) {

                    FirebaseStorage storage= FirebaseStorage.getInstance();
                    final StorageReference storageReference = storage.getReferenceFromUrl("gs://duanphonestore.appspot.com");
                    Calendar calendar = Calendar.getInstance();
                    StorageReference mountainsRef=storageReference.child("Images"+calendar.getTimeInMillis()+".png");
                    imageedit.setDrawingCacheEnabled(true);
                    imageedit.buildDrawingCache();
                    final byte[] data =imageViewToByte(imageedit);

                    UploadTask uploadTask = mountainsRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText( EditUserActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            Task<Uri> dowloadURl=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            dowloadURl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child("nhom 1");
                                    Map<String, Object> updates = new HashMap<String, Object>();

                                    updates.put("email", mail);
                                    updates.put("address", address);
                                    updates.put("phone", phone);
                                    updates.put("images", imageUrl);
                                    ref.updateChildren(updates);
                                    Toast.makeText( EditUserActivity.this, "Đổi thông tin thành công", Toast.LENGTH_SHORT ).show();
                                }
                            });
                        }
                    });
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
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        User userBan = user.getValue(User.class);
                        Picasso.get().load(userBan.getImages()).into(imageedit);
                        edtEmailedit.setText(userBan.getEmail());
                        edtPhoneedit.setText(userBan.getPhone());
                        edtAddressedit.setText(userBan.getAddress());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public  void runTimePermission(){
        Dexter.withContext( EditUserActivity.this).withPermission( Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                galleryIntent();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
    //Pick Image From Gallery
    private void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,GALLER_ACTION_PICK_CODE);
    }
    //Convert Bitmap To Byte
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap= getResizedBitmap( bitmap,1024 );
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    public static Bitmap getResizedBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float bitmapRatio = (float) width / height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    //Convert Byte To BitMap
    public static Bitmap convertCompressedByteArrayToBitmap(byte[] src){
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == GALLER_ACTION_PICK_CODE){
                Uri imageUri = data.getData();
                imageedit.setImageURI(imageUri);
            }
        }
    }
}