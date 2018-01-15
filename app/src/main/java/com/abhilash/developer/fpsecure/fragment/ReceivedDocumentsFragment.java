package com.abhilash.developer.fpsecure.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abhilash.developer.fpsecure.R;
import com.abhilash.developer.fpsecure.Utils;
import com.abhilash.developer.fpsecure.adapter.ReceivedDocumentsAdapter;
import com.abhilash.developer.fpsecure.retrofit.ApiClient;
import com.abhilash.developer.fpsecure.retrofit.ReceivedDocumentsRequest;
import com.abhilash.developer.fpsecure.retrofit.ReceivedDocumentsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReceivedDocumentsFragment extends Fragment {
    private TextView txtStatus;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;

    public ReceivedDocumentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_received_documents, container, false);
        initializeViews();
        getReceivedDocuments();
        return view;
    }

    private void initializeViews() {
        txtStatus=(TextView)view.findViewById(R.id.txtStatus);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReceivedDocuments();
            }
        });
    }

    private void getReceivedDocuments(){
        if(Utils.isInternetAvailable(getActivity(),view)){
            final ProgressDialog progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Fetching documents");
            progressDialog.setMessage("please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Call<ReceivedDocumentsResponse> call= ApiClient.getClient().getReceivedDocuments(new ReceivedDocumentsRequest(
                    getActivity().getSharedPreferences(Utils.USER_PREF, Context.MODE_PRIVATE).getString(Utils.USER_MOBILE,"")
            ));

            call.enqueue(new Callback<ReceivedDocumentsResponse>() {
                @Override
                public void onResponse(Call<ReceivedDocumentsResponse> call, final Response<ReceivedDocumentsResponse> response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if(swipeRefreshLayout.isRefreshing()){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            ReceivedDocumentsAdapter receivedDocumentsAdapter=new ReceivedDocumentsAdapter(getActivity(),response.body().getData());
                            recyclerView.setAdapter(receivedDocumentsAdapter);

                            if(response.body().getData().size()==0){
                                txtStatus.setVisibility(View.VISIBLE);
                            }else{
                                txtStatus.setVisibility(View.GONE);
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<ReceivedDocumentsResponse> call, Throwable t) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if(swipeRefreshLayout.isRefreshing()){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            Toast.makeText(getActivity(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getReceivedDocuments();
    }
}
