package com.aleat0r.v_jettask.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.aleat0r.v_jettask.mvp.SocNetworkAccountContract;
import com.aleat0r.v_jettask.mvp.model.SocNetworkProfileModel;
import com.aleat0r.v_jettask.realm.SocNetworkProfile;
import com.aleat0r.v_jettask.utils.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


/**
 * Created by Aleksandr Kovalenko on 12.08.2016.
 */
public class GooglePlusAccountPresenter implements SocNetworkAccountContract.Presenter, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1234;

    private SocNetworkAccountContract.View mView;
    private SocNetworkAccountContract.Model mModel;

    private GoogleSignInAccount mSignInAccount;
    private GoogleApiClient mGoogleApiClient;
    private Person mPerson;

    private Context mContext;

    public GooglePlusAccountPresenter(Context context, SocNetworkAccountContract.View mView) {
        mContext = context;
        this.mView = mView;
        this.mModel = new SocNetworkProfileModel(context);
    }

    @Override
    public void onCreate() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage((AppCompatActivity) mContext, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        ((AppCompatActivity) mContext).startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mView.showMessage(connectionResult.toString());
        mView.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        mSignInAccount = result.getSignInAccount();
        if (result.isSuccess()) {
            Plus.PeopleApi.load(mGoogleApiClient, mSignInAccount.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                @Override
                public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
                    if (loadPeopleResult.getStatus().isSuccess()) {
                        mPerson = loadPeopleResult.getPersonBuffer().get(0);
                        saveProfile(mSignInAccount, mPerson);
                    } else {
                        showSavedProfile();
                        ;
                    }
                }
            });
        } else {
            showSavedProfile();
        }
    }

    private void showSavedProfile() {
        SocNetworkProfile profile = mModel.getSavedProfile(Constants.SOC_NETWORK_GOOGLE_PLUS);
        if (profile != null) {
            mView.updateTextInfo(profile.getName(), profile.getEmail(), profile.getBirthday());
            mView.updatePicture(mModel.getProfileImageUrl(Constants.SOC_NETWORK_GOOGLE_PLUS));
        } else {
            logOut();
        }
    }

    @Override
    public void logOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        mModel.deleteSavedProfile(Constants.SOC_NETWORK_GOOGLE_PLUS);
                        mView.finish();
                    }
                });
    }

    private void saveProfile(GoogleSignInAccount signInAccount, Person person) {
        String token = signInAccount.getIdToken();
        final String name = signInAccount.getDisplayName();
        final String email = signInAccount.getEmail();
        String profileImageUrl = signInAccount.getPhotoUrl().toString();
        final String birthday = person.getBirthday();

        mModel.saveProfile(Constants.SOC_NETWORK_GOOGLE_PLUS, name, email, birthday, profileImageUrl, token, new SocNetworkAccountContract.Model.OnSaveCallback() {
            @Override
            public void onSuccess() {
                mView.updateTextInfo(name, email, birthday);
                mView.updatePicture(mModel.getProfileImageUrl(Constants.SOC_NETWORK_GOOGLE_PLUS));
            }

            @Override
            public void onFailure(Throwable error) {
                mView.showMessage(error.getLocalizedMessage());
            }
        });
    }
}
