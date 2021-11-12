package com.kiennv.duanphonestore.User.Fragment;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.kiennv.duanphonestore.User.Activity.ChatadminActivity;
import com.kiennv.duanphonestore.User.Activity.EditUserActivity;
import com.kiennv.duanphonestore.User.MainActivity;
import com.kiennv.duanphonestore.User.Model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment {
    private static ImageView image_donhangvanchuyen,img_carduser,img_chatadminuser;
    private static Button logout;
    private static TextView txtNameuser,txtPhoneuser,txtAddressuser,txtChinhsuataikhoan,txtThaydoimatkhau,txtFeedback;
    private static CircleImageView crice_imageuser;
    private static TextInputEditText edtPasswordchange, edtEmailedituser222;
    private List<User> userList= new ArrayList<>();
    private static String URL_getPass = " http://10.0.2.2/Duan/user/getpassword.php";
    private static int id=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user, container, false);

        txtNameuser = v.findViewById(R.id.txtNameuser);
        txtPhoneuser = v.findViewById(R.id.txtPhoneuser);
        txtAddressuser = v.findViewById(R.id.txtAddressuser);
        crice_imageuser = v.findViewById(R.id.crice_imageuser);
        txtChinhsuataikhoan = v.findViewById(R.id.txtChinhsuataikhoan);
        txtThaydoimatkhau = v.findViewById(R.id.txtThaydoimatkhau);

        //lay id nguoi dung ve de update
        SharedPreferences sp = getContext().getSharedPreferences("getuser", Context.MODE_PRIVATE);
        id = sp.getInt("id", 0);

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
        //chuyen sang chat admin
        img_chatadminuser = v.findViewById(R.id.img_chatadminuser);
        img_chatadminuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatadminActivity.class);
                startActivity(intent);
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
                        .setTitle("Thiết lập mật khẩu")
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
                                if (!password.equals("")) {
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_getPass, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("success")) {
                                                Intent intent = new Intent(getContext(), ChangePasssUserActivity.class);
                                                startActivity(intent);
                                            } else if (response.equals("false")) {
                                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Kiểm tra lại mật khẩu")
                                                        .show();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> data = new HashMap<>();
                                            data.put("password", password);
                                            data.put("id", String.valueOf(id));
                                            return data;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                    requestQueue.add(stringRequest);
                                }
                                }
                        });
                edtPasswordchange = view.findViewById(R.id.edtPasswordchange);

                builder.create().show();
            }
        });


        try {
            //lay du lieu user
            userInfo();
        }catch (Exception e){
            e.printStackTrace();
        }



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
        builder.create().show();
    }
    //lay du lieu user
    private void userInfo(){
        SharedPreferences sp = getContext().getSharedPreferences("getuser", Context.MODE_PRIVATE);
//        Log.e( "info: ",String.valueOf(user.getEmail()) );
//        Log.e( "info: ",String.valueOf(user.getPhone()) );
//        Log.e( "info: ",String.valueOf(user.getAddress()) );
        String image = sp.getString("images", "");
        String name = sp.getString("name", "");
        String phone = sp.getString("phone", "");
        String address = sp.getString("address", "");
        txtNameuser.setText(name);
        txtPhoneuser.setText(phone);
        txtAddressuser.setText(address);
        Picasso.get().load(image).into(crice_imageuser);

    }
}

