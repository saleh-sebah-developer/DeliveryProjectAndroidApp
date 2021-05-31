package com.example.salehdeliveryproject.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.salehdeliveryproject.Adapters.AdapterCategories;
import com.example.salehdeliveryproject.Adapters.AdapterEntity;
import com.example.salehdeliveryproject.Adapters.AdapterItem;
import com.example.salehdeliveryproject.Adapters.AdapterItemShopping;
import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.databinding.ActivityCategoriesBinding;
import com.example.salehdeliveryproject.modles.Categories;
import com.example.salehdeliveryproject.modles.Entity;
import com.example.salehdeliveryproject.modles.Item;
import com.example.salehdeliveryproject.modles.Order;
import com.example.salehdeliveryproject.modles.OrderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {
    public static final String ORDERITEM_CART_COLLECTION = "orderItem";
    ActivityCategoriesBinding binding;
    public static final String Categories_COLLECTION = "Categories";
    public static final String Entitys_COLLECTION = "Entitys";
    private static final String ITEMS_COLLECTION = "Items";
    private FirebaseFirestore db;
    private int entityId;
    public static ArrayList<OrderItem> orderItems ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.setTitle("Categories");
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        entityId = intent.getIntExtra("entid", 0);
        loadIbformationEntity(entityId);                          // get data Entity
        getDataFromFirebaseCategories_COLLECTION();         // get data Categories

/*
        Categories cat = new Categories(5,"","All","");
        db.collection(Categories_COLLECTION).add(cat).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getBaseContext(), "Added successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Added Failure", Toast.LENGTH_SHORT).show();
            }
        });
*/

    }

    // items
    private void getDataFromFirebaseITEMS_COLLECTION(String name) {
        db.collection(ITEMS_COLLECTION).whereEqualTo("catg_id", name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    ArrayList<Item> items = new ArrayList<>();
                    for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        Item it = q.toObject(Item.class);
                        items.add(it);
                    }
                    populateItemInRV(items);
                }
            }
        });
    }
    private void populateItemInRV(ArrayList<Item> items) {
        //----- test --
        AdapterItemShopping adapterItemShopping = new AdapterItemShopping(items, new AdapterItemShopping.OnRecyclerViewItemClickListener() {
            @Override
            public void onBtnItemClick(Item item, int num) {
                // put order item at firebase (collection = "orderItem")
                OrderItem orderItem = new OrderItem(1, item, num);
                // test -------
//                orderItems.add(orderItem);
//                Intent intent = new Intent(getBaseContext(),CartActivity.class);
//                intent.putExtra("orderItems",orderItems);
//                startActivity(intent);
                //----------------------------------------
               // orderItems.add(orderItem);
              //  String docID = db.collection(ORDERITEM_CART_COLLECTION).document().getId();
                db.collection(ORDERITEM_CART_COLLECTION).add(orderItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getBaseContext(), "Added To Cart", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Added Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.categoriesRvItem.setAdapter(adapterItemShopping);
        binding.categoriesRvItem.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.categoriesRvItem.setHasFixedSize(true);

    }





    // Categories
    private void getDataFromFirebaseCategories_COLLECTION() {
        db.collection(Categories_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Categories> categoriess = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Categories catt = q.toObject(Categories.class);
                    categoriess.add(catt);
                }
                populateCategoriesInList(categoriess);
            }
        });
    }
    private void populateCategoriesInList(ArrayList<Categories> categoriess) {
        AdapterCategories adapterCategories = new AdapterCategories(categoriess, new AdapterCategories.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(Categories categories) {
                getDataFromFirebaseITEMS_COLLECTION(categories.getName());
            }
        });
        binding.categoriesRvCategory.setAdapter(adapterCategories);
        binding.categoriesRvCategory.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesRvCategory.setHasFixedSize(true);
    }



    private void loadIbformationEntity(int entityId) {
        db.collection(Entitys_COLLECTION).whereEqualTo("id", entityId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    Entity entity1 = new Entity();
                    for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        Entity entity = q.toObject(Entity.class);
                        entity1 = entity;
                    }
                    populateEntityInHeader(entity1);
                }
            }
        });
    }
    private void populateEntityInHeader(Entity entity1) {
        binding.categoriesTvTitle.setText(entity1.getTitle());
        Picasso.get()
                .load(entity1.getUrl_logo())
                .resize(96, 96)
                .centerCrop()
                .into(binding.categoriesIvEntity);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shoping_menu, menu);
        MenuItem cart = menu.findItem(R.id.shopping_menu_cart);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_menu_cart:
                Intent intent = new Intent(getBaseContext(), CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}