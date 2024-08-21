package com.hadjmohamed.hirfati;

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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hadjmohamed.hirfati.Adapter.GridAdapterAdmin;
import com.hadjmohamed.hirfati.model.Craftsman;
import com.hadjmohamed.hirfati.model.ImageResizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CraftsmanAccountActivity extends AppCompatActivity implements View.OnClickListener {
    // Element
    private TextView nameAndFamilyName, crafts, desc;
    private RatingBar ratingBar;
    private ImageView craftsmanInfoImage;
    private TextView accountInfoCraftsmanAccount, settingCraftsmanAccount, logOutCraftsAccount;
    // toolbar
    private Toolbar toolbar;
    private ImageView imageViewToolBar;
    private SearchView search;
    // Image
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView workImage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private int position = 0;
    // GridView
    private GridView gridView;
    private GridAdapterAdmin gridAdapterAdmin;
    public static List<String> works;

    // Firebase
    private FirebaseFirestore firestore;
    private String idUser;
    // Dialog Image delete
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craftsman_account);
        firestore = FirebaseFirestore.getInstance();
        idUser = FirebaseAuth.getInstance().getUid();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        // ToolsBar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        imageViewToolBar = findViewById(R.id.imageToolBar);
        search = findViewById(R.id.search);

        setSupportActionBar(toolbar);
        retrieveImage(imageViewToolBar, idUser);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(CraftsmanAccountActivity.this,
                        SearchPageActivity.class);
                intent.putExtra("search", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Element
        nameAndFamilyName = findViewById(R.id.craftsmanInfoNameAndFamilyName);
        crafts = findViewById(R.id.craftsmanInfoCrafts);
        desc = findViewById(R.id.craftsmanInfoDesc);
        craftsmanInfoImage = findViewById(R.id.craftsmanInfoImage);
        ratingBar = findViewById(R.id.ratingBarCraftsActivity);

        gridView = findViewById(R.id.worksCraftsmenAccount);

        accountInfoCraftsmanAccount = findViewById(R.id.accountInfoCraftsmanAccount);
        settingCraftsmanAccount = findViewById(R.id.settingCraftsmanAccount);
        logOutCraftsAccount = findViewById(R.id.logOutCraftsmanAccount);

        accountInfoCraftsmanAccount.setOnClickListener(this);
        settingCraftsmanAccount.setOnClickListener(this);
        logOutCraftsAccount.setOnClickListener(this);

        getUser();
        // navigation bar Bottom
        BottomNavigation();
    }

    private void gridView(List<String> works) {
        // GridView
        gridAdapterAdmin = new GridAdapterAdmin(CraftsmanAccountActivity.this, works);
        gridView.setAdapter(gridAdapterAdmin);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView imageView = view.findViewById(R.id.imageTypeCraftsmen);
                workImage = view.findViewById(R.id.workImageCraftsmen);
                if (imageView.getText().toString().equals("none")) {
                    Toast.makeText(CraftsmanAccountActivity.this, "Add", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(
                            intent,
                            "Select Image from here..."), PICK_IMAGE_REQUEST);
                    position = i;
                } else if (imageView.getText().toString().equals("fill")) {
                    Toast.makeText(CraftsmanAccountActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                    imageDelete(idUser + "-" + (i + 1));
                }

            }
        });
    }

    private void deleteImageFirestor(String work) {
        firestore.collection("Users")
                .document(idUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.d("get User: ", "failed");
                            return;
                        }
                        Craftsman craftsman = task.getResult().toObject(Craftsman.class);
                        works = craftsman.getWorks();
                        works.remove(work);
                        firestore.collection("Users").document(idUser)
                                .update("works", works).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        finish();
                                        startActivity(getIntent());
                                    }
                                });
                    }
                });
    }

    private void imageDelete(String uid) {
        dialogBuilder = new MaterialAlertDialogBuilder(CraftsmanAccountActivity.this);
        dialogBuilder.setMessage("هل تريد حذف هذه  صورة؟");
        dialogBuilder.setTitle("الطلب");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StorageReference ref = storageReference.child("Image/"
                        + uid);
                ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CraftsmanAccountActivity.this, "Delete done", Toast.LENGTH_SHORT).show();
                        deleteImageFirestor(uid);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Failure", e.getMessage());
                    }
                });
            }
        }).setNegativeButton("لا", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        dialogBuilder.show();

        alertDialog = dialogBuilder.create();
        alertDialog.show();
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
                workImage.setImageBitmap(bitmap);
                Toast.makeText(this, "Upload", Toast.LENGTH_SHORT).show();
                uploadImage(idUser + "-" + (position + 1));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error Upload", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Filed", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(String uid) {
        if (imageUri == null) {
            return;
        }

        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference ref = storageReference.child("Image/"
                + uid);
        Bitmap bitmapOrigin = null;
        try {
            bitmapOrigin = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //convert uri to byteArray
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Bitmap bitmap = ImageResizer.reduceBitmapSize(bitmapOrigin, 614400);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        ref.putBytes(bytes.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(CraftsmanAccountActivity.this,
                                        "Image Uploaded!!",
                                        Toast.LENGTH_SHORT)
                                .show();
                        finish();
                        startActivity(getIntent());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CraftsmanAccountActivity.this,
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
        listImageUpdate(uid);
    }

    private void BottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_home);
        bottomNavigationView.setSelectedItemId(R.id.accountNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeNavigation) {
                startActivity(new Intent(CraftsmanAccountActivity.this, HomePageActivity.class));
                return true;
            } else if (id == R.id.searchNavigation) {
                startActivity(new Intent(CraftsmanAccountActivity.this, SearchPageActivity.class));
                return true;
            } else if (id == R.id.accountNavigation) {
                return true;
            } else if (id == R.id.categoryNavigation) {
                startActivity(new Intent(CraftsmanAccountActivity.this, CraftsPageActivity.class));
                return true;
            } else {
                return false;
            }
        });
    }

    private void getUser() {
        firestore.collection("Users")
                .document(idUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.d("get User: ", "failed");
                            return;
                        }
                        Craftsman craftsman = task.getResult().toObject(Craftsman.class);
                        nameAndFamilyName.setText(craftsman.getName() + " " + craftsman.getFamilyName());
                        crafts.setText(craftsman.getCraft());
                        desc.setText(craftsman.getDescription());
                        works = craftsman.getWorks();
                        ratingBar.setRating(craftsman.getRating());
                        if (works.size() < 6)
                            works.add("gg");
                        gridView(works);
                        retrieveImage(craftsmanInfoImage, idUser);
                    }
                });
    }

    private void listImageUpdate(String work) {
        firestore.collection("Users")
                .document(idUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.d("get User: ", "failed");
                            return;
                        }
                        Craftsman craftsman = task.getResult().toObject(Craftsman.class);
                        works = craftsman.getWorks();
                        works.add(work);
                        firestore.collection("Users").document(idUser)
                                .update("works", works);
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
    public void onClick(View view) {
        if (view == accountInfoCraftsmanAccount) {
            Intent intent = new Intent(CraftsmanAccountActivity.this, CraftsmanAccountInfoActivity.class);
            intent.putExtra("idUser", FirebaseAuth.getInstance().getUid());
            startActivity(intent);
        } else if (view == settingCraftsmanAccount) {
            startActivity(new Intent(CraftsmanAccountActivity.this, SettingsPageActivity.class));
        } else if (view == logOutCraftsAccount) {
            startActivity(new Intent(CraftsmanAccountActivity.this, LogInActivity.class));
            FirebaseAuth.getInstance().signOut();
        }
    }
}