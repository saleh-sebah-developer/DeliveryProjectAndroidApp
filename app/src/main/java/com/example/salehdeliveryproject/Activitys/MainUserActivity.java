package com.example.salehdeliveryproject.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.salehdeliveryproject.Adapters.AdapterEntity;
import com.example.salehdeliveryproject.Adapters.AdapterOrder;
import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.databinding.ActivityMainUserBinding;
import com.example.salehdeliveryproject.modles.Entity;
import com.example.salehdeliveryproject.modles.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainUserActivity extends AppCompatActivity {
    ActivityMainUserBinding binding;
    public static final String Entitys_COLLECTION = "Entitys";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.setTitle("Home");
        db = FirebaseFirestore.getInstance();
        getDataFromFirebaseEntitys_COLLECTION();         // get data

    }
    private void getDataFromFirebaseEntitys_COLLECTION() {
        db.collection(Entitys_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Entity> entities = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Entity ent = q.toObject(Entity.class);
                    entities.add(ent);
                }
                populateEntityInList(entities);
            }
        });
    }
    private void populateEntityInList(ArrayList<Entity> entities) {
        AdapterEntity adapterEntity = new AdapterEntity(entities, new AdapterEntity.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(final Entity entity) {
                if(entity.getId() != -1){
                    db.collection(Entitys_COLLECTION).whereEqualTo("id", -1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                                Toast.makeText(MainUserActivity.this, "Restaurants", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), CategoriesActivity.class);
                                intent.putExtra("entid",entity.getId());
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainUserActivity.this, "Error Restaurants", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    db.collection(Entitys_COLLECTION).whereEqualTo("id", entity.getId() ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                                Toast.makeText(MainUserActivity.this, "Other Shops", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), OtherShopsActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainUserActivity.this, "Error Other Shops", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }





//                        .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainUserActivity.this, "Other Shops", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getBaseContext(), CategoriesActivity.class);
//                        startActivity(intent);
//                    }
//                });


/*
                Intent intent = new Intent(getBaseContext(), CategoriesActivity.class);
                intent.putExtra("entid", entity.getId());
                startActivity(intent);

 */
            }
        });
        binding.recyclerView.setAdapter(adapterEntity);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.recyclerView.setHasFixedSize(true);
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
                Intent intent = new Intent(getBaseContext(),CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}