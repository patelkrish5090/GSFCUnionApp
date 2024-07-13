package com.example.gsfc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginempid, loginpassword;
    Button loginButton;
    TextView signupRedirectText;
    private String empid;
    private String name;
    private String password;
    private String phone;
    private String grade;
    private String designation;
    private String department;
    private String blood;
    private String email;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginempid = findViewById(R.id.login_empid);
        loginpassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateempid() | !validatepassword()) {
                    return;
                } else {
                    checkuser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    public Boolean validateempid() {
        String val = loginempid.getText().toString();
        if (val.isEmpty()) {
            loginempid.setError("Employee id Cannot be Empty");
            return false;
        } else {
            loginempid.setError(null);
            return true;
        }
    }

    public Boolean validatepassword() {
        String val = loginpassword.getText().toString();
        if (val.isEmpty()) {
            loginpassword.setError("Password Cannot be Empty");
            return false;
        } else {
            loginpassword.setError(null);
            return true;
        }
    }

    public void checkuser() {
        progressBar.setVisibility(View.VISIBLE);
        String userUserempid = loginempid.getText().toString().trim();
        String userUserpassword = loginpassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("employees");
        Query checkUserDatabase = reference.orderByChild("empid").equalTo(userUserempid);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                        empid = userSnapshot.child("empid").getValue(String.class);
                        name = userSnapshot.child("name").getValue(String.class);
                        grade = userSnapshot.child("grade").getValue(String.class);
                        designation = userSnapshot.child("designation").getValue(String.class);
                        department = userSnapshot.child("department").getValue(String.class);
                        blood = userSnapshot.child("blood").getValue(String.class);
                        password = userSnapshot.child("password").getValue(String.class);
                        phone = userSnapshot.child("phone").getValue(String.class);
                        email = userSnapshot.child("email").getValue(String.class);

                        SharedPreferences sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("empid", empid);
                        editor.putString("name", name);
                        editor.putString("grade", grade);
                        editor.putString("designation", designation);
                        editor.putString("department", department);
                        editor.putString("blood", blood);
                        editor.putString("password", password);
                        editor.putString("phone", phone);
                        editor.putString("email", email);
                        editor.apply();

                        if (passwordFromDB != null && passwordFromDB.equals(userUserpassword)) {
                            loginempid.setError(null);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Optional: Finish login activity
                        } else {
                            loginpassword.setError("Invalid Credentials");
                            loginpassword.requestFocus();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } else {
                    loginempid.setError("User Does not exist");
                    loginempid.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}