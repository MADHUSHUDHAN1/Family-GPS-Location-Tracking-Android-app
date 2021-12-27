package com.example.familygpstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    String email;
    EditText password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        password =(EditText) findViewById(R.id.password_reg);

        Intent intent = getIntent();
        if (intent != null)
        {
            email = intent.getStringExtra("email");
        }
    }
    public void goToNameActivity(View view)
    {
        if (password.getText().toString().length()>6)
        {
            Intent intent = new Intent(PasswordActivity.this,NameActivity.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password.getText().toString());
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "Password should be more then 6 character", Toast.LENGTH_SHORT).show();
        }
    }

}