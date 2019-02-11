package com.example.imyasfinal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.imyasfinal.Common.CommonArt;
import com.example.imyasfinal.Database.Database;
import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.ViewHolder.ListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class ArtistAddList extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;
    RelativeLayout rootLayout;
    FirebaseDatabase db;
    DatabaseReference list1;
    Query list2;
    FirebaseStorage storage;
    StorageReference storageReference;

    String artistId="";
    Uri saveUri;
    FirebaseRecyclerAdapter<ArtistPorfolio, ListViewHolder> searchadapter;


    MaterialEditText edtName,edtDesc,edtPrice;
    Button btnUpload,btnSelect;

    ArtistPorfolio newartistPorfolio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_add_list);

        db=FirebaseDatabase.getInstance();
        recyclerView =(RecyclerView) findViewById(R.id.recycler_list1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showaddartist();
            }
        });



        if(getIntent() !=null){
            artistId = getIntent().getStringExtra("ArtistId");
            list2 = FirebaseDatabase.getInstance().getReference("ArtistPortfolio").orderByChild("artistID").equalTo(artistId);
            Toast.makeText(this, artistId, Toast.LENGTH_SHORT).show();
            LoadList(artistId);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CommonArt.PICK_IMAGE_REQUEST && requestCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Image Selected!");
        }
    }

    private void uploadImage() {
        if(saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading..");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/+imageName");
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(ArtistAddList.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newartistPorfolio = new ArtistPorfolio();
                                    newartistPorfolio.setName(edtName.getText().toString());
                                    newartistPorfolio.setDescription(edtDesc.getText().toString());
                                    newartistPorfolio.setPrice(edtPrice.getText().toString());
                                    newartistPorfolio.setArtistID(artistId);
                                    newartistPorfolio.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(ArtistAddList.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded "+progress+"%");
                        }
                    });
        }
    }

    private void showaddartist() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ArtistAddList.this);
        alertDialog.setTitle("Add new Portfolio");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_artist_layout, null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtDesc = add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);


        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });



        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(newartistPorfolio != null)
                {
                    list1.push().setValue(newartistPorfolio);
                    Snackbar.make(rootLayout,"New Portfolio"+newartistPorfolio.getName()+"was added",Snackbar.LENGTH_SHORT)
                            .show();;
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        alertDialog.show();

    }


    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), CommonArt.PICK_IMAGE_REQUEST);
    }

    private void LoadList(String artistId) {
        searchadapter = new FirebaseRecyclerAdapter<ArtistPorfolio, ListViewHolder>(ArtistPorfolio.class,R.layout.listitem,ListViewHolder.class,list2) {
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, ArtistPorfolio model, int position) {
                viewHolder.list_name.setText(model.getName());
                Picasso.get().load(model.getImage())
//                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.list_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }
        };
        searchadapter.notifyDataSetChanged();
        recyclerView.setAdapter(searchadapter);
    }
}
