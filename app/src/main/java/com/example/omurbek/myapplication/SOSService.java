package com.example.omurbek.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.example.omurbek.myapplication.SOSBroadcastReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;

/**
 * Created by omurbek on 10/21/2017.
 */

public class SOSService extends Service
{
    BroadcastReceiver mReceiver;
    IntentFilter pqrs_intentFilter;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onStart(Intent intent, int startid)
    {
        pqrs_intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        pqrs_intentFilter.addAction(Intent.ACTION_SCREEN_ON);

        mReceiver = new SOSBroadcastReceiver();
        registerReceiver(mReceiver, pqrs_intentFilter);
    }

}