package com.hadjmohamed.hirfati;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdapterRecCategoryHor extends RecyclerView.Adapter<HolderRecCategory> {

    private final RecViewInterface recViewInterface;
    Context context;
    List<Crafts> craftsList;

    public AdapterRecCategoryHor(Context context, List<Crafts> craftsList, RecViewInterface recViewInterface) {
        this.context = context;
        this.craftsList = craftsList;
        this.recViewInterface = recViewInterface;
    }

    @NonNull
    @Override
    public HolderRecCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecCategory(LayoutInflater.from(context).
                inflate(R.layout.category_item_rec_hor, parent, false), recViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecCategory holder, int position) {
        holder.categoryName.setText(craftsList.get(position).getName());
        retrieveImage(holder.imageView, craftsList.get(position).getName());
    }


    private void retrieveImage(ImageView imageView, String image) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("Image")
                .child(image);

        final File file;
        try {
            file = File.createTempFile("img", "png");

            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
                    Log.e("Image " + image, "Failed");
                }
            });
        } catch (IOException e) {
            imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
            throw new RuntimeException(e);
        }

    }


    @Override
    public int getItemCount() {
        return craftsList.size();
    }
}
