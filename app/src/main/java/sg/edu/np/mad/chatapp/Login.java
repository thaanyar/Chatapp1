package sg.edu.np.mad.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();



        final AppCompatButton loginBtn = findViewById(R.id.l_loginBtn);

        final EditText email = findViewById(R.id.l_email);
        final EditText password = findViewById(R.id.l_password);


        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();


                mAuth.signInWithEmailAndPassword(emailTxt, passwordTxt)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Login.this, "Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



    }
}