package com.example.salehdeliveryproject.ui.ViewAllOrders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salehdeliveryproject.Activitys.AddNewOrderActivity;
import com.example.salehdeliveryproject.Activitys.MapsActivity;
import com.example.salehdeliveryproject.Adapters.AdapterOrder;
import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.modles.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    public static final String MAP_LAT_CUSTOMER_SHOW_KEY = "latshow";
    public static final String MAP_LOG_CUSTOMER_SHOW_KEY = "logshow";
    private MainViewModel mainViewModel;
    public static final String ORDERS_COLLECTION = "Orders";
    FirebaseFirestore db;
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        db= FirebaseFirestore.getInstance();
        View view =inflater.inflate(R.layout.fragment_view_orders, container, false);  // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.fragment_view_orders_rv);            // Inflate the layout for RecyclerView IN fragment
        getDataFromFirebaseORDERS_COLLECTION();         // get data
        mainViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return view;
    }

    private void getDataFromFirebaseORDERS_COLLECTION() {
        db.collection(ORDERS_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Order> orders = new ArrayList<>();
                for(QueryDocumentSnapshot q : queryDocumentSnapshots){
                    Order oi =q.toObject(Order.class);
                    orders.add(oi);
                }
                populateOrderInList(orders);
            }
        });
    }
    //????????????????????
    //????????????????????????????

    private void populateOrderInList(ArrayList<Order> orders) {
        AdapterOrder adapterOrder = new AdapterOrder(orders, new AdapterOrder.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                Intent intent = new Intent(getActivity(), AddNewOrderActivity.class);
                intent.putExtra("oid", order.getId());
                startActivity(intent);
            }
        }, new AdapterOrder.OnClickLocationListener() {
            @Override
            public void OnClickLocation(double lat, double lng) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra(MAP_LAT_CUSTOMER_SHOW_KEY,lat);
                intent.putExtra(MAP_LOG_CUSTOMER_SHOW_KEY,lng);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapterOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

}