package com.aleat0r.v_jettask.mvp;

import android.app.Activity;
import android.content.Intent;

import com.aleat0r.v_jettask.realm.SocNetworkProfile;

/**
 * Created by Aleksandr Kovalenko on 10.08.2016.
 */
public interface SocNetworkAccountContract {

    interface Presenter {

        void onCreate();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void logOut();
    }

    interface View {

        void updateTextInfo(String name, String email, String birthday);

        void updatePicture(String url);

        void finish();

        Activity getActivity();

        void showMessage(String message);
    }

    interface Model {

        SocNetworkProfile getSavedProfile(String id, String socNetwork);

        void saveProfile(String id, String name, String email, String birthday, String photoUrl, String token, String socNetwork, OnSaveCallback callback);

        String getProfileImageUrl(String id);

        interface OnSaveCallback {

            void onSuccess();

            void onFailure(Throwable error);
        }
    }
}
