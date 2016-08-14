package com.aleat0r.v_jettask.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.aleat0r.v_jettask.R;
import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Aleksandr Kovalenko on 14.08.2016.
 */
public class GalleryPagerAdapter extends PagerAdapter {

    private Context mContext;
    private String [] mPictures;

    public GalleryPagerAdapter(Context context, String[] pictures) {
        mContext = context;
        mPictures = pictures;
    }

    @Override
    public int getCount() {
        return mPictures.length;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());

        Glide.with(mContext)
                .load(mPictures[position])
                .placeholder(R.mipmap.ic_launcher)
                .into(photoView);

        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
