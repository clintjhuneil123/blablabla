package com.example.imyasfinal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView register, shopView;
    Button cancelRegister, loginButton;
    EditText loginUsername, loginPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference1,databaseReference2;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.register  = this.findViewById(R.id.textView);
        this.loginButton = this.findViewById(R.id.button);
        shopView = findViewById(R.id.shopView);
        this.loginUsername = this.findViewById(R.id.editText);
        this.loginPassword = this.findViewById(R.id.editText2);
        shopView.setOnClickListener(this);
        register.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        //this.register.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!networkConnection()){
            Toast.makeText(this, "No Internet Connection. Please check internet connection", Toast.LENGTH_SHORT).show();
        }
        else{
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if(currentUser != null){
                mDatabase.getReference("Artists").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Intent intent = new Intent(MainActivity.this, ArtistHome.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Artistssss", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Clientttttt", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
//            else{
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private boolean networkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }




    public void login(){
        String logUsername = this.loginUsername.getText().toString();
        final String logPassword = this.loginPassword.getText().toString();

        if(TextUtils.isEmpty(logUsername)){
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(logPassword)){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }


        loginUsername.setVisibility(View.INVISIBLE);
        loginPassword.setVisibility(View.INVISIBLE);
        mAuth.signInWithEmailAndPassword(logUsername, logPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loginUsername.setVisibility(View.VISIBLE);
                            loginPassword.setVisibility(View.VISIBLE);
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            successful();
                        }
                        else{
                            loginUsername.setVisibility(View.VISIBLE);
                            loginPassword.setVisibility(View.VISIBLE);
                            loginUsername.setText("");
                            loginPassword.setText("");
                            error();
                        }
                    }
                });

    }
    public void successful(){
        Intent successIntent = new Intent(this, Home.class);
        startActivity(successIntent);
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
    }

    public void error(){
        if(!networkConnection()){
            Toast.makeText(this, "No Internet Connection. Please check internet connection", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id){
            case R.id.textView:
                Intent intent = new Intent(this, Register.class);
                startActivity(intent);
                break;
            case R.id.shopView:
                Intent shopReg = new Intent(this, LoginArtist.class);
                startActivity(shopReg);
                break;
            case R.id.button:
                login();
        }
    }
}
