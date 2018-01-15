package com.abhilash.developer.fpsecure;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.abhilash.developer.fpsecure.retrofit.ApiClient;
import com.abhilash.developer.fpsecure.retrofit.UpdateTokenRequest;
import com.abhilash.developer.fpsecure.retrofit.UpdateTokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Abhilash on 17-09-2017
 */

public class Utils {
    public static final String USER_PREF="fp_user_shared_pref";
    public static final String USER_NAME="fp_user_name";
    public static final String USER_MOBILE="fp_user_mobile";
    public static final String USER_DEVICE_TOKEN="fp_user_device_token";
    public static final String DOCUMENT_TYPE="document_type";
    public static final String DOCUMENT="document";
    public static final String TYPE_RECEIVED_DOCUMENT="received";
    public static final String TYPE_SENT_DOCUMENT="sent";

    public static boolean isInternetAvailable(Context context,View view){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected()){
            return true;
        }else{
            Snackbar snackbar=Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
    }

    public static void updateToken(final Context context, String token){
        SharedPreferences sharedPreferences=context.getSharedPreferences(USER_PREF,Context.MODE_PRIVATE);
        Call<UpdateTokenResponse> call= ApiClient.getClient().updateToken(new UpdateTokenRequest(sharedPreferences.getString(Utils.USER_MOBILE,""),token));
        call.enqueue(new Callback<UpdateTokenResponse>() {
            @Override
            public void onResponse(Call<UpdateTokenResponse> call, Response<UpdateTokenResponse> response) {
                if(response.body().isSuccess()){
                    Toast.makeText(context,"Device Token Updated",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, response.body().getData(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateTokenResponse> call, Throwable t) {
                Log.d("fpsecure","Device token not updated: "+t.toString());
                Toast.makeText(context, "Device token not updated.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showAppCloseDialog(final Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Hang on!");
        builder.setMessage("Do you want to close this app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("No",null);
        builder.show();
    }
}
