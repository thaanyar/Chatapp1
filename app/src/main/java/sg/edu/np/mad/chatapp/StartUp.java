package sg.edu.np.mad.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.mad.chatapp.bottomNav.NavMainPage;

public class StartUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Session session;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);


        final AppCompatButton gLogin = findViewById(R.id.g_login);
        final AppCompatButton gRegis = findViewById(R.id.g_register);

        session = new Session(StartUp.this);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String getNumber = mAuth.getCurrentUser().getDisplayName();

                    String name = snapshot.child("users").child(getNumber).child("name").getValue(String.class);
                    String profilePic = snapshot.child("users").child(getNumber).child("profile_pic").getValue(String.class);
//                String bio = snapshot.child("users").child(getNumber).child("bio").getValue(String.class);
//                String email = snapshot.child("users").child(getNumber).child("email").getValue(String.class);

                    Intent intent = new Intent(StartUp.this, NavMainPage.class);
                    intent.putExtra("mobile", getNumber);
                    intent.putExtra("name", name);
                    intent.putExtra("email", "");

                    session.setusername(name);
                    session.setprofilePic(profilePic);


                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }





        gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartUp.this, Login.class);
                startActivity(intent);
            }
        });


        gRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartUp.this, registerpage.class);
                startActivity(intent);
            }
        });


    }
}