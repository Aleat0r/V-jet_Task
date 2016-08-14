package com.aleat0r.v_jettask.mvp;

/**
 * Created by Aleksandr Kovalenko on 11.08.2016.
 */
public interface MainContract {

    interface Presenter {
        void openSocNetworkProfile(String socNetwork);

        void openGallery();
    }

}
