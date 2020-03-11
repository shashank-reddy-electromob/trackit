package org.electromob.tracing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class passwordreset extends AppCompatActivity {

    private EditText emailreset;
    private Button reset;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordreset);

        reset = (Button)findViewById(R.id.reset);
        emailreset = (EditText)findViewById(R.id.regemail);
        firebaseAuth = FirebaseAuth.getInstance();



        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = emailreset.getText().toString().trim();

                if (useremail.isEmpty()){
                    Toast.makeText(passwordreset.this,"Enter the email to recieve an reset link ...",Toast.LENGTH_LONG).show();

                }else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(passwordreset.this,"Password reset email sent..",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(passwordreset.this, login.class));
                            }else{
                                Toast.makeText(passwordreset.this,"Error in sending password reset email...",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
