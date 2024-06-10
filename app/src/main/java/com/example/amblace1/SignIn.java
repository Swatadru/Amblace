package com.example.amblace1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amblace1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    EditText e1_email,e2_password;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);



        e1_email = findViewById(R.id.editTextTextEmailAddress);
        e2_password = findViewById(R.id.editTextTextPassword);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

    }
    public void signinUser(View v){
        dialog.setMessage("Signing in... Please wait");
        dialog.show();
        if (e1_email.getText().toString().equals("") || e2_password.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
            dialog.hide();
        }
        else if(auth.getCurrentUser() != null){
            Button btn = findViewById(R.id.button2);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SignIn.this,Home.class);
                    startActivity(i);
                }
            });
        }
        else{
            auth.signInWithEmailAndPassword(e1_email.getText().toString(),e2_password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                dialog.hide();
                                Toast.makeText(getApplicationContext(),"User Sign in Successful",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SignIn.this,Home.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                dialog.hide();
                                Toast.makeText(getApplicationContext(),"User not found",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}