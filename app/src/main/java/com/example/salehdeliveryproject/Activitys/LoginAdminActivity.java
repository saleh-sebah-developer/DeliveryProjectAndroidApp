package com.example.salehdeliveryproject.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.salehdeliveryproject.databinding.ActivityLoginAdminBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginAdminActivity extends AppCompatActivity {
    //ActivityLoginUserBinding binding;
    ActivityLoginAdminBinding binding;
    public static final String ADMIN_COLLECTION = "Admin";
    public static final String EMAILUSERCURRENT = "emailUserCurrent";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private SharedPreferences sp;
    private String et_email;
    private String et_password;
    private int userOrAdmin = 0;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.setTitle("Login Admin Screen");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        //------------------------------------------------------------------------------------------

        // Get Email and Password From Register Screen
        Intent intent = getIntent();
        String name = intent.getStringExtra(RegisterAdminActivity.EMAIL_KEY);
        binding.loginAdminEtEmail.setText(name);
        String password = intent.getStringExtra(RegisterAdminActivity.PASSWORD_KEY);
        binding.loginAdminEtPassword.setText(password);

        // Go To Register Screen
        binding.loginAdminTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegisterAdminActivity.class);
                startActivity(intent);
            }
        });


        //Login As User
        binding.loginAdminBtnLoginAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_email = binding.loginAdminEtEmail.getText().toString();
                if (TextUtils.isEmpty(et_email)) {
                    binding.loginAdminEtEmail.setError("Please Enter E-mail");
                    binding.loginAdminEtEmail.requestFocus();
                    return;
                }
                et_password = binding.loginAdminEtPassword.getText().toString();
                if (TextUtils.isEmpty(et_password)) {
                    binding.loginAdminEtPassword.setError("Please Enter Password");
                    binding.loginAdminEtPassword.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(et_email).matches()) {
                    binding.loginAdminEtEmail.setError("Invalid Email");
                    binding.loginAdminEtEmail.setFocusable(true);
                } else {
                        // progress bar
                    loginUser(et_email, et_password);
                }
                //----------------------------------------------------------------------------------
                //----------------------------------------------------------------------------------
                /*
                db.collection(RegisterActivity.Entitys_COLLECTION).whereEqualTo("email", et_email).whereEqualTo("password", et_password).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                            Toast.makeText(LoginUserActivity.this, "Login As User", Toast.LENGTH_SHORT).show();
//                            Entity e = queryDocumentSnapshots.getDocuments().get(0).toObject(Entity.class);
                            userOrAdmin = 0;
                            editor.putInt("userOrAdmin", userOrAdmin);
                            editor.putString(EMAILUSERCURRENT, et_email);
                            editor.commit();
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginUserActivity.this, "E-mail Or Password Wrong **User**", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginUserActivity.this, "Error ! FB", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
*/
                //-----------------------------------------------------------------------------------------



          /*
                //Login As Admin
                binding.loginBtnLoginAsAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_email = binding.loginEtEmail.getText().toString();
                        if (TextUtils.isEmpty(et_email)) {
                            binding.loginEtEmail.setError("Please Enter E-mail");
                            return;
                        }
                        et_password = binding.loginEtPassword.getText().toString();
                        if (TextUtils.isEmpty(et_password)) {
                            binding.loginEtPassword.setError("Please Enter Password");
                            return;
                        }
                        db.collection(ADMIN_COLLECTION).whereEqualTo("email", et_email).whereEqualTo("password", et_password).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                                    Toast.makeText(LoginAdminActivity.this, "Login As Admin", Toast.LENGTH_SHORT).show();
                                    userOrAdmin = 1;
                                    editor.putInt("userOrAdmin", userOrAdmin);
                                    editor.commit();
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginAdminActivity.this, "E-mail Or Password Wrong **Admin**", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginAdminActivity.this, "Error ! FB", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
          */

            }



        });
    }


    private void loginUser(final String et_email, String et_password) {

        mAuth.signInWithEmailAndPassword(et_email, et_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            userOrAdmin = 0;
                            editor.putInt("userOrAdmin", userOrAdmin);
                            editor.putString(EMAILUSERCURRENT, et_email);
                            boolean save =false;
                            if(binding.loginAdminChBoxSave.isChecked())
                                save=true;
                                editor.putBoolean("chBox_save", save);
                                editor.commit();

                            startActivity(new Intent(LoginAdminActivity.this, MainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginAdminActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginAdminActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sp.getBoolean("chBox_save", false))
            checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }
    }

}
