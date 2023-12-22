package com.example.groupchatapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupchatapp.adapters.HomeGroupAdapter;
import com.example.groupchatapp.adapters.HomeMessageAdapter;
import com.example.groupchatapp.interfaces.GroupAddMemberListener;
import com.example.groupchatapp.interfaces.MessageListener;
import com.example.groupchatapp.models.ContactsToGroup;
import com.example.groupchatapp.models.GroupModels;
import com.example.groupchatapp.models.MessageData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SendMessageFragment extends Fragment implements GroupAddMemberListener, MessageListener {

    RecyclerView recyclerView, secondRecyclerView;
    Button SendMessage;
    TextView SelectedGroup, SelectedMessage;
    ArrayList<GroupModels> groupModelsArrayList;
    ArrayList<MessageData> messageDataArrayList;
    HomeMessageAdapter homeMessageAdapter;
    HomeGroupAdapter homeGroupAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    String SelectedGroupId = null, SelectedGroupName = null, SelectedMessageContent = null;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);

        SelectedGroup = view.findViewById(R.id.sendMessageSelectedGroupName);
        SelectedMessage = view.findViewById(R.id.sendMessageSelectedMessage);
        SendMessage = view.findViewById(R.id.sendMessageButton);


        SendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    SendSmsMessage();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 100);
                }
            }
        });

        recyclerView = view.findViewById(R.id.sendMessageGroupList);
        secondRecyclerView = view.findViewById(R.id.sendMessageMessages);

        LoadDataProcess();
        LoadMessagesProcess();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(recyclerView.HORIZONTAL);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager secondLinearLayoutManager = new LinearLayoutManager(getActivity());
        secondLinearLayoutManager.setOrientation(secondRecyclerView.HORIZONTAL);
        secondLinearLayoutManager.setReverseLayout(false);
        secondRecyclerView.setLayoutManager(secondLinearLayoutManager);


        groupModelsArrayList = new ArrayList<>();
        messageDataArrayList = new ArrayList<>();

        homeGroupAdapter = new HomeGroupAdapter(groupModelsArrayList, getContext(), this::onItemClicked);
        homeMessageAdapter = new HomeMessageAdapter(messageDataArrayList, getContext(), this::onItemClicked);

        recyclerView.setAdapter(homeGroupAdapter);
        secondRecyclerView.setAdapter(homeMessageAdapter);


        return view;
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SendSmsMessage();
        } else {
            Toast.makeText(getContext(), "Maalesef İzin Alınamadı!", Toast.LENGTH_SHORT).show();
        }
    }


    private void SendSmsMessage() {
        if (SelectedMessageContent != null && SelectedGroupId != null) {
            SmsManager smsManager = SmsManager.getDefault();

            firebaseFirestore.collection("contactsGroup")
                    .whereEqualTo("groupId", String.valueOf(SelectedGroupId))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isComplete()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    ContactsToGroup contactsToGroup = doc.toObject(ContactsToGroup.class);
                                    String PhoneNumber = contactsToGroup.getContactsPhone();
                                    smsManager.sendTextMessage(PhoneNumber, null, SelectedMessageContent, null, null);
                                }
                                Toast.makeText(getContext(), "Mesajınız Başarıyla Gönderildi.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Lütfen Mesaj ve Grup Seçiminizi Yapınız!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClicked(GroupModels groupModels) {
        SelectedGroupId = groupModels.getGroupId();
        SelectedGroupName = groupModels.getGroupName();
        Toast.makeText(getContext(), "Seçilen Grup ID: " + SelectedGroupId, Toast.LENGTH_SHORT).show();
        SelectedGroup.setText("Seçilen Grup: " + String.valueOf(groupModels.getGroupName()));
    }

    @Override
    public void onItemClicked(MessageData messageData) {
        SelectedMessageContent = String.valueOf(messageData.getMessageContent());
        Toast.makeText(getContext(), "Seçilen Mesaj " + SelectedMessage, Toast.LENGTH_SHORT).show();
        SelectedMessage.setText("Seçilen Mesaj : " + String.valueOf(messageData.getMessageName()));
    }

    private void LoadMessagesProcess() {
        firebaseFirestore.collection("messages")
                .whereEqualTo("userId", String.valueOf(firebaseUser.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                MessageData messageData = queryDocumentSnapshot.toObject(MessageData.class);
                                messageDataArrayList.add(messageData);
                            }
                            homeMessageAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void LoadDataProcess() {
        firebaseFirestore.collection("groups")
                .whereEqualTo("userId", firebaseUser.getUid().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                GroupModels groupModels = queryDocumentSnapshot.toObject(GroupModels.class);
                                groupModelsArrayList.add(groupModels);
                            }
                            homeGroupAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}