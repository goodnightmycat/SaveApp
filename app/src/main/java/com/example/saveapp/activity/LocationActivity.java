package com.example.saveapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.saveapp.R;
import com.example.saveapp.bean.Position;

import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LocationActivity extends Activity {
    private MapView mMapView;

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private ProgressDialog mProgressDialog;
    private BitmapDescriptor bitmap;

    public static void start(Context context) {
        Intent intent = new Intent(context, LocationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        //使用百度地图的任何功能都需要先初始化这段代码  最好放在全局中进行初始化
        //百度地图+定位+marker比较简单 我就不放到全局去了
        SDKInitializer.initialize(getApplicationContext());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_location);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("定位中");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        //获取地图控件引用

        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.current_location);

        mMapView = findViewById(R.id.bmapView);

        //获取BaiduMap对象
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                Log.i("777", "onMapStatusChangeFinish: ");
                LatLng centerLatLng = mapStatus.target;
                Position position = new Position();
                position.setLocation(new BmobGeoPoint(centerLatLng.longitude, centerLatLng.latitude));
                position.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            Toast.makeText(LocationActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LocationActivity.this, "保存失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        //声明LocationClient类，这里context考虑是否换成this
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //配置定位参数
        initLocation();
        //开始定位
        mLocationClient.start();
    }

    /**
     * 添加marker
     */
    private void setMarker(double lat, double lon) {
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    /**
     * 设置中心点
     */
    private void setUserMapCenter(double lat, double lon) {
        LatLng center = new LatLng(lat, lon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(center)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    /**
     * 配置定位参数
     */
    private void initLocation() {
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
    }

    /**
     * 上传位置变化情况
     */
    public class MyLocationListener implements BDLocationListener {
        private boolean init = false;
        private double oldLatitude = 0;
        private double oldLongtitude = 0;

        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.i("lat", "onReceiveLocation: " + location.getLatitude());
            Log.i("lon", "onReceiveLocation: " + location.getLongitude());
            LatLng oldPosition = new LatLng(oldLatitude, oldLongtitude);
            LatLng newPosition = new LatLng(location.getLatitude(), location.getLongitude());
            oldLatitude = location.getLatitude();
            oldLongtitude = location.getLongitude();
//            if (DistanceUtil.getDistance(oldPosition, newPosition) >= 0) {
//                Toast.makeText(LocationActivity.this, "你在干什么？", Toast.LENGTH_LONG).show();
//                Position position = new Position();
//                position.setLocation(new BmobGeoPoint(oldLongtitude, oldLatitude));
//                position.save(new SaveListener<String>() {
//                    @Override
//                    public void done(String objectId, BmobException e) {
//                        if (e == null) {
//                            Toast.makeText(LocationActivity.this, "保存成功", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(LocationActivity.this, "保存失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//            }
            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (!init) {
                setMarker(location.getLatitude(), location.getLongitude());
                setUserMapCenter(location.getLatitude(), location.getLongitude());
                mProgressDialog.dismiss();
                init = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(myListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        bitmap.recycle();
        System.gc();

    }

}
