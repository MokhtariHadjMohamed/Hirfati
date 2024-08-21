package com.hadjmohamed.hirfati.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hadjmohamed.hirfati.R;

import java.util.List;

public class GridAdapterUser extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<String> works;
    ProgressDialog progressDialog;

    public GridAdapterUser(Context context, List<String> works) {
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

        deleteImage.setImageResource(0);

        retrieveImage(workImage, works.get(i).toString(), imageType);

        return view;
    }

    private void retrieveImage(ImageView imageView, String image,
                               TextView textView) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("Image/")
                .child(image);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(imageView);
                textView.setText("fill");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
                textView.setText("none");
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
