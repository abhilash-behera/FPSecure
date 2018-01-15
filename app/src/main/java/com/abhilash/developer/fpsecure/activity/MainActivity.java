package com.abhilash.developer.fpsecure.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abhilash.developer.fpsecure.R;
import com.abhilash.developer.fpsecure.Utils;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textView);
        textView.setText("Welcome "+getSharedPreferences(Utils.USER_PREF,MODE_PRIVATE).getString(Utils.USER_NAME,""));
    }

    @Override
    public void onBackPressed() {
        Utils.showAppCloseDialog(MainActivity.this);
    }
}
