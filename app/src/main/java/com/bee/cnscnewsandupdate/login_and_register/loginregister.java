package com.bee.cnscnewsandupdate.login_and_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bee.cnscnewsandupdate.users_ui.MainActivity;
import com.bee.cnscnewsandupdate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginregister extends AppCompatActivity {

    TextView create_account_text;

    private FirebaseAuth auth;

    private Button loginButton;

    private EditText loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginregister);

        create_account_text = (TextView) findViewById(R.id.register_account_text);

        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.email_login);
        loginPassword = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.accountlogin_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(loginregister.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(loginregister.this, MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(loginregister.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        loginPassword.setError("Password cannot be empty");
                    }
                } else if (email.isEmpty()) {
                    loginEmail.setError("Email cannot be empty");
                } else {
                    loginEmail.setError("Please enter valid email");
                }

            }
        });


    }

    public void onclickregisteraccount(View view) {
        Intent create_account_intent = new Intent(this, createaccount.class);
        finish();
        startActivity(create_account_intent);
    }
}