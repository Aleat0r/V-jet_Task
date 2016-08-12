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
    public SocNetworkProfile getSavedProfile(String id) {
        SocNetworkProfile savedProfile = mRealm.where(SocNetworkProfile.class)
                .equalTo("id", id)
                .findFirst();
        return savedProfile;
    }

    @Override
    public void saveProfile(final String id, final String name, final String email, final String birthday, final String photoUrl,
                            final String token, final OnSaveCallback callback) {

        mRealm.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        SocNetworkProfile socNetworkProfile = realm.where(SocNetworkProfile.class)
                                .equalTo("id", id)
                                .findFirst();

                        if (socNetworkProfile == null) {
                            socNetworkProfile = realm.createObject(SocNetworkProfile.class, id);
                        }

                        socNetworkProfile.setName(name);
                        socNetworkProfile.setEmail(email);
                        socNetworkProfile.setBirthday(birthday);
                        socNetworkProfile.setPictureUrl(photoUrl);
                        socNetworkProfile.setToken(token);
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
    public void deleteSavedProfile(String id) {
        mRealm.beginTransaction();
        SocNetworkProfile profile = getSavedProfile(id);
        if (profile != null) {
            profile.deleteFromRealm();
        }
        mRealm.commitTransaction();

    }

    @Override
    public String getProfileImageUrl(String id) {
        SocNetworkProfile cachedProfile = mRealm.where(SocNetworkProfile.class).equalTo("id", id).findFirst();
        return (cachedProfile != null) ? cachedProfile.getPictureUrl() : null;
    }
}
