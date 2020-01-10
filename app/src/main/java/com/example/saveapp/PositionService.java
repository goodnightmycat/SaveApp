package com.example.saveapp;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.saveapp.activity.SplashActivity;
import com.example.saveapp.bean.Position;
import com.example.saveapp.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PositionService extends Service {
    private static final String TAG = "PositionService";
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new PositionService.MyLocationListener();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLocation();
    }

    public class MyLocationListener implements BDLocationListener {
        private LatLng oldPosition = new LatLng(0, 0);

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceiveLocation(BDLocation location) {
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            if (DistanceUtil.getDistance(oldPosition, position) > 10) {
                oldPosition = new LatLng(location.getLatitude(), location.getLongitude());
                Position uploadPosition = new Position();
                uploadPosition.setUser_id(BmobUser.getCurrentUser(User.class).getObjectId());
                uploadPosition.setLocation(new BmobGeoPoint(location.getLongitude(), location.getLatitude()));
                upLoadPosition(uploadPosition);
            }
        }
    }

    private void upLoadPosition(Position uploadPosition) {
        uploadPosition.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "upLoadPositionSucceed: ");
                } else {
                    Log.i(TAG, "upLoadPositionFailed: ");
                }
            }
        });
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //配置定位参数
        //开始定位
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }
}
