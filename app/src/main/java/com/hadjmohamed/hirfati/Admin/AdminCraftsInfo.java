package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hadjmohamed.hirfati.Crafts;
import com.hadjmohamed.hirfati.ImageResizer;
import com.hadjmohamed.hirfati.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AdminCraftsInfo extends AppCompatActivity implements View.OnClickListener {

    // Element
    private TextView numberCrafts, errorAdmin;
    private EditText nameCrafts, descCrafts;
    private Button submit, delete;
    private EditText[] editTexts;
    private CardView editImage;
    // Toolbar
    private TextView textViewToolsBar;
    private ImageView backArrowToolsBar;
    private String idCraft;
    // Image
    private static final int PICK_IMAGE_REQUEST = 1;
    private CardView uploadImage;
    private Uri imageUri;
    private ImageView craftImage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    // Firestore
    private FirebaseFirestore firestore;
    private String uid;
    // progressDialog
    private ProgressDialog progressDialog;
    private Crafts crafts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_crafts_info);
        idCraft = getIntent().getStringExtra("idCraft");

        // FireBase
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        uid = getIntent().getStringExtra("idCraft");
        crafts = new Crafts();

        // Toolbar
        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        textViewToolsBar = findViewById(R.id.toolbarTitle);
        backArrowToolsBar = findViewById(R.id.backArrow);

        textViewToolsBar.setText("الحرفة");
        backArrowToolsBar.setOnClickListener(this);

        // Element
        numberCrafts = findViewById(R.id.craftsNumberAdminCraftsInfo);
        nameCrafts = findViewById(R.id.nameCraftAdminCraftsInfo);
        descCrafts = findViewById(R.id.descAdminCraftsInfo);
        editImage = findViewById(R.id.editImageCraftsmanAccountInfo);
        craftImage = findViewById(R.id.ImageAdminCraftsAccountInfo);
        errorAdmin = findViewById(R.id.errorAdminCraftsmanAccountInfo);
        submit = findViewById(R.id.submitAdminCraftsInfo);
        submit.setOnClickListener(this);
        delete = findViewById(R.id.deleteCraftAdminCraftsInfo);
        delete.setOnClickListener(this);

        editTexts = new EditText[]{nameCrafts, descCrafts};

        getCrafts();
    }

    private boolean editTest(EditText[] editTexts) {
        for (EditText e : editTexts) {
            if (e.getText().toString().isEmpty()) {
                e.setBackgroundResource(R.drawable.custom_input_error);
                errorAdmin.setText("إملء كل خانات");
                return false;
            }
            e.setBackgroundResource(R.drawable.custom_input);
        }
        errorAdmin.setText("");
        return true;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmapOrigin = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Bitmap bitmap = ImageResizer.reduceBitmapSize(bitmapOrigin, 307200);
                craftImage.setImageBitmap(bitmap);
                Toast.makeText(this, "Upload", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error Upload", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Filed", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(String udi) {
        if (imageUri == null)
            return;

        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference ref = storageReference.child("Image/"
                + udi);
        Bitmap bitmapOrigin = null;
        try {
            bitmapOrigin = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //convert uri to byteArray
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Bitmap bitmap = ImageResizer.reduceBitmapSize(bitmapOrigin, 307200);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        ref.putBytes(bytes.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast
                                .makeText(AdminCraftsInfo.this,
                                        "Image Uploaded!!",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminCraftsInfo.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    // Progress Listener for loading
                    // percentage on the dialog box
                    @Override
                    public void onProgress(
                            UploadTask.TaskSnapshot taskSnapshot) {
                        double progress
                                = (100.0
                                * taskSnapshot.getBytesTransferred()
                                / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage(
                                "Uploaded "
                                        + (int) progress + "%");
                        if (progress == 100)
                            progressDialog.cancel();
                    }
                });
    }

    private void getCrafts(){
        firestore.collection("Crafts")
                .document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("GetUsers", "failed");
                            return;
                        }
                        Crafts crafts = task.getResult().toObject(Crafts.class);
                        numberCrafts.setText(crafts.getUid());
                        nameCrafts.setText(crafts.getName());
                        retrieveImage(craftImage, crafts.getName());
                        descCrafts.setText(crafts.getDesc());
                    }
                });
    }

    private void updateCrafts(String uid){
        firestore.collection("Crafts")
                .document(uid)
                .update(crafts.toHashMap());
        startActivity(new Intent(AdminCraftsInfo.this, AdminCrafts.class));
        finish();
    }
    private void deleteCrafts(String uid){
        firestore.collection("Reports")
                .document(uid)
                .delete();
        StorageReference ref = storageReference.child("Image/"
                + nameCrafts.getText().toString());
        ref.delete();
    }

    @Override
    public void onClick(View view) {
        if (view == backArrowToolsBar){
            startActivity(new Intent(AdminCraftsInfo.this, AdminCrafts.class));
            finish();
        } else if (view == submit) {
            if (editTest(editTexts)){
                crafts.setUid(uid);
                crafts.setName(nameCrafts.getText().toString());
                crafts.setDesc(descCrafts.getText().toString());
                updateCrafts(uid);
                uploadImage(uid);
            }
        } else if (view == delete) {
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
            dialogBuilder.setMessage("هل تريد حذف هذه الحرفة؟");
            dialogBuilder.setTitle("الطلب");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteCrafts(uid);
                }
            }).setNegativeButton("لا", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });

        } else if (view == craftImage) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(
                    intent,
                    "Select Image from here..."), PICK_IMAGE_REQUEST);
        }
    }
}