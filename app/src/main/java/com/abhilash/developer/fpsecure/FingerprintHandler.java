package com.abhilash.developer.fpsecure;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Abhilash on 15-09-2017
 */

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;

    public FingerprintHandler(Context context){
        this.context=context;
    }

    public void startAuth(FingerprintManager manager,FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal=new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
            return;
        }
        manager.authenticate(cryptoObject,cancellationSignal,0,this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update(""+errString);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update(""+helpString);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        ((Activity)context).setResult(Activity.RESULT_OK);
        ((Activity)context).finish();
    }

    private void update(String s){
        TextView textView=(TextView)((Activity)context).findViewById(R.id.txtError);
        textView.setText(s);
        textView.setTextColor(Color.RED);
        textView.setVisibility(View.VISIBLE);
    }
}
