package com.example.saveapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
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
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.saveapp.R;
import com.example.saveapp.bean.Position;
import com.example.saveapp.bean.User;
import com.example.saveapp.view.BirthDayPicker;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class FindActivity extends Activity {
    private MapView mMapView;
    private static final String TAG = "FindActivity";
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private ProgressDialog mProgressDialog;
    private BitmapDescriptor bitmap;

    public static void start(Context context) {
        Intent intent = new Intent(context, FindActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        //使用百度地图的任何功能都需要先初始化这段代码  最好放在全局中进行初始化
        //百度地图+定位+marker比较简单 我就不放到全局去了
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find);
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.current_location);
        TextView findPositionLine = findViewById(R.id.find_position_line);
        findPositionLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BirthDayPicker(FindActivity.this, new BirthDayPicker.OnSelectListener() {
                    @Override
                    public void onDateSelect(String date) {
                        findPositionLine(date);
                    }
                }).show();
            }
        });
        TextView findPositionPoint = findViewById(R.id.find_position_point);
        findPositionPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPositionPoint();
            }
        });
        initBaiduMap();
        initLocation();
        findPositionPoint();
    }

    private void findPositionLine(final String choose) {
        BmobQuery<Position> query = new BmobQuery<>();
        query.addWhereEqualTo("user_id", BmobUser.getCurrentUser(User.class).getObjectId());
        query.findObjects(new FindListener<Position>() {
            @Override
            public void done(List<Position> list, BmobException e) {
                if (list != null) {
                    List<LatLng> points = new ArrayList<LatLng>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) != null) {
                            if (choose == null) {
                                points.add(new LatLng(list.get(i).getLocation().getLatitude(), list.get(i).getLocation().getLongitude()));
                            } else if (choose.equals(list.get(0).getCreatedAt().substring(0, 10))) {
                                points.add(new LatLng(list.get(i).getLocation().getLatitude(), list.get(i).getLocation().getLongitude()));
                            }
                        }
                    }
                    setMarker(points);
                }
            }
        });
    }

    private void update() {

    }

    private void findPositionPoint() {
        //最终的查询条件
        BmobQuery<Position> query = new BmobQuery<>();
        query.addWhereEqualTo("user_id", BmobUser.getCurrentUser(User.class).getObjectId());
        query.findObjects(new FindListener<Position>() {
            @Override
            public void done(List<Position> list, BmobException e) {
                if (list != null) {
                    LatLng latLng = new LatLng(list.get(list.size() - 1).getLocation().getLatitude(), list.get(list.size() - 1).getLocation().getLongitude());
                    setMarker(latLng);
                }
            }
        });
    }

    /**
     * 添加marker线
     */
    private void setMarker(List<LatLng> points) {
        mBaiduMap.clear();
        if (points == null || points.size() == 0) {
            Toast.makeText(FindActivity.this, "该日期无定位记录", Toast.LENGTH_SHORT).show();
            return;
        }
        setMapCenter(points.get(points.size() - 1));
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .color(0xAAFF0000)
                .points(points);
        Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
        //定义Maker坐标点);
    }


    private void setMarker(LatLng point) {
        if (point == null) {
            return;
        }
        Toast.makeText(FindActivity.this, "找到手机最近定位", Toast.LENGTH_SHORT).show();
        mBaiduMap.clear();
        setMapCenter(point);
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
    private void setMapCenter(LatLng point) {
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private void initBaiduMap() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("定位中");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //开始定位
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
//                Position mUploadPosition = new Position();
//                mUploadPosition.setUser_id(BmobUser.getCurrentUser(User.class).getObjectId());
//                mUploadPosition.setLocation(new BmobGeoPoint(mapStatus.target.longitude, mapStatus.target.latitude));
//                mUploadPosition.save(new SaveListener<String>() {
//                    @Override
//                    public void done(String objectId, BmobException e) {
//                        if (e == null) {
//                            Log.i(TAG, "upLoadPositionSucceed: ");
//                        } else {
//                            Log.i(TAG, "upLoadPositionFailed: ");
//                        }
//                    }
//                });
            }
        });
    }

    /**
     * 配置定位参数
     */
    private void initLocation() {
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
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

    /**
     * 上传位置变化情况
     */
    public class MyLocationListener implements BDLocationListener {
        private boolean init = false;

        @Override
        public void onReceiveLocation(BDLocation location) {
            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (!init) {
                setMapCenter(new LatLng(location.getLatitude(), location.getLongitude()));
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
