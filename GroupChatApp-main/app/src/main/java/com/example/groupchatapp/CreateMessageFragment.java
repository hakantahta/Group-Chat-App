package com.example.groupchatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.groupchatapp.adapters.MessageAdapter;
import com.example.groupchatapp.models.MessageData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CreateMessageFragment extends Fragment {

    Button CreateMessage;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    TextInputEditText MessageTitle, MessageContent;

    RecyclerView recyclerView;
    ArrayList<MessageData> messageDataArrayList;
    MessageAdapter messageAdapter;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_message, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        CreateMessage = view.findViewById(R.id.createMessageButton);
        MessageTitle = view.findViewById(R.id.messageTitle);
        MessageContent = view.findViewById(R.id.messageContent);

        recyclerView = view.findViewById(R.id.messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        messageDataArrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageDataArrayList, getContext());
        recyclerView.setAdapter(messageAdapter);

        CreateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(MessageTitle.getText().toString()) || TextUtils.isEmpty(MessageContent.getText().toString())) {
                    Toast.makeText(getActivity(), "Lütfen Boş Alan Bırakmayınız!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userId = firebaseUser.getUid();

                MessageData messageData = new MessageData(userId, MessageTitle.getText().toString(), MessageContent.getText().toString());

                firebaseFirestore.collection("messages")
                        .add(messageData)
                        .addOnSuccessListener(val -> {
                            Toast.makeText(getActivity(), "Mesajınız Başarıyla Oluştruldu", Toast.LENGTH_SHORT).show();
                            messageDataArrayList.clear();
                            MessageTitle.setText("");
                            MessageContent.setText("");
                            LoadDataProcess();
                        });
            }
        });

        LoadDataProcess();

        return view;

    }

    private void LoadDataProcess() {
        firebaseFirestore.collection("messages")
                .whereEqualTo("userId", String.valueOf(firebaseUser.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            messageDataArrayList.clear();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                MessageData messageData = queryDocumentSnapshot.toObject(MessageData.class);
                                messageDataArrayList.add(messageData);
                            }
                            messageAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}