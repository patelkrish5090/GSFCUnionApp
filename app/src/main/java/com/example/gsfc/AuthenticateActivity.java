package com.example.gsfc;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AuthenticateActivity extends AppCompatActivity {

    private EditText otpBox1, otpBox2, otpBox3, otpBox4, otpBox5, otpBox6;
    private Button verifyOtpButton, resendOtpButton;
    private String verificationId;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String empid, password, phoneNumber, otp;
    private DatabaseReference employeeRef, specificemployeeRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_activty);

        Intent intent = getIntent();
        empid = intent.getStringExtra("empid");
        password = intent.getStringExtra("password");
        phoneNumber = intent.getStringExtra("phoneNumber");
        otp = intent.getStringExtra("otp");


        otpBox1 = findViewById(R.id.otp_box_1);
        otpBox2 = findViewById(R.id.otp_box_2);
        otpBox3 = findViewById(R.id.otp_box_3);
        otpBox4 = findViewById(R.id.otp_box_4);
        otpBox5 = findViewById(R.id.otp_box_5);
        otpBox6 = findViewById(R.id.otp_box_6);
        verifyOtpButton = findViewById(R.id.btn_verify_otp);
        resendOtpButton = findViewById(R.id.btn_resend_otp);
        progressBar = findViewById(R.id.progressBarAuthenticate);

        verificationId = getIntent().getStringExtra("verificationId");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        specificemployeeRef = database.getReference("employees").child(empid).child("password");


        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = otpBox1.getText().toString().trim() +
                        otpBox2.getText().toString().trim() +
                        otpBox3.getText().toString().trim() +
                        otpBox4.getText().toString().trim() +
                        otpBox5.getText().toString().trim() +
                        otpBox6.getText().toString().trim();

                if (enteredOtp.equals(otp)) {
                    updatePassword();
                } else {
                    Toast.makeText(AuthenticateActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AuthenticateActivity.this, "Please restart the process", Toast.LENGTH_SHORT).show();
            }
        });

        setOtpBoxTextWatchers();
    }

    private void updatePassword() {
        progressBar.setVisibility(View.VISIBLE);
        specificemployeeRef.setValue(password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(AuthenticateActivity.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AuthenticateActivity.this, MainActivity.class));
            } else {
                Toast.makeText(AuthenticateActivity.this, "Failed to Update Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOtpBoxTextWatchers() {
        otpBox1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpBox2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        otpBox2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && before > count) { // backspace key pressed
                    otpBox1.requestFocus();
                    otpBox1.setText("");
                } else if (s.length() == 1) {
                    otpBox3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        otpBox3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && before > count) { // backspace key pressed
                    otpBox2.requestFocus();
                    otpBox2.setText("");
                } else if (s.length() == 1) {
                    otpBox4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        otpBox4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && before > count) { // backspace key pressed
                    otpBox3.requestFocus();
                    otpBox3.setText("");
                } else if (s.length() == 1) {
                    otpBox5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        otpBox5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && before > count) { // backspace key pressed
                    otpBox4.requestFocus();
                    otpBox4.setText("");
                } else if (s.length() == 1) {
                    otpBox6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        otpBox6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && before > count) { // backspace key pressed
                    otpBox5.requestFocus();
                    otpBox5.setText("");
                }
                // No need to handle moving to the next box as this is the last box
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Optional: Handle back press on the first box to move focus to the previous UI element
        otpBox1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Backspace key pressed on otpBox1
                    // Do any necessary handling here (e.g., move focus to previous UI element)
                    return true; // Consume the event
                }
                return false; // Let the system handle other key events
            }
        });
    }


}

