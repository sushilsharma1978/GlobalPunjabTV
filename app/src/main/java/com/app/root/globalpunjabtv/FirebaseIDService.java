package com.app.root.globalpunjabtv;


import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    SharedPreferences sp_token;
    SharedPreferences.Editor ed_token;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sp_token=getSharedPreferences("SaveTokenn",MODE_PRIVATE);
        ed_token=sp_token.edit();

        ed_token.putString("TokenFCM",refreshedToken);
        ed_token.commit();
        Log.d(TAG, "Refreshedtoken: " + refreshedToken);
        //Toast.makeText(this, ""+refreshedToken, Toast.LENGTH_SHORT).show();
       // storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPreference.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}