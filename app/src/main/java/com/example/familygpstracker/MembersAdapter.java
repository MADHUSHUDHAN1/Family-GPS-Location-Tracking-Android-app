package com.example.familygpstracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {

    ArrayList<CreateUser> namelist;
    Context context;

    public MembersAdapter(ArrayList<CreateUser> namelist, Context context) {
        this.namelist = namelist;
        this.context = context;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        MembersViewHolder membersViewHolder = new MembersViewHolder(view,context,namelist);

        return membersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {
        CreateUser currentUserObj = namelist.get(position);
        holder.name_text.setText(currentUserObj.name);
        //holder.user_id.setText(currentUserObj.userid);
        Picasso.get().load(currentUserObj.imageUri).placeholder(R.drawable.profile).into(holder.circleImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MapsActivity.class);
                intent.putExtra("userid",currentUserObj.userid);
                intent.putExtra("lat",currentUserObj.lat);
                intent.putExtra("lng",currentUserObj.lng);
                intent.putExtra("name",currentUserObj.name);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }


    public static class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        View view;
        Context context;
        TextView name_text;
        CircleImageView circleImageView;
        ArrayList<CreateUser> nameArrayList;
        FirebaseAuth auth;
        FirebaseUser user;

        public MembersViewHolder(@NonNull View itemView,  Context context, ArrayList<CreateUser> nameArrayList) {
            super(itemView);
            this.context = context;
            this.nameArrayList = nameArrayList;

            itemView.setOnClickListener(this);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            name_text = itemView.findViewById(R.id.item_title);
            circleImageView = itemView.findViewById(R.id.user_image);


        }

        @Override
        public void onClick(View v) {


            Toast.makeText(context, "You have clicked this user", Toast.LENGTH_SHORT).show();
        }
    }
}
