package com.hadjmohamed.hirfati;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<String> works;
    ProgressDialog progressDialog;

    public GridAdapter(Context context, List<String> works) {
        this.context = context;
        this.works = works;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return works.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = inflater.inflate(R.layout.works, null);

        ImageView workImage, deleteImage;
        TextView imageType;

        // Progress
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        workImage = view.findViewById(R.id.workImageCraftsmen);
        deleteImage = view.findViewById(R.id.deleteImageCraftsmen);
        imageType = view.findViewById(R.id.imageTypeCraftsmen);

        retrieveImage(workImage, works.get(i).toString(), deleteImage, imageType);

        return view;
    }

    private void retrieveImage(ImageView imageView, String image,
                               ImageView delete, TextView textView) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("Image/")
                .child(image);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(imageView);
                delete.setImageResource(R.drawable.trach);
                textView.setText("fill");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                delete.setImageResource(0);
                imageView.setImageResource(R.drawable.add_work);
                Log.e("Image" + image, e.getMessage());
                textView.setText("none");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
