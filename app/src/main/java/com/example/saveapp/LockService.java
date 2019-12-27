package com.example.saveapp;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.saveapp.activity.TakePhotoActivity;

public class LockService extends Service {
    private LockReceiver lockreceiver;

    class LockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //状态更新,通知观察者
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Log.i("LockService", "onReceive: ");
                Intent intent1 = new Intent(LockService.this, TakePhotoActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent1);
            }
        }
    }

    //BroadcastReceiver
    public LockService() {
    }

    @Override
    public void onCreate() {
        lockreceiver = new LockReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //注册观察者
        registerReceiver(lockreceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Log.i("LockService", "onStartCommand: ");
        return Service.START_STICKY;

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(lockreceiver);
        super.onDestroy();
        Intent localIntent = new Intent();
        localIntent.setClass(this, LockService.class); //销毁时重新启动Service
        this.startService(localIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
