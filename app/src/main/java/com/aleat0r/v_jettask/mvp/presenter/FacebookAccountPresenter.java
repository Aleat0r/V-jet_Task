package com.aleat0r.v_jettask.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.aleat0r.v_jettask.R;
import com.aleat0r.v_jettask.mvp.SocNetworkAccountContract;
import com.aleat0r.v_jettask.mvp.model.SocNetworkProfileModel;
import com.aleat0r.v_jettask.realm.SocNetworkProfile;
import com.aleat0r.v_jettask.utils.Constants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Aleksandr Kovalenko on 10.08.2016.
 */
public class FacebookAccountPresenter implements SocNetworkAccountContract.Presenter {

    private SocNetworkAccountContract.View mView;
    private SocNetworkAccountContract.Model mModel;

    private CallbackManager mCallbackManager;
    private AccessToken mAccessToken;

    public FacebookAccountPresenter(Context context, SocNetworkAccountContract.View mView) {
        this.mView = mView;
        this.mModel = new SocNetworkProfileModel(context);
    }

    @Override
    public void onCreate() {

        mCallbackManager = CallbackManager.Factory.create();
        mAccessToken = AccessToken.getCurrentAccessToken();

        if (mAccessToken == null || mAccessToken.isExpired()) {
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {
                    mAccessToken = loginResult.getAccessToken();

                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject data, GraphResponse response) {
                                    String name = null;
                                    try {
                                        name = data.getString("name");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    String email = null;
                                    try {
                                        email = data.getString("email");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    String birthday = null;
                                    try {
                                        birthday = data.getString("birthday");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    String pictureUrl = mView.getActivity().getString(R.string.fb_profile_pic_url) + mAccessToken.getUserId() +
                                            mView.getActivity().getString(R.string.fb_profile_pic_type);

                                    saveProfile(name, email, birthday, pictureUrl);
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", Constants.FACEBOOK_FIELDS);
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    logOut();
                }

                @Override
                public void onError(FacebookException error) {
                    mView.showMessage(error.getLocalizedMessage());
                    logOut();
                }
            });

            LoginManager.getInstance().logInWithReadPermissions(mView.getActivity(), Arrays.asList(Constants.FACEBOOK_PERMISSIONS_FOR_LOGIN));

        } else {
            SocNetworkProfile profile = mModel.getSavedProfile(Constants.SOC_NETWORK_FACEBOOK);
            mView.updateTextInfo(profile.getName(), profile.getEmail(), profile.getBirthday());
            mView.updatePicture(mModel.getProfileImageUrl(Constants.SOC_NETWORK_FACEBOOK));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void logOut() {
        LoginManager.getInstance().logOut();
        mModel.deleteSavedProfile(Constants.SOC_NETWORK_FACEBOOK);
        mView.finish();
    }

    @Override
    public void post() {
        if (!AccessToken.getCurrentAccessToken().getPermissions().contains(Constants.FACEBOOK_PERMISSION_FOR_POST)) {
            LoginManager manager = LoginManager.getInstance();
            manager.logInWithPublishPermissions(mView.getActivity(), Arrays.asList(Constants.FACEBOOK_PERMISSION_FOR_POST));
            manager.registerCallback(mCallbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            publishPost();
                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            mView.showMessage(exception.getLocalizedMessage());
                        }
                    });
        } else {
            publishPost();
        }
    }

    private void publishPost() {
        ShareDialog shareDialog = new ShareDialog(mView.getActivity());
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(mView.getActivity().getString(R.string.link_for_post)))
                .setContentDescription(mView.getActivity().getString(R.string.post))
                .build();
        shareDialog.show(content);
    }

    private void saveProfile(final String name, final String email, final String birthday, String photoUrl) {
        mModel.saveProfile(Constants.SOC_NETWORK_FACEBOOK, name, email, birthday, photoUrl, mAccessToken.getToken(),
                new SocNetworkAccountContract.Model.OnSaveCallback() {
                    @Override
                    public void onSuccess() {
                        mView.updateTextInfo(name, email, birthday);
                        mView.updatePicture(mModel.getProfileImageUrl(Constants.SOC_NETWORK_FACEBOOK));
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        mView.showMessage(error.getLocalizedMessage());
                    }
                });
    }
}