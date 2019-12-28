package com.example.saveapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

    private Button logoutBtn;
    private ListView mListView;
    private EditText editName;
    private int mIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private TextView title;
//    private String menu[] = {"用户名: " + BmobUser.getCurrentUser(User.class).getUsername(), "手机号: " + BmobUser.getCurrentUser(User.class).getMobilePhoneNumber(),"密码：******"};

    private String menu[] = {"用户名: " + "晓阳", "手机号: " + "15816221326", "密码：******"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);
        mListView = view.findViewById(R.id.listview);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(this);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, menu);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

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
}
