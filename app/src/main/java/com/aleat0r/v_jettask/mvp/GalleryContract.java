package com.aleat0r.v_jettask.mvp;

import android.app.Activity;

import com.aleat0r.v_jettask.ui.adapter.GalleryPagerAdapter;

/**
 * Created by Aleksandr Kovalenko on 14.08.2016.
 */
public interface GalleryContract {

    interface Presenter {

        void onCreate();

    }

    interface View {

        Activity getActivity();

        void setGalleryAdapter(GalleryPagerAdapter adapter);
    }

    interface Model {

        String[] getPicturesUrls();

    }

}
