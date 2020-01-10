package com.example.saveapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.saveapp.util.SharePreferenceUtil;

public class BootReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (SharePreferenceUtil.readBoolean(SharePreferenceKey.CALL_POLICE, false)) {
            Intent locationIntent = new Intent(context, PositionService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(locationIntent);
            } else {
                context.startService(locationIntent);
            }
        }

    }
}