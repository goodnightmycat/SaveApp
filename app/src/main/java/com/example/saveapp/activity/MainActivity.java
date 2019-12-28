package com.example.saveapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saveapp.fragment.HomeFragment;
import com.example.saveapp.fragment.SetFragment;
import com.example.saveapp.service.LocationService;
import com.example.saveapp.service.LockService;
import com.example.saveapp.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int mIndex;
    private List<Fragment> fragments=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        initFragment();
//        getAppDetailSettingIntent();
//        requestPermission();
    }

    private void initFragment() {
        HomeFragment homeFragment = new HomeFragment();
        SetFragment setFragment = new SetFragment();
        fragments.add(homeFragment);
        fragments.add(setFragment);
        FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        final ViewPager viewPager=findViewById(R.id.activity_main_viewPager);
        viewPager.setAdapter(fragmentPagerAdapter);
        TextView homw=findViewById(R.id.activity_main_home);
        homw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        TextView set=findViewById(R.id.activity_main_set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void start() {
//        LocationService.start(MainActivity.this);
//        LockService.start(this);
//        TakePhotoActivity.start(MainActivity.this);
//        LocationActivity.start(MainActivity.this);
//        FindActivity.start(MainActivity.this);
    }

    public void requestPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA, Permission.ACCESS_FINE_LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        start();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Toast.makeText(MainActivity.this, "没有存储权限", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }


    private void getAppDetailSettingIntent() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        startActivity(intent);
    }

}
