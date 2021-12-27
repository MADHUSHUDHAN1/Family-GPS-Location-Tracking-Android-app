package com.example.familygpstracker.ui.JoinCircle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.familygpstracker.CircleJoin;
import com.example.familygpstracker.CreateUser;
import com.example.familygpstracker.R;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinCircle_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinCircle_Fragment extends Fragment {

    Pinview pinview;
    Button join;
    DatabaseReference reference,currentReference;
    DatabaseReference circleReference;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id,join_user_id;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JoinCircle_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinCircle_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinCircle_Fragment newInstance(String param1, String param2) {
        JoinCircle_Fragment fragment = new JoinCircle_Fragment();
     //   Bundle args = new Bundle();
      //  args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
       // fragment.setArguments(args);
        return fragment;
    }

  /*  @Override
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
        View view = inflater.inflate(R.layout.fragment_join_circle_, container, false);
        pinview = (Pinview) view.findViewById(R.id.pin_code);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        current_user_id = user.getUid();
        join =(Button) view.findViewById(R.id.button8);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = reference.orderByChild("code").equalTo(pinview.getValue());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            CreateUser createUser = null;
                            for (DataSnapshot childDss : snapshot.getChildren())
                            {
                                createUser = childDss.getValue(CreateUser.class);
                                join_user_id = createUser.userid;

                                circleReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(join_user_id).child("CircleMember");

                                CircleJoin circleJoin = new CircleJoin(current_user_id);
                                CircleJoin circleJoin1 = new CircleJoin(join_user_id);

                                circleReference.child(user.getUid()).setValue(circleJoin)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(getActivity(), "User joined circle successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "Circle code os invalid", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return view;
    }
}