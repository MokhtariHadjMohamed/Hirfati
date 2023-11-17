package com.hadjmohamed.hirfati.Admin;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.storage.StorageReference;
import com.hadjmohamed.hirfati.AdapterRecComment;
import com.hadjmohamed.hirfati.Comment;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;
import com.hadjmohamed.hirfati.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminUserAccount extends AppCompatActivity implements View.OnClickListener, RecViewInterface {

    // Element
    private Button userInfo, reportUser, deleteUser;
    private TextView numberAdmin, nameAdmin, familyNameAdmin, birthdayAdmin, addressAdmin;
    private ImageView userInfoImage;
    private String idUser;
    // AdapterRecComment
    private List<Comment> commentList;
    private AdapterRecComment adapterRecComment;
    private RecyclerView recyclerView;

    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;
    //firebase
    private FirebaseFirestore firestore;
    // Dialog delete
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_account);
        firestore = FirebaseFirestore.getInstance();
        idUser = getIntent().getStringExtra("idUser");

        // Element
        numberAdmin = findViewById(R.id.craftsmanInfoNumber);
        nameAdmin = findViewById(R.id.nameUserAccount);
        familyNameAdmin = findViewById(R.id.familyNameUserAccount);
        birthdayAdmin = findViewById(R.id.birthdayUserAccount);
        addressAdmin = findViewById(R.id.addressUserAccount);
        userInfoImage = findViewById(R.id.userInfoImage);

        userInfo = findViewById(R.id.userInfoAdminUserAccount);
        reportUser = findViewById(R.id.reportUserAdminUserAccount);
        deleteUser = findViewById(R.id.deleteUserAdminUserAccount);

        userInfo.setOnClickListener(this);
        reportUser.setOnClickListener(this);
        deleteUser.setOnClickListener(this);

        // ToolsBar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        backArrow = findViewById(R.id.backArrow);

        toolbarTitle.setText("المستعمل");
        backArrow.setOnClickListener(this);

        // comment RecyclerView
        recyclerView = findViewById(R.id.commintAdminUserAccount);
        commentList = new ArrayList<>();
        adapterRecComment = new AdapterRecComment(getApplicationContext(), commentList, this);
        getComment();

        getUser();
    }

    private void getComment() {
        firestore.collection("Comments")
                .whereEqualTo("uidUser", idUser)
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
                        User user = task.getResult().toObject(User.class);
                        numberAdmin.setText(user.getIdUser());
                        nameAdmin.setText(user.getName());
                        familyNameAdmin.setText(user.getFamilyName());
                        birthdayAdmin.setText(user.getBirthday());
                        addressAdmin.setText(user.getAddress());
                        retrieveImage(userInfoImage, user.getIdUser());
                    }
                });
    }

    private void deleteUser(String uid){
        firestore.collection("Users").document(uid).delete();
        FirebaseAuth.getInstance().getCurrentUser().delete();
        deleteReport();
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

    private void sendReport(String text, String email){
        String[] TO = {"7adj.mo7amed@gmail.com"};
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
            Toast.makeText(AdminUserAccount.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteReport(){
        firestore.collection("Reports")
                .whereEqualTo("reporter", idUser)
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
    public void onClick(View view) {
        if (view == userInfo) {
            Intent intent = new Intent(AdminUserAccount.this, AdminUserAccountInfo.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
        }else if (view == reportUser)
            dialogReport();
        else if (view == deleteUser){
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
        } else if(view == backArrow){
            startActivity(new Intent(AdminUserAccount.this, AdminUser.class));
            finish();
        }
    }

    @Override
    public void onItemClick(String view, int position) {
        Toast.makeText(this, "Click comment", Toast.LENGTH_SHORT).show();
    }
}