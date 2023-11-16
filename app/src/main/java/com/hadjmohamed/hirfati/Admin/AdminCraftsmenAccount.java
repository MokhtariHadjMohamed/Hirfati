package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import com.hadjmohamed.hirfati.AdapterRecComment;
import com.hadjmohamed.hirfati.Comment;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.GridAdapterAdmin;
import com.hadjmohamed.hirfati.ImageResizer;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminCraftsmenAccount extends AppCompatActivity implements View.OnClickListener, RecViewInterface {

    private Button craftsmanInfo, reportCraftsman, deleteCraftsmen;
    private TextView numberAdmin, nameAndFamilyNameAdmin, craftsAdmin, descAdmin;
    private ImageView craftsmanInfoImage;
    private RecyclerView recyclerView;
    private String idUser;

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
    private List<String> works;

    // AdapterRecComment
    private List<Comment> commentList;
    private AdapterRecComment adapterRecComment;

    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;

    //firebase
    private FirebaseFirestore firestore;

    // progressDialog
    private ProgressDialog progressDialog;

    // Dialog Image delete
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_craftsmen_account);
        idUser = getIntent().getStringExtra("idUser");
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // Element
        numberAdmin = findViewById(R.id.numberAdminCraftsmenAccount);
        nameAndFamilyNameAdmin = findViewById(R.id.nameAndFamilyNameAdminCraftsmenAccount);
        craftsAdmin = findViewById(R.id.craftsAdminCraftsmenAccount);
        descAdmin = findViewById(R.id.descAdminCraftsmenAccount);

        craftsmanInfo = findViewById(R.id.craftsmanInfoAdminCraftsmenAccount);
        reportCraftsman = findViewById(R.id.reportCraftsmanAdminCraftsmenAccount);
        deleteCraftsmen = findViewById(R.id.deleteCraftsmenAdminCraftsmenAccount);

        craftsmanInfoImage = findViewById(R.id.craftsmanInfoImage);

        craftsmanInfo.setOnClickListener(this);
        reportCraftsman.setOnClickListener(this);
        deleteCraftsmen.setOnClickListener(this);

        // ToolsBar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        backArrow = findViewById(R.id.backArrow);

        toolbarTitle.setText("الحرفي");
        backArrow.setOnClickListener(this);

        getUser();

        // comment RecyclerView
        recyclerView = findViewById(R.id.commentAdminCraftsmenAccount);
        commentList = new ArrayList<>();
        adapterRecComment = new AdapterRecComment(getApplicationContext(), commentList, this);
        getComment();

        // GridView
        gridView = findViewById(R.id.worksAdminCraftsmenAccount);
        works = new ArrayList<>();
        for (int i = 1; i <= 6; i++){
            works.add(idUser + "-" + i);
        }
        gridAdapterAdmin = new GridAdapterAdmin(AdminCraftsmenAccount.this, works);
        gridView.setAdapter(gridAdapterAdmin);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView imageView = view.findViewById(R.id.imageTypeCraftsmen);
                workImage = view.findViewById(R.id.workImageCraftsmen);
                if (imageView.getText().toString().equals("none")){
                    Toast.makeText(AdminCraftsmenAccount.this, "Add", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(
                            intent,
                            "Select Image from here..."), PICK_IMAGE_REQUEST);
                    position = i;
                }else if (imageView.getText().toString().equals("fill")){
                    Toast.makeText(AdminCraftsmenAccount.this, "Delete", Toast.LENGTH_SHORT).show();
                    imageDelete(idUser + "-" + (i+1));
                }

            }
        });

    }

    private void imageDelete(String uid){
        dialogBuilder = new MaterialAlertDialogBuilder(AdminCraftsmenAccount.this);
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
                        Toast.makeText(AdminCraftsmenAccount.this, "Delete done", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
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
                uploadImage(idUser + "-" + (position+1));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error Upload", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Filed", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(String uid) {
        if (imageUri == null){
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
                        Toast.makeText(AdminCraftsmenAccount.this,
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
                        Toast.makeText(AdminCraftsmenAccount.this,
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

     private void getComment() {
        firestore.collection("Comment")
                .whereEqualTo("uidCraftsman", idUser)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.d("get Comment: ", "failed");
                            return;
                        }
                        for (QueryDocumentSnapshot c : task.getResult()) {
                            commentList.add(c.toObject(Comment.class));
                        }
                        adapterRecComment.notifyDataSetChanged();
                    }
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterRecComment);
    }

    private void getUser() {
        firestore.collection("Users")
                .document(idUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.d("get User: ", "failed");
                            return;
                        }
                        Craftsman craftsman = task.getResult().toObject(Craftsman.class);
                        numberAdmin.setText(craftsman.getIdUser());
                        nameAndFamilyNameAdmin.setText(craftsman.getName() + " " + craftsman.getFamilyName());
                        craftsAdmin.setText(craftsman.getCraft());
                        descAdmin.setText(craftsman.getDescription());
                        retrieveImage(craftsmanInfoImage, craftsman.getIdUser());
                    }
                });
    }

    private void deleteUser(String uid) {
        firestore.collection("Users").document(uid).delete();
        FirebaseAuth.getInstance().getCurrentUser().delete();
        deleteReport();
    }

    private void dialogReport() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.send_raport_custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText text = (EditText) dialog.findViewById(R.id.textSendRaportDialog);
        Button btnSubmit = (Button) dialog.findViewById(R.id.submitSendRaportDialog);
        Button btnCancel = (Button) dialog.findViewById(R.id.cancelSendRaportDialog);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport(text.getText().toString(), "7adj.mo7amed@gmail.com");
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
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
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
                    Log.e("Image " + image, "Failed");
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
        } catch (IOException e) {
            imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            throw new RuntimeException(e);
        }

    }

    private void sendReport(String text, String email) {
        String[] TO = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "إنذار من تطبيق مول الحرف");
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e("Send Report:", ex.getMessage());
            Toast.makeText(AdminCraftsmenAccount.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == craftsmanInfo) {
            Intent intent = new Intent(AdminCraftsmenAccount.this, AdminCraftsmenAccountInfo.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
        } else if (view == reportCraftsman)
            dialogReport();
        else if (view == deleteCraftsmen) {
            dialogBuilder = new MaterialAlertDialogBuilder(this);
            dialogBuilder.setMessage("هل تريد حذف هذه الحرفة؟");
            dialogBuilder.setTitle("الطلب");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteUser(idUser);
                }
            }).setNegativeButton("لا", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });

            alertDialog = dialogBuilder.create();
            alertDialog.show();
        } else if (view == backArrow) {
            startActivity(new Intent(AdminCraftsmenAccount.this, AdminCraftsmen.class));
            finish();
        }
    }

    private void deleteReport(){
        firestore.collection("Reports")
                .whereEqualTo("reported", idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("Report delete: ", "failed");
                            return;
                        }
                        for (QueryDocumentSnapshot d: task.getResult()){
                            Report report = d.toObject(Report.class);
                            firestore.collection("Reports").document(report.getIdReport()).delete();
                        }

                    }
                });
    }

    @Override
    public void onItemClick(String view, int position) {
        dialogBuilder = new MaterialAlertDialogBuilder(AdminCraftsmenAccount.this);
        dialogBuilder.setMessage("هل تريد حذف هذه  تعليق؟");
        dialogBuilder.setTitle("الطلب");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firestore.collection("Comment")
                        .document(commentList.get(position).getUid())
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(getIntent());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Error: ", e.getMessage());
                            }
                        });
            }
        }).setNegativeButton("لا", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}