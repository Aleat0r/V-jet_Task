package com.aleat0r.v_jettask.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.aleat0r.v_jettask.mvp.SocNetworkAccountContract;
import com.aleat0r.v_jettask.mvp.model.SocNetworkProfileModel;
import com.aleat0r.v_jettask.realm.SocNetworkProfile;
import com.aleat0r.v_jettask.utils.Constants;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aleksandr Kovalenko on 13.08.2016.
 */
public class VkontakteAccountPresenter implements SocNetworkAccountContract.Presenter {

    private SocNetworkAccountContract.View mView;
    private SocNetworkAccountContract.Model mModel;

    private final String[] sMyScope = new String[]{VKScope.EMAIL};
    private VKAccessToken mVkAccessToken;

    private Context mContext;

    public VkontakteAccountPresenter(Context context, SocNetworkAccountContract.View mView) {
        mContext = context;
        this.mView = mView;
        this.mModel = new SocNetworkProfileModel(context);
    }

    @Override
    public void onCreate() {
        VKSdk.login((AppCompatActivity) mContext, sMyScope);
    }

    private void showSavedProfile() {
        SocNetworkProfile profile = mModel.getSavedProfile(Constants.SOC_NETWORK_VKONTAKTE);
        if (profile != null) {
            mView.updateTextInfo(profile.getName(), profile.getEmail(), profile.getBirthday());
            mView.updatePicture(mModel.getProfileImageUrl(Constants.SOC_NETWORK_VKONTAKTE));
        } else {
            logOut();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken vkAccessToken) {
                mVkAccessToken = vkAccessToken;
                getUserInfo();
            }

            @Override
            public void onError(VKError error) {
                showSavedProfile();
            }
        });
    }

    private void getUserInfo() {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, Constants.VKONTAKTE_FIElDS));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject user = response.json.getJSONArray("response").getJSONObject(0);

                    String firstName = "";
                    try {
                        firstName = user.getString("first_name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String lastName = "";
                    try {
                        lastName = user.getString("last_name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String bdate = null;
                    try {
                        bdate = user.getString("bdate");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String photo = null;
                    try {
                        photo = user.getString("photo_max");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    saveProfile(firstName + lastName, mVkAccessToken.email, bdate, photo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void logOut() {
        VKSdk.logout();
        mModel.deleteSavedProfile(Constants.SOC_NETWORK_VKONTAKTE);
        mView.finish();
    }

    private void saveProfile(final String name, final String email, final String birthday, String photoUrl) {
        mModel.saveProfile(Constants.SOC_NETWORK_VKONTAKTE, name, email, birthday, photoUrl, mVkAccessToken.accessToken,
                new SocNetworkAccountContract.Model.OnSaveCallback() {
                    @Override
                    public void onSuccess() {
                        mView.updateTextInfo(name, email, birthday);
                        mView.updatePicture(mModel.getProfileImageUrl(Constants.SOC_NETWORK_VKONTAKTE));
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        mView.showMessage(error.getLocalizedMessage());
                    }
                });
    }
}
