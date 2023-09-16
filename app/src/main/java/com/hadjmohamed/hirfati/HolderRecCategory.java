package com.hadjmohamed.hirfati;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HolderRecCategory extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView categoryName;


    public HolderRecCategory(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageHomeCategory);
        categoryName = itemView.findViewById(R.id.categoryNameHome);
    }
}
