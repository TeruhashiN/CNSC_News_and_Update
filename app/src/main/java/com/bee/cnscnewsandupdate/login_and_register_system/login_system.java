package com.bee.cnscnewsandupdate.login_and_register_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bee.cnscnewsandupdate.users_ui.MainActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bee.cnscnewsandupdate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class login_system extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPwd;
    private TextView ForgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG = "login_system";

    TextView create_account_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_system);

        create_account_text = (TextView) findViewById(R.id.register_account_text);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Login");
        }

        editTextLoginEmail = findViewById(R.id.email_login);
        editTextLoginPwd = findViewById(R.id.password_login);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();


        //reset password
        TextView ForgotPassword = findViewById(R.id.forgot_password);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(login_system.this, "You can reset your password now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(login_system.this, ForgotPasswordActivity.class));
            }
        });



        //Login user
        Button buttonLogin = findViewById(R.id.accountlogin_button);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail =  editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPwd.getText().toString();

                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(login_system.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is requred");
                    editTextLoginEmail.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(login_system.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid email is requred");
                    editTextLoginEmail.requestFocus();

                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(login_system.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Password is requred");
                    editTextLoginPwd.requestFocus();
                } else {
                    progressBar.setVisibility(view.VISIBLE);
                    loginUser(textEmail, textPwd);
                }
            }
        });





    }

    private void loginUser(String email, String pwd) {
        authProfile.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(login_system.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //Get instance of the current User
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();

                    //Check if email is verified or not
                    if (firebaseUser.isEmailVerified()) {
                        Toast.makeText(login_system.this, "You are logged in now", Toast.LENGTH_SHORT).show();

                        // Open User Profile
                        //start the User profile activity
                        startActivity(new Intent(login_system.this, MainActivity.class));
                        finish();
                    } else {
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut(); //Sign out user
                        showAlertDialog();
                    }

                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        editTextLoginEmail.setError("User does not exists or is no longer valid. Please register again.");
                        editTextLoginEmail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextLoginEmail.setError("Invalid credentials. Kindly, check and re-enter.");
                        editTextLoginEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(login_system.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(login_system.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please kindly verify your email.");

        //open Email Apps if user clicks/tap continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void onclickregisteraccount(View view) {
        Intent create_account_intent = new Intent(this, activity_register.class);
        finish();
        startActivity(create_account_intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() != null) {
            Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show();

            //start the User profile activity
            startActivity(new Intent(login_system.this, MainActivity.class));
            finish();
        }
        else {
            Toast.makeText(this, "You can login now!", Toast.LENGTH_SHORT).show();
        }
    }
}