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
import com.abhilash.developer.fpsecure.adapter.SentDocumentsAdapter;
import com.abhilash.developer.fpsecure.retrofit.ApiClient;
import com.abhilash.developer.fpsecure.retrofit.SentDocumentsRequest;
import com.abhilash.developer.fpsecure.retrofit.SentDocumentsResponse;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SentDocumentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtStatus;

    public SentDocumentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_sent_documents, container, false);
        initializeViews();
        getSentDocuments();
        return rootView;
    }

    private void initializeViews(){
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        txtStatus=(TextView)rootView.findViewById(R.id.txtStatus);
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSentDocuments();
            }
        });
    }

    private void getSentDocuments(){
        if(Utils.isInternetAvailable(getActivity(),rootView)){
            final ProgressDialog progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Fetching Documents");
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<SentDocumentsResponse> call= ApiClient.getClient().getSentDocuments(
                    new SentDocumentsRequest(getActivity()
                            .getSharedPreferences(Utils.USER_PREF, Context.MODE_PRIVATE)
                            .getString(Utils.USER_MOBILE,"")));
            call.enqueue(new Callback<SentDocumentsResponse>() {
                @Override
                public void onResponse(Call<SentDocumentsResponse> call, final Response<SentDocumentsResponse> response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if(swipeRefreshLayout.isRefreshing()){
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            if(response.body().getData().size()==0){
                                txtStatus.setVisibility(View.VISIBLE);
                            }else{
                                txtStatus.setVisibility(View.GONE);
                            }
                            SentDocumentsAdapter sentDocumentsAdapter=new SentDocumentsAdapter(getActivity(),response.body().getData());
                            recyclerView.setAdapter(sentDocumentsAdapter);

                        }
                    });
                }

                @Override
                public void onFailure(Call<SentDocumentsResponse> call, Throwable t) {
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
        getSentDocuments();
    }
}
