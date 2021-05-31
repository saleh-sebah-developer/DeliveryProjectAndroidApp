package com.example.salehdeliveryproject.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.modles.Entity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Toolbar toolbar2;
    private SharedPreferences sp;
    private ImageView nav_header_iv_logo;
    private TextView nav_header_tv_name, nav_header_tv_email;
    private NavigationView navigationView;
    private View navHeader;
    private FirebaseFirestore db;
    private StorageReference storage;
    private FirebaseAuth mAuth;    // declare_auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //--------
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        nav_header_tv_name = navHeader.findViewById(R.id.nav_header_tv_name);
        nav_header_tv_email = navHeader.findViewById(R.id.nav_header_tv_email);
        nav_header_iv_logo = navHeader.findViewById(R.id.nav_header_iv_logo);



        // fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddNewOrderActivity.class);
                intent.putExtra("oid", -1);
                startActivity(intent);
            }
        });

        // load nav menu header data
        loadNavHeader();

        //???????????????????????????????
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getInt("userOrAdmin", 0) == 0) {   // USer
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_main, R.id.nav_addOrder, R.id.nav_myInformation, R.id.nav_aboutApp, R.id.nav_share, R.id.nav_logout)
                    .setDrawerLayout(drawer)
                    .build();
            Toast.makeText(this, "USer", Toast.LENGTH_SHORT).show();
        } else if (sp.getInt("userOrAdmin", 0) == 1) { // Admin
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_main, R.id.nav_addOrder, R.id.nav_myInformation, R.id.nav_aboutApp, R.id.nav_share, R.id.nav_logout, R.id.nav_addItem)    // ????
                    .setDrawerLayout(drawer)
                    .build();
            Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show();

        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // need to be updated as you navigate between destinations.
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                if (destination.getId() == R.id.nav_logout) {
                    toolbar.setVisibility(View.GONE);
                    toolbar2.setVisibility(View.VISIBLE);
                    toolbar2.setTitle("Login Screen");
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar2.setVisibility(View.GONE);
                }

                int menuId = destination.getId();

                switch (menuId) {
                    case R.id.nav_main:
                        fab.show();
                        break;
                    case R.id.nav_myInformation:
                        fab.hide();
                        break;
                    case R.id.nav_addOrder:
                        fab.hide();
                        // بدي احط الاكشن بار الخاص بعملية الاضافة وليس الخاص ب main نافقيشن بار!!
                        break;
                    case R.id.nav_aboutApp:
                        fab.hide();
                        break;
                    case R.id.nav_logout:
                        fab.hide();
                        mAuth.signOut();
                        Intent intent = new Intent(getBaseContext(), LoginAdminActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_addItem:
                        fab.hide();
                        break;
                    default:
                        fab.show();
                        break;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }

    private void loadNavHeader() {
        getDataFromFirebaseENTITYS_COLLECTION();
        /*
        // name, email
        nav_header_tv_name.setText("Saleh Mohammed");
        nav_header_tv_email.setText("sale@gmail.com");

        // loading header background image
        Picasso.get()
                .load(R.drawable.logo_)
                .resize(96, 96)
                .centerCrop()
                .into(nav_header_iv_logo);
*/
    }

    private void getDataFromFirebaseENTITYS_COLLECTION() {
        db.collection(RegisterAdminActivity.Entitys_COLLECTION).whereEqualTo("email", sp.getString(LoginAdminActivity.EMAILUSERCURRENT, "")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    Entity entity1 = new Entity();
                    for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        Entity entity = q.toObject(Entity.class);
                        entity1 = entity;
                    }
                    populateEntityInNavHeader(entity1);
                }
            }
        });
    }

    private void populateEntityInNavHeader(Entity entity1) {
        // if
        if (sp.getInt("userOrAdmin", 0) == 0) {  //USer
            nav_header_tv_name.setText(entity1.getTitle());
            nav_header_tv_email.setText(entity1.getEmail());
            Picasso.get()
                    .load(entity1.getUrl_logo())
                    .resize(96, 96)
                    .centerCrop()
                    .into(nav_header_iv_logo);
        } else if (sp.getInt("userOrAdmin", 0) == 1) {
            nav_header_tv_name.setText("Your Name");
            nav_header_tv_email.setText("Your Eamil @gmail.com");
            Picasso.get()
                    .load(R.drawable.your_logo_black)
                    .resize(96, 96)
                    .centerCrop()
                    .into(nav_header_iv_logo);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//
//        switch (item.getItemId()) {
//            case R.id.nav_main:
//                fab.show();
//                break;
//            case R.id.nav_myInformation:
//                fab.hide();
//                break;
//            case R.id.nav_addOrder:
//                fab.hide();
//                // بدي احط الاكشن بار الخاص بعملية الاضافة وليس الخاص ب main نافقيشن بار!!
//                //  setSupportActionBar(R.style.AddNewOrderTheme);
//                // setSupportActionBar(binding.detailsToolbar);
//                break;
//            case R.id.nav_aboutApp :
//                fab.hide();
//                break;
//            case R.id.nav_logout :
//                Toast.makeText(getBaseContext(), "LogOut", Toast.LENGTH_SHORT).show();
//                Intent intent =new Intent(getBaseContext(),LoginUserActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
//            default:
//                fab.show();
//                break;
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}