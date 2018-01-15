package com.abhilash.developer.fpsecure.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abhilash.developer.fpsecure.R;
import com.abhilash.developer.fpsecure.Utils;
import com.abhilash.developer.fpsecure.retrofit.ApiClient;
import com.abhilash.developer.fpsecure.retrofit.ApproveDocumentRequest;
import com.abhilash.developer.fpsecure.retrofit.ApproveDocumentResponse;
import com.abhilash.developer.fpsecure.retrofit.ReceivedDocument;
import com.abhilash.developer.fpsecure.retrofit.RejectDocumentRequest;
import com.abhilash.developer.fpsecure.retrofit.RejectDocumentResponse;
import com.abhilash.developer.fpsecure.retrofit.SentDocument;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentViewActivity extends AppCompatActivity {
    private TextView txtStatus;
    private TextView txtTo;
    private TextView txtFrom;
    private TextView txtDescription;
    private Button btnApprove;
    private Button btnReject;
    private ReceivedDocument receivedDocument;
    private SentDocument sentDocument;
    private String documentType;
    private static int APPROVE_REQUEST_CODE=1;
    private static int REJECT_REQUEST_CODE=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        documentType=getIntent().getStringExtra(Utils.DOCUMENT_TYPE);
        initializeViews();
    }

    private void initializeViews() {
        txtStatus=(TextView)findViewById(R.id.txtStatus);
        txtTo=(TextView)findViewById(R.id.txtTo);
        txtFrom=(TextView)findViewById(R.id.txtFrom);
        txtDescription=(TextView)findViewById(R.id.txtDescription);
        btnApprove=(Button)findViewById(R.id.btnApprove);
        btnReject=(Button)findViewById(R.id.btnReject);

        if(documentType.equalsIgnoreCase(Utils.TYPE_RECEIVED_DOCUMENT)){
            getSupportActionBar().setTitle("Received Document");
            receivedDocument=(ReceivedDocument)getIntent().getSerializableExtra(Utils.DOCUMENT);
            txtStatus.setText(receivedDocument.getStatus());
            txtTo.setText(receivedDocument.getTo());
            txtFrom.setText(receivedDocument.getFrom());
            txtDescription.setText(receivedDocument.getDescription());
            if(receivedDocument.getStatus().equalsIgnoreCase("Pending")){
                txtStatus.setBackgroundColor(getColor(R.color.colorPending));
                btnApprove.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
                btnApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(DocumentViewActivity.this,FingerPrintActivity.class);
                        startActivityForResult(intent,APPROVE_REQUEST_CODE);
                    }
                });
                btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(DocumentViewActivity.this,FingerPrintActivity.class);
                        startActivityForResult(intent,REJECT_REQUEST_CODE);
                    }
                });
            }else if(receivedDocument.getStatus().equalsIgnoreCase("Approved")){
                btnApprove.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                txtStatus.setBackgroundColor(getColor(R.color.colorApproved));
            }else if(receivedDocument.getStatus().equalsIgnoreCase("Rejected")){
                btnApprove.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                txtStatus.setBackgroundColor(getColor(R.color.colorRejected));
            }
        }else{
            getSupportActionBar().setTitle("Sent Document");
            btnApprove.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
            sentDocument=(SentDocument)getIntent().getSerializableExtra(Utils.DOCUMENT);
            txtStatus.setText(sentDocument.getStatus());
            txtTo.setText(sentDocument.getTo());
            txtFrom.setText(sentDocument.getFrom());
            txtDescription.setText(sentDocument.getDescription());
            if(sentDocument.getStatus().equalsIgnoreCase("Pending")){
                txtStatus.setBackgroundColor(getColor(R.color.colorPending));
            }else if(sentDocument.getStatus().equalsIgnoreCase("Rejected")){
                txtStatus.setBackgroundColor(getColor(R.color.colorRejected));
            }else if(sentDocument.getStatus().equalsIgnoreCase("Approved")){
                txtStatus.setBackgroundColor(getColor(R.color.colorApproved));
            }
        }
    }

    private void approve(String to, String from,String id) {
        if(Utils.isInternetAvailable(DocumentViewActivity.this,findViewById(R.id.txtStatus))){
            final ProgressDialog progressDialog=new ProgressDialog(DocumentViewActivity.this);
            progressDialog.setTitle("Approving Document");
            progressDialog.setMessage("please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Call<ApproveDocumentResponse> call= ApiClient.getClient().approveDocument(new ApproveDocumentRequest(to,from,id));
            call.enqueue(new Callback<ApproveDocumentResponse>() {
                @Override
                public void onResponse(Call<ApproveDocumentResponse> call, final Response<ApproveDocumentResponse> response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if(response.body().isSuccess()){
                                finish();
                            }
                            Toast.makeText(DocumentViewActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(Call<ApproveDocumentResponse> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(DocumentViewActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void reject(String to,String from, String id){
        if(Utils.isInternetAvailable(DocumentViewActivity.this,findViewById(R.id.txtStatus))){
            final ProgressDialog progressDialog=new ProgressDialog(DocumentViewActivity.this);
            progressDialog.setTitle("Rejecting Document");
            progressDialog.setMessage("please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Call<RejectDocumentResponse> call= ApiClient.getClient().rejectDocument(new RejectDocumentRequest(to,from,id));
            call.enqueue(new Callback<RejectDocumentResponse>() {
                @Override
                public void onResponse(Call<RejectDocumentResponse> call, final Response<RejectDocumentResponse> response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if(response.body().isSuccess()){
                                finish();
                            }
                            Toast.makeText(DocumentViewActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(Call<RejectDocumentResponse> call, Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(DocumentViewActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==APPROVE_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                approve(receivedDocument.getTo(),receivedDocument.getFrom(),receivedDocument.getId());
            }else{
                Snackbar.make(findViewById(R.id.btnApprove),"Authentication failed!",Snackbar.LENGTH_LONG).show();
            }
        }else if(requestCode==REJECT_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                reject(receivedDocument.getTo(),receivedDocument.getFrom(),receivedDocument.getId());
            }else{
                Snackbar.make(findViewById(R.id.btnApprove),"Authentication failed!",Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
