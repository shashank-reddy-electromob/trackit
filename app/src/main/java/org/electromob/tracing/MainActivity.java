package org.electromob.tracing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        if (user == null)
        {
            setContentView(R.layout.activity_main);
        }
        else
        {
            Intent intent = new Intent(MainActivity.this,userlocation.class);
            startActivity(intent);
            finish();
        }
    }
    public void gotologin(View v)
    {
        startActivity(new Intent(MainActivity.this,login.class));
    }

    public void gotoregister(View v)
    {
        startActivity(new Intent(MainActivity.this,registeractivity.class));
    }
}
