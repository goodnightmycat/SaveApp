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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saveapp.R;
import com.example.saveapp.util.Base64Util;
import com.example.saveapp.face.RealManFaceCheck.FaceVerify;
import com.google.android.cameraview.CameraView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TakePhotoActivity extends AppCompatActivity {
    private CameraView mCameraView;
    private static final String TAG = "TakePhotoActivity";
    private Handler mBackgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        Log.i("LockService", "onCreate: ");
        requestPermission();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, TakePhotoActivity.class);
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
        final String phone = "15816221326";
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean right = FaceVerify.isverify(image, "BASE64");
                if (right) {
                    Log.i(TAG, "succeed: ");
//                    boolean right2 = FaceAdd.isadd(image, "sign", phone);
//                    if (right2) {
//                    } else {
//                    }
                } else {
                    Log.i(TAG, "failed: ");
                }
            }
        }).start();

    }

    private void savePhoto(final byte[] data) {
        getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "picture.jpg");
                OutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                    os.write(data);
                    os.close();
                } catch (IOException e) {
                    Log.w(TAG, "Cannot write to " + file, e);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
