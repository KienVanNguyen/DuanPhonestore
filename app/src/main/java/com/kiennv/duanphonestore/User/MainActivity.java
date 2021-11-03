package com.kiennv.duanphonestore.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.badge.BadgeDrawable;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.kiennv.duanphonestore.R;
import com.kiennv.duanphonestore.User.Fragment.CardFragment;
import com.kiennv.duanphonestore.User.Fragment.HomeFragment;
import com.kiennv.duanphonestore.User.Fragment.OrderFragment;
import com.kiennv.duanphonestore.User.Fragment.UserFragment;
import com.kiennv.duanphonestore.User.Model.Card;
import com.kiennv.duanphonestore.User.Model.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Card> listcard;
    private ChipNavigationBar chipNavigationBar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.nav_chipbar);
        // chuyển thẳng vào homeframent
        if(savedInstanceState == null){
            chipNavigationBar.setItemSelected(R.id.home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentManager, homeFragment)
                    .commit();
        }
        //chuyển đổi tab
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch (id){
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.cards:
                        fragment = new CardFragment();
                        break;
                    case R.id.order:
                        fragment = new OrderFragment();
                        break;
                    case R.id.user:
                        fragment = new UserFragment();
                        break;
                }
                if (fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentManager, fragment)
                            .commit();
                }
            }
        });
        if(listcard!=null){

        }else {
            listcard=new ArrayList<>();
        }
    }
}