package com.example.hanium_login_trail2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 각각 fragment를 상속받는 클래스 생성 . inflater를 통해 각 프래그먼트에 해당하는 리소스 id값을 통해 생성된 view 반환
    // fragment 클래스 객체 생성
    private fragment_main fragmentMain = new fragment_main();
    private fragment_menu2 fragmentMenu2 = new fragment_menu2();
    private fragment_menu3 fragmentMenu3 = new fragment_menu3();
    private fragment_menu4 fragmentMenu4 = new fragment_menu4();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // activity_main의 frameLayout에 fragment를 바꿔끼는 형식 - 초기화면 fragmentMain으로
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch(menuItem.getItemId())
                {
                    case R.id.navigation_main:
                        transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_menu2:
                        transaction.replace(R.id.frameLayout, fragmentMenu2).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_menu3:
                        transaction.replace(R.id.frameLayout, fragmentMenu3).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_menu4:
                        transaction.replace(R.id.frameLayout, fragmentMenu4).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });
    }

}
