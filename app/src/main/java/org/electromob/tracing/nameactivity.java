package org.electromob.tracing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class nameactivity extends AppCompatActivity {

    String email,password;
    EditText name;
    CircleImageView circleImageView;
    Uri resulturi,imagepath;
    FirebaseUser user;
    ProgressDialog progressDialog;
    StorageTask muploadtask;
    FirebaseAuth firebaseAuth;
    ImageView imageView;
    FirebaseStorage firebaseStorage;
    int PICK_IMAGE = 123;
    StorageReference mstorageref;
    private Bitmap bitmap1;
    private int PERMISSION_CODE = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nameactivity);
        name = (EditText) findViewById(R.id.infoname);
        //circleImageView = (CircleImageView)findViewById(R.id.circleimageview);

        firebaseStorage = FirebaseStorage.getInstance();

        mstorageref = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        Intent intent3 = getIntent();
        if (intent3 != null) {
            email = intent3.getStringExtra("email");
            password = intent3.getStringExtra("password");

        }




    }

            public void generatecode(View v) {


                String userid;
                userid = firebaseAuth.getUid();

                Date mydate = new Date();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss a", Locale.getDefault());
                String date = format1.format(mydate);
                Random r = new Random();

                int n = 100000 + r.nextInt(9000000);
                String code = String.valueOf(n);




                    Intent intent = new Intent(nameactivity.this, invitecodeactivity.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("date", date);
                    intent.putExtra("issharing", "false");
                    intent.putExtra("code", code);
                    intent.putExtra("imageuri", resulturi);


                    startActivity(intent);
                    finish();


            }
   /* StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(user.getUid()).child("dp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {
            Picasso.get().load(uri).into(imageView);
        }
    });*/



    }
