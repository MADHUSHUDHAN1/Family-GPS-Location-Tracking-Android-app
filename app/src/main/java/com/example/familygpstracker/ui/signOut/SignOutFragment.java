package com.example.familygpstracker.ui.signOut;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.familygpstracker.MainActivity;
import com.example.familygpstracker.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignOutFragment extends Fragment {

    Button logout;
    FirebaseAuth auth;

    public SignOutFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SignOutFragment newInstance(String param1, String param2) {
        SignOutFragment fragment = new SignOutFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_out, container, false);

        logout =(Button) view.findViewById(R.id.log_out);
        auth= FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                Toast.makeText(getActivity(), "LogOut", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}