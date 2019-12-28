package com.example.saveapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.saveapp.R;
import com.example.saveapp.activity.LoginByPasswordActivity;
import com.example.saveapp.bean.User;

import java.util.Objects;

import cn.bmob.v3.BmobUser;

public class SetFragment extends Fragment implements View.OnClickListener {

    private TextView userName;
    private TextView userPassword;
    private TextView userPhone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);
        TextView logout = view.findViewById(R.id.fragment_set_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT).setTitle("提示").setMessage("确定要退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BmobUser.logOut();
                                Intent intent = new Intent();
                                intent.setClass(Objects.requireNonNull(getContext()), LoginByPasswordActivity.class);
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        }).setNegativeButton("取消", null).create().show();
            }
        });
        userName=view.findViewById(R.id.fragment_set_name);
        userPassword=view.findViewById(R.id.fragment_set_password);
        userPhone=view.findViewById(R.id.fragment_set_phone);
        return view;
    }
    private void initView(){
        User user=BmobUser.getCurrentUser(User.class);
        if (user != null) {
            userName.setText("用户名："+user.getUsername());
            userPhone.setText("手机号："+user.getMobilePhoneNumber());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onClick(View v) {

    }
}
