package com.example.amblace1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class MainActivity extends AppCompatActivity {

    EditText p_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent i = new Intent(MainActivity.this, Home.class);
            startActivity(i);
            finish();
        }
        else{
            setContentView(R.layout.activity_main);
        }

        p_name = findViewById(R.id.phone_number_input);


        Button btn = findViewById(R.id.login_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p_name.getText().toString().isEmpty()) {
                    p_name.setError("Enter your phone number");
                    Toast.makeText(getApplicationContext(),"Enter your phone number",Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(MainActivity.this, SignIn.class);
                    startActivity(i);
                }
            }
        });
        Button btn1 = findViewById(R.id.button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });
    }
    public void open_signUp(View v){
        Intent i = new Intent(MainActivity.this, SignUp.class);
        startActivity(i);
    }
    public void open_signIn(View v){
        Intent i = new Intent(MainActivity.this, SignIn.class);
        startActivity(i);
    }

}