package org.electromob.tracing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.widget.Toast.LENGTH_LONG;

public class login extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText username,userpassword;
    ProgressDialog progressDialog;
    TextView reset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.email);
        userpassword = (EditText)findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        reset = (TextView)findViewById(R.id.passwordreset1);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,passwordreset.class));
            }
        });

    }
    public void login(View v)
    {
        progressDialog.setMessage("Sit back loging in!!!");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(username.getText().toString(),userpassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"user login is successfull", LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(login.this,userlocation.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Wrong email entered", LENGTH_LONG).show();
                        }
                    }
                });
    }
}
