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
    private Button setface;
    private boolean isfaceset=false;
    private String image;
    private Handler handler;
    private ProgressDialog progressDialog;
    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, 1);
        handler=new MyHeadler();
        etname = findViewById(R.id.et_name);
        etphone =  findViewById(R.id.et_phone);
        etpassword =  findViewById(R.id.et_password);
        etcode = findViewById(R.id.et_code);
        register = findViewById(R.id.bt_register);
        register.setOnClickListener(this);
        time = new TimeCount(60000, 1000);//第一个是要倒数多少秒，可以改
        send =  findViewById(R.id.send);
        send.setOnClickListener(this);
        setface=findViewById(R.id.bt_setface);
        setface.setOnClickListener(this);
    }

    private void getCode() {
        String phone;
        phone = etphone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobSMS.requestSMSCode(phone, "注册短信", new QueryListener<Integer>() {
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

        if (isfaceset)
        {    user.signOrLogin(code, new SaveListener<User>() {

                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();


                    }
                }
            });
    }
    else Toast.makeText(RegisterActivity.this, "人脸信息未上传，请检查网络或重新设置！", Toast.LENGTH_LONG).show();

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
            case R.id.bt_setface:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {//判断是否有相机应用
                    startActivityForResult(takePictureIntent, 1);
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("人脸识别中");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Bundle extras = data.getExtras();
        Bitmap face=(Bitmap)extras.get("data");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        face.compress(Bitmap.CompressFormat.JPEG, 100, baos);


        FileOutputStream out;
        try {
            String sdCardDir = Environment.getExternalStorageDirectory() + "/DIYLock/";
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦
            File file = new File(sdCardDir, "1.jpg");// 在SDcard的目录下创建图片文,以电影名称为其命名

            out = new FileOutputStream(file);
            face.compress(Bitmap.CompressFormat.JPEG , 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        byte[] Byte_image;
        Byte_image = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image= Base64Util.encode(Byte_image);
        face.recycle();
        new Thread(new Runnable() {
            @Override
            public void run() {

              boolean  right = FaceVerify.isverify(image, "BASE64");
                Message message = Message.obtain();

                if (right)
                {
                    message.obj = "人脸识别成功！";

                                boolean right2 = FaceAdd.isadd(image, "sign", etphone.getText().toString());
                                if (right2) {
                                    isfaceset=true;

                                } else {
                                }
                }
                else
                {
                    message.obj = "没有检测到人脸！请重新设置！";}
                handler.sendMessage(message);
            }
        }).start();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    class MyHeadler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this,msg.obj.toString(), Toast.LENGTH_LONG).show();

        }


    }

}

