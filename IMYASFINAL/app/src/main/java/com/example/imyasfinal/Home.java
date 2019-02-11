package com.example.imyasfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imyasfinal.Common.CommonArt;
import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    FirebaseDatabase database;
    DatabaseReference artist,getCurrentUser,getCurrentClient;

//    String categoryid="";
    FirebaseAuth mAuth;
    TextView txtFullName,txtEmail;

    RecyclerView recyler_menu;
    RecyclerView.LayoutManager LayoutManager;

    FirebaseRecyclerAdapter<Artist, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Artists");
        setSupportActionBar(toolbar);

        //
        String categoryid="";
        database = FirebaseDatabase.getInstance();
        artist = database.getReference("Artist");
        mAuth = FirebaseAuth.getInstance();
        getCurrentUser = FirebaseDatabase.getInstance().getReference();
        getCurrentClient = FirebaseDatabase.getInstance().getReference();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent cartIntent = new Intent(Home.this,Cart.class);
               startActivity(cartIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtEmail = (TextView)headerView.findViewById(R.id.txtEmail);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
//        txtFullName.setText(CommonArt.currentArt.getLastname());
        //txtFullName.setText(mAuth.getCurrentUser().getDisplayName());
        recyler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recyler_menu.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(this);
        recyler_menu.setLayoutManager(LayoutManager);



            loadMenu();


        getCurrentUser = FirebaseDatabase.getInstance().getReference("Artist");
        getCurrentClient = FirebaseDatabase.getInstance().getReference("Client");

        getCurrentUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String email = dataSnapshot.child("email").getValue().toString();
                            String  name = dataSnapshot.child("firstname").getValue().toString();
                            txtFullName.setText(name);
                            txtEmail.setText(email);

                        }
                else
                {
                    getCurrentClient.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String cemail = dataSnapshot.child("email").getValue().toString();
                            String cname = dataSnapshot.child("lastname").getKey().toString();
                            txtFullName.setText(cname);
                            txtEmail.setText(cemail);
                        }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Artist, MenuViewHolder>(Artist.class,R.layout.menu_item,MenuViewHolder.class, artist) {
        @Override
        protected void populateViewHolder(MenuViewHolder viewHolder, Artist model, int position) {
            Toast.makeText(Home.this, model.getLastname(), Toast.LENGTH_SHORT).show();
            viewHolder.txtMenuName.setText(model.getLastname());
            Picasso.get().load(model.getImage())
//               Picasso.with(getBaseContext()).load(model.getImage())
                    .into(viewHolder.imageView);
            final Artist clickItem = model;
            viewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    Intent list = new Intent(Home.this,ArtistList.class);
                    list.putExtra("ArtistId",adapter.getRef(position).getKey());
                    startActivity(list);
                }
            });

        }
    };

        recyler_menu.setAdapter(adapter);


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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        mAuth.getInstance().signOut();
        Intent signIn = new Intent(Home.this,MainActivity.class);
        startActivity(signIn);

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_artist) {

        }else if (id == R.id.nav_view) {
            startActivity(new Intent(Home.this, Cart.class));
            // Handle the camera action
        } else if (id == R.id.nav_book) {
            startActivity(new Intent(Home.this, PendingStatus.class));

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_log) {
            mAuth.getInstance().signOut();
            Intent signIn = new Intent(Home.this,MainActivity.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);

        }

        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.replace(R.id.recycler_menu, fragment);
            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
