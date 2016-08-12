package com.aleat0r.v_jettask.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aleat0r.v_jettask.R;
import com.aleat0r.v_jettask.mvp.SocNetworkAccountContract;
import com.aleat0r.v_jettask.mvp.presenter.FacebookAccountPresenter;
import com.aleat0r.v_jettask.mvp.presenter.GooglePlusAccountPresenter;
import com.aleat0r.v_jettask.utils.Constants;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Aleksandr Kovalenko on 10.08.2016.
 */
public class SocNetworkAccountActivity extends AppCompatActivity implements SocNetworkAccountContract.View {

    @BindView(R.id.btn_log_out)
    Button mBtnLogout;

    @BindView(R.id.img_profile)
    ImageView mImgProfile;

    @BindView(R.id.tv_name)
    TextView mTvName;

    @BindView(R.id.tv_email)
    TextView mTvEmail;

    @BindView(R.id.tv_date)
    TextView mTvDate;

    private String mSocNetworkType;
    private SocNetworkAccountContract.Presenter mSocNetworkPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soc_network_profile);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mSocNetworkType = intent.getStringExtra(Constants.SOC_NETWORK_INTENT_EXTRA);

        selectPresenter();
        mSocNetworkPresenter.onCreate();
    }

    private void selectPresenter() {
        switch (mSocNetworkType) {
            case Constants.SOC_NETWORK_FACEBOOK:
                mSocNetworkPresenter = new FacebookAccountPresenter(this, this);
                break;
            case Constants.SOC_NETWORK_GOOGLE_PLUS:
                mSocNetworkPresenter = new GooglePlusAccountPresenter(this, this);
                break;
            case Constants.SOC_NETWORK_TWITTER:
                break;
            case Constants.SOC_NETWORK_VKONTAKTE:
                break;
        }
    }

    @Override
    public void updateTextInfo(String name, String email, String bDate) {
        if (!TextUtils.isEmpty(name)) {
            mTvName.setText(name);
        }
        if (!TextUtils.isEmpty(email)) {
            mTvEmail.setText(email);
        }
        if (!TextUtils.isEmpty(bDate)) {
            mTvDate.setText(bDate);
        }
    }

    @Override
    public void updatePicture(String url) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                    .into(mImgProfile);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSocNetworkPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btn_log_out)
    public void onClickBtnLogout() {
        mSocNetworkPresenter.logOut();
    }
}