package org.electromob.tracing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class settings_activity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reff;
    Button change;
    TextView passwordchange;
    EditText chngename;
    String name1,name;
    TextView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        reff = FirebaseDatabase.getInstance().getReference().child("users");
        change = (Button)findViewById(R.id.edit);
        passwordchange = (TextView)findViewById(R.id.passwordreset);
        chngename = (EditText)findViewById(R.id.name_change);
        account = (TextView)findViewById(R.id.nameaccount);

        passwordchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settings_activity.this,passwordreset.class));
            }
        });


        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name1 = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);

                account.setText(name1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = chngename.getText().toString().trim();

                reff.child(user.getUid()).child("name").setValue(name);

            }
        });

    }
}
