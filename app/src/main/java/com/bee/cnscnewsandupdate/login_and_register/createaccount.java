package com.bee.cnscnewsandupdate.login_and_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bee.cnscnewsandupdate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class createaccount extends AppCompatActivity {
    TextView login_back_text;

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        login_back_text = (TextView) findViewById(R.id.login_text_in_create);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.create_email);
        signupPassword = findViewById(R.id.create_password);
        signupButton = findViewById(R.id.create_account_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();

                if (user.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                }
                if (pass.isEmpty()) {
                    signupPassword.setError("Password cannot be empty");
                } else {
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(createaccount.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(createaccount.this, loginregister.class));
                            } else {
                                Toast.makeText(createaccount.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    public void login_back(View view) {
        Intent login_back_create = new Intent(this, loginregister.class);
        startActivity(login_back_create);
        finish();
    }
}