package com.example.gsfc.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gsfc.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class PolicyAdapter  extends RecyclerView.Adapter<PolicyAdapter.PolicyViewHolder> {
    private List<Policy> policy;
    private DatabaseReference myRef;

    public PolicyAdapter(List<Policy> policy, DatabaseReference myRef) {
        this.policy = policy;
        this.myRef = myRef;
    }

    @NonNull
    @Override
    public PolicyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_policy,parent,false);
        return new PolicyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyViewHolder holder, int position) {
        Policy currentPolicy = policy.get(position);
        holder.title.setText(currentPolicy.getTitle());
        holder.details.setText(currentPolicy.getDetails());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of details and delete button
                if (holder.details.getVisibility() == View.VISIBLE) {
                    holder.details.setVisibility(View.GONE);
                    holder.linkButton.setVisibility(View.GONE);
                } else {
                    holder.details.setVisibility(View.VISIBLE);
                    holder.linkButton.setVisibility(View.VISIBLE);
                }
            }
        });

        String url = currentPolicy.getLink();
        if (url != null && !url.isEmpty() && !url.equals("none")) {
            holder.linkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        } else {
            holder.linkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.itemView.getContext(), "No file attached to this", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return policy.size();
    }

    static class PolicyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView details;
        public Button linkButton;


        public PolicyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.policy_title);
            details = itemView.findViewById(R.id.policy_details);
            linkButton = itemView.findViewById(R.id.link_button);
        }
    }
}
