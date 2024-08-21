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
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hadjmohamed.hirfati.Adapter.AdapterRecComment;
import com.hadjmohamed.hirfati.Adapter.GridAdapterUser;
import com.hadjmohamed.hirfati.Admin.Report;
import com.hadjmohamed.hirfati.model.Comment;
import com.hadjmohamed.hirfati.model.Craftsman;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CraftsmanInfoActivity extends AppCompatActivity implements View.OnClickListener, RecViewInterface {

    private Button craftsmanCall, reportCraftsman, addComment;
    private TextView nameAndFamilyName, crafts, desc;
    private ImageView craftsmanInfoImage;
    private TextView noneComment;
    private RecyclerView recyclerView;
    private RatingBar ratingBar;
    private float rate = 0;

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
        imageViewToolBar = findViewById(R.id.imageViewToolBar);
        backArrow = findViewById(R.id.backArrow);

        toolbarTitle.setText("الحرفي");
        backArrow.setOnClickListener(this);

        retrieveImage(imageViewToolBar, FirebaseAuth.getInstance().getUid());
        // Element
        nameAndFamilyName = findViewById(R.id.craftsmanInfoNameAndFamilyName);
        crafts = findViewById(R.id.craftsmanInfoCrafts);
        desc = findViewById(R.id.craftsmanInfoDesc);
        noneComment = findViewById(R.id.noneCommentCraftsmanInfoActivity);
        addComment = findViewById(R.id.addCommentCraftsmanInfoActivity);
        addComment.setOnClickListener(this);
        ratingBar = findViewById(R.id.ratingBarCraftsInfoActivity);

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

    }

    private void gridView(List<String> works){
        // GridView
        gridView = findViewById(R.id.workImageCraftsmenInfo);
        gridAdapterUser = new GridAdapterUser(CraftsmanInfoActivity.this, works);
        gridView.setAdapter(gridAdapterUser);
    }

    private void getComment() {
        firestore.collection("Comments")
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
                        if (commentList.isEmpty()){
                            noneComment.setText("اضف تعليق");
                            noneComment.setVisibility(View.VISIBLE);
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
                        works = craftsman.getWorks();
                        ratingBar.setRating(craftsman.getRating());
                        if (works.size() < 6)
                            works.add("gg");
                        gridView(works);
                        retrieveImage(craftsmanInfoImage, craftsman.getIdUser());
                    }
                });
    }

    private void dialogReport() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.send_raport_custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText text = (EditText) dialog.findViewById(R.id.textSendRaportDialog);
        text.setHint("نص الإبلاغ");
        Button btnSubmit = (Button) dialog.findViewById(R.id.submitSendRaportDialog);
        Button btnCancel = (Button) dialog.findViewById(R.id.cancelSendRaportDialog);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport(FirebaseAuth.getInstance().getUid(), idUser, text.getText().toString());
                dialog.cancel();
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
    private void sendReport(String uidUser, String uidCraftsman, String text) {

        DocumentReference document = firestore.collection("Reports").document();

        Report report = new Report();
        report.setIdUser(uidUser);
        report.setIdCraftsman(uidCraftsman);
        report.setDesc(text);
        report.setIdReport(document.getId());

        document.set(report);
    }

    private void dialogComment() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.send_comment_custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RatingBar ratingBar = dialog.findViewById(R.id.ratingBarComment);
        EditText text = (EditText) dialog.findViewById(R.id.textSendRaportDialog);
        text.setHint("إضافة تعليق");
        Button btnSubmit = (Button) dialog.findViewById(R.id.submitSendRaportDialog);
        Button btnCancel = (Button) dialog.findViewById(R.id.cancelSendRaportDialog);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment(FirebaseAuth.getInstance().getUid(), idUser, text.getText().toString(), ratingBar.getRating());
                dialog.cancel();
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

    private void uploadRatingAndCommentNumber(Float rate){
        firestore.collection("Comments")
                .whereEqualTo("uidCraftsman", idUser)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.d("get Comment: ", "failed");
                            return;
                        }
                        float rate = 0;
                        for (QueryDocumentSnapshot c : task.getResult()) {
                            rate += c.toObject(Comment.class).getRate();
                        }
                        int commentNumber = task.getResult().size();
                        firestore.collection("Users").document(idUser).update("rating", rate / commentNumber);
                        finish();
                        startActivity(getIntent());
                    }
                });
    }
    private void addComment(String uidUser, String uidCraftsman, String text, float rate) {

        DocumentReference document = firestore.collection("Comments").document();

        Comment comment = new Comment();
        comment.setUidUser(uidUser);
        comment.setUidCraftsman(uidCraftsman);
        comment.setText(text);
        comment.setUid(document.getId());
        comment.setRate(rate);
        document.set(comment);

        uploadRatingAndCommentNumber(rate);
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
        } else if (view == addComment) {
            dialogComment();
        }
    }

    @Override
    public void onItemClick(String view, int position) {
        
    }
}