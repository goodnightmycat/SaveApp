package com.example.saveapp.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    private String lockPassword;

    public String getLockPassword() {
        return lockPassword;
    }

    public void setLockPassword(String lockPassword) {
        this.lockPassword = lockPassword;
    }
}
