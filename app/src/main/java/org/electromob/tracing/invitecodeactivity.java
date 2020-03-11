package org.electromob.tracing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class invitecodeactivity extends AppCompatActivity {

    String name,email,password,date,issharing,code,userid;
    Uri imageuri;
    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    createuser createuser;
    CircleImageView circleImageView;
    Uri resulturi,imagepath;
    StorageTask muploadtask;
    ImageView imageView;
    FirebaseStorage firebaseStorage;
    int PICK_IMAGE = 123;
    StorageReference mstorageref;
    private Bitmap bitmap1;
    private int PERMISSION_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitecodeactivity);

        Intent intent = getIntent();
        t1 = (TextView)findViewById(R.id.code);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getInstance().getReference().child("users");
        firebaseStorage = FirebaseStorage.getInstance();
        imageView = (ImageView) findViewById(R.id.dp);
        mstorageref = FirebaseStorage.getInstance().getReference();
        user = auth.getCurrentUser();

        if (intent!=null)
        {
            name = intent.getStringExtra("name");
            password = intent.getStringExtra("password");
            email = intent.getStringExtra("email");
            code = intent.getStringExtra("code");
            issharing = intent.getStringExtra("issharing");
            imageuri = intent.getParcelableExtra("imageuri");

        }

        t1.setText(code);
        if (imagepath == null) {

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(getApplicationContext(), "TEST 1", Toast.LENGTH_SHORT).show();

                    if (muploadtask != null && muploadtask.isInProgress()) {
                        Toast.makeText(invitecodeactivity.this, "Upload in progress ...", Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent2 = new Intent();
                        intent2.setType("image/*");
                        intent2.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent2, PICK_IMAGE);
                    }

                }
            });
        }

    }

    public void registeruser(View v)
    {
        progressDialog.setMessage("Please wait while we are creating an account for you");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {

                    user = auth.getCurrentUser();
                    userid = user.getUid();

                    createuser = new createuser(name,email,password,code,"false",0,0,"na",userid);

                    user = auth.getCurrentUser();

                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    reference.child(userid).setValue(createuser);
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "user registered successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                    Intent intent = new Intent(invitecodeactivity.this, login.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Failed to send email",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }


                    if (imagepath != null) {

                        progressDialog.setMessage("Relax a bit we are uploading your pic ...");
                        progressDialog.show();
                        StorageReference fileReference = mstorageref.child("users").child(userid).child("dp");

                        muploadtask = fileReference.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(invitecodeactivity.this, "Process success", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(invitecodeactivity.this, "Process failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Please choose an image", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {


            //Toast.makeText(aadhar.this,"test2",Toast.LENGTH_SHORT).show();

            imagepath = data.getData();

            Picasso.get().load(imagepath).into(imageView);

        }
    }

}
