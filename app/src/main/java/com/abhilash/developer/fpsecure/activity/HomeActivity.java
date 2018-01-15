package com.abhilash.developer.fpsecure.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhilash.developer.fpsecure.R;
import com.abhilash.developer.fpsecure.Utils;
import com.abhilash.developer.fpsecure.fragment.ReceivedDocumentsFragment;
import com.abhilash.developer.fpsecure.fragment.SentDocumentsFragment;
import com.abhilash.developer.fpsecure.retrofit.ApiClient;
import com.abhilash.developer.fpsecure.retrofit.SendDocumentResponse;
import com.abhilash.developer.fpsecure.retrofit.UserExistenceRequest;
import com.abhilash.developer.fpsecure.retrofit.UserExistenceResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean userAvailable=false;
    private String to;
    private String from;
    private String description;
    private AlertDialog dialog;
    private static final int AUTHENTICATION_REQUEST_CODE=1;
    private static final int DOCUMENT_REQUEST_CODE=2;
    private static final int PERMISSION_REQUEST_CODE=3;
    private Uri attachmentUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkPermissions();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View v=inflater.inflate(R.layout.document_alert_layout,null);
                final TextView txtMobileStatus=(TextView)v.findViewById(R.id.txtMobileStatus);
                final EditText txtMobile=(EditText)v.findViewById(R.id.txtMobile);
                final Button btnMobileStatus=(Button)v.findViewById(R.id.btnMobileStatus);
                final EditText txtDescription=(EditText)v.findViewById(R.id.txtDescription);
                final Button btnSend=(Button)v.findViewById(R.id.btnSend);
                final LinearLayout attachmentLayout=v.findViewById(R.id.attachmentLayout);
                AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);


                txtMobile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        txtMobileStatus.setText("Click on Check");
                        txtMobileStatus.setTextColor(Color.GRAY);
                        userAvailable=false;
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                btnMobileStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(txtMobile.getText().toString().length()>4){
                            if(Utils.isInternetAvailable(HomeActivity.this,view)){
                                Call<UserExistenceResponse> call= ApiClient.getClient().checkUserExistence(new UserExistenceRequest(txtMobile.getText().toString()));
                                call.enqueue(new Callback<UserExistenceResponse>() {
                                    @Override
                                    public void onResponse(Call<UserExistenceResponse> call, final Response<UserExistenceResponse> response) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(response.body().isSuccess()){
                                                    txtMobileStatus.setText(response.body().getData());
                                                    txtMobileStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark,getTheme()));
                                                    userAvailable=true;
                                                }else{
                                                    if(response.code()==209){
                                                        userAvailable=false;
                                                        txtMobileStatus.setText(response.body().getData());
                                                        txtMobileStatus.setTextColor(Color.RED);
                                                    }else{
                                                        Toast.makeText(HomeActivity.this,response.body().getData(),Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<UserExistenceResponse> call, Throwable t) {
                                        Log.d("fuckecure","Error: "+t.toString());
                                        Toast.makeText(HomeActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else{
                            Toast.makeText(HomeActivity.this, "Please enter a valid mobile number.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!userAvailable){
                            Toast.makeText(HomeActivity.this, "Please enter a valid mobile number and click check.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(txtDescription.getText().toString().isEmpty()){
                            Toast.makeText(HomeActivity.this, "Please enter some text.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(Utils.isInternetAvailable(HomeActivity.this,btnSend)){
                            to=txtMobile.getText().toString();
                            from=getSharedPreferences(Utils.USER_PREF,MODE_PRIVATE).getString(Utils.USER_MOBILE,"");
                            description=txtDescription.getText().toString();
                            Intent intent=new Intent(HomeActivity.this,FingerPrintActivity.class);
                            startActivityForResult(intent,AUTHENTICATION_REQUEST_CODE);
                            dialog.dismiss();
                        }
                    }
                });

                attachmentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(Intent.ACTION_PICK);
                        startActivityForResult(intent,DOCUMENT_REQUEST_CODE);
                    }
                });

                builder.setTitle("Send Document");
                builder.setView(v);
                builder.setNegativeButton("Cancel", null);
                dialog=builder.create();
                dialog.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try{
            if(getIntent().getStringExtra(Utils.DOCUMENT_TYPE).equalsIgnoreCase(Utils.TYPE_SENT_DOCUMENT)){
                navigationView.getMenu().performIdentifierAction(R.id.sentDocs,0);
            }else if(getIntent().getStringExtra(Utils.DOCUMENT_TYPE).equalsIgnoreCase(Utils.TYPE_RECEIVED_DOCUMENT)){
                navigationView.getMenu().performIdentifierAction(R.id.receivedDocs,1);
            }
        }catch (Exception e){
            navigationView.getMenu().performIdentifierAction(R.id.sentDocs,0);
        }
    }

    private void checkPermissions() {
        String[] permissions={
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        if(
                ContextCompat.checkSelfPermission(HomeActivity.this,permissions[0])!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomeActivity.this,permissions,PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Utils.showAppCloseDialog(HomeActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Hang on!");
                builder.setMessage("Are you sure? You want to Logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getSharedPreferences(Utils.USER_PREF,MODE_PRIVATE)
                                .edit()
                                .remove(Utils.USER_NAME)
                                .remove(Utils.USER_MOBILE)
                                .remove(Utils.USER_DEVICE_TOKEN)
                                .apply();
                        Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No",null);
                builder.show();
                break;
            case R.id.sentDocs:
                getSupportActionBar().setSubtitle("Sent Documents");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new SentDocumentsFragment()).commit();
                break;
            case R.id.receivedDocs:
                getSupportActionBar().setSubtitle("Received Documents");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new ReceivedDocumentsFragment()).commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==AUTHENTICATION_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){
                final ProgressDialog progressDialog=new ProgressDialog(HomeActivity.this);
                progressDialog.setTitle("Sending Document");
                progressDialog.setMessage("please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                File file=new File(attachmentUri.toString());
                Log.d("fuck","File : "+file);

                RequestBody requestFile=RequestBody.create(
                        MediaType.parse(getContentResolver().getType(attachmentUri)),
                        file
                );


                MultipartBody.Part body=MultipartBody.Part.createFormData("attachment",file.getName(),requestFile);

                RequestBody requestTo=RequestBody.create(MultipartBody.FORM,to);
                RequestBody requestFrom=RequestBody.create(MultipartBody.FORM,from);
                RequestBody requestDescription=RequestBody.create(MultipartBody.FORM,description);

                Call<SendDocumentResponse> call=ApiClient.getClient().sendDocument(requestDescription,requestTo,requestFrom,body);
                call.enqueue(new Callback<SendDocumentResponse>() {
                    @Override
                    public void onResponse(Call<SendDocumentResponse> call, final Response<SendDocumentResponse> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(HomeActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<SendDocumentResponse> call, Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
            }else{
                Snackbar.make(findViewById(R.id.fab),"Cannot send a document without authentication.",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(Color.RED)
                        .show();
            }
        }else if(requestCode==DOCUMENT_REQUEST_CODE){
            Log.d("fuck","Got data: "+data.getData().getPath());
            attachmentUri=data.getData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_REQUEST_CODE){
            if((grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(HomeActivity.this,"Yeah!! All permissions granted",Toast.LENGTH_LONG).show();
            }else{
                finish();
                Toast.makeText(this, "Please give all the permissions before using this app.", Toast.LENGTH_LONG).show();
            }
        }
    }
}