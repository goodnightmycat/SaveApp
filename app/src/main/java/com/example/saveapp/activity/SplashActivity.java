package com.example.saveapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.saveapp.R;
import com.example.saveapp.bean.User;

import cn.bmob.v3.BmobUser;

public class SplashActivity extends Activity {
    private ImageView mImgStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        mImgStart = findViewById(R.id.splash);
        initImage();
    }

    private void initImage() {

        mImgStart.setImageResource(R.drawable.ontime);

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.85f,
                1.0f,
                0.85f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImgStart.startAnimation(scaleAnimation);
    }

    private void startActivity() {
        Intent intent;
        if (BmobUser.isLogin()) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, LoginByPasswordActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}

