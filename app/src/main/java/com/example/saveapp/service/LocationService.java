package com.example.saveapp.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.saveapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LocationService extends Service {
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new LocationService.MyLocationListener();
    MediaPlayer mediaPlayer;
    private AudioManager audioManager = null; // Audio管理器，用了控制音量

    public LocationService() {
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LocationService.class);
        activity.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        SDKInitializer.initialize(getApplicationContext());
        //声明LocationClient类，这里context考虑是否换成this
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //配置定位参数
        initLocation();
        //开始定位
        mLocationClient.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(String message){
       if("close".equals(message)){
           Log.i("1", "receiveMessage: ");
           mLocationClient.stop();
       }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
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

    private void maxVoice() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        final int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获取最大音量值
        new CountDownTimer(Integer.MAX_VALUE, 2000) {
            @Override
            public void onTick(long l) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); //tempVolume:音量绝对值
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public class MyLocationListener implements BDLocationListener {
        private double oldLatitude = 0;
        private double oldLongtitude = 0;
        private boolean play = false;

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.i("lat", "onReceiveLocation: " + location.getLatitude());
            Log.i("lon", "onReceiveLocation: " + location.getLongitude());
            LatLng oldPosition = new LatLng(oldLatitude, oldLongtitude);
            LatLng newPosition = new LatLng(location.getLatitude(), location.getLongitude());
            if (DistanceUtil.getDistance(oldPosition, newPosition) >= 5) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                } else if (play) {
                    maxVoice();
                    MediaPlayer mediaPlayer = MediaPlayer.create(LocationService.this, R.raw.police);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                    oldLatitude = location.getLatitude();
                    oldLongtitude = location.getLongitude();
                    Toast.makeText(LocationService.this, "你在干什么？", Toast.LENGTH_LONG).show();
                    play = true;
//                Position position = new Position();
//                position.setLocation(new BmobGeoPoint(oldLongtitude, oldLatitude));
//                position.save(new SaveListener<String>() {
//                    @Override
//                    public void done(String objectId, BmobException e) {
//                        if (e == null) {
//
//                        } else {
//                        }
//                    }
//                });
                }
            }
        }
    }
}