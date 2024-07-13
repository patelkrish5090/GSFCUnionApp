package com.example.gsfc.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gsfc.ForgetPasswordActivity;
import com.example.gsfc.MainActivity;
import com.example.gsfc.R;

public class ProfileFragment extends Fragment {

    private Button resetPasswordButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Retrieve data from SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String empid = sharedPref.getString("empid", "");
        String name = sharedPref.getString("name", "");
        String phone = sharedPref.getString("phone", "");
        String grade = sharedPref.getString("grade", "");
        String designation = sharedPref.getString("designation", "");
        String department = sharedPref.getString("department", "");
        String blood = sharedPref.getString("blood", "");
        String password = sharedPref.getString("password", "");
        String email = sharedPref.getString("email", "");

        // Update TextViews with retrieved data
        TextView textEmpId = view.findViewById(R.id.text_emp_id);
        TextView textName = view.findViewById(R.id.text_emp_name);
        TextView textPhone = view.findViewById(R.id.text_emp_phone);
        TextView textGrade = view.findViewById(R.id.text_emp_grade);
        TextView textDesignation = view.findViewById(R.id.text_emp_designation);
        TextView textDepartment = view.findViewById(R.id.text_emp_department);
        TextView textBlood = view.findViewById(R.id.text_emp_blood);
        TextView textPassword = view.findViewById(R.id.text_emp_password);
        TextView textEmail = view.findViewById(R.id.text_emp_email);

        textEmpId.setText(empid);
        textName.setText(name);
        textPhone.setText(phone);
        textGrade.setText(grade);
        textDesignation.setText(designation);
        textDepartment.setText(department);
        textBlood.setText(blood);
        textPassword.setText(password);
        textEmail.setText(email);

        resetPasswordButton = view.findViewById(R.id.reset_password_button);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ForgetPasswordActivity
                Intent intent = new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
