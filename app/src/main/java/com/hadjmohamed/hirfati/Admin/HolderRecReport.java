package com.hadjmohamed.hirfati.Admin;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;

public class HolderRecReport extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView reporter, reported, desc;

    public HolderRecReport(@NonNull View itemView, RecViewInterface recViewInterface) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageReport);
        reported = itemView.findViewById(R.id.reportedName);
        reporter = itemView.findViewById(R.id.reporterName);
        desc = itemView.findViewById(R.id.reportText);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recViewInterface != null){
                    int pos = getAdapterPosition();
                    Log.d("NUMBER:", String.valueOf(pos));
                    if (pos != RecyclerView.NO_POSITION)
                        recViewInterface.onItemClick("report", pos);
                }
            }
        });
    }

}
