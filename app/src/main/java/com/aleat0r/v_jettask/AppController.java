package com.aleat0r.v_jettask;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Aleksandr Kovalenko on 10.08.2016.
 */
public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initFacebookSdk();
        configureRealm();
    }

    private void initFacebookSdk() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private void configureRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
