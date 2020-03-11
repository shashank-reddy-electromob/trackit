package org.electromob.tracing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class bus_circle extends AppCompatActivity {

    TextView t1,t2,t3;
    Button b1;
    EditText ed1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,currentreference,circlereference,reference1;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    String current_user_id,join_user_id,usercode;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_circle);

        t1 = (TextView)findViewById(R.id.buscircletv);
        t2 = (TextView)findViewById(R.id.invitecodebuscircle);
        t3 = (TextView)findViewById(R.id.textViewbuscircle);
        b1 = (Button)findViewById(R.id.submitbuscircle);
        ed1 = (EditText)findViewById(R.id.buscircleet);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference1 = FirebaseDatabase.getInstance().getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        reference = firebaseDatabase.getInstance().getReference().child("users");
        currentreference = firebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        current_user_id = user.getUid();

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usercode = dataSnapshot.child(current_user_id).child("code").getValue(String.class);

                t2.setText(usercode);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = reference.orderByChild("code").equalTo(ed1.getText().toString().trim());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            //Toast.makeText(joincircle.this,"Test 1",Toast.LENGTH_SHORT).show();

                            createuser createuser = null;

                            for (DataSnapshot childDss : dataSnapshot.getChildren())
                            {
                                createuser = childDss.getValue(createuser.class);
                                join_user_id = createuser.userid;

                                circlereference = firebaseDatabase.getInstance().getReference().child("users")
                                        .child(join_user_id).child("Bus_Circle").child("Circlemembers");

                                circlejoin circlejoin = new circlejoin(current_user_id);
                                circlejoin circlejoin1 = new circlejoin(join_user_id);

                                circlereference.child(user.getUid()).setValue(circlejoin)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(getApplicationContext(),"user joined circle",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(bus_circle.this,userlocation.class));
                                                }
                                            }
                                        });
                                //Toast.makeText(joincircle.this,"Test 1",Toast.LENGTH_LONG).show();

                            }

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"circle code is invalid",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
