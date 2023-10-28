package com.hadjmohamed.hirfati.Admin;

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
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;
import com.hadjmohamed.hirfati.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdapterRecUser extends RecyclerView.Adapter<HolderRecUser>{

    private final RecViewInterface recViewInterface;
    Context context;
    List<User> userList;

    public AdapterRecUser(Context context, List<User> userList, RecViewInterface recViewInterface) {
        this.context = context;
        this.userList = userList;
        this.recViewInterface = recViewInterface;
    }

    @NonNull
    @Override
    public HolderRecUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecUser(LayoutInflater.from(context).
                inflate(R.layout.user_item, parent, false), recViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecUser holder, int position) {
        holder.userUid.setText(userList.get(position).getIdUser());
        holder.userName.setText(userList.get(position).getName() + " " + userList.get(position).getFamilyName());
        retrieveImage(holder.imageView, userList.get(position).getIdUser());
    }

    @Override
    public int getItemCount() {
        return userList.size();
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
}
