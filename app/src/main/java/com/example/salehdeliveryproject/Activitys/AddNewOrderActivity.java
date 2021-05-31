package com.example.salehdeliveryproject.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.salehdeliveryproject.Adapters.AdapterItemSp;
import com.example.salehdeliveryproject.Adapters.AdapterOrderItem;
import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.databinding.ActivityAddNewOrderBinding;
import com.example.salehdeliveryproject.modles.Item;
import com.example.salehdeliveryproject.modles.Order;
import com.example.salehdeliveryproject.modles.OrderItem;
import com.example.salehdeliveryproject.ui.ViewAllOrders.MainFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.graphics.Color.BLACK;

public class AddNewOrderActivity extends AppCompatActivity {
    Order currentOrder;
    public static final int MAPS_CUSTOMER_REQ_CODE = 3;
    private static String Item_COLLECTION = "Items";
    public static final String ORDERS_COLLECTION = "Orders";
    private String orderId;
    ActivityAddNewOrderBinding binding;
    FirebaseFirestore db;
    ArrayList<OrderItem> orderItems = new ArrayList<>();
    double lat, log;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.setTitle("Add New Order");
        db = FirebaseFirestore.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getInt("userOrAdmin", 0) == 0) {
           // binding.addNewOrderRadioGroup.setVisibility(View.GONE);
            binding.addNewOrderRadioGroup.setVisibility(View.VISIBLE);
        } else if (sp.getInt("userOrAdmin", 0) == 1) {
            binding.addNewOrderRadioGroup.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(binding.detailsToolbar);
        getDataFromFirebaseITEM_CCOLLECTION();   // Method --> Fill the spinner through the firebase
        Intent intent = getIntent();   // Receive from click at recycler view in main
        orderId = intent.getStringExtra("oid");
        if (orderId == null) {    // new Order
            enableFields();
            clearFields();
        } else {   // show Order
            disableFields();
            db.collection(ORDERS_COLLECTION).whereEqualTo("id", orderId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                        Order oi = queryDocumentSnapshots.getDocuments().get(0).toObject(Order.class);
                        if (oi != null) {
                            fillOrderItemToFields(oi);
                        }
                    }
                }
            });
        }

        binding.addNewOrderTvImageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivityForResult(intent, MAPS_CUSTOMER_REQ_CODE);
            }
        });

        binding.addNewOrderBtnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = (binding.customAddNewItemEtNumItem.getText().toString());
                if (TextUtils.isEmpty(number)) {
                    binding.customAddNewItemEtNumItem.setError("please Enter Number Item");
                    return;
                }
                Item selectedItem = (Item) binding.addNewOrderSpinnerItem.getSelectedItem();
                OrderItem oi = new OrderItem(1, selectedItem, Integer.parseInt(number));
                orderItems.add(oi);

                populateOrderItemsIntoRV();
            }
        });
        // The save button is canceled (Cancel)
        // Replaced with a button in the menu
        /*
        binding.addNewOrderBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameOwnerOrder = binding.addNewOrderEtNameOwnerOrder.getText().toString();
                if (TextUtils.isEmpty(nameOwnerOrder)) {
                    binding.addNewOrderEtNameOwnerOrder.setError("please Enter Name Owner Order");
                    return;
                }
                String phone = binding.addNewOrderEtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    binding.addNewOrderEtPhone.setError("please Enter Phone");
                    return;
                }
                String address = binding.addNewOrderEtAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    binding.addNewOrderEtAddress.setError("please Enter Address");
                    return;
                }

                // get lat and lng from map if the user(resturant) long click in the location clite at map
                // you shoud difined virable string in the class Order and in firebase

                String statusRb_stu = "";
                int statusRb = 0;
                if (binding.addNewOrderRbPending.isChecked()) {
                    statusRb = -1;
                    statusRb_stu = "pending";
                } else if (binding.addNewOrderRbConnecting.isChecked()) {
                    statusRb = 0;
                    statusRb_stu = "Connecting";
                } else if (binding.addNewOrderRbReceived.isChecked()) {
                    statusRb = 1;
                    statusRb_stu = "Received";
                }
                // lat and log at customer ????
                // بدي ابعت الحالة ك نص مشان يبان انو في اي حالة
                Order order = new Order(1, 1, orderItems, statusRb_stu, nameOwnerOrder, phone, address, 0, 0);
                db.collection("Orders").add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddNewOrderActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        //  View RV Fragment
                        Intent intent1 = new Intent(getBaseContext(), MainFragment.class);
                        startActivity(intent1);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewOrderActivity.this, "Added Failure", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        */
    }

    private void populateOrderItemsIntoRV() {
        AdapterOrderItem adapterOrderItem;
        if (currentOrder == null)
            adapterOrderItem = new AdapterOrderItem(orderItems);
        else
            adapterOrderItem = new AdapterOrderItem(currentOrder.getOrderItems());
        binding.addNewOrderRv.setAdapter(adapterOrderItem);
        binding.addNewOrderRv.setHasFixedSize(true);
        binding.addNewOrderRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getDataFromFirebaseITEM_CCOLLECTION() {
        db.collection(Item_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Item> items = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Item item = q.toObject(Item.class);
                    items.add(item);
                }
                populateItemsInList(items);
            }
        });
    }

    private void populateItemsInList(ArrayList<Item> items) {
        AdapterItemSp adapterItemSp = new AdapterItemSp(items);
        binding.addNewOrderSpinnerItem.setAdapter(adapterItemSp);
    }

    private void fillOrderItemToFields(Order order) {
        currentOrder = order;
        binding.addNewOrderEtNameOwnerOrder.setText(order.getName_owner_order());
        binding.addNewOrderEtPhone.setText(order.getNumber());
        binding.addNewOrderEtAddress.setText(order.getAddress());
        populateOrderItemsIntoRV();
        if (order.getStatus().equals("pending")) {
            binding.addNewOrderRbPending.setChecked(true);
        } else if (order.getStatus().equals("Connecting")) {
            binding.addNewOrderRbConnecting.setChecked(true);
        } else if (order.getStatus().equals("Received")) {
            binding.addNewOrderRbReceived.setChecked(true);
        }
        orderItems =currentOrder.getOrderItems();
        populateOrderItemsIntoRV();
    }

    private void disableFields() {
        binding.detailsIv.setEnabled(false);
        binding.addNewOrderEtNameOwnerOrder.setEnabled(false);
        binding.addNewOrderEtPhone.setEnabled(false);
        binding.addNewOrderEtAddress.setEnabled(false);
        binding.addNewOrderSpinnerItem.setEnabled(false);
        binding.customAddNewItemEtNumItem.setEnabled(false);
        binding.addNewOrderBtnAddItem.setEnabled(false);
        binding.addNewOrderBtnSave.setEnabled(false);
        binding.addNewOrderRbReceived.setEnabled(false);
        binding.addNewOrderRbConnecting.setEnabled(false);
        binding.addNewOrderRbPending.setEnabled(false);
        binding.addNewOrderTvImageLocation.setEnabled(false);
    }
    private void enableFields() {
        binding.detailsIv.setEnabled(true);
        binding.addNewOrderEtNameOwnerOrder.setEnabled(true);
        binding.addNewOrderEtPhone.setEnabled(true);
        binding.addNewOrderEtAddress.setEnabled(true);
        binding.addNewOrderSpinnerItem.setEnabled(true);
        binding.customAddNewItemEtNumItem.setEnabled(true);
        binding.addNewOrderBtnAddItem.setEnabled(true);
        binding.addNewOrderBtnSave.setEnabled(true);
        binding.addNewOrderRbReceived.setEnabled(true);
        binding.addNewOrderRbConnecting.setEnabled(true);
        binding.addNewOrderRbPending.setEnabled(true);
        binding.addNewOrderTvImageLocation.setEnabled(true);
    }
    private void clearFields() {
        binding.detailsIv.setImageURI(null);
        binding.addNewOrderEtNameOwnerOrder.setText("");
        binding.addNewOrderEtPhone.setText("");
        binding.addNewOrderEtAddress.setText("");
        binding.customAddNewItemEtNumItem.setText("");
        binding.addNewOrderRbReceived.setChecked(false);
        binding.addNewOrderRbConnecting.setChecked(false);
        binding.addNewOrderRbPending.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_details_menu, menu);
        MenuItem save = menu.findItem(R.id.details_menu_save);
        MenuItem edit = menu.findItem(R.id.details_menu_edit);
        MenuItem delete = menu.findItem(R.id.details_menu_delete);
        if (orderId == null) {  // add
            save.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);
        } else {                 // show
            save.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details_menu_save:
                String nameOwnerOrder = binding.addNewOrderEtNameOwnerOrder.getText().toString();
                if (TextUtils.isEmpty(nameOwnerOrder)) {
                    binding.addNewOrderEtNameOwnerOrder.setError("please Enter Name Owner Order");
                    break;
                }
                String phone = binding.addNewOrderEtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    binding.addNewOrderEtPhone.setError("please Enter Phone");
                    break;
                }
                String address = binding.addNewOrderEtAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    binding.addNewOrderEtAddress.setError("please Enter Address");
                    break;
                }
                String statusRb_stu = "";
                // int statusRb = 0;
                if (binding.addNewOrderRbPending.isChecked()) {
                    statusRb_stu = "pending";
                } else if (binding.addNewOrderRbConnecting.isChecked()) {
                    statusRb_stu = "Connecting";
                } else if (binding.addNewOrderRbReceived.isChecked()) {
                    statusRb_stu = "Received";
                }

                if (orderId == null) {
                    String docID = db.collection("Orders").document().getId();
                    Order order = new Order(lat, log, docID, 1, orderItems, statusRb_stu, nameOwnerOrder, phone, address,0,null);
                    db.collection("Orders").document(docID).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getBaseContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewOrderActivity.this, "Added Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    currentOrder = new Order(lat, log, currentOrder.getId(), 1, orderItems, statusRb_stu, nameOwnerOrder, phone, address,0,null);
                    db.collection("Orders").document(currentOrder.getId()).set(currentOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getBaseContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                         }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Updated Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;

            case R.id.details_menu_edit:
                enableFields();
                MenuItem save = binding.detailsToolbar.getMenu().findItem(R.id.details_menu_save);
                MenuItem edit = binding.detailsToolbar.getMenu().findItem(R.id.details_menu_edit);
                MenuItem delete = binding.detailsToolbar.getMenu().findItem(R.id.details_menu_delete);
                delete.setVisible(false);
                edit.setVisible(false);
                save.setVisible(true);
                return true;

            case R.id.details_menu_delete:
                if (currentOrder != null) {
                    db.collection(ORDERS_COLLECTION).document(currentOrder.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddNewOrderActivity.this, "Delete Success ", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewOrderActivity.this, "Delete Failure ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAPS_CUSTOMER_REQ_CODE && resultCode == RESULT_OK && data != null) {
            lat = data.getDoubleExtra(MapsActivity.MAP_LAT_CUSTOMER_KEY, 0);
            log = data.getDoubleExtra(MapsActivity.MAP_LNG_CUSTOMER_KEY, 0);
        }
    }
}