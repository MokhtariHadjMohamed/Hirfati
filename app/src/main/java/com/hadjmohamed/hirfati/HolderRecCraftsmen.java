package com.hadjmohamed.hirfati;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HolderRecCraftsmen extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameAndFamilyName, crafts, description;

    public HolderRecCraftsmen(@NonNull View itemView) {
        super(itemView);
        nameAndFamilyName = itemView.findViewById(R.id.nameHome);
        crafts = itemView.findViewById(R.id.craftHome);
        description = itemView.findViewById(R.id.decsHome);
        imageView = itemView.findViewById(R.id.userImageHome);
    }
}