package com.example.saveapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.saveapp.R;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

public class LoginBySMSActivity extends Activity implements View.OnClickListener {
    private EditText etphone;
    private EditText etcode;
    private Button login;
    private Button send;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loginbysms);
        etphone = findViewById(R.id.et_phone);
        etcode = findViewById(R.id.et_code);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        send = findViewById(R.id.send);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login: {
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

                BmobUser.loginBySMSCode(phone, code, new LogInListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            Intent intent = new Intent(LoginBySMSActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginBySMSActivity.this, "验证码错误，请重新输入！", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
                break;
            }
            case R.id.send: {
                String phone = etphone.getText().toString();
                BmobSMS.requestSMSCode(phone, "Bomb", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId, BmobException e) {
                        if (e == null) {

                        } else {

                        }
                    }
                });
                break;
            }
        }

    }
}
