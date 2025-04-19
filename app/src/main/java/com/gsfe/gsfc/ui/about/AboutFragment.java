package com.gsfe.gsfc.ui.about;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsfe.gsfc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AboutFragment extends Fragment {

    private RecyclerView recyclerView;
    private PeopleAdapter peopleAdapter;
    private List<Person> peopleList;
    private DatabaseReference databaseReference;

    // Define the designation order
    private static final List<String> DESIGNATION_ORDER = Arrays.asList(
            "President", "Vice President", "General Secretary",
            "Joint Secretary", "Treasurer", "Executive Member"
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        recyclerView = view.findViewById(R.id.rv_people_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        peopleList = new ArrayList<>();
        peopleAdapter = new PeopleAdapter(peopleList, getContext());
        recyclerView.setAdapter(peopleAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("contacts");
        fetchContactsFromFirebase();

        return view;
    }

    private void fetchContactsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                peopleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Person person = snapshot.getValue(Person.class);
                    if (person != null) {
                        peopleList.add(person);
                    }
                }
                // Sort the list based on designation and position
                Collections.sort(peopleList, new Comparator<Person>() {
                    @Override
                    public int compare(Person p1, Person p2) {
                        // Get the index of designations in the DESIGNATION_ORDER list
                        int index1 = DESIGNATION_ORDER.indexOf(p1.getDesignation());
                        int index2 = DESIGNATION_ORDER.indexOf(p2.getDesignation());

                        // If designation not found in the order list, push it to the end
                        if (index1 == -1) index1 = Integer.MAX_VALUE;
                        if (index2 == -1) index2 = Integer.MAX_VALUE;

                        // Compare designations first
                        if (index1 != index2) {
                            return Integer.compare(index1, index2);
                        }

                        // If designations are the same, compare positions
                        try {
                            int pos1 = Integer.parseInt(p1.getPosition());
                            int pos2 = Integer.parseInt(p2.getPosition());
                            return Integer.compare(pos1, pos2);
                        } catch (NumberFormatException e) {
                            // If position is not a valid number, treat it as a large value
                            Log.e("AboutFragment", "Invalid position value: " + e.getMessage());
                            return p1.getPosition() != null && p2.getPosition() != null
                                    ? p1.getPosition().compareTo(p2.getPosition()) : 0;
                        }
                    }
                });
                peopleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AboutFragment", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }
}