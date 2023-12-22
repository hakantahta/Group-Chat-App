package com.example.groupchatapp.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupchatapp.interfaces.ContactsListener;
import com.example.groupchatapp.models.Contacts;
import com.example.groupchatapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    Context context;
    List<Contacts> contactsList;
    private ContactsListener listener;

    public ContactsAdapter() {
    }

    public ContactsAdapter(Context context, List<Contacts> contactsList, ContactsListener listener) {
        this.context = context;
        this.contactsList = contactsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Contacts contacts = contactsList.get(position);
        myViewHolder.contactsName.setText(contacts.getUserName());
        myViewHolder.contactsNumber.setText(contacts.getUserPhoneNumber());

        if (contacts.getUserPhoneNumber() != null) {
            Picasso.get().load(contacts.getUserPhoto()).into(myViewHolder.contactsImage);
        } else {
            myViewHolder.contactsImage.setImageResource(R.mipmap.ic_launcher_round);
        }

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClicked(contactsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView contactsImage;
        TextView contactsName, contactsNumber;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            contactsImage = itemView.findViewById(R.id.contactCardImage);
            contactsName = itemView.findViewById(R.id.contactCardName);
            contactsNumber = itemView.findViewById(R.id.contactCardPhone);
            cardView = itemView.findViewById(R.id.contactCardItem);
        }
    }
}
