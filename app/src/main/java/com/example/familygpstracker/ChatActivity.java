package com.example.familygpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String ReciverImage,ReciverUID,ReciverName,senderUID;
    CircleImageView profileImage;
    TextView reciverName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String senderImage;
    public static String reciverImage;
    public static String ReciverLatitude,ReciverLongitude;
    CardView sendBtn;
    EditText editMessage;

    ImageView currentLocation;

    ChatsAdapter mAdapter;

    RecyclerView messageAdapter;

    ArrayList<Chats> messagesArrayList;
    String senderRoom,reciverRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database =FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        if (intent !=  null)
        {
            ReciverName =intent.getStringExtra("name");
            ReciverImage = intent.getStringExtra("imageUri");
            ReciverUID= intent.getStringExtra("userid");
        }

        messagesArrayList = new ArrayList<>();
        profileImage = findViewById(R.id.profile_image);
        reciverName = findViewById(R.id.reciver_name);

        messageAdapter = findViewById(R.id.messageAdapter);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        mAdapter = new ChatsAdapter(ChatActivity.this,messagesArrayList);
        messageAdapter.setAdapter(mAdapter);

        sendBtn = findViewById(R.id.send_btn);
        editMessage = findViewById(R.id.editText_message);

        Picasso.get().load(ReciverImage).into(profileImage);
        reciverName.setText(""+ReciverName);

        senderUID = firebaseAuth.getUid();

        senderRoom = senderUID+ReciverUID;
        reciverRoom = ReciverUID+senderUID;

        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");


        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesArrayList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Chats messages =dataSnapshot.getValue(Chats.class);
                    messagesArrayList.add(messages);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImage =  snapshot.child("imageuri").getValue().toString();
                reciverImage = ReciverImage;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editMessage.getText().toString();
                if (message.isEmpty())
                {
                    Toast.makeText(ChatActivity.this, "Please Enter Valid Message", Toast.LENGTH_SHORT).show();
                    return;
                }
                editMessage.setText("");
                Date date = new Date();

                Chats messages = new Chats(message,senderUID,date.getTime());

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .push()
                                .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });

    }
}