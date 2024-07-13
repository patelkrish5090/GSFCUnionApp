package com.example.gsfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gsfc.LoginActivity;
import com.example.gsfc.R;
import com.example.gsfc.MainActivity;
import com.example.gsfc.ConnectivityUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (ConnectivityUtils.isNetworkConnected(this)) {
            // If network is connected, proceed to the main activity
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If network is not connected, show a toast and finish the activity
            Toast.makeText(this, "No internet connection. Please connect to the internet and try again.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
