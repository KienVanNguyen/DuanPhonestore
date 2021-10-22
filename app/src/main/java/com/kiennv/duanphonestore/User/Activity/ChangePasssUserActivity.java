package com.kiennv.duanphonestore.User.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiennv.duanphonestore.LoginActivity;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.RegisterActivity;
import com.kiennv.duanphonestore.User.Fragment.UserFragment;

import java.util.HashMap;
import java.util.Map;

public class ChangePasssUserActivity extends AppCompatActivity {

    private ImageView imgchanglepass;
    private TextInputEditText edtPasswordnewchange,edtPasswordnewnhaplai;
    private Button btnConfirmchangle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passs_user);


        edtPasswordnewchange = findViewById(R.id.edtPasswordnewchange);
        edtPasswordnewnhaplai = findViewById(R.id.edtPasswordnewnhaplai);

        //tro lai fragment
        Toolbar toolbar = findViewById(R.id.toolbarDoimatkhau);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnConfirmchangle = findViewById(R.id.btnConfirmchangle);
        btnConfirmchangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String passMoi= edtPasswordnewchange.getText().toString();
                    String passMoinhaplai= edtPasswordnewnhaplai.getText().toString();

//
                if (TextUtils.isEmpty(passMoi) && TextUtils.isEmpty(passMoinhaplai)) {
                    edtPasswordnewchange.setError("Chưa nhập mật khẩu");
                    edtPasswordnewnhaplai.setError("Chưa nhập mật khẩu");
                    return;
                }
                if (!passMoi.equals(passMoinhaplai)) {
                   edtPasswordnewchange.setError("Mật khẩu không giống nhau");
                }else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").child("nhom 1");
                    Map<String, Object> updates = new HashMap<String, Object>();

                    updates.put("password", passMoi);
                    updates.put("password", passMoinhaplai);
                    ref.updateChildren(updates);
                    Intent intent = new Intent(ChangePasssUserActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText( ChangePasssUserActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT ).show();
                }


            }
        });

    }

}