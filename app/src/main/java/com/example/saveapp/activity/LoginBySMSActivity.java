package com.example.saveapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.saveapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

public class LoginBySMSActivity extends Activity implements View.OnClickListener {
    private EditText etPhone;
    private EditText etCode;
    private Button login;
    private Button send;
    private boolean isPhone = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loginbysms);
        etPhone = findViewById(R.id.et_phone);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isPhone = isPhone(s.toString());
            }
        });
        etCode = findViewById(R.id.et_code);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        send = findViewById(R.id.send);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login: {
                if (isPhone) {
                    String phone = etPhone.getText().toString();
                    String code = etCode.getText().toString();
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
            }
            case R.id.send: {
                if (isPhone) {
                    String phone = etPhone.getText().toString();
                    BmobSMS.requestSMSCode(phone, "SaveApp", new QueryListener<Integer>() {
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

    /**
     * 判断是否为手机号码
     *
     * @param phone
     * @return true：是 false：否
     */
    public static boolean isPhone(String phone) {
        String regex = "\\d{11}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }
}
