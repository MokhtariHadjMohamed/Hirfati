package com.hadjmohamed.hirfati;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdapterRecComment extends RecyclerView.Adapter<HolderRecComment> {

    private Context context;
    private List<Comment> commentList;
    private final RecViewInterface recViewInterface;

    public AdapterRecComment(Context context, List<Comment> commentList, RecViewInterface recViewInterface) {
        this.context = context;
        this.commentList = commentList;
        this.recViewInterface = recViewInterface;
    }

    @NonNull
    @Override
    public HolderRecComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecComment(LayoutInflater.from(context)
                .inflate(R.layout.comment_item, parent, false), recViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecComment holder, int position) {
        if(commentList.get(position) == null){
            Log.e("ERROR", "______________________________");
            return;
        }
        holder.description.setText(commentList.get(position).getText());
        holder.ratingBar.setRating(commentList.get(position).getRate());
        getUser(commentList.get(position), holder.imageView, holder.nameAndFamilyName);
    }

    private void getUser(Comment comment, ImageView imageView, TextView nameAndFamilyName) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(comment.getUidUser())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.d("get User: ", "failed");
                            return;
                        }
                        User user = task.getResult().toObject(Craftsman.class);
                        retrieveImage(imageView, user.getIdUser());
                        nameAndFamilyName.setText(String.valueOf(user.getName() + " " + user.getFamilyName()));
                    }
                });
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
        return commentList.size();
    }
}
