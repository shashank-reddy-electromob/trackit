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

public class joincircle extends AppCompatActivity {

    EditText code;
    Button submit,buscircle;
    TextView invite_code;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,currentreference,circlereference,reference1;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id,join_user_id,usercode;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joincircle);

        code = (EditText)findViewById(R.id.editText);
        buscircle = (Button)findViewById(R.id.buscircle);
        invite_code = (TextView)findViewById(R.id.invitecode);
        submit = (Button)findViewById(R.id.button);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference1 = FirebaseDatabase.getInstance().getReference().child("users");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = firebaseDatabase.getInstance().getReference().child("users");
        currentreference = firebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        current_user_id = user.getUid();

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usercode = dataSnapshot.child(current_user_id).child("code").getValue(String.class);

                invite_code.setText(usercode);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Toast.makeText(getApplicationContext(),usercode,Toast.LENGTH_LONG).show();

        buscircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(joincircle.this,bus_circle.class));

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = reference.orderByChild("code").equalTo(code.getText().toString().trim());
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
                                        .child(join_user_id).child("Circlemembers");

                                circlejoin circlejoin = new circlejoin(current_user_id);
                                circlejoin circlejoin1 = new circlejoin(join_user_id);

                                circlereference.child(user.getUid()).setValue(circlejoin)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(getApplicationContext(),"user joined circle",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(joincircle.this,userlocation.class));
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
