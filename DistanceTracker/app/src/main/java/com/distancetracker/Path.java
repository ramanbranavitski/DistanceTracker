package com.distancetracker;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by branavitski on 30.03.2016.
 */
public class Path extends RealmObject {

    @PrimaryKey
    private long id = 0;
    RealmList<Point> points;

    public Path() {
        points = new RealmList<>();
    }

    public List<LatLng> getPoints() {
        List<LatLng> result = new ArrayList<>();
        for (Point point : points) {
            result.add(new LatLng(point.getLatitude(), point.getLongitude()));
        }
        return result;
    }

    public void addPoint(Point point) {
        points.add(point);
        Realm.getDefaultInstance().copyToRealmOrUpdate(this);
    }
}
