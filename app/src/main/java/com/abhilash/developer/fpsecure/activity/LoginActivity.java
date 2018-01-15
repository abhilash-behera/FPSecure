package com.abhilash.developer.fpsecure.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abhilash.developer.fpsecure.R;
import com.abhilash.developer.fpsecure.Utils;
import com.abhilash.developer.fpsecure.retrofit.ApiClient;
import com.abhilash.developer.fpsecure.retrofit.LoginRequest;
import com.abhilash.developer.fpsecure.retrofit.LoginResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout inputMobile;
    private TextInputLayout inputPassword;
    private EditText txtMobile;
    private EditText txtPassword;
    private TextView txtSignup;
    private TextView txtForgot;
    private Button btnLogin;
    private ImageView imgIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();

        if(getSharedPreferences(Utils.USER_PREF,MODE_PRIVATE).contains(Utils.USER_MOBILE)){
            Intent intent=new Intent(LoginActivity.this,FingerPrintActivity.class);
            startActivityForResult(intent,1);
        }
    }

    private void initializeViews(){
        inputMobile=(TextInputLayout)findViewById(R.id.inputMobile);
        inputPassword=(TextInputLayout)findViewById(R.id.inputPassword);
        txtMobile=(EditText)findViewById(R.id.txtMobile);
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        txtSignup=(TextView)findViewById(R.id.txtSignup);
        txtForgot=(TextView)findViewById(R.id.txtForgot);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        imgIcon=(ImageView)findViewById(R.id.imgIcon);

        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSharedPreferences(Utils.USER_PREF,MODE_PRIVATE).contains(Utils.USER_MOBILE)){
                    Intent intent=new Intent(LoginActivity.this,FingerPrintActivity.class);
                    startActivityForResult(intent,1);
                }
            }
        });

        txtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputMobile.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputPassword.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtMobile.getText().toString().isEmpty()||txtMobile.getText().toString().length()<4){
                    inputMobile.setError("Enter a valid mobile number");
                    return;
                }

                if(txtPassword.getText().toString().isEmpty()){
                    inputPassword.setError("Enter Password");
                    return;
                }

                login();
            }
        });

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("abhilash","clicked");
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Authenticating");
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<LoginResponse> call= ApiClient.getClient().login(new LoginRequest(txtMobile.getText().toString(),txtPassword.getText().toString()));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                if(response.code()==208){
                    Toast.makeText(LoginActivity.this, "Mobile number does not exist.", Toast.LENGTH_LONG).show();
                }else if(response.code()==209){
                    Toast.makeText(LoginActivity.this, "Invalie Username or Password.", Toast.LENGTH_LONG).show();
                }else{
                    SharedPreferences sharedPreferences=getSharedPreferences(Utils.USER_PREF,MODE_PRIVATE);
                    sharedPreferences.edit()
                            .putString(Utils.USER_NAME,response.body().getData().getName())
                            .putString(Utils.USER_MOBILE,response.body().getData().getMobile())
                            .apply();
                    String token=FirebaseInstanceId.getInstance().getToken();
                    if(token.compareTo(sharedPreferences.getString(Utils.USER_DEVICE_TOKEN,""))!=0){
                        sharedPreferences.edit().putString(Utils.USER_DEVICE_TOKEN,token).apply();
                    }
                    if(sharedPreferences.getString(Utils.USER_DEVICE_TOKEN,"").compareTo(response.body().getData().getDevice_token())!=0){
                        Utils.updateToken(LoginActivity.this,token);
                    }

                    Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("fpsecure","Error in login: "+t.toString());
                Toast.makeText(LoginActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Utils.showAppCloseDialog(LoginActivity.this);
    }
}
