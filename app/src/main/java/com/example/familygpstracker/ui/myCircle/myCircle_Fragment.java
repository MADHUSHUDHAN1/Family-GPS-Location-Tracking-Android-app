package com.example.familygpstracker.ui.myCircle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.familygpstracker.CreateUser;
import com.example.familygpstracker.MembersAdapter;
import com.example.familygpstracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link myCircle_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myCircle_Fragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth auth;
    FirebaseUser user;
    CreateUser createUser;
    ArrayList<CreateUser> namelist;
    DatabaseReference reference,usersReference;
    String circlememberid;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public myCircle_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myCircle_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static myCircle_Fragment newInstance(String param1, String param2) {
        myCircle_Fragment fragment = new myCircle_Fragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
       // fragment.setArguments(args);
        return fragment;
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_my_circle_, container, false);

       recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
       auth = FirebaseAuth.getInstance();
       user = auth.getCurrentUser();
       namelist = new ArrayList<>();

       layoutManager = new LinearLayoutManager(getContext());
       recyclerView.setLayoutManager(layoutManager);
       recyclerView.setHasFixedSize(true);

       usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
       reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("CircleMember");

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               namelist.clear();
               if (snapshot.exists())
               {
                   for (DataSnapshot dataSnapshot: snapshot.getChildren())
                   {
                       circlememberid = dataSnapshot.child("circlememberid").getValue(String.class);
                       usersReference.child(circlememberid)
                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                     createUser = snapshot.getValue(CreateUser.class);
                                     namelist.add(createUser);
                                     adapter.notifyDataSetChanged();
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {
                                       Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                   }
                               });
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });

       adapter = new MembersAdapter(namelist,getContext());
       recyclerView.setAdapter(adapter);
       adapter.notifyDataSetChanged();
      return view;
    }
}