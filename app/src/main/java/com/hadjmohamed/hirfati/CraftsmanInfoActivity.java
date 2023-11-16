package com.hadjmohamed.hirfati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CraftsmanInfoActivity extends AppCompatActivity implements View.OnClickListener, RecViewInterface {

    private Button craftsmanCall, reportCraftsman;
    private TextView nameAndFamilyName, crafts, desc;
    private ImageView craftsmanInfoImage;
    private RecyclerView recyclerView;

    // GridView
    private GridView gridView;
    private GridAdapterUser gridAdapterUser;
    private List<String> works;

    // AdapterRecComment
    private List<Comment> commentList;
    private AdapterRecComment adapterRecComment;

    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;

    // Firebase
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    // progressDialog
    private ProgressDialog progressDialog;
    private String idUser, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craftsman_info);
        idUser = getIntent().getStringExtra("idUser");
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getUser();

        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // ToolsBar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        backArrow = findViewById(R.id.backArrow);

        toolbarTitle.setText("الحرفي");
        backArrow.setOnClickListener(this);

        // Element
        nameAndFamilyName = findViewById(R.id.craftsmanInfoNameAndFamilyName);
        crafts = findViewById(R.id.craftsmanInfoCrafts);
        desc = findViewById(R.id.craftsmanInfoDesc);

        craftsmanCall = findViewById(R.id.callPhoneNumberCraftsman);
        reportCraftsman = findViewById(R.id.reportCraftsmanCraftsmenAccount);

        craftsmanInfoImage = findViewById(R.id.craftsmanInfoImage);

        craftsmanCall.setOnClickListener(this);
        reportCraftsman.setOnClickListener(this);

        // comment RecyclerView
        recyclerView = findViewById(R.id.commentCraftsmanInfo);
        commentList = new ArrayList<>();
        adapterRecComment = new AdapterRecComment(getApplicationContext(), commentList, this);
        getComment();

        // GridView
        gridView = findViewById(R.id.workImageCraftsmenInfo);
        works = new ArrayList<>();
        for (int i = 1; i <= 6; i++){
            works.add(idUser + "-" + i);
        }
        gridAdapterUser = new GridAdapterUser(CraftsmanInfoActivity.this, works);
        gridView.setAdapter(gridAdapterUser);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView imageView = view.findViewById(R.id.imageTypeCraftsmen);
//                workImage = view.findViewById(R.id.workImageCraftsmenInfo);
//                if (imageView.getText().toString().equals("none")){
//                    Toast.makeText(CraftsmanInfoActivity.this, "Add", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(
//                            intent,
//                            "Select Image from here..."), PICK_IMAGE_REQUEST);
//                    position = i;
//                }else if (imageView.getText().toString().equals("fill")){
//                    Toast.makeText(AdminCraftsmenAccount.this, "Delete", Toast.LENGTH_SHORT).show();
//                    imageDelete(idUser + "-" + (i+1));
//                }
//
//            }
//        });
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
        Log.d("idUser", "idUser" + idUser);
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
                        nameAndFamilyName.setText(craftsman.getName() + " " + craftsman.getFamilyName());
                        crafts.setText(craftsman.getCraft());
                        desc.setText(craftsman.getDescription());
                        phone = craftsman.getPhoneNumber();
                        retrieveImage(craftsmanInfoImage, craftsman.getIdUser());
                    }
                });
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
            Toast.makeText(CraftsmanInfoActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        if (view == backArrow){
            startActivity(new Intent(CraftsmanInfoActivity.this, HomePageActivity.class));
            finish();
        }else if(view == reportCraftsman){
            dialogReport();
        }else if(view == craftsmanCall){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(String view, int position) {
        
    }
}