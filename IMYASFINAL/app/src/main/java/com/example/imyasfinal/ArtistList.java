package com.example.imyasfinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.ViewHolder.ListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArtistList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    Query list;
    String artistId="";

    FirebaseRecyclerAdapter<ArtistPorfolio, ListViewHolder> adapter;

    //Search
    FirebaseRecyclerAdapter<ArtistPorfolio, ListViewHolder> searchadapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);

        database = FirebaseDatabase.getInstance();


        recyclerView =(RecyclerView) findViewById(R.id.recycler_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent() !=null){
            artistId = getIntent().getStringExtra("ArtistId");
            list = FirebaseDatabase.getInstance().getReference("ArtistPortfolio").orderByChild("artistID").equalTo(artistId);
            Toast.makeText(this, artistId, Toast.LENGTH_SHORT).show();
            LoadList(artistId);
        }

}

    private void LoadList(String artistId) {
        adapter = new FirebaseRecyclerAdapter<ArtistPorfolio, ListViewHolder>(ArtistPorfolio.class,R.layout.listitem,ListViewHolder.class,list) {
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, ArtistPorfolio model, int position) {
                viewHolder.list_name.setText(model.getName());
                Picasso.get().load(model.getImage())
//                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.list_image);

                final ArtistPorfolio  local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent artistDetail = new Intent(ArtistList.this,ArtistDetail.class);
                        artistDetail.putExtra("ArtistID",adapter.getRef(position).getKey());
                        startActivity(artistDetail);


                        }
                });

            }
        };
        recyclerView.setAdapter(adapter);

      }


}
