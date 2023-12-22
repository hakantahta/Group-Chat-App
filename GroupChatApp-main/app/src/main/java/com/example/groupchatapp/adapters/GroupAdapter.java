package com.example.groupchatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupchatapp.R;
import com.example.groupchatapp.models.GroupModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolders> {

    private ArrayList<GroupModels> groupModelsArrayList;
    private Context context;

    public GroupAdapter(ArrayList<GroupModels> groupModelsArrayList, Context context) {
        this.groupModelsArrayList = groupModelsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolders(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolders myViewHolders, int position) {
        Picasso.get().load(groupModelsArrayList.get(position).getLink()).into(myViewHolders.GroupImage);
        myViewHolders.GroupName.setText(groupModelsArrayList.get(position).getGroupName());
        myViewHolders.GroupDescription.setText(groupModelsArrayList.get(position).getGroupDescription());
    }

    @Override
    public int getItemCount() {
        return groupModelsArrayList.size();
    }

    static class MyViewHolders extends RecyclerView.ViewHolder {
        ImageView GroupImage;
        TextView GroupName, GroupDescription;

        public MyViewHolders(@NonNull View view) {
            super(view);
            GroupImage = view.findViewById(R.id.group_img);
            GroupName = view.findViewById(R.id.groupItemName);
            GroupDescription = view.findViewById(R.id.groupItemDescription);

        }
    }
}
