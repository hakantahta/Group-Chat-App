package com.example.groupchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.groupchatapp.adapters.GroupAdapter;
import com.example.groupchatapp.models.GroupModels;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreateGroupFragment extends Fragment {
    Button SelectedImageButton, CreateGroup;
    ImageView PrevImageView;
    Uri imageUri;
    String link;
    TextInputEditText GroupName, GroupDescription;

    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    RecyclerView recyclerView;
    ArrayList<GroupModels> groupModelsArrayList;
    GroupAdapter groupAdapter;

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null && result.getData() != null) {
                        imageUri = result.getData().getData();
                        PrevImageView.setImageURI(imageUri);
                    }
                }
            });

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        SelectedImageButton = view.findViewById(R.id.selectImgButton);
        CreateGroup = view.findViewById(R.id.createGroupButton);

        PrevImageView = view.findViewById(R.id.prevImage);
        GroupName = view.findViewById(R.id.groupName);
        GroupDescription = view.findViewById(R.id.groupDescription);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.createGroupGroups);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        groupModelsArrayList = new ArrayList<>();
        groupAdapter = new GroupAdapter(groupModelsArrayList, getContext());
        recyclerView.setAdapter(groupAdapter);

        LoadDataProcess();
        SelectedImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImageProcess();
            }
        });

        CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (GroupName.getText().toString().isEmpty() || GroupDescription.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Lütfen tüm alanları doldurunuz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imageUri == null) {
                    Toast.makeText(getActivity(), "Lütfen bir resim seçiniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                UploadImageProcess();

            }
        });


        return view;
    }


    private void SelectImageProcess() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityIntent.launch(intent);
    }

    private void UploadImageProcess() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Fotoğraf Yükleniyor. Lütfen Bekleyiniz.");
        progressDialog.show();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss", Locale.ROOT);
        Date today = new Date();
        String fileName = simpleDateFormat.format(today);

        storageReference = FirebaseStorage.getInstance().getReference("resimler/" + fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        PrevImageView.setImageURI(null);
                        Toast.makeText(getActivity(), "Resim Başarıyla Yüklendi.", Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Resim Maalesef Yüklenemedi...", Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            String userId = firebaseUser.getUid();

                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String groupId = UUID.randomUUID().toString();
                                        GroupModels groupModels = new GroupModels(
                                                userId,
                                                GroupName.getText().toString(),
                                                GroupDescription.getText().toString(),
                                                task.getResult().toString(),
                                                groupId
                                        );

                                        firebaseFirestore.collection("groups")
                                                .add(groupModels)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        Toast.makeText(getActivity(), "Grup Başarıyla Oluşturuldu", Toast.LENGTH_SHORT).show();
                                                        GroupName.setText("");
                                                        GroupDescription.setText("");
                                                        groupModelsArrayList.clear();
                                                        LoadDataProcess();
                                                    }
                                                });
                                    }
                                }
                            });
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
                            groupAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}