package com.example.imyasfinal;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterArtist extends AppCompatActivity implements View.OnClickListener {
    Button btnNext, btnBack, btnFinish, btnHome;
    LinearLayout step1, step2;
    EditText shopFirstname, shopLastname, shopEmail, shopContact, shopPassword, shopName, shopLocation, shopDescription;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobilePattern = "^(09|\\+639)\\d{9}$";
    ProgressBar mProgress;
    private Uri pickedImgUri;
    ImageView ImgArtistPhoto;
    static int PreqCode = 1;
    static int REQUESCODE = 1;
    private static String imgpath = "";
    private static final int IMAGE_REQUEST_1 = 1;
    FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_artist);

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnFinish = findViewById(R.id.btnFinish);
        btnHome = findViewById(R.id.btnHome);
        mProgress = findViewById(R.id.progressBar3);
        step1 = findViewById(R.id.registerStep1);
        step2 = findViewById(R.id.registerStep2);

        ImgArtistPhoto = findViewById(R.id.artistphoto);

        ImgArtistPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT <= 22) {
                    checkandRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference();
        shopFirstname = findViewById(R.id.regshopFirstname);
        shopLastname = findViewById(R.id.regshopLastname);
        shopEmail = findViewById(R.id.regshopEmail);
        shopContact = findViewById(R.id.regshopContact);
        shopPassword = findViewById(R.id.regshopPassword);
        shopLocation = findViewById(R.id.regShopLocation);
        shopDescription = findViewById(R.id.regShopdescription);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnHome.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkandRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegisterArtist.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterArtist.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegisterArtist.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(RegisterArtist.this, new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, PreqCode);
            }

        } else
            openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_1 && resultCode == RESULT_OK) {
            pickedImgUri = data.getData();
            Picasso.get().load(pickedImgUri).fit().centerCrop().into(ImgArtistPhoto);

            pickedImgUri = data.getData();
            ImgArtistPhoto.setImageURI(pickedImgUri);
            final String path = System.currentTimeMillis() + "." + getFileExtension(pickedImgUri);

            StorageReference storageReference = mStorageRef.child("Images").child(path);
            storageReference.putFile(pickedImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    mStorageRef.child("Images/"+path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgpath = uri.toString();
                        }
                    });
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getApplication().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }



    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            Intent mainIntent = new Intent(this, Home.class);
            startActivity(mainIntent);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnNext) {
            step1.setVisibility(View.GONE);
            step2.setVisibility(View.VISIBLE);
        }

        if(view.getId() == R.id.btnBack){
            step1.setVisibility(View.VISIBLE);
            step2.setVisibility(View.GONE);
        }

        if(view.getId() == R.id.btnHome){
            Intent loginShop = new Intent(this, LoginArtist.class);
            startActivity(loginShop);
        }

        if(view.getId() == R.id.btnFinish){
            final String sFirstname = shopFirstname.getText().toString();
            final String sLastname = shopLastname.getText().toString();
            final String sEmail = shopEmail.getText().toString();
            final String sContact = shopContact.getText().toString();
            final String sPassword = shopPassword.getText().toString();
            final String sLocation = shopLocation.getText().toString();
            final String sDescription = shopDescription.getText().toString();

            if(TextUtils.isEmpty(sFirstname)){
                Toast.makeText(getApplicationContext(), "Enter firstname", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(sLastname)){
                Toast.makeText(getApplicationContext(), "Enter lastname", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(sEmail)){
                Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(sContact)){
                Toast.makeText(getApplicationContext(), "Enter contactnumber", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(sPassword)){
                Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(sLocation)){
                Toast.makeText(getApplicationContext(), "Enter shop location", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(sDescription)){
                Toast.makeText(getApplicationContext(), "Enter shop description", Toast.LENGTH_SHORT).show();
                return;
            }
            if(sPassword.length() < 6){
                Toast.makeText(getApplicationContext(), "Enter at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!sEmail.matches(emailPattern)){
                Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!sContact.matches(mobilePattern)){
                Toast.makeText(getApplicationContext(), "Invalid number", Toast.LENGTH_SHORT).show();
                return;
            }

            mProgress.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgress.setVisibility(View.GONE);
                            if(task.isSuccessful()){

                                Artist artists = new Artist(
                                        sFirstname,
                                        sLastname,
                                        sContact,
                                        sLocation,
                                        sDescription,
                                        sPassword,
                                        sEmail,
                                        imgpath
                                );

                                FirebaseDatabase.getInstance().getReference("Artist")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(artists).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterArtist.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterArtist.this, ArtistHome.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(RegisterArtist.this, "Registration Fail", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

        }
    }
}

