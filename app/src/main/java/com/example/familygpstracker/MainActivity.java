package com.example.familygpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    PermissionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user==null)
        {
            setContentView(R.layout.activity_main);
            manager=new PermissionManager() {};
            manager.checkAndRequestPermissions(this);
        }
        else {
            Intent intent = new Intent(MainActivity.this,MyNavigationActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        manager.checkResult(requestCode, permissions, grantResults);
        ArrayList<String> denied_permission = manager.getStatus().get(0).denied;
        if (denied_permission.isEmpty()) {
            Toast.makeText(this, "Permission enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToLogin(View view)
    {
        Intent intent = new Intent(MainActivity.this,LogInActivity.class);
        startActivity(intent);
    }
    public void goToRegister(View view)
    {
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}