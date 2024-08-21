package com.hadjmohamed.hirfati.Adapter.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;

public class HolderRecCraftsmen extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameAndFamilyName, crafts, description;
    RatingBar ratingBar;

    public HolderRecCraftsmen(@NonNull View itemView,  RecViewInterface recViewInterface) {
        super(itemView);
        nameAndFamilyName = itemView.findViewById(R.id.nameHome);
        crafts = itemView.findViewById(R.id.craftHome);
        description = itemView.findViewById(R.id.decsHome);
        imageView = itemView.findViewById(R.id.userImageHome);
        ratingBar = itemView.findViewById(R.id.ratingBarItem);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recViewInterface != null){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        recViewInterface.onItemClick("Craftsmen", pos);
                    }
                }
            }
        });
    }
}
