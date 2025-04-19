package com.gsfe.gsfc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.gsfe.gsfc.R;

public class AnnouncementViewActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView dateTextView;
    private TextView detailsTextView;
    private Button openAttachmentButton;
    private String attachmentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_view);

        titleTextView = findViewById(R.id.text_title);
        dateTextView = findViewById(R.id.text_announcement_date);
        detailsTextView = findViewById(R.id.text_announcement_details);
        openAttachmentButton = findViewById(R.id.button_open_attachment);

        // Get the data from the intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        String details = intent.getStringExtra("details");
        attachmentUrl = intent.getStringExtra("attachmentUrl");

        // Set the data to the views
        titleTextView.setText(title);
        dateTextView.setText(date);
        detailsTextView.setText(details);

        if (attachmentUrl != null && !attachmentUrl.isEmpty() && !attachmentUrl.equals("none")) {
            openAttachmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(attachmentUrl));
                    startActivity(intent);
                }
            });
        } else {
            openAttachmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AnnouncementViewActivity.this, "No file attached to this", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
