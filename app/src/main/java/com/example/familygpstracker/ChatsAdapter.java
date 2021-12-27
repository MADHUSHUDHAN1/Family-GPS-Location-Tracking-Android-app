package com.example.familygpstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.familygpstracker.ChatActivity.reciverImage;
import static com.example.familygpstracker.ChatActivity.senderImage;

public class ChatsAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Chats> messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVED=2;


    public ChatsAdapter(Context context, ArrayList<Chats> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout_item,parent,false);
            return new ReciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

      Chats messages = messagesArrayList.get(position);

        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder =(SenderViewHolder) holder;
            viewHolder.textmessage.setText(messages.getMessage());

            Picasso.get().load(senderImage).into(viewHolder.circleImageView);
        }
        else {
            ReciverViewHolder viewHolder =( ReciverViewHolder) holder;
            viewHolder.textmessage.setText(messages.getMessage());
            Picasso.get().load(reciverImage).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Chats messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND;
        }
        else {
            return ITEM_RECIVED;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView textmessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView =itemView.findViewById(R.id.profile_image);
            textmessage = itemView.findViewById(R.id.text_Messages);
        }
    }

    class ReciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView textmessage;

        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView =itemView.findViewById(R.id.profile_image);
            textmessage = itemView.findViewById(R.id.text_Messages);
        }
    }
}
