package com.hadjmohamed.hirfati;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
public class AdapterRecCraftsmen extends RecyclerView.Adapter<HolderRecCraftsmen> {

    private RecViewInterface recViewInterface;
    Context context;
    List<Craftsman> craftsmanList;
    public AdapterRecCraftsmen(Context context, List<Craftsman> craftsmanList,RecViewInterface recViewInterface) {
        this.context = context;
        this.craftsmanList = craftsmanList;
        this.recViewInterface = recViewInterface;
    }

    @NonNull
    @Override
    public HolderRecCraftsmen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecCraftsmen(LayoutInflater.from(context).
                inflate(R.layout.craftsman_item_rec, parent, false), recViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecCraftsmen holder, int position) {
        if (craftsmanList.get(position) == null){
            Log.e("ERROR", "______________________________");
            return;
        }


        holder.nameAndFamilyName.setText(String.valueOf(craftsmanList.get(position).getName() + " "
                + craftsmanList.get(position).getFamilyName()));
        holder.crafts.setText(craftsmanList.get(position).getCraft());
        holder.description.setText(craftsmanList.get(position).getDescription());
        retrieveImage(holder.imageView, craftsmanList.get(position).getIdUser());
    }

    private void retrieveImage(ImageView imageView, String image) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("Image")
                .child(image);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
                Log.e("Image" + image, e.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return craftsmanList.size();
    }
}
