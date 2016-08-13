package com.aleat0r.v_jettask;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Aleksandr Kovalenko on 10.08.2016.
 */
public class AppController extends Application {

    private static final String TWITTER_KEY = "gZLWHIv76SHxpCuUqxzyVqAg3";
    private static final String TWITTER_SECRET = "RHsvQbGdwse01f8d7afuv3hpp3bFovsVaanvvvAvQ4tQxXkdfv";

    @Override
    public void onCreate() {
        super.onCreate();
        initTwitter();
        initFacebookSdk();
        configureRealm();
    }

    private void initTwitter() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
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
