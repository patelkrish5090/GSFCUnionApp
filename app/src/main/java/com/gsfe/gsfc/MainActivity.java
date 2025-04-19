package com.gsfe.gsfc;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gsfe.gsfc.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    private DatabaseReference databaseReference;
    private String userId;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);

        // Initialize SharedPreferences
        prefManager = new PrefManager(this);

        // Check if the user has agreed to the terms
        if (!prefManager.isAgreed()) {
            showTermsDialog();
        } else {
            proceedWithApp();
        }
    }

    private void showTermsDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_terms_conditions);
        dialog.setCancelable(false);

        Button btnAgree = dialog.findViewById(R.id.btnAgree);
        Button btnDisagree = dialog.findViewById(R.id.btnDisagree);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setAgreed(true);
                dialog.dismiss();
                proceedWithApp();
            }
        });

        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the app if the user disagrees
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish(); // Close the app if the user closes the dialog
            }
        });

        dialog.show();
    }

    private void proceedWithApp() {
        // Initialize the main app UI and functionality
        SharedPreferences sharedPref = this.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        userId = sharedPref.getString("empid", "");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_votes, R.id.navigation_about, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}