package com.distancetracker;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int RC_PERMISSION_FINE_LOCATION = 20;
    public static final int RC_SERVICE_MESSAGES = 30;
    private Button mStartStopTrackingButton;
    private Button mShowDistanceButton;
    private Button mShowLocationButton;

    private LocationService mLocationService;
    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
        PendingIntent pi = createPendingResult(RC_SERVICE_MESSAGES, new Intent(), 0);
        mServiceIntent = new Intent(MainActivity.this, LocationService.class).putExtra(LocationService.EXTRA_PENDING_INTENT, pi);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationService != null && mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SERVICE_MESSAGES) {
            updateStartStopBtn();
        }
    }

    private void updateStartStopBtn() {
        mStartStopTrackingButton.setText(mLocationService == null || !mLocationService.isTracking() ? getString(R.string.start_tracking_text) : getString(R.string.stop_tracking_text));
        mStartStopTrackingButton.setOnClickListener(mLocationService == null || !mLocationService.isTracking() ? mStartTrackingClickListener : mStopTrackingClickListener);
    }

    private void setListeners() {
        updateStartStopBtn();
        mShowLocationButton.setOnClickListener(mShowLocationClickListener);
        mShowDistanceButton.setOnClickListener(mShowDistanceClickListener);
    }

    private void findViews() {
        mStartStopTrackingButton = (Button) findViewById(R.id.btn_start_stop_tracking);
        mShowDistanceButton = (Button) findViewById(R.id.btn_show_distance);
        mShowLocationButton = (Button) findViewById(R.id.btn_show_location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RC_PERMISSION_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent serviceIntent = new Intent(MainActivity.this, LocationService.class);
                    startService(serviceIntent);

                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private View.OnClickListener mStartTrackingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        RC_PERMISSION_FINE_LOCATION);
            } else {
                startService(mServiceIntent);
            }
        }
    };

    private View.OnClickListener mStopTrackingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mLocationService != null) {
                stopService(mServiceIntent);
                unbindService(mServiceConnection);
                PathDatabaseManager.getInstance().clearPath();
                mLocationService = null;
                updateStartStopBtn();

            }
        }
    };

    private View.OnClickListener mShowDistanceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            intent.putExtra(MapActivity.EXTRA_SHOW_PATH, true);
            startActivity(intent);
        }
    };

    private View.OnClickListener mShowLocationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mLocationService = ((LocationService.LocationServiceBinder) service).getService();
            updateStartStopBtn();
        }

        public void onServiceDisconnected(ComponentName className) {
            mLocationService = null;
            updateStartStopBtn();
        }
    };
}
