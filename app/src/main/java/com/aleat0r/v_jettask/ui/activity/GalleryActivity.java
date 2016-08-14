package com.aleat0r.v_jettask.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.aleat0r.v_jettask.R;
import com.aleat0r.v_jettask.ui.adapter.GalleryPagerAdapter;
import com.aleat0r.v_jettask.mvp.GalleryContract;
import com.aleat0r.v_jettask.mvp.presenter.GalleryPresenter;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aleksandr Kovalenko on 14.08.2016.
 */

public class GalleryActivity extends AppCompatActivity implements GalleryContract.View {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.indicator)
    CirclePageIndicator mCirclePageIndicator;

    private GalleryContract.Presenter mGalleryPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        mGalleryPresenter = new GalleryPresenter(this, this);
        mGalleryPresenter.onCreate();

        mCirclePageIndicator.setViewPager(mViewPager);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setGalleryAdapter(GalleryPagerAdapter adapter) {
        mViewPager.setAdapter(adapter);
    }
}
