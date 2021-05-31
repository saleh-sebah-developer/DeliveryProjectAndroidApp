package com.example.salehdeliveryproject.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.salehdeliveryproject.Adapters.AdapterCategories;
import com.example.salehdeliveryproject.Adapters.AdapterCategorySp;
import com.example.salehdeliveryproject.Adapters.AdapterItem;
import com.example.salehdeliveryproject.Adapters.AdapterItemSp;
import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.databinding.ActivityAddNewItemBinding;
import com.example.salehdeliveryproject.modles.Categories;
import com.example.salehdeliveryproject.modles.Item;
import com.example.salehdeliveryproject.modles.Order;
import com.example.salehdeliveryproject.modles.OrderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddNewItem extends AppCompatActivity {
    private static final String ITEMS_COLLECTION = "Items";
    private static final String Categories_COLLECTION = "Categories";
    ActivityAddNewItemBinding binding;
    private int PIK_IMAGE_ITEM_REQ_CODE = 6;
    Uri images_uri;
    FirebaseFirestore db;
    StorageReference storage;
    DatabaseReference databaseReference;
    ArrayList<Item> items = new ArrayList<>() ;
    //Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.setTitle("Add New Item");
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        getDataFromFirebaseCATEGRY_CCOLLECTION();   // Method --> Fill the spinner through the firebase

        // photo from gallery
        binding.addNewItemIvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                } else {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, PIK_IMAGE_ITEM_REQ_CODE);
            }
        });

        // Button Show
        binding.addNewItemBtnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addNewItemBtnShow.setEnabled(false);
                binding.addNewItemBtnAdd.setEnabled(false);
                binding.progressBar.setVisibility(View.VISIBLE);
                getDataFromFirebaseITEMS_COLLECTION();
            }
        });

        // Button Add
        binding.addNewItemBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addNewItemBtnShow.setEnabled(false);
                binding.addNewItemBtnAdd.setEnabled(false);
                binding.progressBar.setVisibility(View.VISIBLE);
                addItem();
            }
        });

    }


    private void getDataFromFirebaseCATEGRY_CCOLLECTION() {
        db.collection(Categories_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Categories> categs = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Categories cat = q.toObject(Categories.class);
                    categs.add(cat);
                }
                populateItemsInList(categs);
            }
        });
    }
    private void populateItemsInList(ArrayList<Categories> categs) {
        AdapterCategorySp adapterCategorySp = new AdapterCategorySp(categs);
        binding.addNewItemSpinnerCategoryItem.setAdapter(adapterCategorySp);
    }

    private void getDataFromFirebaseITEMS_COLLECTION() {
        db.collection(ITEMS_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null){
                    ArrayList<Item> items = new ArrayList<>();
                    for(QueryDocumentSnapshot q : queryDocumentSnapshots){
                        Item it =q.toObject(Item.class);
                        items.add(it);
                    }
                    populateItemInList(items);
                }
            }
        });
    }
    private void populateItemInList(ArrayList<Item> items) {
        AdapterItem adapterItem = new AdapterItem(items);
        binding.rvAllItem.setAdapter(adapterItem);
        binding.rvAllItem.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.rvAllItem.setHasFixedSize(true);
        binding.addNewItemBtnShow.setEnabled(true);
        binding.addNewItemBtnAdd.setEnabled(true);
        binding.progressBar.setVisibility(View.GONE);
    }

    private void addItem() {
        if (images_uri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference mountainImageRef = storageRef.child("imageItem/" + images_uri.getLastPathSegment());
            mountainImageRef.putFile(images_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mountainImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    addItemToFirebase(uri.toString());
                                }
                            });
                        }
                    });
        }
        else {
            Toast.makeText(this, "Pleas Enter Image Item", Toast.LENGTH_SHORT).show();
            binding.addNewItemBtnShow.setEnabled(true);
            binding.addNewItemBtnAdd.setEnabled(true);
            binding.progressBar.setVisibility(View.GONE);
        }
    }
    private void addItemToFirebase(String image) {
        String nameItem = binding.addNewItemEtNameItem.getText().toString();
        if (TextUtils.isEmpty(nameItem)) {
            binding.addNewItemEtNameItem.setError("please Enter Name  Item");
            return;
        }
        String priceItem = binding.addNewItemEtPrice.getText().toString();
        if (TextUtils.isEmpty(priceItem)) {
            binding.addNewItemEtPrice.setError("please Enter Price  Item");
            return;
        }
        String descriptionItem = binding.addNewItemEtDescription.getText().toString();

        //------------------
        Categories selectedCategory = (Categories) binding.addNewItemSpinnerCategoryItem.getSelectedItem();

        Item item = new Item(1,nameItem,Integer.parseInt(priceItem),image,descriptionItem,selectedCategory.getName());
        items.add(item);
        populateItemsIntoRV();
        db.collection(ITEMS_COLLECTION).add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddNewItem.this, "Added Item Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewItem.this, "Added Item Failure", Toast.LENGTH_SHORT).show();
            }
        });
        binding.addNewItemBtnShow.setEnabled(true);
        binding.addNewItemBtnAdd.setEnabled(true);
        binding.progressBar.setVisibility(View.GONE);
    }
    private void populateItemsIntoRV() {
        AdapterItem adapterItem = new AdapterItem(items);
        binding.rvAllItem.setAdapter(adapterItem);
        binding.rvAllItem.setHasFixedSize(true);
        binding.rvAllItem.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIK_IMAGE_ITEM_REQ_CODE && resultCode == RESULT_OK && data != null) {
            images_uri = data.getData();
            binding.addNewItemIvItem.setImageURI(images_uri);
        }
    }
}