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
import com.abhilash.developer.fpsecure.retrofit.SentDocument;

import java.util.ArrayList;

/**
 * Created by Abhilash on 19-09-2017
 */

public class SentDocumentsAdapter extends RecyclerView.Adapter<SentDocumentsAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<SentDocument> sentDocuments;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTo;
        public TextView txtStatus;
        public TextView txtDescription;
        public RelativeLayout rootView;

        public MyViewHolder(View view){
            super(view);
            txtTo=(TextView)view.findViewById(R.id.txtTo);
            txtStatus=(TextView)view.findViewById(R.id.txtStatus);
            txtDescription=(TextView)view.findViewById(R.id.txtDescription);
            rootView=(RelativeLayout)view.findViewById(R.id.rootView);
        }
    }

    public SentDocumentsAdapter(Context context,ArrayList<SentDocument> sentDocuments){
        this.context=context;
        this.sentDocuments=sentDocuments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.sent_docs_row_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SentDocument document=sentDocuments.get(position);
        holder.txtStatus.setText(document.getStatus());
        if(document.getStatus().equalsIgnoreCase("Pending")){
            holder.txtStatus.setBackgroundColor(context.getColor(R.color.colorPending));

        }else if(document.getStatus().equalsIgnoreCase("Approved")){
            holder.txtStatus.setBackgroundColor(context.getColor(R.color.colorApproved));
        }else if(document.getStatus().equalsIgnoreCase("Rejected")){
            holder.txtStatus.setBackgroundColor(context.getColor(R.color.colorRejected));
        }

        holder.txtTo.setText(document.getTo());
        holder.txtDescription.setText(document.getDescription());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DocumentViewActivity.class);
                intent.putExtra(Utils.DOCUMENT_TYPE,Utils.TYPE_SENT_DOCUMENT);
                intent.putExtra(Utils.DOCUMENT,document);
                ((Activity)context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sentDocuments.size();
    }
}
