package com.example.saveapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.saveapp.R;

import java.util.ArrayList;
import java.util.List;


public class LocationActivity extends Activity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    private MapView mMapView;
    private BitmapDescriptor centerbitmap;
    private Marker centerMarker;
    private Button mButton;
    private boolean isFirstLocation = true;

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private SuggestionSearch mSuggestionSearch = null;
    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;

    private double lat;
    private double lon;
    private double centerlat;
    private double centerlon;
    private String location_describe;
    private Button bt_citysearch;
    private PoiSearch poiSearch;
    private String city;

    private ProgressDialog mProgressDialog;
    private BitmapDescriptor bitmap;

    public static void start(Context context){
        Intent intent=new Intent(context,LocationActivity.class);
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
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setTitle("定位中");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        poiSearch= PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
        keyWorldsView = findViewById(R.id.searchkey);
        sugAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        keyWorldsView.setThreshold(1);

       keyWorldsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           }
       });
        keyWorldsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    return;
                }

                /* 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新 */
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(cs.toString())
                        .city(city));
            }
        });


        //获取地图控件引用

        bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.current_location);

        mMapView =  findViewById(R.id.bmapView);
        bt_citysearch=findViewById(R.id.bt_citysearch);
        bt_citysearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mBaiduMap.clear();
                    setcenterMarker();
                    setMarker();
                String keystr = keyWorldsView.getText().toString();

                poiSearch.searchInCity((new PoiCitySearchOption().cityLimit(false))
                        .city(city)
                        .keyword(keystr)
                        .pageNum(0)
                        .scope(1));

            }
        });
        mButton= findViewById(R.id.regester);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                location_describe=keyWorldsView.getText().toString();

                intent.putExtra("lat",centerlat);
                intent.putExtra("lon",centerlon);
                intent.putExtra("location_describe",location_describe);
               setResult(1,intent);
             finish();
            }
        });

        centerbitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.choosed_location);
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
                requestCenterPos(mapStatus);
                setcenterMarker();

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
    private void setMarker() {
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
    private void setUserMapCenter() {
        LatLng cenpt = new LatLng(lat,lon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
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
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
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
     * 实现定位监听 位置一旦有所改变就会调用这个方法
     * 可以在这个方法里面获取到定位之后获取到的一系列数据
     */
    public void requestCenterPos(MapStatus status) {
        LatLng mCenterLatLng = status.target;
        centerlat = mCenterLatLng.latitude;
        centerlon = mCenterLatLng.longitude;
    }

    // 根据坐标获取地址


    @Override
    public void onGetPoiResult(PoiResult poiResult) {

        if(poiResult.getAllPoi()!=null) {
               centerlat = poiResult.getAllPoi().get(0).getLocation().latitude;
                centerlon = poiResult.getAllPoi().get(0).getLocation().longitude;

            LatLng center = new LatLng(centerlat,centerlon);
            setcenterMarker();
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(center)
                    .zoom(18)
                    .build();
            //定义MapStatusUpdate对象  ，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);

        }

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }

        List<String> suggest = new ArrayList<>();
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }

        sugAdapter = new ArrayAdapter<>(LocationActivity.this, android.R.layout.simple_dropdown_item_1line,
                suggest);
        keyWorldsView.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location

            lat = location.getLatitude();
            lon = location.getLongitude();

            //这个判断是为了防止每次定位都重新设置中心点和marker
            if(isFirstLocation){
                isFirstLocation = false;
                setMarker();
                setUserMapCenter();
                centerlat=lat;
                centerlon=lon;
                city=location.getCity();
                mProgressDialog.dismiss();
                LatLng center = new LatLng(centerlat,centerlon);
                OverlayOptions ooA=new MarkerOptions().position(center).icon(centerbitmap);
                centerMarker=(Marker)mBaiduMap.addOverlay(ooA);

            }

        }

    }

    public void setcenterMarker(){
        if(centerMarker!=null)
        { centerMarker.remove();}

        LatLng center = new LatLng(centerlat,centerlon);
        OverlayOptions ooA=new MarkerOptions().position(center).icon(centerbitmap);
        centerMarker=(Marker)mBaiduMap.addOverlay(ooA);
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
        centerbitmap.recycle();
        poiSearch.destroy();
        bitmap.recycle();
        System.gc();

    }

}
