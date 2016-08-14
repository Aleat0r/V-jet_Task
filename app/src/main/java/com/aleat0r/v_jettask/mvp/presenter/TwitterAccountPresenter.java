package com.aleat0r.v_jettask.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.webkit.CookieManager;

import com.aleat0r.v_jettask.R;
import com.aleat0r.v_jettask.mvp.SocNetworkAccountContract;
import com.aleat0r.v_jettask.mvp.model.SocNetworkProfileModel;
import com.aleat0r.v_jettask.realm.SocNetworkProfile;
import com.aleat0r.v_jettask.utils.Constants;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Aleksandr Kovalenko on 13.08.2016.
 */
public class TwitterAccountPresenter implements SocNetworkAccountContract.Presenter {

    private SocNetworkAccountContract.View mView;
    private SocNetworkAccountContract.Model mModel;

    private TwitterAuthClient mTwitterClient;
    private TwitterSession mSession;

    public TwitterAccountPresenter(Context context, SocNetworkAccountContract.View mView) {
        this.mView = mView;
        this.mModel = new SocNetworkProfileModel(context);
    }

    @Override
    public void onCreate() {
        mTwitterClient = new TwitterAuthClient();
        mTwitterClient.authorize(mView.getActivity(), new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                mSession = result.data;
                getUserData();
            }

            @Override
            public void failure(TwitterException e) {
                showSavedProfile();
            }
        });
    }

    private void getUserData() {
        Twitter.getApiClient(mSession).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {

                    @Override
                    public void failure(TwitterException e) {
                        showSavedProfile();
                    }

                    @Override
                    public void success(Result<User> userResult) {
                        User user = userResult.data;
                        String name = user.name;
                        String profilePictureUrl = user.profileImageUrl.replace(mView.getActivity().getString(R.string.twitter_profile_pic_type), "");
                        getEmail(name, profilePictureUrl);
                    }
                });
    }

    private void getEmail(final String name, final String profilePictureUrl) {
        mTwitterClient.requestEmail(mSession, new Callback<String>() {
            @Override
            public void success(Result<String> stringResult) {
                saveProfile(name, stringResult.data, null, profilePictureUrl);
            }

            @Override
            public void failure(TwitterException e) {
                saveProfile(name, null, null, profilePictureUrl);
            }
        });
    }

    private void showSavedProfile() {
        SocNetworkProfile profile = mModel.getSavedProfile(Constants.SOC_NETWORK_TWITTER);
        if (profile != null) {
            mView.updateTextInfo(profile.getName(), profile.getEmail(), profile.getBirthday());
            mView.updatePicture(mModel.getProfileImageUrl(Constants.SOC_NETWORK_TWITTER));
        } else {
            logOut();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTwitterClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void logOut() {
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (twitterSession != null) {

            CookieManager cookieManager = CookieManager.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                cookieManager.removeSessionCookies(null);
            } else {
                cookieManager.removeSessionCookie();
            }

            Twitter.getSessionManager().clearActiveSession();
            Twitter.logOut();
        }

        mModel.deleteSavedProfile(Constants.SOC_NETWORK_TWITTER);
        mView.finish();
    }

    @Override
    public void post() {
        TweetComposer.Builder builder = null;
        try {
            builder = new TweetComposer.Builder(mView.getActivity())
                    .text(mView.getActivity().getString(R.string.post))
                    .url(new URL(mView.getActivity().getString(R.string.link_for_post)));
            builder.show();
        } catch (MalformedURLException e) {
            mView.showMessage(e.getLocalizedMessage());
        }
    }

    private void saveProfile(final String name, final String email, final String birthday, String photoUrl) {
        mModel.saveProfile(Constants.SOC_NETWORK_TWITTER, name, email, birthday, photoUrl, mSession.getAuthToken().token,
                new SocNetworkAccountContract.Model.OnSaveCallback() {
                    @Override
                    public void onSuccess() {
                        mView.updateTextInfo(name, email, birthday);
                        mView.updatePicture(mModel.getProfileImageUrl(Constants.SOC_NETWORK_TWITTER));
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        mView.showMessage(error.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void onDestroy() {
    }
}
