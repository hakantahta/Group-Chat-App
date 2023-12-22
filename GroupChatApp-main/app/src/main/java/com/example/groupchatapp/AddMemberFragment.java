package com.example.groupchatapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupchatapp.adapters.AddMemberAdapter;
import com.example.groupchatapp.adapters.ContactsAdapter;
import com.example.groupchatapp.models.Contacts;
import com.example.groupchatapp.models.ContactsToGroup;
import com.example.groupchatapp.models.GroupModels;
import com.example.groupchatapp.interfaces.ContactsListener;
import com.example.groupchatapp.interfaces.GroupAddMemberListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class AddMemberFragment extends Fragment implements GroupAddMemberListener, ContactsListener {
    RecyclerView recyclerView, secondRecyclerView;
    ArrayList<GroupModels> groupModelsArrayList;
    AddMemberAdapter addMemberAdapter;
    TextView SelectedGroup;

    List<Contacts> contactsList;
    ContactsAdapter contactsAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    String SelectedGroupId = null, SelectedGroupName = null, SelectedPersonPhoneNumber = null;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        contactsList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(getContext(), contactsList, this::OnItemClicked);
        LoadDataProcess();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);

        SelectedGroup = view.findViewById(R.id.selectedGroupName);

        recyclerView = view.findViewById(R.id.groupList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(recyclerView.HORIZONTAL);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        secondRecyclerView = view.findViewById(R.id.persons);
        LinearLayoutManager secondLinearLayoutManager = new LinearLayoutManager(getActivity());
        secondLinearLayoutManager.setOrientation(secondRecyclerView.VERTICAL);
        secondLinearLayoutManager.setReverseLayout(false);
        secondRecyclerView.setLayoutManager(secondLinearLayoutManager);

        groupModelsArrayList = new ArrayList<>();
        addMemberAdapter = new AddMemberAdapter(groupModelsArrayList, getContext(), this::onItemClicked);
        recyclerView.setAdapter(addMemberAdapter);
        secondRecyclerView.setAdapter(contactsAdapter);

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (response.getPermissionName().equals(Manifest.permission.READ_CONTACTS)) {
                            getContacts();
                        }
                    }

                    private void getContacts() {
                        Cursor phones = getActivity().getApplicationContext().getContentResolver()
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

                        while (phones.moveToNext()) {
                            String UserName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String UserPhoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String UserPhoto = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                            Contacts contacts = new Contacts(UserName, UserPhoneNumber, UserPhoto);
                            contactsList.add(contacts);
                            contactsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "Maalesef İzin Alınamadı.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.cancelPermissionRequest();
                    }
                }).check();

        return view;
    }

    private void LoadDataProcess() {
        firebaseFirestore.collection("groups")
                .whereEqualTo("userId", firebaseUser.getUid().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            groupModelsArrayList.clear();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                GroupModels groupModels = queryDocumentSnapshot.toObject(GroupModels.class);
                                groupModelsArrayList.add(groupModels);
                                addMemberAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public void onItemClicked(GroupModels groupModels) {
        SelectedGroupName = groupModels.getGroupName();
        SelectedGroupId = groupModels.getGroupId();
        SelectedGroup.setText(String.valueOf("Seçilen Grup: " + SelectedGroupName));
    }

    @Override
    public void OnItemClicked(Contacts contacts) {
        SelectedPersonPhoneNumber = contacts.getUserPhoneNumber();

        if (SelectedPersonPhoneNumber == null) {
            Toast.makeText(getContext(), "Lütfen Listeden Bir Kişi Seçiniz!", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder Builder = new AlertDialog.Builder(getContext());
        Builder.setTitle("Gruba Üye Ekleme");
        Builder.setMessage(SelectedGroupName + " Adındaki Gruba" + SelectedPersonPhoneNumber + " Numaralı Kişi Eklenecek Onaylıyor Musunuz?");
        Builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userId = firebaseUser.getUid();

                ContactsToGroup contactsToGroup = new ContactsToGroup(userId, SelectedGroupId, SelectedPersonPhoneNumber);

                firebaseFirestore.collection("contactGroups")
                        .add(contactsToGroup)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(getContext(), "Seçilen Kişi Başarıyla Gruba Eklendi.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        Builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        Builder.show();
    }
}