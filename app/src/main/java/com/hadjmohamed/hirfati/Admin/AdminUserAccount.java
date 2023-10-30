package com.hadjmohamed.hirfati.Admin;

import android.app.Dialog;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.User;

import java.io.File;
import java.io.IOException;

public class AdminUserAccount extends AppCompatActivity implements View.OnClickListener {

    // Element
    private Button userInfo, reportUser, deleteUser;
    private TextView numberAdmin, nameAdmin, familyNameAdmin, birthdayAdmin, addressAdmin;
    private ImageView userInfoImage;
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

        getUser();
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

    @Override
    public void onClick(View view) {
        if (view == userInfo) {
            Intent intent = new Intent(AdminUserAccount.this, AdminUserAccountInfo.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
        }else if (view == reportUser)
            dialogReport();
        else if (view == deleteUser)
            deleteUser(idUser);
        else if(view == backArrow){
            startActivity(new Intent(AdminUserAccount.this, AdminUser.class));
            finish();
        }
    }
}