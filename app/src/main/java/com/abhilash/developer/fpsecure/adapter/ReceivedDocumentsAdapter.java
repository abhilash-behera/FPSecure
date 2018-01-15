package com.abhilash.developer.fpsecure.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abhilash.developer.fpsecure.R;
import com.abhilash.developer.fpsecure.Utils;
import com.abhilash.developer.fpsecure.activity.DocumentViewActivity;
import com.abhilash.developer.fpsecure.retrofit.ReceivedDocument;

import java.util.ArrayList;

/**
 * Created by Abhilash on 19-09-2017
 */

public class ReceivedDocumentsAdapter extends RecyclerView.Adapter<ReceivedDocumentsAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<ReceivedDocument> receivedDocuments;

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtStatus;
        public TextView txtFrom;
        public TextView txtDescription;
        public RelativeLayout rootView;

        public MyViewHolder(View view){
            super(view);
            txtStatus=(TextView)view.findViewById(R.id.txtStatus);
            txtFrom=(TextView)view.findViewById(R.id.txtFrom);
            txtDescription=(TextView)view.findViewById(R.id.txtDescription);
            rootView=(RelativeLayout)view.findViewById(R.id.rootView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.received_docs_row_view,parent,false);
        return new MyViewHolder(itemView);
    }

    public ReceivedDocumentsAdapter(Context context,ArrayList<ReceivedDocument> receivedDocuments){
        this.context=context;
        this.receivedDocuments=receivedDocuments;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ReceivedDocument receivedDocument=receivedDocuments.get(position);
        holder.txtStatus.setText(receivedDocument.getStatus());
        if(receivedDocument.getStatus().equalsIgnoreCase("Pending")){
            holder.txtStatus.setBackgroundColor(context.getColor(R.color.colorPending));
        }else if(receivedDocument.getStatus().equalsIgnoreCase("Approved")){
            holder.txtStatus.setBackgroundColor(context.getColor(R.color.colorApproved));
        }else if(receivedDocument.getStatus().equalsIgnoreCase("Rejected")){
            holder.txtStatus.setBackgroundColor(context.getColor(R.color.colorRejected));
        }

        holder.txtFrom.setText(receivedDocument.getFrom());
        holder.txtDescription.setText(receivedDocument.getDescription());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DocumentViewActivity.class);
                intent.putExtra(Utils.DOCUMENT_TYPE,Utils.TYPE_RECEIVED_DOCUMENT);
                intent.putExtra(Utils.DOCUMENT,receivedDocument);
                ((Activity)context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receivedDocuments.size();
    }
}
