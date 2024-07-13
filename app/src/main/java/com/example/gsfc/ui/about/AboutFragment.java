//package com.example.gsfc.ui.about;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//
//import com.example.gsfc.R;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class AboutFragment extends Fragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_about, container, false);
//
//        // Set the header details
//        ((TextView) view.findViewById(R.id.tv_union_name)).setText("GUJARAT STATE FERTILIZERS EMPLOYEES UNION");
//        ((TextView) view.findViewById(R.id.tv_union_address)).setText("P.O. FERTILIZER NAGAR - 391 750. Dist.: VADODARA");
//        ((TextView) view.findViewById(R.id.tv_union_phone)).setText("Phone: 0265 - 3092580, 3093580");
//        ((TextView) view.findViewById(R.id.tv_union_email)).setText("Email: gsfeunion2580@gmail.com");
//
//        // Example list of people
//        List<Person> people = Arrays.asList(
//                new Person("John Doe", "President", "1234567890"),
//                new Person("Jane Smith", "Secretary", "0987654321")
//                // Add more people as needed
//        );
//
//        // Add people to the scroll view
//        LinearLayout llPeopleList = view.findViewById(R.id.ll_people_list);
//        for (Person person : people) {
//            View personView = inflater.inflate(R.layout.person_item, llPeopleList, false);
//
//            ((TextView) personView.findViewById(R.id.tv_person_name)).setText("Name: " + person.name);
//            ((TextView) personView.findViewById(R.id.tv_person_role)).setText("Role: " + person.role);
//            ((TextView) personView.findViewById(R.id.tv_person_phone)).setText("Phone: " + person.phone);
//
//            llPeopleList.addView(personView);
//        }
//
//        return view;
//    }
//
//    static class Person {
//        String name;
//        String role;
//        String phone;
//
//        Person(String name, String role, String phone) {
//            this.name = name;
//            this.role = role;
//            this.phone = phone;
//        }
//    }
//}

package com.example.gsfc.ui.about;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gsfc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AboutFragment extends Fragment {

    private RecyclerView recyclerView;
    private PeopleAdapter peopleAdapter;
    private List<Person> peopleList;
    private DatabaseReference databaseReference;

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
                peopleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Log.e("YourFragment", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }
}

