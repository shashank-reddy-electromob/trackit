package org.electromob.tracing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class passwordactivity extends AppCompatActivity {

    String email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordactivity);
        password = (EditText)findViewById(R.id.password1);
        Intent intent = getIntent();
        if (intent!=null)
        {
            email = intent.getStringExtra("email");
        }
    }
    public void gotonamepickactivity(View v)
    {
        if (password.getText().toString().length() > 6)
        {
            Intent intent = new Intent(passwordactivity.this,nameactivity.class);
            intent.putExtra("email",email);
            intent.putExtra("password",password.getText().toString());
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Password length not matched",Toast.LENGTH_LONG).show();
        }
    }
}
