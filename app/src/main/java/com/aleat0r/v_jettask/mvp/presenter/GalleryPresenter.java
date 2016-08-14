package com.aleat0r.v_jettask.mvp.presenter;

import android.content.Context;

import com.aleat0r.v_jettask.mvp.GalleryContract;
import com.aleat0r.v_jettask.mvp.model.GalleryModel;
import com.aleat0r.v_jettask.ui.adapter.GalleryPagerAdapter;

/**
 * Created by Aleksandr Kovalenko on 14.08.2016.
 */
public class GalleryPresenter implements GalleryContract.Presenter {

    private GalleryContract.View mView;
    private GalleryContract.Model mModel;

    public GalleryPresenter(Context context, GalleryContract.View mView) {
        this.mView = mView;
        this.mModel = new GalleryModel(context);
    }

    @Override
    public void onCreate() {
        GalleryPagerAdapter galleryPagerAdapter = new GalleryPagerAdapter(mView.getActivity(), mModel.getPicturesUrls());
        mView.setGalleryAdapter(galleryPagerAdapter);
    }
}
