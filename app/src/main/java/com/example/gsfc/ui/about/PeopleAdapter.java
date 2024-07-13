package com.example.gsfc.ui.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gsfc.R;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PersonViewHolder> {

    private List<Person> peopleList;
    private Context context;

    public PeopleAdapter(List<Person> peopleList, Context context) {
        this.peopleList = peopleList;
        this.context = context;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = peopleList.get(position);
        holder.bind(person, context);
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView tvPersonName, tvPersonDesignation, tvPersonPhone;
        Button btnCall;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPersonName = itemView.findViewById(R.id.tv_person_name);
            tvPersonDesignation = itemView.findViewById(R.id.tv_person_designation);
            tvPersonPhone = itemView.findViewById(R.id.tv_person_phone);
            btnCall = itemView.findViewById(R.id.btn_call);
        }

        public void bind(Person person, Context context) {
            tvPersonName.setText(person.getName());
            tvPersonDesignation.setText(person.getDesignation());
            tvPersonPhone.setText(person.getPhone());

            btnCall.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + person.getPhone()));
                context.startActivity(intent);
            });
        }
    }
}
