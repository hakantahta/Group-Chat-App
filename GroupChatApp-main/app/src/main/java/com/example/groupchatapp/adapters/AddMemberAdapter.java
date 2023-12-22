package com.example.groupchatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupchatapp.interfaces.GroupAddMemberListener;
import com.example.groupchatapp.models.GroupModels;
import com.example.groupchatapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddMemberAdapter extends RecyclerView.Adapter<AddMemberAdapter.MyViewHolder> {

    private ArrayList<GroupModels> groupModelsArrayList;
    private Context context;
    private GroupAddMemberListener listener;

    public AddMemberAdapter(ArrayList<GroupModels> groupModelsArrayList, Context context, GroupAddMemberListener listener) {
        this.groupModelsArrayList = groupModelsArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_member_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(groupModelsArrayList.get(position).getLink()).into(holder.groupImage);
        holder.groupname.setText(groupModelsArrayList.get(position).getGroupName());
        holder.groupdesc.setText(groupModelsArrayList.get(position).getGroupDescription());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(groupModelsArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupModelsArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView groupImage;
        TextView groupname,groupdesc;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            groupImage = itemView.findViewById(R.id.addMemberCardImage);
            groupname = itemView.findViewById(R.id.addMemberCardName);
            groupdesc = itemView.findViewById(R.id.addMemberCardDescription);
            cardView = itemView.findViewById(R.id.addMemberCardView);
        }
    }
}
