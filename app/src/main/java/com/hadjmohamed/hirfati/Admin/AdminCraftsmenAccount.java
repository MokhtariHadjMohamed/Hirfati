package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hadjmohamed.hirfati.AdapterRecComment;
import com.hadjmohamed.hirfati.Comment;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.CraftsmanAccountActivity;
import com.hadjmohamed.hirfati.CraftsmanAccountInfoActivity;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminCraftsmenAccount extends AppCompatActivity implements View.OnClickListener {

    private Button craftsmanInfo, reportCraftsman, deleteCraftsmen;
    private TextView numberAdmin, nameAndFamilyNameAdmin, craftsAdmin, descAdmin;
    private ImageView craftsmanInfoImage;
    private String idUser;

    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;

    //firebase
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_craftsmen_account);
        idUser = getIntent().getStringExtra("idUser");
        firestore = FirebaseFirestore.getInstance();

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
        RecyclerView recyclerView = findViewById(R.id.commentAdminCraftsmenAccount);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));
        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));
        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(new AdapterRecComment(getApplicationContext(), commentList));
    }

    private void getUser(){
        firestore.collection("Users")
                .document(idUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()){
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

    private void deleteUser(String uid){
        firestore.collection("Users").document(uid).delete();
        FirebaseAuth.getInstance().getCurrentUser().delete();
    }

    private void dialogReport(){
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

    private void sendReport(String text, String email){
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
        if (view == craftsmanInfo){
            Intent intent = new Intent(AdminCraftsmenAccount.this, AdminCraftsmenAccountInfo.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
        }
        else if(view == reportCraftsman)
            dialogReport();
        else if(view == deleteCraftsmen){
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
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
        } else if(view == backArrow){
            startActivity(new Intent(AdminCraftsmenAccount.this, AdminCraftsmen.class));
            finish();
        }
    }
}