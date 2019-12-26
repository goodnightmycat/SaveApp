package com.example.saveapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.saveapp.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TakePhotoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TakePhotoActivity.start(MainActivity.this);
    }



}
