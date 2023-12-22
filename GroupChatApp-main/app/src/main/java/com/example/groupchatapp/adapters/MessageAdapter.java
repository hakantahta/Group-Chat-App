package com.example.groupchatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupchatapp.models.MessageData;
import com.example.groupchatapp.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private ArrayList<MessageData> messageDataArrayList;
    private Context context;

    public MessageAdapter(ArrayList<MessageData> messageDataArrayList, Context context) {
        this.messageDataArrayList = messageDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.messagetitle.setText(messageDataArrayList.get(position).getMessageName());
        holder.messagedesc.setText(messageDataArrayList.get(position).getMessageContent());
    }

    @Override
    public int getItemCount() {
        return messageDataArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView messagetitle,messagedesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            messagetitle = itemView.findViewById(R.id.messageCardTitle);
            messagedesc = itemView.findViewById(R.id.messageCardContent);
        }
    }

}