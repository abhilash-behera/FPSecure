package com.abhilash.developer.fpsecure.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abhilash.developer.fpsecure.R;
import com.abhilash.developer.fpsecure.Utils;
import com.abhilash.developer.fpsecure.retrofit.ApiClient;
import com.abhilash.developer.fpsecure.retrofit.SignUpRequest;
import com.abhilash.developer.fpsecure.retrofit.SignUpResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private TextInputLayout inputName;
    private TextInputLayout inputMobile;
    private TextInputLayout inputPassword;
    private TextInputLayout inputConfirmPassword;
    private EditText txtName;
    private EditText txtMobile;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViews();
    }

    private void initializeViews(){
        inputName=(TextInputLayout)findViewById(R.id.inputName);
        inputMobile=(TextInputLayout)findViewById(R.id.inputMobile);
        inputPassword=(TextInputLayout)findViewById(R.id.inputPassword);
        inputConfirmPassword=(TextInputLayout)findViewById(R.id.inputConfirmPassword);
        txtName=(EditText)findViewById(R.id.txtName);
        txtMobile=(EditText)findViewById(R.id.txtMobile);
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        txtConfirmPassword=(EditText)findViewById(R.id.txtConfirmPassword);
        btnSignUp =(Button)findViewById(R.id.btnSignup);

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputName.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                inputConfirmPassword.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputPassword.setError("");
                inputConfirmPassword.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtName.getText().toString().length()<3){
                    inputName.setError("Enter a valid name");
                    return;
                }

                if(txtMobile.getText().toString().length()<6){
                    inputMobile.setError("Enter a valid mobile number");
                    return;
                }

                if(txtPassword.getText().toString().length()==0){
                    inputPassword.setError("Please enter password");
                    return;
                }

                if(txtConfirmPassword.getText().toString().length()==0){
                    inputConfirmPassword.setError("Please re-enter the password");
                    return;
                }

                if(txtPassword.getText().toString().compareTo(txtConfirmPassword.getText().toString())!=0){
                    inputConfirmPassword.setError("Passwords do not match");
                    return;
                }

                if(Utils.isInternetAvailable(SignupActivity.this,btnSignUp)){
                    signUp();
                }

            }
        });
    }

    private void signUp(){
        final ProgressDialog progressDialog=new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Creting Account");
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String device_token="";
        device_token=getSharedPreferences(Utils.USER_PREF,MODE_PRIVATE).getString(Utils.USER_DEVICE_TOKEN,"");

        if(device_token.isEmpty()){
            Log.d("testing","Got new token: "+device_token);
            device_token= FirebaseInstanceId.getInstance().getToken();
            getSharedPreferences(Utils.USER_PREF,MODE_PRIVATE)
                    .edit()
                    .putString(Utils.USER_DEVICE_TOKEN,device_token)
                    .apply();
        }
        Call<SignUpResponse> call= ApiClient.getClient().signUp(new SignUpRequest(
                txtName.getText().toString(),
                txtMobile.getText().toString(),
                txtPassword.getText().toString(),
                device_token));
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                progressDialog.dismiss();
                if(response.body().isSuccess()){
                    Toast.makeText(SignupActivity.this, "Account created successfully.", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(SignupActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("fpsecure","Error occurred:"+t.toString());
                Toast.makeText(SignupActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}