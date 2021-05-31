package com.example.salehdeliveryproject.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.salehdeliveryproject.Adapters.AdapterItemCart;
import com.example.salehdeliveryproject.databinding.ActivityCartBinding;
import com.example.salehdeliveryproject.modles.Order;
import com.example.salehdeliveryproject.modles.OrderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    FirebaseFirestore db;
    public static ArrayList<OrderItem> orderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.setTitle("Cart list");
        db = FirebaseFirestore.getInstance();
        getDataFromFirebaseORDERITEM_CART_COLLECTION();
        // test -----------------------------------
//        Intent intent = getIntent();
//        orderItems =  intent.getParcelableArrayListExtra("orderItems");
//        AdapterItemCart adapterItemCart = new AdapterItemCart((List<OrderItem>) orderItems, new AdapterItemCart.OnButtonCancelItemClickListener() {
//            @Override
//            public void onBtnItemClick(OrderItem orderItem) {
//
//            }
//        });
//        binding.cartRvItemShoppint.setAdapter(adapterItemCart);
//        binding.cartRvItemShoppint.setHasFixedSize(true);
//        binding.cartRvItemShoppint.setLayoutManager(new LinearLayoutManager(this));
        //-----------------------------


        binding.cartBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ConfirmOrderActivity.class);
                startActivity(intent);
            }
        });

    }


    // show order item in rv and if you delete any order item .
    private void getDataFromFirebaseORDERITEM_CART_COLLECTION() {
        db.collection(CategoriesActivity.ORDERITEM_CART_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                orderItems = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    OrderItem oi = q.toObject(OrderItem.class);
                    orderItems.add(oi);
                }
                populateOrderItemCartInList(orderItems);
            }
        });
    }

    private void populateOrderItemCartInList(ArrayList<OrderItem> orderItems) {
        AdapterItemCart adapterItemCart = new AdapterItemCart(orderItems, new AdapterItemCart.OnButtonCancelItemClickListener() {
            @Override
            public void onBtnItemClick(OrderItem orderItem) {
                // deleted order item from cart
                // i want defend String id to delete document //hint you shoud insert string id in order item class and deleted order .....
/*
                db.collection(CategoriesActivity.ORDERITEM_CART_COLLECTION).document("7gQkQbNvAjeHLlByDGy9").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CartActivity.this, "Delete Success ", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, "Delete Failure ", Toast.LENGTH_SHORT).show();
                    }
                });
*/
            }
        });

        binding.cartRvItemShoppint.setAdapter(adapterItemCart);
        binding.cartRvItemShoppint.setHasFixedSize(true);
        binding.cartRvItemShoppint.setLayoutManager(new LinearLayoutManager(this));
    }


}