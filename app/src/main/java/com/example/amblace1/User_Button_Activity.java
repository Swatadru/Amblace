package com.example.amblace1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Button_Activity extends AppCompatActivity {

    FirebaseAuth auth;
    ImageButton imgButton;
    FirebaseAuth Auth;
    FirebaseUser user;
    String user_id;
    DatabaseReference reference;
    TextView t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_button);
        auth = FirebaseAuth.getInstance();
        Button btn = findViewById(R.id.Signout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    auth.signOut();
                    Intent intent = new Intent(User_Button_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"User is already signed out",Toast.LENGTH_LONG).show();
                }
            }
        });
        imgButton = findViewById(R.id.imageButton4);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(User_Button_Activity.this, Home.class);
                startActivity(i);
            }
        });
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(User_Button_Activity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else {
            user_id = user.getUid();
            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    t1 = findViewById(R.id.textView3);
                    t2 = findViewById(R.id.textView2);
                    t1.setText(name);
                    t2.setText(email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}