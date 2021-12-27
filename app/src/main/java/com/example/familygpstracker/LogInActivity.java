package com.example.familygpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1,e2;
    String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        e1 = (EditText) findViewById(R.id.login_email);
        e2 = (EditText) findViewById(R.id.login_password);
        auth= FirebaseAuth.getInstance();
    }
    public void login(View view)
    {
        email = e1.getText().toString();
        password = e2.getText().toString();

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                                Intent intent = new Intent(LogInActivity.this,MyNavigationActivity.class);
                                startActivity(intent);
                                Toast.makeText(LogInActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(LogInActivity.this, "Wrong email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}