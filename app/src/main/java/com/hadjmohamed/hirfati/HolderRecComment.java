package com.hadjmohamed.hirfati;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HolderRecComment extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameAndFamilyName, description;

    public HolderRecComment(@NonNull View itemView, RecViewInterface recViewInterface) {
        super(itemView);
        nameAndFamilyName = itemView.findViewById(R.id.nameComment);
        description = itemView.findViewById(R.id.commentText);
        imageView = itemView.findViewById(R.id.imageComment);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recViewInterface != null){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        recViewInterface.onItemClick("Comment", pos);
                    }
                }
            }
        });
    }
}
