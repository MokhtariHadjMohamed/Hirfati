package com.hadjmohamed.hirfati.Admin;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;

public class HolderRecUser extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView userName, userUid;

    public HolderRecUser(@NonNull View itemView, RecViewInterface recViewInterface) {
        super(itemView);

        userUid = itemView.findViewById(R.id.userUdiAdmin);
        imageView = itemView.findViewById(R.id.userImageAdmin);
        userName = itemView.findViewById(R.id.userNameAdmin);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recViewInterface != null){
                    int pos = getAdapterPosition();
                    Log.d("NUMBER:", String.valueOf(pos));
                    if (pos != RecyclerView.NO_POSITION)
                        recViewInterface.onItemClick("user", pos);
                }
            }
        });

    }

}
