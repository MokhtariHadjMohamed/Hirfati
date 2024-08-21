package com.hadjmohamed.hirfati.Adapter.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;

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
                        recViewInterface.onItemClick("Crafts", pos);
                    }
                }
            }
        });
    }
}
