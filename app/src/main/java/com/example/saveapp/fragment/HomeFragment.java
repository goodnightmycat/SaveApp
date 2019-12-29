package com.example.saveapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.saveapp.R;
import com.example.saveapp.activity.FindActivity;
import com.example.saveapp.activity.LockActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private int startType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView open = view.findViewById(R.id.fragment_home_open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startType=1;
                    requestPermission();
            }
        });

        TextView find = view.findViewById(R.id.fragment_home_find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startType=2;
                    requestPermission();
            }
        });
        TextView grant = view.findViewById(R.id.fragment_home_grant);
        grant.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                getAppDetailSettingIntent();
            }
        });

        return view;
    }

    public void requestPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA, Permission.ACCESS_FINE_LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if(startType==1){
                            Toast.makeText(getActivity(), "安全模式启动", Toast.LENGTH_SHORT).show();
                            LockActivity.start(getActivity());

                        } else if(startType==2){
                            FindActivity.start(getActivity());
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Toast.makeText(getActivity(), "没有获取权限", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getAppDetailSettingIntent() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", Objects.requireNonNull(getActivity()).getPackageName(), null));
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        startActivity(intent);
    }
}
