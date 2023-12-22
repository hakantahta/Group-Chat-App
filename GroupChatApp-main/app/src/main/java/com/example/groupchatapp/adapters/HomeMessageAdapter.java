package com.example.groupchatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupchatapp.interfaces.MessageListener;
import com.example.groupchatapp.models.MessageData;
import com.example.groupchatapp.R;

import java.util.ArrayList;

public class HomeMessageAdapter extends RecyclerView.Adapter<HomeMessageAdapter.MyViewHolder> {

    private ArrayList<MessageData> messageDataArrayList;
    private Context context;
    private MessageListener listener;

    public HomeMessageAdapter(ArrayList<MessageData> messageDataArrayList, Context context, MessageListener listener) {
        this.messageDataArrayList = messageDataArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomeMessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_message_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMessageAdapter.MyViewHolder holder, int position) {
        holder.MessageName.setText(messageDataArrayList.get(position).getMessageName());
        holder.MessageContent.setText(messageDataArrayList.get(position).getMessageContent());

        holder.CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(messageDataArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageDataArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView MessageName, MessageContent;
        CardView CardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            CardView = itemView.findViewById(R.id.homeMessageCard);
            MessageName = itemView.findViewById(R.id.homeMessageCardTitle);
            MessageContent = itemView.findViewById(R.id.homeMessageCardContent);
        }
    }
}
