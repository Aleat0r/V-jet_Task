package com.aleat0r.v_jettask.mvp.model;

import android.content.Context;

import com.aleat0r.v_jettask.R;
import com.aleat0r.v_jettask.mvp.GalleryContract;

/**
 * Created by Aleksandr Kovalenko on 14.08.2016.
 */
public class GalleryModel implements GalleryContract.Model {

    private Context mContext;

    public GalleryModel(Context context) {
        mContext = context;
    }

    @Override
    public String[] getPicturesUrls() {
        String [] picturesUrls = mContext.getResources().getStringArray(R.array.picture_urls);
        return picturesUrls;
    }
}
