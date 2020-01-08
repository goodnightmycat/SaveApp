package com.example.saveapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saveapp.R;
import com.example.saveapp.bean.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginByPasswordActivity extends Activity implements View.OnClickListener {
    private EditText etUserPhone;
    private EditText etPassword;
    private boolean isPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginbypassword);
        etUserPhone = findViewById(R.id.et_number);
        etUserPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isPhone=isPhone(s.toString());
            }
        });
        etPassword = findViewById(R.id.et_password);
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Button login = findViewById(R.id.login);
        login.setOnClickListener(this);
        TextView forgetPassword = findViewById(R.id.forget_password);
        forgetPassword.setOnClickListener(this);
        TextView register = findViewById(R.id.register);
        register.setOnClickListener(this);

    }

    private void loginByPhone() {
        if (!isPhone) {
            return;
        }
        String phone = etUserPhone.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO 此处替换为你的手机号码和密码
        BmobUser.loginByAccount(phone, password, new LogInListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (user != null) {
                    if (e == null) {
                        Intent intent = new Intent(LoginByPasswordActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {

                    }

                } else
                    Toast.makeText(LoginByPasswordActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login: {
                loginByPhone();
                break;
            }
            case R.id.forget_password:
                Intent intent = new Intent(LoginByPasswordActivity.this, LoginBySMSActivity.class);
                startActivity(intent);
                break;
            case R.id.register:
                Intent intent3 = new Intent(LoginByPasswordActivity.this, RegisterActivity.class);
                startActivity(intent3);
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
