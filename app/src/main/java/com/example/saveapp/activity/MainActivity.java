package com.example.saveapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.saveapp.fragment.HomeFragment;
import com.example.saveapp.fragment.SetFragment;
import com.example.saveapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        initFragment();
    }

    private void initFragment() {
        HomeFragment homeFragment = new HomeFragment();
        SetFragment setFragment = new SetFragment();
        fragments.add(homeFragment);
        fragments.add(setFragment);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        final ViewPager viewPager = findViewById(R.id.activity_main_viewPager);
        viewPager.setAdapter(fragmentPagerAdapter);
        TextView home = findViewById(R.id.activity_main_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        TextView set = findViewById(R.id.activity_main_set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
    }


}
