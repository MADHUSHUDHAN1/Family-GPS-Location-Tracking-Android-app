package com.example.familygpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InviteCodeActivity extends AppCompatActivity {

    String name,email,password,date,isSharing,code;
    Uri imageUri;
    ProgressDialog progressDialog;
    TextView t1;
    String imageURI;
    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference storageReference;
    DatabaseReference reference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);

        t1 =(TextView) findViewById(R.id.code);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
      storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://gps-tracker-app-f704c.appspot.com").child("User_images");

        Intent intent= getIntent();
        if (intent!=null)
        {
            name=intent.getStringExtra("name");
            email=intent.getStringExtra("email");
            password=intent.getStringExtra("password");
            date=intent.getStringExtra("date");
            isSharing=intent.getStringExtra("isSharing");
            code=intent.getStringExtra("code");
            imageUri=intent.getParcelableExtra("imageUri");
        }
        t1.setText(code);

    }
   /* public void registerUser(View view)
    {
        progressDialog.setMessage("Creating account......");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful())
               {
                   reference = database.getReference().child("Users").child(auth.getUid());
                   storageReference = storage.getReference().child("User_images").child(auth.getUid());

                   storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                          if (task.isSuccessful())
                          {
                              storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                  @Override
                                  public void onSuccess(Uri uri) {
                                    imageURI = uri.toString();
                                      CreateUser createUser = new CreateUser( name,email,password,code,"false","na","na",imageURI);
                                      reference.setValue(createUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              if (task.isSuccessful())
                                              {
                                                  progressDialog.dismiss();
                                                  Toast.makeText(InviteCodeActivity.this, "Email send for verification.Check Mail", Toast.LENGTH_SHORT).show();
                                                  sendVerificationEmail();
                                                  Intent intent = new Intent(InviteCodeActivity.this,MainActivity.class);
                                                  startActivity(intent);
                                                  finish();
                                              }
                                              else {
                                                  progressDialog.dismiss();
                                                  Toast.makeText(InviteCodeActivity.this, "An error occured while registering", Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      });
                                  }
                              });
                          }
                       }
                   });
               }
            }
        });
    }
    */

      public void registerUser(View view)
    {
        progressDialog.setMessage("Creating account......");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            user = auth.getCurrentUser();
                            CreateUser createUser = new CreateUser( name,email,password,code,"false","na","na","na",user.getUid());

                            userId = user.getUid();
                            reference.child(userId).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                StorageReference sr = storageReference.child(user.getUid());
                                                sr.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                           sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                               @Override
                                                               public void onSuccess(Uri uri) {
                                                                   if (uri != null)
                                                                   {
                                                                       imageURI = uri.toString();
                                                                       reference.child(user.getUid()).child("imageUri").setValue(imageURI)
                                                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                   @Override
                                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                                       if (task.isSuccessful())
                                                                                       {
                                                                                           progressDialog.dismiss();
                                                                                           Toast.makeText(InviteCodeActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                                                                           Intent intent = new Intent(InviteCodeActivity.this,MyNavigationActivity.class);
                                                                                           startActivity(intent);
                                                                                       }
                                                                                       else {
                                                                                           progressDialog.dismiss();
                                                                                           Toast.makeText(InviteCodeActivity.this, "An error occoured while creating account", Toast.LENGTH_SHORT).show();
                                                                                       }
                                                                                   }
                                                                               });
                                                                   }
                                                               }
                                                           });
                                                        }
                                                    }
                                                });
                                            }
                                            else {
                                                progressDialog.dismiss();
                                                Toast.makeText(InviteCodeActivity.this, "User not Registered", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


  /*  public void sendVerificationEmail()
    {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(InviteCodeActivity.this, "Email sent for Verification", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    finish();

                }
                else {
                    Toast.makeText(InviteCodeActivity.this, "Could not send Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

   */

}