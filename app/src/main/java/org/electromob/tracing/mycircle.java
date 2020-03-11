package org.electromob.tracing;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class  mycircle extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference reference,usersreference;
    FirebaseAuth auth;
    FirebaseUser user;
    private createuser createuser;
    ArrayList<createuser> namelist;
    private String circlememberid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycircle);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        namelist = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        usersreference = FirebaseDatabase.getInstance().getReference().child("users");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Circlemembers");

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               namelist.clear();
               if (dataSnapshot.exists())
               {
                   for (DataSnapshot dss : dataSnapshot.getChildren()){


                       circlememberid = (dss.child("circlememberid").getValue(String.class));
                       usersreference.child((circlememberid))
                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       try {

                                           createuser = dataSnapshot.getValue(createuser.class);
                                           namelist.add(createuser);
                                           adapter.notifyDataSetChanged();
                                           //Toast.makeText(getApplicationContext(),"Test 1",Toast.LENGTH_LONG).show();
                                       }
                                       catch (DatabaseException e)
                                       {
                                           Toast.makeText(getApplicationContext(), circlememberid,Toast.LENGTH_LONG).show();
                                           dataSnapshot.getKey();
                                       }

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                       Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                   }
                               });

                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

               Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
           }
       });

        adapter = new MembersAdapter(namelist,getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

}
