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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    ArrayList<CreateUser> namelist;
    Context context;

    public MessageAdapter(ArrayList<CreateUser> namelist, Context context) {
        this.namelist = namelist;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        MessageAdapter.MessageViewHolder messageViewHolder = new MessageAdapter.MessageViewHolder(view,context,namelist);

        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        CreateUser currentUserObj = namelist.get(position);
        holder.name_text.setText(currentUserObj.name);
        //holder.user_id.setText(currentUserObj.userid);
        Picasso.get().load(currentUserObj.imageUri).placeholder(R.drawable.profile).into(holder.circleImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("userid",currentUserObj.userid);
                intent.putExtra("name",currentUserObj.name);
                intent.putExtra("imageUri",currentUserObj.imageUri);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        View view;
        Context context;
        TextView name_text;
        CircleImageView circleImageView;
        ArrayList<CreateUser> nameArrayList;
        FirebaseAuth auth;
        FirebaseUser user;

        public MessageViewHolder(@NonNull View itemView, Context context,  ArrayList<CreateUser> nameArrayList) {
            super(itemView);
            this.context = context;
            this.nameArrayList = nameArrayList;


            itemView.setOnClickListener(this);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            name_text = itemView.findViewById(R.id.item_title);
            circleImageView = itemView.findViewById(R.id.user_image);
            //user_id = itemView.findViewById(R.id.user_id);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "You have clicked this user", Toast.LENGTH_SHORT).show();
        }
    }
}
