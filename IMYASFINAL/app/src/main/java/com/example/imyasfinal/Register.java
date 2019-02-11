package com.example.imyasfinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText name;
    EditText username;
    EditText email;
    EditText contactnumber;
    EditText password;
    EditText firstname;
    EditText lastname;
    Button btnRegister, btnRegisterCancel;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobilePattern = "^(09|\\+639)\\d{9}$";
    ProgressBar mProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstname = findViewById(R.id.registerFirstname);
        lastname = findViewById(R.id.registerLastname);
        username = findViewById(R.id.registerUsername);
        email = findViewById(R.id.registerEmail);
        contactnumber = findViewById(R.id.registerContact);
        password = findViewById(R.id.registerPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegisterCancel = findViewById(R.id.btnRegisterCancel);
        mProgress = findViewById(R.id.progressBar);
        btnRegister.setOnClickListener(this);
        btnRegisterCancel.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            Intent dashhboardintent = new Intent(this, Home.class);
            startActivity(dashhboardintent);

        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnRegisterCancel){
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }

        if(view.getId() == R.id.btnRegister){

            final String finalFirstname = firstname.getText().toString();
            final String finalLastname = lastname.getText().toString();
            final String finalUsername = username.getText().toString();
            final String finalEmail = email.getText().toString();
            final String finalContact = contactnumber.getText().toString();
            final String finalPassword = password.getText().toString();

            if(TextUtils.isEmpty(finalFirstname)){
                Toast.makeText(getApplicationContext(), "Enter firstname", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(finalLastname)){
                Toast.makeText(getApplicationContext(), "Enter lastname", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(finalUsername)){
                Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(finalEmail)){
                Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(finalContact)){
                Toast.makeText(getApplicationContext(), "Enter contact number", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(finalPassword)){
                Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            if(finalPassword.length() < 6){
                Toast.makeText(getApplicationContext(), "Password too short... Minimum of 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!finalContact.matches(mobilePattern)){
                Toast.makeText(getApplicationContext(), "Please input a valid number", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!finalEmail.matches(emailPattern)){
                Toast.makeText(getApplicationContext(), "Please input a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            firstname.setVisibility(View.INVISIBLE);
            lastname.setVisibility(View.INVISIBLE);
            username.setVisibility(View.INVISIBLE);
            email.setVisibility(View.INVISIBLE);
            contactnumber.setVisibility(View.INVISIBLE);
            password.setVisibility(View.INVISIBLE);
            mProgress.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(finalEmail, finalPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgress.setVisibility(View.GONE);
                            firstname.setVisibility(View.VISIBLE);
                            lastname.setVisibility(View.VISIBLE);
                            username.setVisibility(View.VISIBLE);
                            email.setVisibility(View.VISIBLE);
                            contactnumber.setVisibility(View.VISIBLE);
                            password.setVisibility(View.VISIBLE);
                            if(task.isSuccessful()){
                                Client client = new Client(
                                        finalFirstname,
                                        finalLastname,
                                        finalUsername,
                                        finalContact,
                                        finalPassword,
                                        finalEmail
                                );
                                FirebaseDatabase.getInstance().getReference("Clients")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Register.this, "Succesful Registration", Toast.LENGTH_SHORT).show();
                                            Intent signin = new Intent(Register.this, Home.class);
                                            startActivity(signin);
                                        }
                                        else{
                                            Toast.makeText(Register.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
//                                    Toast.makeText(Register.this, "Registered Successful", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Register.this, "Registration Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }




}


