package com.example.saveapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.saveapp.R;
import com.example.saveapp.bean.User;
import com.example.saveapp.face.RealManFaceCheck.FaceVerify;
import com.example.saveapp.face.faceBase.FaceAdd;
import com.example.saveapp.util.Base64Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText etphone;
    private EditText etpassword;
    private EditText etname;
    private EditText etcode;
    private Button register;
    private Button send;
    private TimeCount time;

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, 1);
        etname = findViewById(R.id.et_name);
        etphone = findViewById(R.id.et_phone);
        etpassword = findViewById(R.id.et_password);
        etcode = findViewById(R.id.et_code);
        register = findViewById(R.id.bt_register);
        register.setOnClickListener(this);
        time = new TimeCount(60000, 1000);//第一个是要倒数多少秒，可以改
        send = findViewById(R.id.send);
        send.setOnClickListener(this);
    }

    private void getCode() {
        String phone;
        phone = etphone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobSMS.requestSMSCode(phone, "DataSDK", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, "短信发送成功！", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void signOrLogin() {

        String name = etname.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = etphone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = etcode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = etpassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        //设置手机号码（必填）
        user.setMobilePhoneNumber(phone);
        //设置用户名，如果没有传用户名，则默认为手机号码
        user.setUsername(name);
        user.setPassword(password);
        user.signOrLogin(code, new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
//                    Toast.makeText(RegisterActivity.this, "success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        //计时过程显示
        public void onTick(long millisUntilFinished) {
            send.setClickable(false);

            send.setText((millisUntilFinished / 1000) + "后可重新获取");
        }

        @Override
        //计时完成显示
        public void onFinish() {
            send.setText("获取验证码");
            send.setClickable(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                signOrLogin();
                break;
            case R.id.send:
                Toast.makeText(RegisterActivity.this, "已发送", Toast.LENGTH_SHORT).show();
                time.start();
                getCode();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

