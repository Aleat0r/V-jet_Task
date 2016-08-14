package com.aleat0r.v_jettask.mvp.presenter;

import android.content.Context;
import android.content.Intent;

import com.aleat0r.v_jettask.ui.activity.GalleryActivity;
import com.aleat0r.v_jettask.ui.activity.SocNetworkAccountActivity;
import com.aleat0r.v_jettask.mvp.MainContract;
import com.aleat0r.v_jettask.utils.Constants;

/**
 * Created by Aleksandr Kovalenko on 11.08.2016.
 */
public class MainPresenter implements MainContract.Presenter {

    private Context mContext;

    public MainPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void openSocNetworkProfile(String socNetwork) {
        Intent intent = new Intent(mContext, SocNetworkAccountActivity.class);
        intent.putExtra(Constants.SOC_NETWORK_INTENT_EXTRA, socNetwork);
        mContext.startActivity(intent);
    }

    @Override
    public void openGallery() {
        Intent intent = new Intent(mContext, GalleryActivity.class);
        mContext.startActivity(intent);
    }
}
