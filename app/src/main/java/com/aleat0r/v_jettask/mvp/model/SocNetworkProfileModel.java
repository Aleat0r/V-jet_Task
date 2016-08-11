package com.aleat0r.v_jettask.mvp.model;

import android.content.Context;

import com.aleat0r.v_jettask.mvp.SocNetworkAccountContract;
import com.aleat0r.v_jettask.realm.SocNetworkProfile;

import io.realm.Realm;

/**
 * Created by Aleksandr Kovalenko on 10.08.2016.
 */
public class SocNetworkProfileModel implements SocNetworkAccountContract.Model {

    private Realm mRealm;

    public SocNetworkProfileModel(Context context) {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public SocNetworkProfile getSavedProfile(String id, String socNetwork) {
        SocNetworkProfile cachedProfile = mRealm.where(SocNetworkProfile.class)
                .equalTo("id", id)
                .equalTo("socNetwork", socNetwork)
                .findFirst();
        return cachedProfile;
    }

    @Override
    public void saveProfile(final String id, final String name, final String email, final String birthday, final String photoUrl,
                            final String token, final String socNetwork, final OnSaveCallback callback) {

        mRealm.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        SocNetworkProfile socNetworkProfile = realm.where(SocNetworkProfile.class)
                                .equalTo("id", id)
                                .equalTo("socNetwork", socNetwork)
                                .findFirst();

                        if (socNetworkProfile == null) {
                            socNetworkProfile = realm.createObject(SocNetworkProfile.class, id);
                        }

                        socNetworkProfile.setName(name);
                        socNetworkProfile.setEmail(email);
                        socNetworkProfile.setBirthday(birthday);
                        socNetworkProfile.setPictureUrl(photoUrl);
                        socNetworkProfile.setToken(token);
                        socNetworkProfile.setSocNetwork(socNetwork);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        callback.onSuccess();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        callback.onFailure(error);
                    }
                }
        );
    }

    @Override
    public String getProfileImageUrl(String id) {
        SocNetworkProfile cachedProfile = mRealm.where(SocNetworkProfile.class).equalTo("id", id).findFirst();
        return (cachedProfile != null) ? cachedProfile.getPictureUrl() : null;
    }
}
