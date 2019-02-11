package com.example.imyasfinal.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imyasfinal.Common.CommonArt;
import com.example.imyasfinal.Interface.ItemClickListener;
import com.example.imyasfinal.R;

public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener{

    public TextView list_name;
    public ImageView list_image;

    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public ListViewHolder(@NonNull View itemView) {
        super(itemView);

        list_image = (ImageView) itemView.findViewById(R.id.list_image);
        list_name = (TextView) itemView.findViewById(R.id.list_name);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), CommonArt.UPDATE);
        contextMenu.add(0,0,getAdapterPosition(), CommonArt.DELETE);
    }
}
