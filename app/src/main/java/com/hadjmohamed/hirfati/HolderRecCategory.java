package com.hadjmohamed.hirfati;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HolderRecCategory extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView categoryName;


    public HolderRecCategory(@NonNull View itemView, RecViewInterface recViewInterface) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageHomeCategory);
        categoryName = itemView.findViewById(R.id.categoryNameHome);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recViewInterface != null){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        recViewInterface.onItemClick("Category", pos);
                    }
                }
            }
        });
    }
}
