package com.gsfe.gsfc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.security.SecureRandom;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText empIdEditText, newpassEditText, confpassEditText;
    private Button getOtpButton;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        empIdEditText = findViewById(R.id.et_empid);
        newpassEditText = findViewById(R.id.et_new_password);
        confpassEditText = findViewById(R.id.et_confirm_password);
        getOtpButton = findViewById(R.id.btn_get_otp);
        progressBar = findViewById(R.id.progressBarForget);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("employees");
        executorService = Executors.newSingleThreadExecutor();

        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String empId = empIdEditText.getText().toString().trim();
                final String newPassword = newpassEditText.getText().toString().trim();
                final String confirmPassword = confpassEditText.getText().toString().trim();

                int passwordCheck = checkPassword(newPassword, confirmPassword);

                if (!empId.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                    if (passwordCheck == 1) {
                        databaseReference.child(empId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String email = dataSnapshot.child("email").getValue(String.class);
                                    String user = dataSnapshot.child("name").getValue(String.class);
                                    sendEmail(user, email, newPassword, empId);
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(ForgetPasswordActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if(passwordCheck == 2){
                        newpassEditText.setError("Password must be of 8 characters");
                    } else {
                        confpassEditText.setError("New and Confirm Password are not the same");
                    }
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmail(final String user, final String email, final String newPassword, final String empId) {
        progressBar.setVisibility(View.VISIBLE);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Set up mail server properties
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
                    props.put("mail.smtp.socketFactory.port", "465"); // SSL Port
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
                    props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
                    props.put("mail.smtp.port", "465"); // SMTP Port

                    // Your mail server authentication credentials
                    final String username = "gsfeotpservice@gmail.com";
                    final String password = "owfiycjwwydvtlgk";

                    // Create session with authentication
                    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

                    // Create email message
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username)); // Sender email
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Recipient email
                    message.setSubject("Password Reset OTP"); // Email subject
                    String otp = generateOTP(); // Generate OTP
                    String emailContent = "Dear " + user + ",<br><br>"
                            + "Your OTP for password reset is: <strong>" + otp + "</strong><br><br>"
                            + "Please use this OTP to reset your password.<br><br>"
                            + "If you did not request this change, please ignore this email.<br><br>"
                            + "Thank you,<br>"
                            + "The GSFE Union Tech Team";

                    message.setContent(emailContent, "text/html; charset=utf-8"); // Email content as HTML

                    // Send email
                    Transport.send(message);

                    // Open AuthenticateActivity with necessary data
                    Intent intent = new Intent(ForgetPasswordActivity.this, AuthenticateActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", newPassword);
                    intent.putExtra("empid", empId);
                    intent.putExtra("otp", otp);
                    startActivity(intent);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.e("EmailSending", "Failed to send email: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgetPasswordActivity.this, "Email sending failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    int checkPassword(String newpass, String confpass) {
        if(newpass.equals(confpass)) {
            if(newpass.length()<8) {
                return 2;
            }else{return 1;}

        }
        else return 3;
    }

    // Method to generate OTP (you can customize it as per your requirements)
    private String generateOTP() {
        // Length of OTP
        int otpLength = 6;

        // Generate random digits
        StringBuilder otp = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10)); // Generates random integers from 0 to 9
        }

        return otp.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}

