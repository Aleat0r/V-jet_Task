package com.aleat0r.v_jettask.mvp.presenter;

import android.content.Context;
import android.content.Intent;

import com.aleat0r.v_jettask.activity.SocNetworkAccountActivity;
import com.aleat0r.v_jettask.mvp.MainActivityContract;
import com.aleat0r.v_jettask.utils.Constants;

/**
 * Created by Aleksandr Kovalenko on 11.08.2016.
 */
public class MainActivityPresenter implements MainActivityContract.Presenter {

    private Context mContext;

    public MainActivityPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void openSocNetworkProfile(String socNetwork) {
        Intent intent = new Intent(mContext, SocNetworkAccountActivity.class);
        intent.putExtra(Constants.SOC_NETWORK_INTENT_EXTRA, socNetwork);
        mContext.startActivity(intent);
    }
}
