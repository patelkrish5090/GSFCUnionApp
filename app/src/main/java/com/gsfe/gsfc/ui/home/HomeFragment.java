package com.gsfe.gsfc.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.gsfe.gsfc.AnnouncementViewActivity;
import com.gsfe.gsfc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewPolicy;
    private AnnouncementsAdapter adapter;
    private PolicyAdapter padapter;
    private List<Announcement> announcements;
    private List<Policy> policy;
    private TextView homeTitle, policyTitle;
    private DatabaseReference myRef;
    private DatabaseReference myRefAnnouncement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        homeTitle = view.findViewById(R.id.home_title);
        recyclerView = view.findViewById(R.id.recycler_view_announcements);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        policyTitle = view.findViewById(R.id.policyTitle);
        recyclerViewPolicy = view.findViewById(R.id.recycler_view_policy);
        recyclerViewPolicy.setLayoutManager(new LinearLayoutManager(getContext()));


        announcements = new ArrayList<>();
        policy = new ArrayList<>();
        // Add more announcements as needed

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("policy");
        myRefAnnouncement = database.getReference("announcements");

        //Policy
        padapter = new PolicyAdapter(policy, myRef);
        recyclerViewPolicy.setAdapter(padapter);

        //Announcements
        adapter = new AnnouncementsAdapter(announcements, myRefAnnouncement);
        recyclerView.setAdapter(adapter);


        fetchPolicy();
        fetchAnnouncements();

        adapter.setOnItemClickListener(new AnnouncementsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Announcement announcement) {
                // Handle item click here
                // For example, start the AnnouncementViewActivity with the clicked announcement's details
                Intent intent = new Intent(getContext(), AnnouncementViewActivity.class);
                intent.putExtra("title", announcement.getTitle());
                intent.putExtra("date", announcement.getDate());
                intent.putExtra("details", announcement.getDetails());
                intent.putExtra("attachmentUrl", announcement.getLink());
                startActivity(intent);
            }
        });

        return view;
    }

    private void fetchPolicy() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                policy.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Policy policy1 = snapshot.getValue(Policy.class);
                    if (policy1 != null) {
                        policy.add(policy1);
                    } else {
                        Log.w(TAG, "Policy is null");
                    }
                }
                padapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error){
                Log.w(TAG, "Policy to read value.", error.toException());
            }
        });
    }

    private void fetchAnnouncements() {
        myRefAnnouncement.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                announcements.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Announcement announcement = snapshot.getValue(Announcement.class);
                    if (announcement != null) {
                        announcements.add(announcement);
                    } else {
                        Log.w(TAG, "Announcement is null");
                    }
                }

                // Sort announcements in descending order based on the id
                Collections.sort(announcements, new Comparator<Announcement>() {
                    @Override
                    public int compare(Announcement a1, Announcement a2) {
                        return a2.getId().compareTo(a1.getId()); // Descending order
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
