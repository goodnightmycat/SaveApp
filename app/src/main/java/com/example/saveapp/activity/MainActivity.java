package com.example.saveapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.saveapp.LockService;
import com.example.saveapp.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TakePhotoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }
    private void start(){
        startService(new Intent(MainActivity.this, LockService.class));
//        TakePhotoActivity.start(MainActivity.this);
//        LocationActivity.start(MainActivity.this);
    }
    public void requestPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA,Permission.ACCESS_FINE_LOCATION)
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


}
