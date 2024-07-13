package com.example.gsfc.ui.Vote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gsfc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VoteFragment extends Fragment {

    private TextView tvQuestion, tvStatus;
    private RadioGroup rgOptions;
    private Button btnVote;
    private DatabaseReference voteRef;
    private String userId;
    private String currentPollId;

    public VoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vote, container, false);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        rgOptions = view.findViewById(R.id.rgOptions);
        btnVote = view.findViewById(R.id.btnVote);
        tvStatus = view.findViewById(R.id.tvStatus);

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        userId = sharedPref.getString("empid", "");

        voteRef = FirebaseDatabase.getInstance().getReference().child("vote");

        fetchVoteData();

        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedOptionId = rgOptions.getCheckedRadioButtonId();
                if (selectedOptionId != -1) {
                    RadioButton selectedRadioButton = view.findViewById(selectedOptionId);
                    String selectedOptionKey = (String) selectedRadioButton.getTag();
                    registerVote(selectedOptionKey);
                }
            }
        });

        return view;
    }

    private void fetchVoteData() {
        voteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean activePollFound = false;
                boolean userHasVoted = false;

                for (DataSnapshot pollSnapshot : snapshot.getChildren()) {
                    String status = pollSnapshot.child("status").getValue(String.class);
                    if ("active".equals(status)) {
                        DataSnapshot optionsSnapshot = pollSnapshot.child("options");
                        for (DataSnapshot option : optionsSnapshot.getChildren()) {
                            if (option.hasChild("voters")) {
                                DataSnapshot votersSnapshot = option.child("voters");
                                for (DataSnapshot voter : votersSnapshot.getChildren()) {
                                    String voterId = voter.child("empid").getValue(String.class);
                                    if (userId.equals(voterId)) {
                                        userHasVoted = true;
                                        break;
                                    }
                                }
                            }
                            if (userHasVoted) break;
                        }
                        if (userHasVoted) break;

                        activePollFound = true;
                        currentPollId = pollSnapshot.getKey(); // Store the current poll ID
                        String question = pollSnapshot.child("question").getValue(String.class);
                        tvQuestion.setText(question);

                        for (DataSnapshot option : optionsSnapshot.getChildren()) {
                            String optionKey = option.getKey();
                            String optionValue = option.child("value").getValue(String.class);
                            RadioButton radioButton = new RadioButton(getContext());
                            radioButton.setText(optionValue);
                            radioButton.setTag(optionKey);
                            rgOptions.addView(radioButton);
                        }

                        tvQuestion.setVisibility(View.VISIBLE);
                        rgOptions.setVisibility(View.VISIBLE);
                        btnVote.setVisibility(View.VISIBLE);
                        tvStatus.setVisibility(View.GONE);
                        break; // Exit the loop after finding the first active poll
                    }
                }

                if (!activePollFound || userHasVoted) {
                    showNoPollLiveNow();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void showNoPollLiveNow() {
        tvStatus.setVisibility(View.VISIBLE);
        tvQuestion.setVisibility(View.GONE);
        rgOptions.setVisibility(View.GONE);
        btnVote.setVisibility(View.GONE);
    }

    private void registerVote(String optionKey) {
        DatabaseReference optionVotersRef = voteRef.child(currentPollId).child("options").child(optionKey).child("voters");
        optionVotersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // If the voter's list doesn't exist, create it
                    optionVotersRef.child(userId).child("empid").setValue(userId);
                } else {
                    // If the voter's list exists, add the vote
                    optionVotersRef.child(userId).child("empid").setValue(userId);
                }

                DatabaseReference optionCountRef = voteRef.child(currentPollId).child("options").child(optionKey).child("count");
                optionCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String currentCountStr = snapshot.getValue(String.class);
                        if (currentCountStr != null) {
                            int currentCount = Integer.parseInt(currentCountStr);
                            currentCount++; // Increment count
                            String updatedCountStr = String.valueOf(currentCount); // Convert back to string
                            optionCountRef.setValue(updatedCountStr);
                        }

                        DatabaseReference totalCountRef = voteRef.child(currentPollId).child("total_count");
                        totalCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String totalCountStr = snapshot.getValue(String.class);
                                if (totalCountStr != null) {
                                    int totalCount = Integer.parseInt(totalCountStr);
                                    totalCount++; // Increment total count
                                    String updatedTotalCountStr = String.valueOf(totalCount); // Convert back to string
                                    totalCountRef.setValue(updatedTotalCountStr);
                                }

                                Toast.makeText(getContext(), "Your vote was submitted.", Toast.LENGTH_SHORT).show();
                                showNoPollLiveNow(); // Show no poll live now after voting
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle possible errors.
                            }
                        });

                        Toast.makeText(getContext(), "Your vote was submitted.", Toast.LENGTH_SHORT).show();
                        showNoPollLiveNow(); // Show no poll live now after voting
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors.
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
}
