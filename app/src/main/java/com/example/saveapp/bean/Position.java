package com.example.saveapp.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class Position extends BmobObject {
    private BmobGeoPoint position;

    public BmobGeoPoint getLocation() {
        return position;
    }

    public void setLocation(BmobGeoPoint location) {
        this.position = location;
    }

}
