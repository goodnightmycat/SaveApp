package com.example.saveapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saveapp.R;
import com.example.saveapp.bean.User;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LockActivity extends Activity {
    private  EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LockActivity", "onCreate: ");
        setContentView(R.layout.activity_lock);
        password = findViewById(R.id.lock_activity_password);
        TextView cancel = findViewById(R.id.lock_activity_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 User user = new User();
//                user.setUsername("晓阳");
//                user.setPassword("qq690297610" );
//                user.setLockPassword("qq690297610");
//                user.setMobilePhoneNumber("15521205231");
//                user.signUp(new SaveListener<User>() {
//                    @Override
//                    public void done(User user, BmobException e) {
//                        if (e == null) {
//                            Toast.makeText(LockActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(LockActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                String passwordString = password.getText().toString();
                if (passwordString.equals(BmobUser.getCurrentUser(User.class).getLockPassword())) {
                    EventBus.getDefault().post("close");
                    finish();
                } else {
                    Toast.makeText(LockActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
