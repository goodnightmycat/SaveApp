package com.example.saveapp.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class Position extends BmobObject {
    private BmobGeoPoint position;
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public BmobGeoPoint getLocation() {
        return position;
    }

    public void setLocation(BmobGeoPoint location) {
        this.position = location;
    }

}
