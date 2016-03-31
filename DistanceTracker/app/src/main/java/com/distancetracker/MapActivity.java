package com.distancetracker;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by branavitski on 30.03.2016.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_SHOW_PATH = "SHOW PATH";

    private LocationManager mLocationManager;
    private boolean mShowPath;
    private GoogleMap mGoogleMap;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mShowPath = getIntent().getBooleanExtra(EXTRA_SHOW_PATH, false);
        mRealm = Realm.getDefaultInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        mRealm.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                Log.d("TEST", "Database updated");
                showPath();
            }
        });

        if (mShowPath) {

            showPath();
        }
    }


    private void showPath() {
        if (mShowPath) {

            mGoogleMap.clear();

            List<LatLng> points = PathDatabaseManager.getInstance().getPath().getPoints();
            PolylineOptions polyLineOptions = new PolylineOptions();

            Log.d("TEST", "Point size = " + points.size());
            polyLineOptions.addAll(points);
            polyLineOptions.width(20);
            polyLineOptions.color(Color.BLUE);

            mGoogleMap.addPolyline(polyLineOptions);
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
            mLocationManager.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
