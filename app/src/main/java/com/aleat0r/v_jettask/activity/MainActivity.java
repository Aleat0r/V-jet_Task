package com.aleat0r.v_jettask.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aleat0r.v_jettask.R;
import com.aleat0r.v_jettask.mvp.MainActivityContract;
import com.aleat0r.v_jettask.mvp.presenter.MainActivityPresenter;
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

    private MainActivityContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mPresenter = new MainActivityPresenter(this);
    }

    @OnClick({R.id.btn_facebook, R.id.btn_twitter, R.id.btn_google_plus, R.id.btn_vkontakte})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_facebook:
                mPresenter.openSocNetworkProfile(Constants.SOC_NETWORK_FACEBOOK);
                break;
            case R.id.btn_twitter:
                break;
            case R.id.btn_google_plus:
                mPresenter.openSocNetworkProfile(Constants.SOC_NETWORK_GOOGLE_PLUS);
                break;
            case R.id.btn_vkontakte:
                break;
            default:
                break;
        }
    }

}
