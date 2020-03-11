package org.electromob.tracing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class registeractivity extends AppCompatActivity {

    private EditText email;
    FirebaseAuth auth;
    ImageView facebook_signin,google_signin;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);

        email = (EditText)findViewById(R.id.email1);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        google_signin = (ImageView)findViewById(R.id.google_signin);
        facebook_signin = (ImageView)findViewById(R.id.facebook_signin);

        google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Redirecting to google...");
                dialog.show();

                dialog.dismiss();
            }
        });

        facebook_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Redirecting to facebook...");
                dialog.show();

                dialog.dismiss();
            }
        });

    }
    public void gotopasswordactivity(View v)
    {
        dialog.setMessage("checking email address");
        dialog.show();
        auth.fetchSignInMethodsForEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful())
                        {
                            dialog.dismiss();
                            boolean check = !task.getResult().getSignInMethods().isEmpty();

                            if (!check)
                            {
                                Intent intent = new Intent(registeractivity.this,passwordactivity.class);
                                intent.putExtra("email",email.getText().toString());
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),"this email is already registered",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}
