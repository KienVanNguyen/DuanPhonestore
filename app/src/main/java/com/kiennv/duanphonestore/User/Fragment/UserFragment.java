package com.kiennv.duanphonestore.User.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.hsalf.smilerating.SmileRating;
import com.kiennv.duanphonestore.LoginActivity;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Activity.ChangePasssUserActivity;
import com.kiennv.duanphonestore.User.Activity.EditUserActivity;
import com.kiennv.duanphonestore.User.Model.User;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment {
    private ImageView image_donhangvanchuyen,img_carduser;
    private Button logout;
    private TextView txtNameuser,txtEmailuser,txtPhoneuser,txtAddressuser,txtChinhsuataikhoan,txtThaydoimatkhau,txtFeedback;
    private CircleImageView crice_imageuser;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private TextInputEditText edtPasswordchange;
    private StorageReference storageReference;
    private String profileid;
    private GoogleSignInClient googleSignInClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user, container, false);

        txtNameuser = v.findViewById(R.id.txtNameuser);
        txtEmailuser = v.findViewById(R.id.txtEmailuser);
        txtPhoneuser = v.findViewById(R.id.txtPhoneuser);
        txtAddressuser = v.findViewById(R.id.txtAddressuser);
        crice_imageuser = v.findViewById(R.id.crice_imageuser);
        txtChinhsuataikhoan = v.findViewById(R.id.txtChinhsuataikhoan);
        txtThaydoimatkhau = v.findViewById(R.id.txtThaydoimatkhau);

        //chuyen sang don hang da mua
        image_donhangvanchuyen = v.findViewById(R.id.image_donhangvanchuyen);
        image_donhangvanchuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment login = new OrderFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentManager,login);
                ft.commit();
            }
        });
        //chuyen sang gio hang
        img_carduser = v.findViewById(R.id.img_carduser);
        img_carduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment login = new CardFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentManager,login);
                ft.commit();
            }
        });

        fAuth = FirebaseAuth.getInstance();
//        profileid = fAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();

        //google
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(signInAccount != null){
            txtNameuser.setText(signInAccount.getDisplayName());
            txtEmailuser.setText(signInAccount.getEmail());
        }
        FirebaseUser firebaseUser = fAuth.getCurrentUser();
        if (firebaseUser != null){
            Glide.with(UserFragment.this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(crice_imageuser);
            txtNameuser.setText(firebaseUser.getDisplayName());
        }
        googleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN);

        //chuyen sang man hinh chinh sua tai khoan
        txtChinhsuataikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditUserActivity.class);
                startActivity(intent);
            }
        });
        //doi mat khau
        txtThaydoimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_change_pass, null);
                builder.setView(view)
                        .setTitle("Thay đổi mật khẩu")
                        .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String password = edtPasswordchange.getText().toString();
                                if(TextUtils.isEmpty(password)){
                                    edtPasswordchange.setError("Chưa nhập mật khẩu");
                                    return;
                                }
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // dataSnapshot is the "issue" node with all children with id 0

                                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                                // do something with the individual "issues"
                                                User usersBean = user.getValue(User.class);

                                                if ( usersBean.getPassword().equals(edtPasswordchange.getText().toString().trim())) {
                                                    Intent intent = new Intent(getContext(), ChangePasssUserActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Kiểm tra lại mật khẩu")
                                                            .show();
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
                edtPasswordchange = view.findViewById(R.id.edtPasswordchange);

                builder.create().show();
            }
        });

        //lay du lieu user
        userInfo();

        //danh gia
        txtFeedback = v.findViewById(R.id.txtFeedback);
        txtFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackApp();
            }
        });

        //dang xuat
        logout=v.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                //Cài đặt các thuộc tính
                builder.setTitle("Đăng Xuất");
                builder.setMessage("Bạn có muốn đăng xuất?");
                builder.setIcon(R.drawable.logout);
                // Cài đặt button Cancel- Hiển thị Toast
                builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        getActivity().onBackPressed();
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        return v;
    }
    //danh gia
    private void feedbackApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_rating_dialog, null);
        builder.setView(view);

//                .setNegativeButton("", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                })
//                .setPositiveButton("", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                    }
//                });
        builder.create().show();
    }
    //lay du lieu user
    private void userInfo(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        User userBan = user.getValue(User.class);
                        Picasso.get().load(userBan.getImages()).into(crice_imageuser);
                        txtNameuser.setText(userBan.getFullName());
                        txtEmailuser.setText(userBan.getEmail());
                        txtPhoneuser.setText(userBan.getPhone());
                        txtAddressuser.setText(userBan.getAddress());

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}