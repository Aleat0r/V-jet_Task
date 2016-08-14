package com.aleat0r.v_jettask.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aleat0r.v_jettask.R;
import com.aleat0r.v_jettask.mvp.MainContract;
import com.aleat0r.v_jettask.mvp.presenter.MainPresenter;
import com.aleat0r.v_jettask.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_facebook)
    Button mBtnFacebook;

    @BindView(R.id.btn_twitter)
    Button mBtnTwitter;

    @BindView(R.id.btn_google_plus)
    Button mBtnGooglePlus;

    @BindView(R.id.btn_vkontakte)
    Button mBtnVkontakte;

    @BindView(R.id.btn_gallery)
    Button mBtnGallery;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mPresenter = new MainPresenter(this);
    }

    @OnClick({R.id.btn_facebook, R.id.btn_twitter, R.id.btn_google_plus, R.id.btn_vkontakte, R.id.btn_gallery})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_facebook:
                mPresenter.openSocNetworkProfile(Constants.SOC_NETWORK_FACEBOOK);
                break;
            case R.id.btn_twitter:
                mPresenter.openSocNetworkProfile(Constants.SOC_NETWORK_TWITTER);
                break;
            case R.id.btn_google_plus:
                mPresenter.openSocNetworkProfile(Constants.SOC_NETWORK_GOOGLE_PLUS);
                break;
            case R.id.btn_vkontakte:
                mPresenter.openSocNetworkProfile(Constants.SOC_NETWORK_VKONTAKTE);
                break;
            case R.id.btn_gallery:
                mPresenter.openGallery();
                break;
            default:
                break;
        }
    }

}
