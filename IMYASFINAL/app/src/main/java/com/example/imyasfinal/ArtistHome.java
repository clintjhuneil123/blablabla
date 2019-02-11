package com.example.imyasfinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class ArtistHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

      FirebaseAuth mAuth;
//    FirebaseUser currentartist;

    TextView txtFullName,txtemail;
    ImageView spicture;


    FirebaseDatabase database;
    DatabaseReference artistadd, getCurrentUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<Artist, MenuViewHolder> adapter;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;


    //add
    MaterialEditText edtName;
    Button btnUpload,btnSelect;

    Artist newAddartist;
    Uri saveUri;


    DrawerLayout drawer;



    //



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        artistadd = database.getReference("Artist");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getCurrentUser = FirebaseDatabase.getInstance().getReference();




        mAuth = FirebaseAuth.getInstance();
//        currentartist = mAuth.getCurrentUser();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               showDialog();
//            }
//        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        spicture = (ImageView)headerView.findViewById(R.id.photo);
        txtFullName = (TextView)headerView.findViewById(R.id.txtartFullName);
        txtemail = (TextView)headerView.findViewById(R.id.txtartEmail);
//        txtFullName.setText(CommonArt.currentArt.getLastname());
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);



        loadMenu();

        getCurrentUser = FirebaseDatabase.getInstance().getReference("Artist");

        getCurrentUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String picture = dataSnapshot.child("image").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String name = dataSnapshot.child("firstname").getValue().toString();
                    Picasso.get().load(picture).into(spicture);
                    txtFullName.setText(name);
                    txtemail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        getCurrentUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String email = dataSnapshot.child("email").getValue().toString();
//                    String name = dataSnapshot.child("firstname").getValue().toString();
//                    txtFullName.setText(name);
//                    txtEmail.setText(email);
//
//                });




    }


    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Artist, MenuViewHolder>(Artist.class,R.layout.menu_item,MenuViewHolder.class,artistadd) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Artist model, int position) {
                viewHolder.txtMenuName.setText(model.getLastname());
                Picasso.get().load(model.getImage()).into(viewHolder.imageView);
                final  Artist clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent list = new Intent(ArtistHome.this, ArtistAddList.class);
                        list.putExtra("ArtistId", adapter.getRef(position).getKey());
                        startActivity(list);



                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.artist_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        mAuth.getInstance().signOut();
        Intent signIn = new Intent(ArtistHome.this,MainActivity.class);
        startActivity(signIn);

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
