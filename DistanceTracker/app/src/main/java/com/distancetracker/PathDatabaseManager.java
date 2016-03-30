package com.distancetracker;

import io.realm.Realm;

/**
 * Created by branavitski on 30.03.2016.
 */
public class PathDatabaseManager {

    private static PathDatabaseManager mInstance;

    public static PathDatabaseManager getInstance() {
        if (mInstance == null) {
            mInstance = new PathDatabaseManager();
        }
        return mInstance;
    }

    public void addPoint(Realm realm, Point point) {
        realm.beginTransaction();
        getPath().addPoint(point);
        realm.commitTransaction();
    }

    public Path getPath() {
        Path path = Realm.getDefaultInstance().where(Path.class).findFirst();
        if (path == null) {
            path = new Path();
        }
        return path;
    }

    public void clearPath() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.clear(Path.class);
        realm.commitTransaction();
    }
}
