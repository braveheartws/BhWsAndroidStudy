package com.havefun.bhwsandroidstudy.rxjavazip;


import android.os.HandlerThread;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class LocationManager {
    private static LocationManager sInstance;
    public static LocationManager getInstance() {
        if (sInstance == null) {
            sInstance = new LocationManager();
        }
        return sInstance;
    }

    private LocationManager() {
        HandlerThread thread = new HandlerThread("test");

        thread.start();

    }

    private ObservableEmitter<String> mEmitter;

    public Observable<String> startLocation() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mEmitter = emitter;
            }
        });
        start();
        return observable;
    }

    private void start() {

    }


}
