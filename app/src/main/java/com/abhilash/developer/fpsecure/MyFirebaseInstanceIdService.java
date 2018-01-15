package com.abhilash.developer.fpsecure;


import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.abhilash.developer.fpsecure.retrofit.ApiClient;
import com.abhilash.developer.fpsecure.retrofit.UpdateTokenRequest;
import com.abhilash.developer.fpsecure.retrofit.UpdateTokenResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Abhilash on 17-09-2017
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private SharedPreferences sharedPreferences;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("fpsecure", "Refreshed token: " + refreshedToken);

        sharedPreferences=getSharedPreferences(Utils.USER_PREF,MODE_PRIVATE);

        sharedPreferences.edit().putString(Utils.USER_DEVICE_TOKEN,refreshedToken).apply();
        if(sharedPreferences.contains(Utils.USER_MOBILE)){
            sendRegistrationToServer(refreshedToken);
        }
    }

    private void sendRegistrationToServer(String token){
        Utils.updateToken(this,token);
    }
}
