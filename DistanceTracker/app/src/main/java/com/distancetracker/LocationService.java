package com.distancetracker;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import io.realm.Realm;

/**
 * Created by branavitski on 30.03.2016.
 */
public class LocationService extends Service implements LocationListener {

    private static final String TAG = LocationService.class.getSimpleName();

    public static final String EXTRA_PENDING_INTENT = "extra_pending_intent";

    private static final int TIME_INTERVAL = 10 * 1000;
    private LocationManager mLocationManager;
    private PendingIntent mPendingIntent;
    private boolean mIsTracking = false;
    private boolean mIsNetworkProviderAvailable = false;
    private boolean mIsGpsProviderAvailable = false;

    private Path mPath;

    @Override
    public void onCreate() {
        mPath = new Path();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (intent != null) {
            mPendingIntent = intent.getParcelableExtra(EXTRA_PENDING_INTENT);
        }
        registerForLocationUpdates();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocationServiceBinder();
    }

    @Override
    public void onDestroy() {
        unregisterFromLocationUpdates();
        super.onDestroy();
    }

    private void registerForLocationUpdates() {
        Log.d(TAG, "registerForLocationUpdates");
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_INTERVAL, 50, this);
            mIsGpsProviderAvailable = true;
        }
        setTracking(true);
    }

    private void unregisterFromLocationUpdates() {
        Log.d(TAG, "unregisterFromLocationUpdates");
        mLocationManager.removeUpdates(this);
        setTracking(false);
    }

    private void setTracking(boolean value) {
        mIsTracking = value;
        try {
            if (mPendingIntent != null) {
                mPendingIntent.send();
            }
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        PathDatabaseManager.getInstance().addPoint(Realm.getDefaultInstance(), new Point(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
            mIsNetworkProviderAvailable = true;
        } else if (LocationManager.GPS_PROVIDER.equals(provider)) {
            mIsGpsProviderAvailable = true;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
            mIsNetworkProviderAvailable = false;
        } else if (LocationManager.GPS_PROVIDER.equals(provider)) {
            mIsGpsProviderAvailable = false;
        }
    }

    public boolean isTracking() {
        return mIsTracking;
    }

    public class LocationServiceBinder extends Binder {

        LocationService getService() {
            return LocationService.this;
        }
    }
}
