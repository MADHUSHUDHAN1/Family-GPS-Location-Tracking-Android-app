package com.example.familygpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RegisterActivity extends AppCompatActivity {

    EditText e4_email;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e4_email = (EditText) findViewById(R.id.email_reg);
        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
    }

    public void goToPasswordActivity(View view)
    {
        dialog.setMessage("Checking Email address.....");
        dialog.show();
        auth.fetchSignInMethodsForEmail(e4_email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful())
                        {
                            dialog.dismiss();
                            boolean check = !task.getResult().getSignInMethods().isEmpty();
                            if (!check)
                            {
                                Intent intent = new Intent(RegisterActivity.this,PasswordActivity.class);
                                intent.putExtra("email",e4_email.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "This Email is already exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}