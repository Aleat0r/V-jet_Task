package com.aleat0r.v_jettask;

import android.app.Application;

import com.aleat0r.v_jettask.utils.Constants;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Aleksandr Kovalenko on 10.08.2016.
 */
public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initTwitterSdk();
        initFacebookSdk();
        initVkontakteSdk();
        configureRealm();
    }

    private void initTwitterSdk() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }

    private void initFacebookSdk() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private void initVkontakteSdk() {
        VKSdk.initialize(this).withPayments();
    }

    private void configureRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

}
