/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.saveapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.example.saveapp.R;
import com.example.saveapp.face.faceBase.FaceAdd;
import com.example.saveapp.util.Base64Util;
import com.example.saveapp.face.RealManFaceCheck.FaceVerify;
import com.google.android.cameraview.CameraView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class TakePhotoActivity extends Activity {
    private CameraView mCameraView;
    private static final String TAG = "TakePhotoActivity";
    private Handler mBackgroundHandler;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_take_photo);
        Log.i("LockService", "onCreate: ");
        mUserName=getIntent().getStringExtra("userName");
        requestPermission();
    }

    public static void start(Context context,String userName) {
        Intent intent = new Intent(context, TakePhotoActivity.class);
        intent.putExtra("userName",userName);
        context.startActivity(intent);
    }

    public void requestPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        initCameraView();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Toast.makeText(TakePhotoActivity.this, "没有存储权限", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    public void initCameraView() {
        mCameraView = findViewById(R.id.camera);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        mCameraView.setFacing(CameraView.FACING_FRONT);
        mCameraView.start();
        TextView fab = findViewById(R.id.take_picture);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraView != null) {
                    mCameraView.takePicture();
                }
            }
        });
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }


        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Toast.makeText(cameraView.getContext(), "拍照成功", Toast.LENGTH_SHORT).show();
//            savePhoto(data);
            checkAlive(data);
        }
    };

    private void checkAlive(byte[] data) {
        final String image = Base64Util.encode(data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean right = FaceVerify.isverify(image, "BASE64");
                if (right) {
                    Log.i(TAG, "succeed: ");
                    boolean right2 = FaceAdd.isadd(image, "sign", mUserName);
                    if (right2) {
                        EventBus.getDefault().post("add");
                    } else {
                    }
                } else {
                    Log.i(TAG, "failed: ");
                }
            }
        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
